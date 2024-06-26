package net.wanji.business.exercise;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.CdjhsDeviceImageRecord;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.domain.param.TessParam;
import net.wanji.business.entity.TjDeviceDetail;
import net.wanji.business.exercise.dto.*;
import net.wanji.business.exercise.dto.report.ReportCurrentPointInfo;
import net.wanji.business.exercise.dto.report.ReportData;
import net.wanji.business.listener.*;
import net.wanji.business.mapper.CdjhsDeviceImageRecordMapper;
import net.wanji.business.mapper.CdjhsExerciseRecordMapper;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.business.service.RestService;
import net.wanji.business.util.LongitudeLatitudeUtils;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.config.SpringContextHolder;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.common.utils.StringUtils;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author: jenny
 * @create: 2024-06-24 11:21 上午
 */
@Slf4j
public class TaskExercise implements Runnable{
    private static CdjhsExerciseRecordMapper cdjhsExerciseRecordMapper = SpringContextHolder.getBean("cdjhsExerciseRecordMapper");

    private static CdjhsDeviceImageRecordMapper cdjhsDeviceImageRecordMapper = SpringContextHolder.getBean("cdjhsDeviceImageRecordMapper");

    private static RedisCache redisCache = SpringContextHolder.getBean("redisCache");

    private static ImageListReportListener imageListReportListener = SpringContextHolder.getBean("imageListReportListener");

    private static ImageDelResultListener imageDelResultListener = SpringContextHolder.getBean("imageDelResultListener");

    private static ImageIssueResultListener imageIssueResultListener = SpringContextHolder.getBean("imageIssueResultListener");

    private static TestIssueResultListener testIssueResultListener = SpringContextHolder.getBean("testIssueResultListener");

    private static RestService restService = SpringContextHolder.getBean("restService");

    private static TjDeviceDetailMapper tjDeviceDetailMapper = SpringContextHolder.getBean("tjDeviceDetailMapper");

    private static RedisMessageListenerContainer redisMessageListenerContainer = SpringContextHolder.getBean("redisMessageListenerContainer");

    private Integer imageLengthThresold;

    private CdjhsExerciseRecord record;

    private String uniques;

    private String tessIp;

    private Integer tessPort;

    private Double radius = 2.0;

    private MainCarTrajectoryListener trajectoryListener;

    public TaskExercise(Integer imageLengthThresold, CdjhsExerciseRecord record, String uniques, String tessIp, Integer tessPort){
        this.imageLengthThresold = imageLengthThresold;
        this.record = record;
        this.uniques = uniques;
        this.tessIp = tessIp;
        this.tessPort = tessPort;
    }

