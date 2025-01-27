package net.wanji.business.exercise;

import ch.qos.logback.classic.Logger;
import com.alibaba.fastjson.JSONObject;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.CdjhsDeviceImageRecord;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.domain.dto.ToLocalDto;
import net.wanji.business.domain.tess.*;
import net.wanji.business.domain.vo.SceneDetailVo;
import net.wanji.business.entity.DataFile;
import net.wanji.business.entity.TjDeviceDetail;
import net.wanji.business.entity.TjTask;
import net.wanji.business.exercise.dto.*;
import net.wanji.business.exercise.dto.evaluation.*;
import net.wanji.business.exercise.dto.jidaevaluation.evaluation.EvaluationCreateData;
import net.wanji.business.exercise.dto.jidaevaluation.evaluation.EvaluationCreateDto;
import net.wanji.business.exercise.dto.jidaevaluation.evaluation.KafkaTopic;
import net.wanji.business.exercise.dto.jidaevaluation.network.*;
import net.wanji.business.exercise.dto.luansheng.CAMatchProcess;
import net.wanji.business.exercise.dto.luansheng.TaskCacheDto;
import net.wanji.business.exercise.dto.report.ReportCurrentPointInfo;
import net.wanji.business.exercise.dto.report.ReportData;
import net.wanji.business.exercise.dto.simulation.SimulationSceneDto;
import net.wanji.business.exercise.dto.simulation.SimulationSceneParticipant;
import net.wanji.business.exercise.dto.strategy.CaseStrategy;
import net.wanji.business.exercise.dto.strategy.DeviceConnInfo;
import net.wanji.business.exercise.dto.strategy.Strategy;
import net.wanji.business.exercise.enums.*;
import net.wanji.business.exercise.utils.SimulationAreaCalculator;
import net.wanji.business.listener.*;
import net.wanji.business.mapper.*;
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
import org.springframework.data.redis.core.ZSetOperations;
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

    private CdjhsTeamInfoMapper cdjhsTeamInfoMapper;

    private InteractionFuc interactionFuc;

    private CdjhsExerciseRecord record;

    private String uniques;

    private TimeoutConfig timeoutConfig;

    private ParamConfig paramConfig;

    private Logger log;

    private MainCarTrajectoryListener trajectoryListener;

    private int current = -1;

    private long firstFrameTimestamp;

    private Point2D.Double firstFramePostion;

    private boolean firstMinute = true;

    private Integer taskStatus;

    private TjDeviceDetail detail;

    private String tessDataChannel;

    private ToLocalDto toLocalDto;

    private Map<Integer, Boolean> sceneStartTriggerMap = new HashMap<>();

    private Map<Integer, Boolean> sceneEndTriggerMap = new HashMap<>();

    public TaskExercise(CdjhsExerciseRecord record, String uniques, CdjhsExerciseRecordMapper cdjhsExerciseRecordMapper, CdjhsDeviceImageRecordMapper cdjhsDeviceImageRecordMapper,
                        RedisCache redisCache, ImageListReportListener imageListReportListener, ImageDelResultListener imageDelResultListener, ImageIssueResultListener imageIssueResultListener,
                        TestIssueResultListener testIssueResultListener, RestService restService, TjDeviceDetailMapper tjDeviceDetailMapper, RedisMessageListenerContainer redisMessageListenerContainer,
                        KafkaProducer kafkaProducer, DataFileService dataFileService, KafkaTrajectoryConsumer kafkaTrajectoryConsumer, TjTaskMapper tjTaskMapper, InteractionFuc interactionFuc,
                        TimeoutConfig timeoutConfig, ParamConfig paramConfig, Logger logger, CdjhsTeamInfoMapper cdjhsTeamInfoMapper){
        this.record = record;
        this.uniques = uniques;
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
        this.timeoutConfig = timeoutConfig;
        this.paramConfig = paramConfig;
        this.log = logger;
        this.cdjhsTeamInfoMapper = cdjhsTeamInfoMapper;
        this.taskStatus = TaskExerciseEnum.START_INTERACTION.getStatus();
    }

    @Override
    public void run() {
        try{
            log.info("开始准备与域控{}进行交互...", uniques);
            boolean isCompetition = record.getIsCompetition() == 1;
            if(isCompetition){
                //给孪生发送比赛开始进程信息
                CAMatchProcess process = CAMatchProcess.buildRunning(record.getId(), record.getTeamId());
                sendProcess(process);
                if(record.getTestPaperType() == 3){
                    //更新团队比赛状态
                    if(Objects.nonNull(record.getTeamId())){
                        cdjhsTeamInfoMapper.updateStatusByTeamName(record.getTeamId(), TaskStatusEnum.RUNNING.getStatus());
                    }
                    //更新任务缓存
                    TaskCacheDto cache = new TaskCacheDto(record.getId(), record.getTeamId(), TaskStatusEnum.RUNNING.getStatus(), 0);
                    redisCache.setCacheObject(RedisKeyUtils.CDJHS_CURRENT_TASK_CACHE, cache);
                }
            }
            record.setDeviceId(uniques);
            record.setWaitingNum(0);
            record.setStatus(TaskStatusEnum.RUNNING.getStatus());
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);

            //查询域控和仿真设备通道信息
            detail = tjDeviceDetailMapper.selectByUniques(uniques);
            if(Objects.isNull(detail) || StringUtils.isEmpty(detail.getDataChannel()) || StringUtils.isEmpty(detail.getCommandChannel())){
                record.setCheckResult(CheckResultEnum.FAILURE.getResult());
                String checkMsg = String.format("数据库中没有查询到练习设备%s的数据通道或指令通道", uniques);
                record.setCheckMsg(checkMsg);
                record.setStatus(TaskStatusEnum.FINISHED.getStatus());
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            String dataChannel = detail.getDataChannel();
            String commandChannel = detail.getCommandChannel();
            //构建唤醒仿真数据结构体
            TessStartReq tessStartReq = buildTessStartReq(record.getUserName(), record.getId(), dataChannel, paramConfig.networkId,
                    paramConfig.host, paramConfig.port, paramConfig.db, paramConfig.pwd, paramConfig.simulatorChannel);
            String tessCommandChannel = tessStartReq.getData().getInteractiveConfig().getCommandChannel();
            tessDataChannel = tessStartReq.getData().getInteractiveConfig().getTessngChannel();
            String tessStatusChannel = tessStartReq.getData().getInteractiveConfig().getHeartChannel();

            String mirrorId = record.getMirrorId();
            if(!isCompetition){
                //获取镜像列表
                List<String> imageList = getImageListReport(uniques);
                if(Objects.isNull(imageList)){
                    record.setCheckResult(CheckResultEnum.FAILURE.getResult());
                    record.setCheckMsg("练习设备没有上报镜像列表,任务结束");
                    record.setStatus(TaskStatusEnum.FINISHED.getStatus());
                    cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                    return;
                }
                log.info("开始对镜像列表长度进行校验...");
                if(!imageList.contains(mirrorId)){
                    if(imageList.size() >= paramConfig.imageLengthThresold){
                        //镜像清除指令下发 找出镜像列表中下发最早的镜像
                        String image = cdjhsDeviceImageRecordMapper.selectEarliestImage(uniques, imageList.toArray(new String[0]));
                        Integer status = imageDelete(uniques, image);
                        if(Objects.isNull(status)){
                            record.setCheckResult(CheckResultEnum.FAILURE.getResult());
                            record.setCheckMsg(String.format("练习设备没有上报清除镜像结果状态: %s", image));
                            record.setStatus(TaskStatusEnum.FINISHED.getStatus());
                            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                            return;
                        }else if(status == 0){
                            record.setCheckResult(CheckResultEnum.FAILURE.getResult());
                            record.setCheckMsg(String.format("练习设备清除镜像失败: %s", image));
                            record.setStatus(TaskStatusEnum.FINISHED.getStatus());
                            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                            return;
                        }
                    }
                    //镜像下发
                    ImageIssueResultDto imageIssueResultDto = imageIssue(uniques, record.getMd5(), mirrorId, record.getMirrorPath());
                    if(Objects.isNull(imageIssueResultDto)){
                        record.setCheckResult(CheckResultEnum.FAILURE.getResult());
                        record.setCheckMsg(String.format("%s镜像下发后,未收到练习设备上报结果", mirrorId));
                        record.setStatus(TaskStatusEnum.FINISHED.getStatus());
                        cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                        return;
                    }else if(imageIssueResultDto.getImageStatus() == 0){
                        record.setCheckResult(CheckResultEnum.FAILURE.getResult());
                        record.setCheckMsg(String.format("%s镜像文件完整性校验失败,异常信息如下:\n%s", mirrorId, imageIssueResultDto.getMessage()));
                        record.setStatus(TaskStatusEnum.FINISHED.getStatus());
                        cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                        return;
                    }
                }
            }
            //练习任务下发
            TjTask tjTask = tjTaskMapper.selectById(record.getTestId());
            if(Objects.isNull(tjTask) || StringUtils.isEmpty(tjTask.getMainPlanFile())){
                record.setCheckResult(CheckResultEnum.FAILURE.getResult());
                record.setCheckMsg(String.format("测试用例%d不存在或者主车路径文件不存在", record.getTestId()));
                record.setStatus(TaskStatusEnum.FINISHED.getStatus());
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            String routeFile = FileUploadUtils.getAbsolutePathFileName(tjTask.getMainPlanFile());
            List<SimulationTrajectoryDto> trajectories = FileUtils.readOriTrajectory(routeFile);
            TestIssueResultDto testIssueResultDto = issueTaskExercise2YK(uniques, mirrorId, trajectories);
            log.info("练习设备{}是否准备就绪: {}", uniques, JSONObject.toJSONString(testIssueResultDto));
            if(Objects.isNull(testIssueResultDto) || testIssueResultDto.getStatus() == 0){
                record.setCheckResult(CheckResultEnum.FAILURE.getResult());
                String checkMsg = Objects.isNull(testIssueResultDto) ? "练习设备未上报练习任务下发结果" : String.format("镜像运行失败,异常信息如下:\n%s", testIssueResultDto.getMessage());
                record.setCheckMsg(checkMsg);
                record.setStatus(TaskStatusEnum.FINISHED.getStatus());
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            //唤醒仿真
            int tessStatus = restService.startTessng(paramConfig.tessIp, paramConfig.tessPort, tessStartReq, log);
            if(tessStatus != 1){
                record.setCheckResult(CheckResultEnum.FAILURE.getResult());
                record.setCheckMsg("唤醒仿真失败,任务结束");
                record.setStatus(TaskStatusEnum.FINISHED.getStatus());
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            taskStatus = TaskExerciseEnum.IS_TESS_AWAKENDED.getStatus();
            log.info("唤醒仿真成功");

            boolean isSimulationReady = false;
            long startTime = System.currentTimeMillis();
            String simulationPrepareStatusKey = RedisKeyUtils.getSimulationPrepareStatusKey(record.getId().intValue(), tessStatusChannel);
            while ((System.currentTimeMillis() - startTime) < timeoutConfig.simulationReadyStatus){
                Integer state = redisCache.getCacheObject(simulationPrepareStatusKey);
                if(Objects.nonNull(state) && state == 1){
                    isSimulationReady = true;
                    break;
                }
                Thread.sleep(2000);
            }
            if(!isSimulationReady){
                record.setCheckResult(CheckResultEnum.FAILURE.getResult());
                record.setCheckMsg("仿真设备不具备测试条件");
                record.setStatus(TaskStatusEnum.FINISHED.getStatus());
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }

            //查询仿真下发片段式场景参与者点位集
            SimulationSceneDto simulationSceneInfo = interactionFuc.getSimulationSceneInfo(record.getTestId().intValue());
            if(Objects.isNull(simulationSceneInfo)){
                record.setCheckResult(CheckResultEnum.FAILURE.getResult());
                record.setCheckMsg("获取片段式场景参与者点位集信息失败");
                record.setStatus(TaskStatusEnum.FINISHED.getStatus());
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            //获取每个场景的起点列表
            List<SceneSitePoint> sceneSitePoints = interactionFuc.getSceneSitePoints(record.getTestId().intValue());
            //任务开始前准备工作
            //添加主车轨迹数据通道监听
            LinkedBlockingQueue<String> queue = getAVRedisQueue(dataChannel);
            taskStatus = TaskExerciseEnum.STARTING_LISTEN_MAIN_TRAJECTORY.getStatus();
            //创建融合数据存储记录
            DataFile dataFile = new DataFile();
            dataFile.setFileName(record.getId() + File.separator + 0 + File.separator
                    + UUID.randomUUID());
            dataFileService.save(dataFile);
            // 监听kafka、文件记录
            String evaluationKafkaTopic = Constants.ChannelBuilder.buildTaskEvaluationKafkaTopic(record.getId());
            List<StartPoint> sceneStartPoints = interactionFuc.getSceneStartPoints(record.getTestId().intValue());
            Logger fusionAppender = AppenderManager.createAppender(record.getId(), LogTypeEnum.FUSION_TRAJECTORT.getName(), isCompetition);
            toLocalDto = new ToLocalDto(record.getId().intValue(), 0, dataFile.getFileName(),
                    dataFile.getId(), evaluationKafkaTopic, record.getUserName(),
                    sceneStartPoints, paramConfig.radius, dataChannel, isCompetition, fusionAppender);
            kafkaTrajectoryConsumer.subscribe(toLocalDto);
            //向kafka发送数据融合策略
            CaseStrategy caseStrategy = buildCaseStrategy(record.getId().intValue(), 1, detail);
            String startStrategy = JSONObject.toJSONString(caseStrategy);
            kafkaProducer.sendMessage(paramConfig.kafkaTopic, startStrategy);
            log.info("向topic-{}发送数据融合开始策略: {}", paramConfig.kafkaTopic, startStrategy);
            taskStatus = TaskExerciseEnum.FUSION_STRATEGY_IS_ISSUED.getStatus();
            //域控开始任务指令
            TestStartReqDto ykStartReq = buildYKTestStart(dataChannel, tessDataChannel);
            String ykMessage = JSONObject.toJSONString(ykStartReq);
            JSONObject ykStartMessage = JSONObject.parseObject(ykMessage);
            redisCache.publishMessage(commandChannel, ykStartMessage);
            taskStatus = TaskExerciseEnum.IS_TASK_STARTED.getStatus();
            log.info("向域控{}下发开始任务指令: {}", uniques, ykMessage);
            //更新练习开始时间
            long taskStartTime = System.currentTimeMillis();
            record.setCheckResult(CheckResultEnum.SUCCESS.getResult());
            record.setStartTime(new Date());
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
            //等待任务结束
            SimulationTrajectoryDto participantTrajectory = trajectories.get(trajectories.size() - 1);
            List<TrajectoryValueDto> points = participantTrajectory.getValue();
            TrajectoryValueDto trajectoryValueDto = points.get(points.size() - 1);
            Point2D.Double endPoint = new Point2D.Double(trajectoryValueDto.getLongitude(), trajectoryValueDto.getLatitude());

            while (!Thread.currentThread().isInterrupted()){
                String reportDataString = queue.poll(timeoutConfig.mainCarTrajectory, TimeUnit.SECONDS);
                if(Objects.isNull(reportDataString)){
                    log.info("{}s内没有接收到主车轨迹数据", timeoutConfig.mainCarTrajectory);
                    stop(toLocalDto, detail, tessDataChannel);
                    break;
                }
                //暂时添加try-catch捕获主车轨迹转成实体类异常
                ReportData reportData;
                try {
                    reportData = JSONObject.parseObject(reportDataString, ReportData.class);
                }catch (Exception e){
                    log.error("主车轨迹转成实体类异常: {}", reportDataString);
                    continue;
                }
                ReportCurrentPointInfo vehicleCurrentInfo = mainVehicleCurrentInfo(reportData);
                if(Objects.isNull(vehicleCurrentInfo)){
                    continue;
                }
                Point2D.Double position = new Point2D.Double(vehicleCurrentInfo.getLongitude(), vehicleCurrentInfo.getLatitude());
                //循环检测第一帧数据是否重复，且持续时间超过1分钟
                if(firstMinute){
                    if(Objects.isNull(firstFramePostion)){
                        firstFrameTimestamp = System.currentTimeMillis();
                        firstFramePostion = position;
                    }else if(System.currentTimeMillis() - firstFrameTimestamp >= 60000){
                        firstMinute = false;
                        boolean motionless = LongitudeLatitudeUtils.isInCriticalDistance(firstFramePostion, position, 0.5);
                        if(motionless){
                            log.info("1分钟内重复发送第一帧数据,强制结束任务");
                            stop(toLocalDto, detail, tessDataChannel);
                            break;
                        }
                    }

                }
                //动态检测指定检测时间内的移动距离
                String globalTimeStamp = vehicleCurrentInfo.getGlobalTimeStamp();
                long currentTimeStamp = Long.parseLong(globalTimeStamp);
                String mainCarTrajectoryKey = RedisKeyUtils.getCdjhsMainCarTrajectoryKey(record.getId());
                redisCache.zAdd(mainCarTrajectoryKey, position, currentTimeStamp);
                Set<ZSetOperations.TypedTuple<Point2D.Double>> typedTuples = redisCache.rangeWithScores(mainCarTrajectoryKey, 0, 0);
                ZSetOperations.TypedTuple<Point2D.Double> typedTuple = new ArrayList<>(typedTuples).get(0);
                Double score = typedTuple.getScore();
                Point2D.Double tupleValue = typedTuple.getValue();
                assert score != null;
                if(currentTimeStamp - score.longValue() >= timeoutConfig.driveDectionTime * 60 * 1000){
                    assert tupleValue != null;
                    double distance = LongitudeLatitudeUtils.ellipsoidalDistance(tupleValue, position);
                    if(distance < timeoutConfig.driveDistance){
                        log.info("【{}】--【{}】时间段内，车辆从 【{}】 行驶到 【{}】,移动距离【{}】m,小于【{}】m,任务结束",
                                score.longValue(), globalTimeStamp,
                                tupleValue.x + "," + tupleValue.y,
                                position.x + "," + position.y,
                                distance, timeoutConfig.driveDistance);
                        stop(toLocalDto, detail, tessDataChannel);
                        break;
                    }
                    long max = currentTimeStamp - timeoutConfig.driveDectionTime * 60 * 1000;
                    redisCache.removeRange(mainCarTrajectoryKey, 0, max);
                }

                boolean inPolygon = LongitudeLatitudeUtils.isInPolygon(position, MapArea.areaPoints);
                if(!inPolygon){
                    log.info("主车轨迹超出地图区域");
                    stop(toLocalDto, detail, tessDataChannel);
                    break;
                }
                boolean taskEnd = LongitudeLatitudeUtils.isInCriticalDistance(endPoint, position, paramConfig.radius);
                if(taskEnd){
                    log.info("主车已到达终点,任务结束");
                    stop(toLocalDto, detail, tessDataChannel);
                    break;
                }
                if(System.currentTimeMillis() - taskStartTime > timeoutConfig.taskDuration * 60 * 1000){
                    log.info("任务运行时间已超过{}分钟,强制结束任务", timeoutConfig.taskDuration);
                    stop(toLocalDto, detail, tessDataChannel);
                    break;
                }
                //判断主车位置是否到达场景起点和场景终点 支持非连续场景触发，连续跳过场景不能超过3个
                if(!sceneSitePoints.isEmpty() && current < sceneSitePoints.size()){
                    int waiting = current;
                    for(int i = current + 1; i < Math.min(sceneSitePoints.size(), current + 5); i++){
                        SceneSitePoint sceneSitePoint = sceneSitePoints.get(i);
                        ScenePos startPoint = sceneSitePoint.getStartPoint();
                        Point2D.Double sceneStartPoint = new Point2D.Double(startPoint.getLongitude(), startPoint.getLatitude());
                        boolean arrivedSceneStartPoint = LongitudeLatitudeUtils.isInCriticalDistance(sceneStartPoint,
                                position,
                                paramConfig.radius);
                        if(arrivedSceneStartPoint){
                            waiting = i;
                            break;
                        }
                    }
                    if(waiting > current && !sceneStartTriggerMap.containsKey(waiting)){
                        log.info("是否到达场景{}的开始触发点:{}", waiting + 1, true);
                        Integer state = redisCache.getCacheObject(simulationPrepareStatusKey);
                        if(Objects.isNull(state) || state != 1){
                            log.info("主车已行驶到场景{}开始触发点,没有收到仿真心跳或仿真准备状态异常,仿真不具备继续测试条件,任务结束", waiting + 1);
                            stop(toLocalDto, detail, tessDataChannel);
                            break;
                        }
                        //当前场景开始指令数据组装
                        SimulationSceneParticipant simulationSceneParticipant = simulationSceneInfo.getParams().getParam1().get(waiting);
                        String tessStart = JSONObject.toJSONString(simulationSceneParticipant);
                        JSONObject tessStartMessage = JSONObject.parseObject(tessStart);

                        redisCache.publishMessage(tessCommandChannel, tessStartMessage);
                        log.info("开始给仿真指令通道-{}下发场景{}任务开始指令: {}", tessCommandChannel, (waiting + 1), tessStart);
                        sceneStartTriggerMap.put(waiting, true);
                        //场景切换
                        current = waiting;
                    }
                    if(current > -1 && !sceneEndTriggerMap.containsKey(current)){
                        SceneSitePoint sceneSitePoint = sceneSitePoints.get(current);
                        ScenePos siteEndPoint = sceneSitePoint.getEndPoint();
                        Point2D.Double sceneEndPoint = new Point2D.Double(siteEndPoint.getLongitude(), siteEndPoint.getLatitude());
                        boolean arrivedSceneEndPoint = LongitudeLatitudeUtils.isInCriticalDistance(sceneEndPoint,
                                position,
                                paramConfig.radius);
                        if(arrivedSceneEndPoint){
                            log.info("是否到达场景{}的结束触发点:{}", current + 1, true);
                            Integer state = redisCache.getCacheObject(simulationPrepareStatusKey);
                            if(Objects.isNull(state) || state != 1){
                                log.info("主车已行驶到场景{}结束触发点,没有收到仿真心跳或仿真准备状态异常,仿真不具备继续测试条件,任务结束", current + 1);
                                stop(toLocalDto, detail, tessDataChannel);
                                break;
                            }
                            //当前场景结束数据组装
                            SimulationSceneParticipant sceneEndReq = interactionFuc.createSceneEndReq("场景" + (current + 1));
                            String tessSceneEnd = JSONObject.toJSONString(sceneEndReq);
                            JSONObject tessEndMessage = JSONObject.parseObject(tessSceneEnd);
                            redisCache.publishMessage(tessCommandChannel, tessEndMessage);
                            log.info("开始给仿真指令通道-{}下发场景{}任务结束指令: {}", tessCommandChannel, current + 1, tessSceneEnd);
                            sceneEndTriggerMap.put(current, true);
                        }
                    }
                }
            }
            taskStatus = TaskExerciseEnum.TASK_IS_FINISHED.getStatus();
            processAfterTaskEnd();
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
        } catch (InterruptedException e){
            log.info("任务被强制结束...");
            if(taskStatus.compareTo(TaskExerciseEnum.TASK_IS_FINISHED.getStatus()) != 0){
                forceEnd();
            }
            record.setCheckMsg("任务被管理员强制结束");
            record.setCheckResult(CheckResultEnum.FAILURE.getResult());
            record.setStatus(TaskStatusEnum.FINISHED.getStatus());
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
        } catch (Exception e){
            log.info("正在执行任务的线程捕获到异常", e);
            if(taskStatus.compareTo(TaskExerciseEnum.TASK_IS_FINISHED.getStatus()) != 0){
                forceEnd();
            }
            record.setCheckResult(CheckResultEnum.FAILURE.getResult());
            record.setCheckMsg("后台原因");
            record.setStatus(TaskStatusEnum.FINISHED.getStatus());
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
        } finally {
            if(record.getIsCompetition() == 1 && record.getTestPaperType() == 3){
                //给孪生发送比赛结束进程信息
                CAMatchProcess process = CAMatchProcess.buildFinished(record.getId(), record.getTeamId(), paramConfig.objectivePercent, paramConfig.subjectivePercent);
                sendProcess(process);
                //更新团队比赛状态
                if(Objects.nonNull(record.getTeamId())){
                    cdjhsTeamInfoMapper.updateStatusByTeamName(record.getTeamId(), TaskStatusEnum.FINISHED.getStatus());
                }
                //更新任务缓存状态
                TaskCacheDto cache = new TaskCacheDto(record.getId(), record.getTeamId(), TaskStatusEnum.FINISHED.getStatus(), 0);
                redisCache.setCacheObject(RedisKeyUtils.CDJHS_CURRENT_TASK_CACHE, cache);
            }
            //释放域控设备的占用
            ExerciseHandler.occupationMap.remove(uniques);
            ExerciseHandler.taskThreadMap.remove(record.getId());
            //删除缓存主车轨迹数据
            String mainCarTrajectoryKey = RedisKeyUtils.getCdjhsMainCarTrajectoryKey(record.getId());
            redisCache.deleteObject(mainCarTrajectoryKey);
            //自定义appender销毁
            AppenderManager.destroyAppender(record.getId(), LogTypeEnum.COMMAND.getName());
        }
    }

    private void sendProcess(CAMatchProcess process) {
        String json = JSONObject.toJSONString(process);
        kafkaProducer.sendMessage(paramConfig.luanshengProcess, json);
    }

    private void processAfterTaskEnd() {
        //更新测试结束和融合数据本地存储路径
        record.setStatus(TaskStatusEnum.FINISHED.getStatus());
        record.setEndTime(new Date());
        //计算测试时长
        int sec = (int) (record.getEndTime().getTime() - record.getStartTime().getTime()) / 1000;
        record.setDuration(DateUtils.secondsToDuration(sec));
        String fusionFilePath = dataFileService.getPath() + File.separator + toLocalDto.getFileName();
        record.setFusionFilePath(fusionFilePath);
        //请求算法输出场景评分
        String evaluationUrl = getOfflineEvaluationUrl(record.getTestId(), toLocalDto.getKafkaTopic(), toLocalDto.getMainVehicleId());
        record.setEvaluationUrl(evaluationUrl);
    }

    private void forceEnd(){
        if(taskStatus.compareTo(TaskExerciseEnum.IS_TASK_STARTED.getStatus()) == 0){
            stop(toLocalDto, detail, tessDataChannel);
            processAfterTaskEnd();//练习任务开始后，强制结束或后端异常仍然需要评价
        }else if(taskStatus.compareTo(TaskExerciseEnum.FUSION_STRATEGY_IS_ISSUED.getStatus()) == 0){
            stop(toLocalDto, detail, tessDataChannel);
        }else if(taskStatus.compareTo(TaskExerciseEnum.STARTING_LISTEN_MAIN_TRAJECTORY.getStatus()) == 0){
            issueEnd2YK(detail.getDataChannel(), tessDataChannel, detail.getCommandChannel());
            stopListenMainTrajectory(detail.getDataChannel());
            closeSimulationServer();
        }else if(taskStatus.compareTo(TaskExerciseEnum.IS_TESS_AWAKENDED.getStatus()) == 0){
            issueEnd2YK(detail.getDataChannel(), tessDataChannel, detail.getCommandChannel());
            closeSimulationServer();
        }else if(taskStatus.compareTo(TaskExerciseEnum.TASK_ISSUED.getStatus()) == 0){
            issueEnd2YK(detail.getDataChannel(), tessDataChannel, detail.getCommandChannel());
        }else if(taskStatus.compareTo(TaskExerciseEnum.IMAGE_ISSUED.getStatus()) == 0){
            issueEnd2YK(detail.getDataChannel(), tessDataChannel, detail.getCommandChannel());
        }
    }

    private String getOfflineEvaluationUrl(Long testId, String evaluationKafkaTopic, String mainVehicleId) {
        try {
            String networkId = getNetworkId(testId);
            if(Objects.nonNull(networkId)){
                EvaluationCreateDto evaluationCreateDto = new EvaluationCreateDto();
                evaluationCreateDto.setTimestamp(String.valueOf(System.currentTimeMillis()));

                EvaluationCreateData data = new EvaluationCreateData();
                data.setNetId(networkId);
                data.setMainVehiId(mainVehicleId);

                String[] split = paramConfig.kafkaHost.split(":");
                String host = split[0].trim();
                String port = split[1].trim();
                KafkaTopic kafkaTopic = new KafkaTopic();
                kafkaTopic.setHost(host);
                kafkaTopic.setPort(port);
                kafkaTopic.setTopic(evaluationKafkaTopic);

                data.setKafkaTopic(kafkaTopic);
                evaluationCreateDto.setData(data);
                return restService.createEvaluation(evaluationCreateDto);
            }
        }catch (Exception e){
            log.info("请求新建评价接口报错", e);
        }
        return null;
    }

    private String getNetworkId(Long testId) {
        try {
            //组装新建路网请求参数
            NetworkCreateDto networkCreateDto = new NetworkCreateDto();
            networkCreateDto.setTimestamp(String.valueOf(System.currentTimeMillis()));
            NetworkData data = new NetworkData();
            RegionalWeight regionalWeight = new RegionalWeight();
            //获取场景信息
            List<EvaluationAreaInfo> evaluationAreaInfos = new ArrayList<>();
            List<SceneDetailVo> sceneDetails = interactionFuc.findSceneDetail(testId.intValue());
            for(int i = 0; i < sceneDetails.size(); i++){
                SceneDetailVo sceneDetailVo = sceneDetails.get(i);
                EvaluationAreaInfo evaluationAreaInfo = new EvaluationAreaInfo();
                evaluationAreaInfo.setId(sceneDetailVo.getId());
                evaluationAreaInfo.setName(sceneDetailVo.getTestSceneDesc());
                evaluationAreaInfo.setWeights(String.valueOf(sceneDetailVo.getEvoNum()));
                //组装场景的角度信息
                JSONObject simuArea = SimulationAreaCalculator.getSimuArea(sceneDetailVo.getRoadCondition(), 0.1);
                assert simuArea != null;
                double leftTopX = simuArea.getDoubleValue("leftTopX");
                double leftTopY = simuArea.getDoubleValue("leftTopY");
                double rightBottomX = simuArea.getDoubleValue("rightBottomX");
                double rightBottomY = simuArea.getDoubleValue("rightBottomY");
                Region region = new Region();
                RegionPosition posFrom = new RegionPosition();
                posFrom.setX(leftTopX);
                posFrom.setY(leftTopY);
                region.setPosFrom(posFrom);

                RegionPosition posTo = new RegionPosition();
                posTo.setX(rightBottomX);
                posTo.setY(rightBottomY);
                region.setPosTo(posTo);

                String jsonRegion = JSONObject.toJSONString(region);
                evaluationAreaInfo.setRegion(jsonRegion);

                evaluationAreaInfos.add(evaluationAreaInfo);
            }
            regionalWeight.setEvaluationArea(evaluationAreaInfos);
            data.setRegionalWeight(regionalWeight);
            //场景评价指标信息
            String json = "{\"EvaluationSafeWeights\":[{\"id\":1,\"weights\":10,\"indicatorName\":\"是否碰撞\",\"calculationFormula\":\"碰撞-100\",\"indicatorDescription\":\"判断主车与障碍物是否发生碰撞，若是，则不通过。\"},{\"id\":2,\"weights\":10,\"indicatorName\":\"碰撞时间TTC\",\"calculationFormula\":\"TTC危险时长/任务耗时*100%\",\"indicatorDescription\":\"计算主车的碰撞时间， 若TTC<1s， 则认为存在碰撞风险， 累计违规时长。\"},{\"id\":3,\"weights\":10,\"indicatorName\":\"逆向行驶\",\"calculationFormula\":\"逆向行驶时长/任务耗时*100%\",\"indicatorDescription\":\"检测主车是否出现逆向行驶的情况，若是，则累计违规时长。\"},{\"id\":4,\"weights\":10,\"indicatorName\":\"压实线行驶\",\"calculationFormula\":\"压实线行驶时长/任务耗时*100%\",\"indicatorDescription\":\"检测主车行驶过程中是否压实线，若是，则累计违规时长。\"},{\"id\":5,\"weights\":10,\"indicatorName\":\"超速行驶\",\"calculationFormula\":\"超速行驶时长/任务耗时*100%\",\"indicatorDescription\":\"检测主车行驶速度是否超过地图上的道路限速，若是，则累计违规时长。\"},{\"id\":6,\"weights\":10,\"indicatorName\":\"四轮在路\",\"calculationFormula\":\"四轮未在路行驶时长/任务耗时*100%\",\"indicatorDescription\":\"判断主车四轮是否全程都在道路内，若不是，则累计违规时长。\"},{\"id\":7,\"weights\":40,\"indicatorName\":\"横向间距\",\"calculationFormula\":\"危险横向间距行驶时长/任务耗时*100%\",\"indicatorDescription\":\"计算主车与周围车的横向车间距，若小于1米，则认为存在碰撞风险，累计违规时长。\"},{\"id\":8,\"weights\":10,\"indicatorName\":\"在禁行区禁行\",\"calculationFormula\":\"禁行区行驶-100\",\"indicatorDescription\":\"检测主车年是否在应急车道、非机动车道、公交专用车道等禁行区域行驶，若是，则不通过。\"}],\"EvaluationClassWeights\":[{\"id\":1,\"name\":\"安全\",\"weights\":\"60\"},{\"id\":2,\"name\":\"效率\",\"weights\":\"40\"},{\"id\":3,\"name\":\"舒适\",\"weights\":\"20\"}],\"EvaluationComfortWeights\":[{\"id\":1,\"weights\":10,\"indicatorName\":\"横向加速度\",\"calculationFormula\":\"横向加速度超出阈值时长/任务耗时*100%\",\"indicatorDescription\":\"检测规划轨迹点的横向加速度是否在合理上下限范围内[-4,4]m/s2，若不是，则累计违规时长。\"},{\"id\":2,\"weights\":10,\"indicatorName\":\"横向急动度\",\"calculationFormula\":\"横向急动度超出阈值时长/任务耗时*100%\",\"indicatorDescription\":\"检测规划轨迹点的横向加速度变化率是否在合理上下限范围内[-4,4]m/s3，若不是，则累计违规时长。\"},{\"id\":3,\"weights\":10,\"indicatorName\":\"纵向加速度\",\"calculationFormula\":\"纵向加速度超出阈值时长/任务耗时*100%\",\"indicatorDescription\":\"检测规划轨迹点的纵向加速度是否在合理上下限范围内[-4,4]m/s2，若不是，则累计违规时长。\"},{\"id\":4,\"weights\":10,\"indicatorName\":\"纵向急动度\",\"calculationFormula\":\"纵向急动度超出阈值时长/任务耗时*100%\",\"indicatorDescription\":\"检测轨迹规划点的纵向加速度变化率是否在合理上下限范围内[-4,4]m/s3，若不是，则累计违规时长。\"},{\"id\":5,\"weights\":60,\"indicatorName\":\"角速度\",\"calculationFormula\":\"航向角变化超出阈值时长/任务耗时*100%\",\"indicatorDescription\":\"检测航向角是否反复变化，若航向角频繁变化，则累计违规时长。\"}],\"EvaluationEfficiencyWeights\":[{\"id\":1,\"weights\":50,\"indicatorName\":\"任务完成时间\",\"calculationFormula\":\"期望时间/实际用时*100%\",\"indicatorDescription\":\"按照超出期望用时的比例进行扣分，在期望用时内完成任务为满分。\"},{\"id\":2,\"weights\":50,\"indicatorName\":\"平均速度\",\"calculationFormula\":\"行驶里程/行驶时间\",\"indicatorDescription\":\"计算主车在行驶全程中的平均速度。\"}]}";
            ProjectWeight projectWeight = JSONObject.parseObject(json, ProjectWeight.class);
            data.setProjectWeight(projectWeight);
            networkCreateDto.setData(data);

            return restService.createNetwork(networkCreateDto);
        }catch (Exception e){
            log.info("请求新建路网接口报错", e);
        }
        return null;
    }

    private TestIssueResultDto issueTaskExercise2YK(String uniques, String mirrorId, List<SimulationTrajectoryDto> trajectories) throws InterruptedException {
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
        taskStatus = TaskExerciseEnum.TASK_ISSUED.getStatus();
        log.info("给设备{}下发练习任务信息成功: {}", uniques, testMessage);
        return testIssueResultListener.awaitingMessage(uniques, timeoutConfig.taskIssue, TimeUnit.MINUTES);
    }

    private ImageIssueResultDto imageIssue(String uniques, String md5, String mirrorId, String mirrorPath) throws InterruptedException {
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
        taskStatus = TaskExerciseEnum.IMAGE_ISSUED.getStatus();
        ImageIssueResultDto imageIssueResultDto = imageIssueResultListener.awaitingMessage(uniques, mirrorId, timeoutConfig.imageIssue, TimeUnit.MINUTES);
        log.info("域控{}上报镜像下发结果: {}", uniques, JSONObject.toJSONString(imageIssueResultDto));
        //添加镜像下发域控记录
        CdjhsDeviceImageRecord deviceImageRecord = new CdjhsDeviceImageRecord();
        deviceImageRecord.setUniques(uniques);
        deviceImageRecord.setImageId(mirrorId);
        deviceImageRecord.setCreateTime(DateUtils.getNowDate());
        cdjhsDeviceImageRecordMapper.insertCdjhsDeviceImageRecord(deviceImageRecord);
        return imageIssueResultDto;
    }

    private Integer imageDelete(String uniques, String image) throws InterruptedException {
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
        Integer status = imageDelResultListener.awaitingMessage(uniques, imageDelReq.getImageId(), timeoutConfig.imageDelete, TimeUnit.SECONDS);
        log.info("收到域控{}上报镜像清除结果: {}", uniques, status);
        return status;
    }

    private List<String> getImageListReport(String uniques) throws InterruptedException {
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
        List<String> imageList = imageListReportListener.awaitMessage(uniques, timeoutConfig.imageReport, TimeUnit.SECONDS);
        log.info("获取到域控-{}上报的镜像列表:{}", uniques, JSONObject.toJSONString(imageList));
        return imageList;
    }

    private void stop(ToLocalDto toLocalDto, TjDeviceDetail detail, String tessDataChannel){
        //域控下发任务结束
        issueEnd2YK(detail.getDataChannel(), tessDataChannel, detail.getCommandChannel());

        //停止数据融合
        stopFusion(toLocalDto, detail);

        //停止监听主车轨迹
        stopListenMainTrajectory(detail.getDataChannel());

        //关闭仿真
        closeSimulationServer();
    }

    private void stopFusion(ToLocalDto toLocalDto, TjDeviceDetail detail) {
        //停止数据融合
        CaseStrategy endCaseStrategy = buildCaseStrategy(record.getId().intValue(), 0, detail);
        String endMessage = JSONObject.toJSONString(endCaseStrategy);
        kafkaProducer.sendMessage(paramConfig.kafkaTopic, endMessage);
        log.info("停止数据融合: {}", endMessage);
        //停止监听kafka和文件记录
        kafkaTrajectoryConsumer.unSubscribe(toLocalDto);
        //删除融合数据appender
        AppenderManager.destroyAppender(record.getId(), LogTypeEnum.FUSION_TRAJECTORT.getName());
    }

    private TessStartReq buildTessStartReq(String username, Long taskId, String dataChannel, String networkId,
                                           String host, Integer port, Integer db, String pwd, String mvCarChannel){
        //redis配置信息
        RedisConfigure configure = RedisConfigure.builder()
                .host(host)
                .port(port)
                .db(db)
                .pwd(pwd)
                .build();
        //交互信息
        TessInteractiveConfig interactiveConfig = TessInteractiveConfig.builder()
                .configure(configure)
                .commandChannel(Constants.ChannelBuilder.buildTaskControlChannel(username, taskId.intValue()))
                .tessngChannel(Constants.ChannelBuilder.buildTaskDataChannel(username, taskId.intValue()))
                .heartChannel(Constants.ChannelBuilder.buildTaskStatusChannel(username, taskId.intValue()))
                .mainCarChannel(dataChannel)
                .mvCarChannel(mvCarChannel)
                .build();

        TessStartParam data = TessStartParam.builder()
                .taskId(String.valueOf(taskId))
                .interactiveConfig(interactiveConfig)
                .networkId(networkId)
                .build();

        //创建仿真请求体数据结构
        return TessStartReq.builder()
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .data(data)
                .build();
    }

    private TessStopReq buildTessStopReq(List<String> taskIds, String status){
        TessStopParam data = TessStopParam.builder()
                .taskIds(taskIds)
                .status(status)
                .build();

        return TessStopReq.builder()
                .timestamp(String.valueOf(System.currentTimeMillis()))
                .data(data)
                .build();
    }

    private LinkedBlockingQueue<String> getAVRedisQueue(String dataChannel) {
        trajectoryListener = new MainCarTrajectoryListener();
        Logger mainTrajectoryLogger = AppenderManager.createAppender(record.getId(), LogTypeEnum.MAIN_CAR_TRAJECTORY.getName(), record.getIsCompetition() == 1);
        trajectoryListener.add(dataChannel, new LinkedBlockingQueue<>(100), mainTrajectoryLogger);
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

    private void stopListenMainTrajectory(String dataChannel) {
        //停止监听主车数据通道
        redisMessageListenerContainer.removeMessageListener(trajectoryListener, new ChannelTopic(dataChannel));
        trajectoryListener.remove(dataChannel);
        AppenderManager.destroyAppender(record.getId(), LogTypeEnum.MAIN_CAR_TRAJECTORY.getName());
        log.info("停止监听主车轨迹通道: {}", dataChannel);
    }

    private void issueEnd2YK(String dataChannel, String tessDataChannel, String commandChannel){
        TestStartReqDto yk = buildYKTestEnd(dataChannel, tessDataChannel);
        String ykMessage = JSONObject.toJSONString(yk);
        JSONObject ykEndMessage = JSONObject.parseObject(ykMessage);
        redisCache.publishMessage(commandChannel, ykEndMessage);
        log.info("给域控指令通道-{}下发任务结束:{}", commandChannel, ykMessage);
    }

    private TestStartReqDto buildYKTestStart(String dataChannel, String tessDataChannel){
        return buildYKTestStartEnd(dataChannel, tessDataChannel, 1);
    }

    private TestStartReqDto buildYKTestEnd(String dataChannel, String tessDataChannel){
        return buildYKTestStartEnd(dataChannel, tessDataChannel, 0);
    }

    public static TestStartReqDto buildYKTestStartEnd(String dataChannel, String tessDataChannel, int taskType){
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
                .taskType(taskType)
                .protocols(protocols)
                .build();
        return TestStartReqDto.builder()
                .timestamp(System.currentTimeMillis())
                .params(params)
                .type(OperationTypeEnum.TEST_CONTROL_REQ.getType())
                .build();
    }

    private void closeSimulationServer(){
        TessStopReq tessStopReq = buildTessStopReq(Collections.singletonList(String.valueOf(record.getId())), null);
        boolean isClosed = restService.stopTessng(paramConfig.tessIp, paramConfig.tessPort, tessStopReq, log);
        log.info("关闭仿真{}", isClosed ? "成功" : "失败");
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
