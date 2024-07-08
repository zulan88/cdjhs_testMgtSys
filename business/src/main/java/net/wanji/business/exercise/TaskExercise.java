package net.wanji.business.exercise;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.CdjhsDeviceImageRecord;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.domain.dto.TjDeviceDetailDto;
import net.wanji.business.domain.dto.ToLocalDto;
import net.wanji.business.domain.dto.device.DeviceStateDto;
import net.wanji.business.domain.param.TessParam;
import net.wanji.business.domain.vo.DeviceDetailVo;
import net.wanji.business.domain.vo.SceneDetailVo;
import net.wanji.business.entity.DataFile;
import net.wanji.business.entity.TjDeviceDetail;
import net.wanji.business.entity.TjTask;
import net.wanji.business.exercise.dto.*;
import net.wanji.business.exercise.dto.evaluation.EvaluationOutputReq;
import net.wanji.business.exercise.dto.evaluation.EvaluationOutputResult;
import net.wanji.business.exercise.dto.evaluation.SceneDetail;
import net.wanji.business.exercise.dto.evaluation.StartPoint;
import net.wanji.business.exercise.dto.report.ReportCurrentPointInfo;
import net.wanji.business.exercise.dto.report.ReportData;
import net.wanji.business.exercise.dto.simulation.SimulationSceneDto;
import net.wanji.business.exercise.dto.strategy.CaseStrategy;
import net.wanji.business.exercise.dto.strategy.DeviceConnInfo;
import net.wanji.business.exercise.dto.strategy.Strategy;
import net.wanji.business.exercise.enums.OperationTypeEnum;
import net.wanji.business.listener.*;
import net.wanji.business.mapper.CdjhsDeviceImageRecordMapper;
import net.wanji.business.mapper.CdjhsExerciseRecordMapper;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.business.mapper.TjTaskMapper;
import net.wanji.business.service.KafkaProducer;
import net.wanji.business.service.RestService;
import net.wanji.business.service.record.DataFileService;
import net.wanji.business.trajectory.KafkaTrajectoryConsumer;
import net.wanji.business.util.InteractionFuc;
import net.wanji.business.util.LongitudeLatitudeUtils;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.file.FileUploadUtils;
import net.wanji.common.utils.file.FileUtils;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.awt.geom.Point2D;
import java.io.File;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author: jenny
 * @create: 2024-06-24 11:21 上午
 */
@Slf4j(topic = "exercise")
public class TaskExercise implements Runnable{
    private CdjhsExerciseRecordMapper cdjhsExerciseRecordMapper;

    private CdjhsDeviceImageRecordMapper cdjhsDeviceImageRecordMapper;

    private RedisCache redisCache;

    private ImageListReportListener imageListReportListener;

    private ImageDelResultListener imageDelResultListener;

    private ImageIssueResultListener imageIssueResultListener;

    private TestIssueResultListener testIssueResultListener;

    private RestService restService;

    private TjDeviceDetailMapper tjDeviceDetailMapper;

    private RedisMessageListenerContainer redisMessageListenerContainer;

    private KafkaProducer kafkaProducer;

    private DataFileService dataFileService;

    private KafkaTrajectoryConsumer kafkaTrajectoryConsumer;

    private TjTaskMapper tjTaskMapper;

    private InteractionFuc interactionFuc;

    private Integer imageLengthThresold;

    private CdjhsExerciseRecord record;

    private String uniques;

    private String tessIp;

    private Integer tessPort;

    private Double radius;

    private String kafkaTopic;

    private MainCarTrajectoryListener trajectoryListener;

    private int sceneIndex = 0;