    @Override
    public void run() {
        try{
            record.setDeviceId(uniques);
            record.setStatus(1);
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
            //获取镜像列表
            String channelOfImageListReport = RedisKeyUtils.getImageListReportChannel(uniques);
            ImageListReportReq listReportReq = ImageListReportReq.builder()
                    .timestamp(System.currentTimeMillis())
                    .deviceId(uniques)
                    .type(OperationTypeEnum.IMAGE_LIST_REPORT_REQ.getType())
                    .build();
            String imageListReportMessage = JSONObject.toJSONString(listReportReq);
            redisCache.publishMessage(channelOfImageListReport, imageListReportMessage);
            List<String> imageList = imageListReportListener.awaitMessage(uniques, 2, TimeUnit.MINUTES);
            if(Objects.isNull(imageList)){
                //任务结束
                record.setCheckResult(1);
                record.setCheckMsg("练习设备没有上报镜像列表,任务结束");
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            String mirrorId = record.getMirrorId();
            if(!imageList.contains(mirrorId)){
                if(imageList.size() >= imageLengthThresold){
                    //镜像清除指令下发 找出镜像列表中下发最早的镜像
                    String image = cdjhsDeviceImageRecordMapper.selectEarliestImage(uniques, imageList.toArray(new String[0]));
                    ImageDeleteReqDto imageDelReq = ImageDeleteReqDto.builder()
                            .timestamp(System.currentTimeMillis())
                            .deviceId(uniques)
                            .imageId(image)
                            .build();

                    String imageDelMessage = JSONObject.toJSONString(imageDelReq);
                    String imageDelChannel = RedisKeyUtils.getImageDelChannel(uniques);
                    redisCache.publishMessage(imageDelChannel, imageDelMessage);
                    Integer status = imageDelResultListener.awaitingMessage(uniques, imageDelReq.getImageId(), 2, TimeUnit.MINUTES);
                    if(Objects.isNull(status)){
                        //任务结束
                        record.setCheckResult(1);
                        record.setCheckMsg(String.format("练习设备没有上报清除镜像结果状态: %s", imageDelReq.getImageId()));
                        cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                        return;
                    }else if(status == 0){
                        record.setCheckResult(1);
                        record.setCheckMsg(String.format("练习设备清除镜像失败: %s", imageDelReq.getImageId()));
                        cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                        return;
                    }
                }
                //镜像下发
                ImageIssueReqDto imageIssueReq = ImageIssueReqDto.builder()
                        .timestamp(System.currentTimeMillis())
                        .deviceId(uniques)
                        .md5(record.getMd5())
                        .imageId(record.getMirrorId())
                        .imgPath(record.getMirrorPath())
                        .build();
                String imageIssueChannel = RedisKeyUtils.getImageIssueChannel(uniques);
                String imageIssueMessage = JSONObject.toJSONString(imageIssueReq);
                redisCache.publishMessage(imageIssueChannel, imageIssueMessage);
                //添加镜像下发域控记录
                CdjhsDeviceImageRecord deviceImageRecord = new CdjhsDeviceImageRecord();
                deviceImageRecord.setUniques(uniques);
                deviceImageRecord.setImageId(record.getMirrorId());
                deviceImageRecord.setCreateTime(DateUtils.getNowDate());
                cdjhsDeviceImageRecordMapper.insertCdjhsDeviceImageRecord(deviceImageRecord);
                Integer integrityStatus = imageIssueResultListener.awaitingMessage(uniques, imageIssueReq.getImageId(), 2, TimeUnit.MINUTES);
                if(Objects.isNull(integrityStatus)){
                    record.setCheckResult(1);
                    record.setCheckMsg(String.format("%s镜像下发后,未收到练习设备上报结果", imageIssueReq.getImageId()));
                    cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                    return;
                }else if(integrityStatus == 0){
                    record.setCheckResult(1);
                    record.setCheckMsg(String.format("%s镜像文件完整性校验失败", imageIssueReq.getImageId()));
                    cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                    return;
                }
            }
            //练习任务下发 todo   需要根据测试用例查询主车轨迹文件
            TestParams params = TestParams.builder()
                    .imageId(record.getMirrorId())
                    .participantTrajectories(new ArrayList<>())
                    .build();

            TestReqDto testReq = TestReqDto.builder()
                    .timestamp(System.currentTimeMillis())
                    .type(OperationTypeEnum.TEST_ISSUE_REQ.getType())
                    .params(params)
                    .build();
            String testMessage = JSONObject.toJSONString(testReq);
            String testIssueChannel = RedisKeyUtils.getTestIssueChannel(uniques);
            redisCache.publishMessage(testIssueChannel, testMessage);
            //唤醒仿真 构建唤醒仿真开始结构体
            TessParam tessParam = buildTessServerParam(1, record.getCreateBy(), record.getId(), record.getTestId(), Arrays.asList("21"));
            int tessStatus = restService.startServer(tessIp, tessPort, tessParam);
            if(tessStatus != 1){
                record.setCheckResult(1);
                record.setCheckMsg("唤醒仿真失败,任务结束");
                return;
            }
            Integer complianceStatus = testIssueResultListener.awaitingMessage(uniques, 10, TimeUnit.MINUTES);
            if(Objects.isNull(complianceStatus) || complianceStatus == 0){
                record.setCheckResult(1);
                String checkMsg = Objects.isNull(complianceStatus) ? "练习设备未上报练习任务下发结果" : "合规性校验不通过";
                record.setCheckMsg(checkMsg);
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                //停止仿真
                restService.stopTessNg(tessIp, String.valueOf(tessPort), tessParam.getDataChannel(), 1);
                return;
            }
            //给仿真和域控管理插件下发任务开始指令
            TjDeviceDetail detail = tjDeviceDetailMapper.selectByUniques(uniques);
            if(Objects.isNull(detail) || StringUtils.isEmpty(detail.getDataChannel()) || StringUtils.isEmpty(detail.getCommandChannel())){
                record.setCheckResult(1);
                String checkMsg = String.format("数据库中没有查询到练习设备%s的数据通道或指令通道", uniques);
                record.setCheckMsg(checkMsg);
                cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                return;
            }
            TestStartReqDto ykStartReq = buildYKTestStart(detail, tessParam.getDataChannel());
            String ykMessage = JSONObject.toJSONString(ykStartReq);
            TestStartReqDto tessStartReq = buildTessTestStart(detail, tessParam.getDataChannel(), tessParam.getCommandChannel());
            String tessMessage = JSONObject.toJSONString(tessStartReq);
            //添加主车轨迹数据通道监听
            String dataChannel = detail.getDataChannel();
            LinkedBlockingQueue<String> queue = getAVRedisQueue(dataChannel);
            redisCache.publishMessage(ykStartReq.getControlChannel(), ykMessage);
            redisCache.publishMessage(tessStartReq.getControlChannel(), tessMessage);
            //更新练习开始时间
            record.setStatus(2);
            record.setStartTime(new Date());
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
            //等待任务结束
            List<ParticipantTrajectory> trajectoryList = params.getParticipantTrajectories();
            ParticipantTrajectory participantTrajectory = trajectoryList.get(trajectoryList.size() - 1);
            List<TrajectoryValueDto> points = participantTrajectory.getValue();
            TrajectoryValueDto trajectoryValueDto = points.get(points.size() - 1);
            Point2D.Double endPoint = new Point2D.Double(trajectoryValueDto.getLongitude(), trajectoryValueDto.getLatitude());
            while (!Thread.currentThread().isInterrupted()){
                String reportDataString = queue.poll(5, TimeUnit.SECONDS);
                if(Objects.isNull(reportDataString)){
                    stop(ykStartReq, tessStartReq, dataChannel);
                    break;
                }
                ReportData reportData = JSONObject.parseObject(reportDataString, ReportData.class);
                ReportCurrentPointInfo vehicleCurrentInfo = mainVehicleCurrentInfo(reportData);
                if(Objects.isNull(vehicleCurrentInfo)){
                    continue;
                }
                boolean taskEnd = LongitudeLatitudeUtils.isInCriticalDistance(
                        endPoint,
                        new Point2D.Double(vehicleCurrentInfo.getLongitude(),
                                vehicleCurrentInfo.getLatitude()),
                        radius);
                if(taskEnd){
                    stop(ykStartReq, tessStartReq, dataChannel);
                    break;
                }
            }
            //更新测试结束
            record.setStatus(3);
            record.setEndTime(new Date());
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //释放域控设备的占用
            ExerciseHandler.occupationMap.remove(uniques);
        }
    }

    private TessParam buildTessServerParam(Integer roadNum, String username,
                                           Long taskId, Long testId, List<String> mapList) {
        return new TessParam().buildTaskStartParam(roadNum,
                Constants.ChannelBuilder.buildTaskDataChannel(username, taskId, testId),
                Constants.ChannelBuilder.buildTaskControlChannel(username, taskId, testId),
                Constants.ChannelBuilder.buildTaskStatusChannel(username, taskId, testId), mapList);
    }

    private TestStartReqDto buildYKTestStart(TjDeviceDetail deviceDetail, String tessDataChannel){
        List<TestProtocol> protocols = new ArrayList<>();
        //数据接收-背景车
        TestProtocol receiveProtocol = TestProtocol.builder()
                .type(0)
                .channel(tessDataChannel)
                .build();
        protocols.add(receiveProtocol);
        //主车数据发送
        String dataChannel = deviceDetail.getDataChannel();
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
                .type(2)
                .controlChannel(deviceDetail.getCommandChannel())
                .build();
    }

    private TestStartReqDto buildTessTestStart(TjDeviceDetail deviceDetail, String tessDataChannel, String tessCommandChannel){
        List<TestProtocol> protocols = new ArrayList<>();
        //数据发送-主车轨迹数据
        TestProtocol receiveProtocol = TestProtocol.builder()
                .type(0)
                .channel(deviceDetail.getDataChannel())
                .build();
        protocols.add(receiveProtocol);
        //数据发送-背景车
        TestProtocol backgroundProtocol = TestProtocol.builder()
                .type(1)
                .channel(tessDataChannel)
                .build();
        protocols.add(backgroundProtocol);

        TestStartParams params = TestStartParams.builder()
                .taskType(1)
                .protocols(protocols)
                .build();

        return TestStartReqDto.builder()
                .type(2)
                .timestamp(System.currentTimeMillis())
                .params(params)
                .controlChannel(tessCommandChannel)
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

    private void stop(TestStartReqDto yk, TestStartReqDto tess,  String dataChannel) {
        //域控
        TestStartParams ykParams = yk.getParams();
        ykParams.setTaskType(0);
        yk.setParams(ykParams);
        yk.setTimestamp(System.currentTimeMillis());
        String ykCommandChannel = yk.getControlChannel();
        String ykMessage = JSONObject.toJSONString(yk);

        //仿真
        TestStartParams tessParams = tess.getParams();
        tessParams.setTaskType(0);
        tess.setParams(tessParams);
        tess.setTimestamp(System.currentTimeMillis());
        String tessCommandChannel = tess.getControlChannel();
        String tessMessage = JSONObject.toJSONString(tess);
        redisCache.publishMessage(ykCommandChannel, ykMessage);
        redisCache.publishMessage(tessCommandChannel, tessMessage);

        //停止监听主车数据通道
        redisMessageListenerContainer.removeMessageListener(trajectoryListener, new ChannelTopic(dataChannel));
        //删除消息队列
        trajectoryListener.remove(dataChannel);
    }
}