    public TaskExercise(Integer imageLengthThresold, CdjhsExerciseRecord record, String uniques, String tessIp, Integer tessPort, Double radius, String kafkaTopic,
                        CdjhsExerciseRecordMapper cdjhsExerciseRecordMapper, CdjhsDeviceImageRecordMapper cdjhsDeviceImageRecordMapper, RedisCache redisCache,
                        ImageListReportListener imageListReportListener, ImageDelResultListener imageDelResultListener, ImageIssueResultListener imageIssueResultListener,
                        TestIssueResultListener testIssueResultListener, RestService restService, TjDeviceDetailMapper tjDeviceDetailMapper, RedisMessageListenerContainer redisMessageListenerContainer,
                        KafkaProducer kafkaProducer, DataFileService dataFileService, KafkaTrajectoryConsumer kafkaTrajectoryConsumer, TjTaskMapper tjTaskMapper, InteractionFuc interactionFuc){
        this.imageLengthThresold = imageLengthThresold;
        this.record = record;
        this.uniques = uniques;
        this.tessIp = tessIp;
        this.tessPort = tessPort;
        this.radius = radius;
        this.kafkaTopic = kafkaTopic;
        this.cdjhsExerciseRecordMapper = cdjhsExerciseRecordMapper;
        this.cdjhsDeviceImageRecordMapper = cdjhsDeviceImageRecordMapper;
        this.redisCache = redisCache;
        this.imageListReportListener = imageListReportListener;
        this.imageDelResultListener = imageDelResultListener;
        this.imageIssueResultListener = imageIssueResultListener;
        this.testIssueResultListener = testIssueResultListener;
        this.restService = restService;
        this.tjDeviceDetailMapper = tjDeviceDetailMapper;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
        this.kafkaProducer = kafkaProducer;
        this.dataFileService = dataFileService;
        this.kafkaTrajectoryConsumer = kafkaTrajectoryConsumer;
        this.tjTaskMapper = tjTaskMapper;
        this.interactionFuc = interactionFuc;
    }

    @Override
    public void run() {
        try{
            log.info("开始准备与域控{}进行交互...", uniques);
            record.setDeviceId(uniques);
            record.setStatus(1);
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
            //获取镜像列表
            List<String> imageList = getImageListReport(uniques);
            if(Objects.isNull(imageList)){
                //任务结束
                record.setCheckResult(1);
                record.setCheckMsg("练习设备没有上报镜像列表,任务结束");
                record.setStatus(3);
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            log.info("开始对镜像列表长度进行校验...");
            String mirrorId = record.getMirrorId();
            if(!imageList.contains(mirrorId)){
                if(imageList.size() >= imageLengthThresold){
                    //镜像清除指令下发 找出镜像列表中下发最早的镜像
                    String image = cdjhsDeviceImageRecordMapper.selectEarliestImage(uniques, imageList.toArray(new String[0]));
                    Integer status = imageDelete(uniques, image);
                    if(Objects.isNull(status)){
                        //任务结束
                        record.setCheckResult(1);
                        record.setCheckMsg(String.format("练习设备没有上报清除镜像结果状态: %s", image));
                        record.setStatus(3);
                        cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                        return;
                    }else if(status == 0){
                        record.setCheckResult(1);
                        record.setCheckMsg(String.format("练习设备清除镜像失败: %s", image));
                        record.setStatus(3);
                        cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                        return;
                    }
                }
                //镜像下发
                Integer integrityStatus = imageIssue(uniques, record.getMd5(), mirrorId, record.getMirrorPath());
                if(Objects.isNull(integrityStatus)){
                    record.setCheckResult(1);
                    record.setCheckMsg(String.format("%s镜像下发后,未收到练习设备上报结果", mirrorId));
                    record.setStatus(3);
                    cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                    return;
                }else if(integrityStatus == 0){
                    record.setCheckResult(1);
                    record.setCheckMsg(String.format("%s镜像文件完整性校验失败", mirrorId));
                    record.setStatus(3);
                    cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                    return;
                }
            }
            //练习任务下发
            TjTask tjTask = tjTaskMapper.selectById(record.getTestId());
            if(Objects.isNull(tjTask) || StringUtils.isEmpty(tjTask.getMainPlanFile())){
                record.setCheckResult(1);
                record.setCheckMsg(String.format("测试用例%d不存在或者主车路径文件不存在", record.getTestId()));
                record.setStatus(3);
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            String routeFile = FileUploadUtils.getAbsolutePathFileName(tjTask.getMainPlanFile());
            List<SimulationTrajectoryDto> trajectories = FileUtils.readOriTrajectory(routeFile);
            Integer complianceStatus = issueTaskExercise2YK(uniques, mirrorId, trajectories);
            log.info("练习设备{}是否准备就绪: {}", uniques, complianceStatus);
            if(Objects.isNull(complianceStatus) || complianceStatus == 0){
                record.setCheckResult(1);
                String checkMsg = Objects.isNull(complianceStatus) ? "练习设备未上报练习任务下发结果" : "合规性校验不通过";
                record.setCheckMsg(checkMsg);
                record.setStatus(3);
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            //唤醒仿真 构建唤醒仿真开始结构体
            TessParam tessParam = buildTessServerParam(21, record.getUserName(), record.getId(), Arrays.asList("21"));
            String tessCommandChannel = tessParam.getCommandChannel();
            String tessDataChannel = tessParam.getDataChannel();
            String tessStatusChannel = tessParam.getStatusChannel();
            int tessStatus = restService.startServer(tessIp, tessPort, tessParam);
            if(tessStatus != 1){
                record.setCheckResult(1);
                record.setCheckMsg("唤醒仿真失败,任务结束");
                record.setStatus(3);
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            log.info("唤醒仿真成功");

            TjDeviceDetailDto query = new TjDeviceDetailDto();
            query.setDeviceType(net.wanji.common.common.Constants.SIMULATION);
            List<DeviceDetailVo> simulations = tjDeviceDetailMapper.selectByCondition(query);
            if(simulations.isEmpty()){
                record.setCheckResult(1);
                record.setCheckMsg("数据库没有录入仿真软件信息");
                record.setStatus(3);
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            //请求仿真上报状态
            Integer simulationId = simulations.get(0).getDeviceId();
            simulationStatusReq(tessCommandChannel, simulationId);
            //给仿真下发片段式场景参与者点位集
            SimulationSceneDto simulationSceneInfo = interactionFuc.getSimulationSceneInfo(record.getTestId().intValue());
            if(Objects.isNull(simulationSceneInfo)){
                record.setCheckResult(1);
                record.setCheckMsg("获取片段式场景参与者点位集信息失败");
                record.setStatus(3);
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            String simulationSceneMessage = JSONObject.toJSONString(simulationSceneInfo);
            JSONObject simulationScene = JSONObject.parseObject(simulationSceneMessage);
            redisCache.publishMessage(tessCommandChannel, simulationScene);
            log.info("给仿真指令通道-{}-下发片段式场景信息", tessCommandChannel);
            //仿真是否准备就绪
            boolean isSimulationReady = false;
            long startTime = System.currentTimeMillis();
            String simulationPrepareStatusKey = RedisKeyUtils.getSimulationPrepareStatusKey(simulationId, tessStatusChannel);
            while ((System.currentTimeMillis() - startTime) < 300000){
                Integer state = redisCache.getCacheObject(simulationPrepareStatusKey);
                if(Objects.nonNull(state) && state == 1){
                    isSimulationReady = true;
                    break;
                }
                Thread.sleep(2000);
            }
            if(!isSimulationReady){
                record.setCheckResult(1);
                record.setCheckMsg("仿真设备不具备测试条件");
                record.setStatus(3);
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            //获取每个场景的起点列表
            List<StartPoint> startPoints = interactionFuc.getSceneStartPoints(record.getTestId().intValue());
            //任务开始
            TjDeviceDetail detail = tjDeviceDetailMapper.selectByUniques(uniques);
            if(Objects.isNull(detail) || StringUtils.isEmpty(detail.getDataChannel()) || StringUtils.isEmpty(detail.getCommandChannel())){
                record.setCheckResult(1);
                String checkMsg = String.format("数据库中没有查询到练习设备%s的数据通道或指令通道", uniques);
                record.setCheckMsg(checkMsg);
                record.setStatus(3);
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            String dataChannel = detail.getDataChannel();
            String commandChannel = detail.getCommandChannel();
            //仿真开始测试数据组装
            TestStartReqDto tessStartReq = buildTessTestStart(dataChannel, tessDataChannel);
            String tessMessage = JSONObject.toJSONString(tessStartReq);
            JSONObject tessStartMessage = JSONObject.parseObject(tessMessage);
            //添加主车轨迹数据通道监听
            LinkedBlockingQueue<String> queue = getAVRedisQueue(dataChannel);
            //创建融合数据存储记录
            DataFile dataFile = new DataFile();
            dataFile.setFileName(record.getId() + File.separator + 0 + File.separator
                    + UUID.randomUUID());
            dataFileService.save(dataFile);
            // 监听kafka、文件记录
            ToLocalDto toLocalDto = new ToLocalDto(record.getId().intValue(), 0, dataFile.getFileName(),
                    dataFile.getId());
            kafkaTrajectoryConsumer.subscribe(toLocalDto);
            //向kafka发送数据融合策略
            CaseStrategy caseStrategy = buildCaseStrategy(record.getId().intValue(), 1, detail);
            String startStrategy = JSONObject.toJSONString(caseStrategy);
            kafkaProducer.sendMessage(kafkaTopic, startStrategy);
            log.info("向topic-{}发送数据融合开始策略: {}", kafkaTopic, startStrategy);
            //域控开始任务指令
            TestStartReqDto ykStartReq = buildYKTestStart(dataChannel, tessDataChannel);
            String ykMessage = JSONObject.toJSONString(ykStartReq);
            JSONObject ykStartMessage = JSONObject.parseObject(ykMessage);
            redisCache.publishMessage(detail.getCommandChannel(), ykStartMessage);
            log.info("向域控{}下发开始任务指令: {}", uniques, ykMessage);
            //更新练习开始时间
            record.setCheckResult(0);
            record.setStatus(2);
            record.setStartTime(new Date());
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
            //等待任务结束
            SimulationTrajectoryDto participantTrajectory = trajectories.get(trajectories.size() - 1);
            List<TrajectoryValueDto> points = participantTrajectory.getValue();
            TrajectoryValueDto trajectoryValueDto = points.get(points.size() - 1);
            Point2D.Double endPoint = new Point2D.Double(trajectoryValueDto.getLongitude(), trajectoryValueDto.getLatitude());

            while (!Thread.currentThread().isInterrupted()){
                String reportDataString = queue.poll(5, TimeUnit.SECONDS);
                if(Objects.isNull(reportDataString)){
                    log.info("5s内没有接收到主车轨迹数据");
                    stopFusion(toLocalDto, detail);
                    stop(ykStartReq, tessStartReq, commandChannel, dataChannel, tessCommandChannel, tessDataChannel);
                    break;
                }
                ReportData reportData = JSONObject.parseObject(reportDataString, ReportData.class);
                ReportCurrentPointInfo vehicleCurrentInfo = mainVehicleCurrentInfo(reportData);
                if(Objects.isNull(vehicleCurrentInfo)){
                    continue;
                }
                log.info("当前主车轨迹经纬度-{}-{}", vehicleCurrentInfo.getLongitude(), vehicleCurrentInfo.getLatitude());
                //判断主车位置是否到达场景起点
                if(!startPoints.isEmpty() && sceneIndex < startPoints.size()){
                    StartPoint startPoint = startPoints.get(sceneIndex);
                    Integer sequence = startPoint.getSequence();
                    log.info("当前场景{}的起点经纬度是-{}-{}", sequence, startPoint.getLongitude(), startPoint.getLatitude());
                    Point2D.Double sceneStartPoint = new Point2D.Double(startPoint.getLongitude(), startPoint.getLatitude());
                    boolean arrivedSceneStartPoint = LongitudeLatitudeUtils.isInCriticalDistance(sceneStartPoint,
                            new Point2D.Double(vehicleCurrentInfo.getLongitude(),
                                    vehicleCurrentInfo.getLatitude()),
                            radius);
                    log.info("是否到达场景{}的触发点:{}", sequence, arrivedSceneStartPoint);
                    if(arrivedSceneStartPoint){
                        redisCache.publishMessage(tessCommandChannel, tessStartMessage);
                        log.info("开始给仿真指令通道-{}下发场景{}任务开始指令: {}", tessCommandChannel, sequence, tessMessage);
                        //场景切换
                        sceneIndex++;
                    }
                }
                boolean taskEnd = LongitudeLatitudeUtils.isInCriticalDistance(
                        endPoint,
                        new Point2D.Double(vehicleCurrentInfo.getLongitude(),
                                vehicleCurrentInfo.getLatitude()),
                        radius);
                if(taskEnd){
                    log.info("主车已到达终点,任务结束");
                    stopFusion(toLocalDto, detail);
                    stop(ykStartReq, tessStartReq, commandChannel, dataChannel, tessCommandChannel, tessDataChannel);
                    break;
                }
            }
            //更新测试结束和融合数据本地存储路径
            record.setStatus(3);
            record.setEndTime(new Date());
            //计算测试时长
            int sec = (int) (record.getEndTime().getTime() - record.getStartTime().getTime()) / 1000;
            record.setDuration(DateUtils.secondsToDuration(sec));
            String fusionFilePath = dataFileService.getPath() + File.separator + toLocalDto.getFileName();
            record.setFusionFilePath(fusionFilePath);
            //请求算法输出场景评分
            EvaluationOutputReq param = EvaluationOutputReq.builder()
                    .taskId(record.getId())
                    .fusionFilePath(fusionFilePath)
                    .startPoints(startPoints)
                    .mainChannel(dataChannel)
                    .pointsNum(20)
                    .build();
            String evaluationParas = JSONObject.toJSONString(param);
            log.info("测试评价参数: {}", evaluationParas);
            String evaluationOutput = restService.getEvaluationOutput(param);
            evaluationOutput = dataComplete(evaluationOutput, record.getTestId());
            if(StringUtils.isNotEmpty(evaluationOutput)){
                double score = JSONObject.parseObject(evaluationOutput).getDoubleValue("score");
                record.setScore(score);
            }
            record.setEvaluationOutput(evaluationOutput);
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放域控设备的占用
            ExerciseHandler.occupationMap.remove(uniques);
        }
    }

    private void simulationStatusReq(String tessCommandChannel, Integer simulationId) {
        DeviceStateDto deviceStateDto = buildTessReportStatusReq(simulationId);
        String simuDeviceStatus = JSONObject.toJSONString(deviceStateDto);
        JSONObject tessStatusReq = JSONObject.parseObject(simuDeviceStatus);
        redisCache.publishMessage(tessCommandChannel, tessStatusReq);
        log.info("给仿真指令通道-{}下发状态上报请求: {}", tessCommandChannel, simuDeviceStatus);
    }

    private Integer issueTaskExercise2YK(String uniques, String mirrorId, List<SimulationTrajectoryDto> trajectories) {
        TestParams params = TestParams.builder()
                .imageId(mirrorId)
                .participantTrajectories(trajectories)
                .build();

        TestReqDto testReq = TestReqDto.builder()
                .timestamp(System.currentTimeMillis())
                .type(OperationTypeEnum.TEST_ISSUE_REQ.getType())
                .params(params)
                .build();
        String testMessage = JSONObject.toJSONString(testReq);
        JSONObject testIssue = JSONObject.parseObject(testMessage);
        String testIssueChannel = RedisKeyUtils.getTestIssueChannel(uniques);
        redisCache.publishMessage(testIssueChannel, testIssue);
        log.info("给设备{}下发练习任务信息成功", uniques);
        return testIssueResultListener.awaitingMessage(uniques, 20, TimeUnit.MINUTES);
    }

    private Integer imageIssue(String uniques, String md5, String mirrorId, String mirrorPath) {
        ImageIssueReqDto imageIssueReq = ImageIssueReqDto.builder()
                .timestamp(System.currentTimeMillis())
                .deviceId(uniques)
                .md5(md5)
                .imageId(mirrorId)
                .imgPath(mirrorPath)
                .build();
        String imageIssueChannel = RedisKeyUtils.getImageIssueChannel(uniques);
        String imageIssueMessage = JSONObject.toJSONString(imageIssueReq);
        JSONObject imageIssue = JSONObject.parseObject(imageIssueMessage);
        log.info("向域控{}下发镜像: {}", uniques, imageIssueMessage);
        redisCache.publishMessage(imageIssueChannel, imageIssue);
        Integer integrityStatus = imageIssueResultListener.awaitingMessage(uniques, mirrorId, 30, TimeUnit.MINUTES);
        log.info("域控{}上报镜像下发结果: {}", uniques, integrityStatus);
        //添加镜像下发域控记录
        CdjhsDeviceImageRecord deviceImageRecord = new CdjhsDeviceImageRecord();
        deviceImageRecord.setUniques(uniques);
        deviceImageRecord.setImageId(mirrorId);
        deviceImageRecord.setCreateTime(DateUtils.getNowDate());
        cdjhsDeviceImageRecordMapper.insertCdjhsDeviceImageRecord(deviceImageRecord);
        return integrityStatus;
    }

    private Integer imageDelete(String uniques, String image) {
        ImageDeleteReqDto imageDelReq = ImageDeleteReqDto.builder()
                .timestamp(System.currentTimeMillis())
                .deviceId(uniques)
                .imageId(image)
                .build();

        String imageDelMessage = JSONObject.toJSONString(imageDelReq);
        JSONObject imageDel = JSONObject.parseObject(imageDelMessage);
        String imageDelChannel = RedisKeyUtils.getImageDelChannel(uniques);
        redisCache.publishMessage(imageDelChannel, imageDel);
        log.info("给域控{}下发镜像清除指令: {}", uniques, imageDelMessage);
        Integer status = imageDelResultListener.awaitingMessage(uniques, imageDelReq.getImageId(), 10, TimeUnit.SECONDS);
        log.info("收到域控{}上报镜像清除结果: {}", uniques, status);
        return status;
    }

    private List<String> getImageListReport(String uniques) {
        String channelOfImageListReport = RedisKeyUtils.getImageListReportChannel(uniques);
        ImageListReportReq listReportReq = ImageListReportReq.builder()
                .timestamp(System.currentTimeMillis())
                .deviceId(uniques)
                .type(OperationTypeEnum.IMAGE_LIST_REPORT_REQ.getType())
                .build();
        String imageListReportMessage = JSONObject.toJSONString(listReportReq);
        JSONObject imageListResponse = JSONObject.parseObject(imageListReportMessage);
        redisCache.publishMessage(channelOfImageListReport, imageListResponse);
        log.info("开始获取镜像列表,下发通道和数据:{}-{}", channelOfImageListReport, imageListReportMessage);
        List<String> imageList = imageListReportListener.awaitMessage(uniques, 10, TimeUnit.SECONDS);
        log.info("获取到域控-{}上报的镜像列表:{}", uniques, JSONObject.toJSONString(imageList));
        return imageList;
    }

    private DeviceStateDto buildTessReportStatusReq(Integer deviceId) {
        DeviceStateDto deviceStateDto = new DeviceStateDto();
        deviceStateDto.setDeviceId(deviceId);
        deviceStateDto.setType(0);
        deviceStateDto.setTimestamp(System.currentTimeMillis());
        return deviceStateDto;
    }

    private String dataComplete(String evaluationOutput, Long testId) {
        if(StringUtils.isNotEmpty(evaluationOutput)){
            List<SceneDetailVo> sceneDetails = interactionFuc.findSceneDetail(testId.intValue());
            EvaluationOutputResult result = JSONObject.parseObject(evaluationOutput, EvaluationOutputResult.class);
            List<SceneDetail> details = result.getDetails();
            for(int i = 0; i < details.size(); i++){
                SceneDetailVo sceneDetailVo = sceneDetails.get(i);
                SceneDetail sceneDetail = details.get(i);
                sceneDetail.setSceneCode(sceneDetailVo.getNumber());
                sceneDetail.setSceneCategory(sceneDetailVo.getSceneSort());
            }

            result.setDetails(details);
            return JSONObject.toJSONString(result);
        }
        return null;
    }

    private void stopFusion(ToLocalDto toLocalDto, TjDeviceDetail detail) {
        //停止数据融合
        CaseStrategy endCaseStrategy = buildCaseStrategy(record.getId().intValue(), 0, detail);
        String endMessage = JSONObject.toJSONString(endCaseStrategy);
        kafkaProducer.sendMessage(kafkaTopic, endMessage);
        log.info("停止数据融合: {}", endMessage);
        //停止监听kafka和文件记录
        kafkaTrajectoryConsumer.unSubscribe(toLocalDto);
    }

    private TessParam buildTessServerParam(Integer roadNum, String username,
                                           Long taskId, List<String> mapList) {
        return new TessParam().buildTaskParam(roadNum,
                Constants.ChannelBuilder.buildTaskDataChannel(username, taskId.intValue()),
                Constants.ChannelBuilder.buildTaskControlChannel(username, taskId.intValue()),
                Constants.ChannelBuilder.buildTaskEvaluateChannel(username, taskId.intValue()),
                Constants.ChannelBuilder.buildTaskStatusChannel(username, taskId.intValue()), mapList);
    }

    private TestStartReqDto buildYKTestStart(String dataChannel, String tessDataChannel){
        List<TestProtocol> protocols = new ArrayList<>();
        //数据接收-背景车
        TestProtocol receiveProtocol = TestProtocol.builder()
                .type(0)
                .channel(tessDataChannel)
                .build();
        protocols.add(receiveProtocol);
        //主车数据发送
        TestProtocol carProtocol = TestProtocol.builder()
                .type(1)
                .channel(dataChannel)
                .build();
        protocols.add(carProtocol);
        //融合数据发送
        String mixedDataChannel = StringUtils.format("{}_{}", dataChannel, Constants.ChannelBuilder.MIXED_SUFFIX);
        TestProtocol mixedDataProtocol = TestProtocol.builder()
                .type(2)
                .channel(mixedDataChannel)
                .build();
        protocols.add(mixedDataProtocol);

        TestStartParams params = TestStartParams.builder()
                .taskType(1)
                .protocols(protocols)
                .build();
        return TestStartReqDto.builder()
                .timestamp(System.currentTimeMillis())
                .params(params)
                .type(OperationTypeEnum.TEST_CONTROL_REQ.getType())
                .build();
    }

    private TestStartReqDto buildTessTestStart(String dataChannel, String tessDataChannel){
        List<TestProtocol> protocols = new ArrayList<>();
        //数据发送-主车轨迹数据
        TestProtocol receiveProtocol = TestProtocol.builder()
                .type(0)
                .channel(dataChannel)
                .params(JSONObject.parseObject("{}"))
                .build();
        protocols.add(receiveProtocol);
        //数据发送-背景车
        TestProtocol backgroundProtocol = TestProtocol.builder()
                .type(1)
                .channel(tessDataChannel)
                .params(JSONObject.parseObject("{}"))
                .build();
        protocols.add(backgroundProtocol);

        TestStartParams params = TestStartParams.builder()
                .taskType(1)
                .protocols(protocols)
                .build();

        return TestStartReqDto.builder()
                .type(OperationTypeEnum.TEST_CONTROL_REQ.getType())
                .timestamp(System.currentTimeMillis())
                .params(params)
                .build();
    }

    private LinkedBlockingQueue<String> getAVRedisQueue(String dataChannel) {
        trajectoryListener = new MainCarTrajectoryListener();
        trajectoryListener.add(dataChannel, new LinkedBlockingQueue<>(100));
        LinkedBlockingQueue<String> queue = trajectoryListener.getQueue(dataChannel);
        redisMessageListenerContainer.addMessageListener(trajectoryListener, new ChannelTopic(dataChannel));
        return queue;
    }

    private ReportCurrentPointInfo mainVehicleCurrentInfo(ReportData reportData) {
        Optional<ReportCurrentPointInfo> mainVehicleCurrentInfo;
        try {
            mainVehicleCurrentInfo = reportData.getValue().getValue().stream()
                    .findFirst();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("report data [{}] error!", reportData, e);
            }
            return null;
        }
        return mainVehicleCurrentInfo.orElse(null);
    }

    private void stop(TestStartReqDto yk, TestStartReqDto tess,  String commandChannel, String dataChannel, String tessCommandChannel, String tessDataChannel) {
        //停止监听主车数据通道
        redisMessageListenerContainer.removeMessageListener(trajectoryListener, new ChannelTopic(dataChannel));
        //删除消息队列
        trajectoryListener.remove(dataChannel);
        //域控
        TestStartParams ykParams = yk.getParams();
        ykParams.setTaskType(0);
        yk.setParams(ykParams);
        yk.setTimestamp(System.currentTimeMillis());
        String ykMessage = JSONObject.toJSONString(yk);
        JSONObject ykEndMessage = JSONObject.parseObject(ykMessage);
        redisCache.publishMessage(commandChannel, ykEndMessage);
        log.info("给域控指令通道-{}下发任务结束:{}", commandChannel, ykMessage);

        //仿真
        TestStartParams tessParams = tess.getParams();
        tessParams.setTaskType(0);
        tess.setParams(tessParams);
        tess.setTimestamp(System.currentTimeMillis());
        String tessMessage = JSONObject.toJSONString(tess);
        JSONObject tessEndMessage = JSONObject.parseObject(tessMessage);
        redisCache.publishMessage(tessCommandChannel, tessEndMessage);
        log.info("给仿真下发指令通道-{}下发任务结束: {}", tessCommandChannel, tessMessage);

        //仿真关闭
        restService.stopTessNg(tessIp, String.valueOf(tessPort), tessDataChannel, 1);
        log.info("关闭仿真...");
    }

    private CaseStrategy buildCaseStrategy(int taskId, int state, TjDeviceDetail deviceDetail){
        CaseStrategy caseStrategy = new CaseStrategy();
        caseStrategy.setTaskId(taskId);
        caseStrategy.setState(state);
        boolean taskEnd = state == 0;
        caseStrategy.setTaskEnd(taskEnd);

        Strategy strategy = new Strategy();
        strategy.setBenchmarkDataChannel(deviceDetail.getDataChannel());
        //域控
        DeviceConnInfo av = new DeviceConnInfo(deviceDetail.getCommandChannel(), deviceDetail.getDataChannel(), "av", new HashMap<>());
        strategy.getSourceDevicesInfo().add(av);
        //感知和背景车融合数据
        String mixedDataChannel = StringUtils.format("{}_{}", deviceDetail.getDataChannel(), Constants.ChannelBuilder.MIXED_SUFFIX);
        DeviceConnInfo mvSimulation = new DeviceConnInfo(deviceDetail.getCommandChannel(), mixedDataChannel, "mvSimulation", new HashMap<>());
        strategy.getSourceDevicesInfo().add(mvSimulation);
        caseStrategy.setStrategy(strategy);

        return caseStrategy;
    }
}
