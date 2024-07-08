package net.wanji.business.service.impl;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.domain.evaluation.*;
import net.wanji.business.entity.TjDeviceDetail;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.exercise.ExerciseHandler;
import net.wanji.business.exercise.dto.evaluation.ComfortDetail;
import net.wanji.business.exercise.dto.evaluation.EvaluationOutputResult;
import net.wanji.business.exercise.dto.evaluation.IndexDetail;
import net.wanji.business.exercise.dto.evaluation.SceneDetail;
import net.wanji.business.exercise.enums.TaskStatusEnum;
import net.wanji.business.mapper.CdjhsExerciseRecordMapper;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.business.pdf.enums.IndexTypeEnum;
import net.wanji.business.schedule.RealPlaybackSchedule;
import net.wanji.business.service.ICdjhsExerciseRecordService;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.file.FileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 练习记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-06-19
 */
@Service
public class CdjhsExerciseRecordServiceImpl implements ICdjhsExerciseRecordService
{
    @Autowired
    private CdjhsExerciseRecordMapper cdjhsExerciseRecordMapper;

    @Autowired
    private TjDeviceDetailMapper tjDeviceDetailMapper;

    @Value("${download.proxy}")
    private String downloadProxy;

    private static ReentrantLock lock = new ReentrantLock();

    /**
     * 查询练习记录
     * 
     * @param id 练习记录主键
     * @return 练习记录
     */
    @Override
    public CdjhsExerciseRecord selectCdjhsExerciseRecordById(Long id)
    {
        return cdjhsExerciseRecordMapper.selectCdjhsExerciseRecordById(id);
    }

    /**
     * 查询练习记录列表
     * 
     * @param cdjhsExerciseRecord 练习记录
     * @return 练习记录
     */
    @Override
    public List<CdjhsExerciseRecord> selectCdjhsExerciseRecordList(CdjhsExerciseRecord cdjhsExerciseRecord)
    {
        Long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        boolean admin = SecurityUtils.isAdmin(userId);
        if(!admin){
            String username = SecurityUtils.getUsername();
            cdjhsExerciseRecord.setUserName(username);
        }
        return cdjhsExerciseRecordMapper.selectCdjhsExerciseRecordList(cdjhsExerciseRecord);
    }

    /**
     * 新增练习记录
     * 
     * @param cdjhsExerciseRecord 练习记录
     * @return 结果
     */
    @Override
    public int insertCdjhsExerciseRecord(CdjhsExerciseRecord cdjhsExerciseRecord)
    {
        cdjhsExerciseRecord.setUserName(SecurityUtils.getUsername());
        cdjhsExerciseRecord.setCreateTime(DateUtils.getNowDate());
        //镜像地址改成代理地址
        String mirrorPath = cdjhsExerciseRecord.getMirrorPath();
        String downloadPath = WanjiConfig.getDownloadPath();
        String proxyUrl = downloadProxy + mirrorPath.substring(downloadPath.length());
        cdjhsExerciseRecord.setMirrorPath(proxyUrl);
        int i = cdjhsExerciseRecordMapper.insertCdjhsExerciseRecord(cdjhsExerciseRecord);
        putIntoTaskQueue(cdjhsExerciseRecord);
        return i;
    }

    @Override
    public void putIntoTaskQueue(CdjhsExerciseRecord record){
        lock.lock();
        try {
            int size = ExerciseHandler.taskQueue.size();
            ExerciseHandler.taskQueue.put(record);
            record.setStatus(TaskStatusEnum.WAITING.getStatus());
            record.setWaitingNum(size);
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 修改练习记录
     * 
     * @param cdjhsExerciseRecord 练习记录
     * @return 结果
     */
    @Override
    public int updateCdjhsExerciseRecord(CdjhsExerciseRecord cdjhsExerciseRecord)
    {
        cdjhsExerciseRecord.setUpdateTime(DateUtils.getNowDate());
        return cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(cdjhsExerciseRecord);
    }

    /**
     * 批量删除练习记录
     * 
     * @param ids 需要删除的练习记录主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsExerciseRecordByIds(Long[] ids)
    {
        //查找待开始状态任务
        List<CdjhsExerciseRecord> unexecutedRecords = selectCdjhsExerciseRecordByStatusAndIds(1, ids);
        if(!unexecutedRecords.isEmpty()){
            lock.lock();
            ExerciseHandler.qualified.set(false);
            try {
                LinkedBlockingQueue<CdjhsExerciseRecord> taskQueue = ExerciseHandler.taskQueue;
                int waiting = 0;
                Iterator<CdjhsExerciseRecord> iterator = taskQueue.iterator();
                while (iterator.hasNext()){
                    CdjhsExerciseRecord next = iterator.next();
                    boolean existed = unexecutedRecords.stream()
                            .anyMatch(item -> item.getId().compareTo(next.getId()) == 0);
                    if(existed){
                        iterator.remove();
                        continue;
                    }
                    next.setWaitingNum(waiting);
                    cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(next);
                    waiting++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                ExerciseHandler.qualified.set(true);
                lock.unlock();
            }
        }
        return cdjhsExerciseRecordMapper.deleteCdjhsExerciseRecordByIds(ids);
    }

    /**
     * 删除练习记录信息
     * 
     * @param id 练习记录主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsExerciseRecordById(Long id)
    {
        return cdjhsExerciseRecordMapper.deleteCdjhsExerciseRecordById(id);
    }

    @Override
    public EvaluationReport reviewReport(Long taskId) {
        CdjhsExerciseRecord record = cdjhsExerciseRecordMapper.selectCdjhsExerciseRecordById(taskId);
        if(StringUtils.isNotEmpty(record.getEvaluationOutput())){
            EvaluationOutputResult outputResult = JSONObject.parseObject(record.getEvaluationOutput(), EvaluationOutputResult.class);
            EvaluationReport report = EvaluationReport.builder()
                    .taskId(taskId)
                    .score(outputResult.getScore())
                    .taskDetails(record)
                    .avgSpeed(outputResult.getAvgSpeed())
                    .build();

            //试验场路段场景评分
            List<SceneDetail> sceneDetails = outputResult.getDetails();
            List<SceneInfo> sceneInfos = sceneDetails.stream()
                    .map(data -> SceneInfo.builder()
                            .sceneCode(data.getSceneCode())
                            .sceneCategory(data.getSceneCategory())
                            .sequence(data.getSequence())
                            .duration(DateUtils.secondsToDuration(data.getDuration()))
                            .securityScore(data.getSecurityScore())
                            .efficencyScore(data.getEfficencyScore())
                            .comfortScore(data.getComfortScore())
                            .sceneScore(data.getSceneScore())
                            .build())
                    .collect(Collectors.toList());
            sceneInfos = sceneInfos.stream()
                    .sorted(Comparator.comparingInt(SceneInfo::getSequence))
                    .collect(Collectors.toList());
            report.setSceneDetails(sceneInfos);

            //试验场路段行为表征与分析
            ActionAnalysis actionAnalysis = new ActionAnalysis();
            //1. 安全
            SecurityAnalysis securityAnalysis = new SecurityAnalysis();
            List<IndexDetail> securityIndexs = sceneDetails.stream()
                    .map(SceneDetail::getSecurityIndexDetails)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            Map<Integer, Long> securityIndexMap = securityIndexs.stream()
                    .collect(Collectors.groupingBy(IndexDetail::getIndex, Collectors.counting()));
            //补全key
            Map<Integer, IndexTypeEnum> securityIndex = IndexTypeEnum.getIndexTypeByCategory(net.wanji.common.common.Constants.SECURITY);
            for(Integer index: securityIndex.keySet()){
                securityIndexMap.putIfAbsent(index, 0L);
            }
            securityAnalysis.setStats(securityIndexMap);
            if(securityIndexMap.containsKey(1)){
                securityAnalysis.setCollapse(securityIndexMap.get(1).intValue());
            }
            int infraction = (int) securityIndexMap.values().stream()
                    .mapToLong(data -> data)
                    .sum();
            securityAnalysis.setInfraction(infraction);
            actionAnalysis.setSecurity(securityAnalysis);
            //舒适
            ComfortAnalysis comfortAnalysis = new ComfortAnalysis();
            List<ComfortDetail> comfortDetails = sceneDetails.stream()
                    .map(SceneDetail::getComfortDetails)
                    .collect(Collectors.toList());
            for(ComfortDetail comfortDetail: comfortDetails){
                comfortAnalysis.setRapidAcceleration(comfortAnalysis.getRapidAcceleration() + comfortDetail.getRapidAcceleration());
                comfortAnalysis.setRapidDeceleration(comfortAnalysis.getRapidDeceleration() + comfortDetail.getRapidDeceleration());
                comfortAnalysis.setSteeringWheel(comfortAnalysis.getSteeringWheel() + comfortDetail.getSteeringWheel());
            }
            actionAnalysis.setComfort(comfortAnalysis);
            //效率
            EfficencyAnalysis efficencyAnalysis = new EfficencyAnalysis();
            for(SceneDetail sceneDetail: sceneDetails){
                Integer duration = sceneDetail.getDuration();
                Integer expectDuration = sceneDetail.getExpectDuration();
                if(Objects.nonNull(duration) && Objects.nonNull(expectDuration)){
                    if(duration.compareTo(expectDuration) < 0){
                        efficencyAnalysis.setInternal1(efficencyAnalysis.getInternal1() + 1);
                    }else {
                        double rate = (duration - expectDuration) / expectDuration.doubleValue();
                        if(rate <= 0.1){
                            efficencyAnalysis.setInternal2(efficencyAnalysis.getInternal2() + 1);
                        }else if(rate <= 0.3){
                            efficencyAnalysis.setInternal3(efficencyAnalysis.getInternal3() + 1);
                        }else{
                            efficencyAnalysis.setInternal4(efficencyAnalysis.getInternal4() + 1);
                        }
                    }
                }
            }
            actionAnalysis.setEfficency(efficencyAnalysis);
            report.setActionAnalysis(actionAnalysis);
            //试验场路段场景测试概况
            TestOverview testOverview = new TestOverview();
            Map<Integer, IndexRateEnum> rateEnumMap = IndexRateEnum.getIndexRateEnumMap();
            //1. 安全
            Map<Integer, Long> securityRateMap = sceneDetails.stream()
                    .collect(Collectors.groupingBy(s -> {
                        if (s.getSecurityScore() >= 40) {
                            return 0;
                        } else if (s.getSecurityScore() >= 30) {
                            return 1;
                        } else if (s.getSecurityScore() >= 20) {
                            return 2;
                        } else if (s.getSecurityScore() >= 10) {
                            return 3;
                        } else {
                            return 4;
                        }
                    }, Collectors.counting()));
            completeData(securityRateMap, rateEnumMap);
            testOverview.setSecurity(securityRateMap);
            //2. 效率
            Map<Integer, Long> efficencyRateMap = sceneDetails.stream()
                    .collect(Collectors.groupingBy(s -> {
                        if (s.getEfficencyScore() >= 24) {
                            return 0;
                        } else if (s.getEfficencyScore() >= 18) {
                            return 1;
                        } else if (s.getEfficencyScore() >= 12) {
                            return 2;
                        } else if (s.getEfficencyScore() >= 6) {
                            return 3;
                        } else {
                            return 4;
                        }
                    }, Collectors.counting()));
            completeData(efficencyRateMap, rateEnumMap);
            testOverview.setEfficency(efficencyRateMap);
            //3. 舒适
            Map<Integer, Long> comfortRateMap = sceneDetails.stream()
                    .collect(Collectors.groupingBy(s -> {
                        if (s.getComfortScore() >= 16) {
                            return 0;
                        } else if (s.getComfortScore() >= 12) {
                            return 1;
                        } else if (s.getComfortScore() >= 8) {
                            return 2;
                        } else if (s.getComfortScore() >= 4) {
                            return 3;
                        } else {
                            return 4;
                        }
                    }, Collectors.counting()));
            completeData(comfortRateMap, rateEnumMap);
            testOverview.setComfort(comfortRateMap);
            report.setTestOverview(testOverview);
            return report;
        }
        return null;
    }

    private void completeData(Map<Integer, Long> rateMap, Map<Integer, IndexRateEnum> rateEnumMap) {
        for(Integer rateIndex: rateEnumMap.keySet()){
            rateMap.putIfAbsent(rateIndex, 0L);
        }
    }

    @Override
    public void playback(Integer taskId, Integer action) throws BusinessException, IOException {
        String key = Constants.ChannelBuilder.buildTaskPreviewChannel(
                SecurityUtils.getUsername(), taskId, null);
        switch (action) {
            case Constants.PlaybackAction.START:
                CdjhsExerciseRecord record = cdjhsExerciseRecordMapper.selectCdjhsExerciseRecordById(taskId.longValue());
                if(Objects.isNull(record) || StringUtils.isEmpty(record.getFusionFilePath())){
                    throw new BusinessException("未查询到练习记录或任何可用的轨迹文件");
                }
                String fusionFilePath = record.getFusionFilePath();
                List<List<ClientSimulationTrajectoryDto>> trajectories = FileUtils.readRealRouteFile2(fusionFilePath);
                // 2.数据校验
                if (CollectionUtils.isEmpty(trajectories)) {
                    throw new BusinessException("未查询到任何可用轨迹文件，请先进行试验");
                }
                //查询练习设备的数据通道
                TjDeviceDetail avDevice = tjDeviceDetailMapper.selectByUniques(record.getDeviceId());
                //算法场景评分
                String evaluationOutput = record.getEvaluationOutput();
                EvaluationOutputResult evaluationOutputResult = null;
                if(StringUtils.isNotEmpty(evaluationOutput)){
                    evaluationOutputResult = JSONObject.parseObject(evaluationOutput, EvaluationOutputResult.class);
                }
                RealPlaybackSchedule.startSendingData(key, avDevice.getDataChannel(), trajectories, evaluationOutputResult);
                break;
            case Constants.PlaybackAction.SUSPEND:
                RealPlaybackSchedule.suspend(key);
                break;
            case Constants.PlaybackAction.CONTINUE:
                RealPlaybackSchedule.goOn(key);
                break;
            case Constants.PlaybackAction.STOP:
                RealPlaybackSchedule.stopSendingData(key);
                break;
            default:
                break;
        }

    }

    @Override
    public List<CdjhsExerciseRecord> selectUnexecutedExercises() {
        return cdjhsExerciseRecordMapper.selectUnexecutedExercises();
    }

    @Override
    public List<CdjhsExerciseRecord> selectCdjhsExerciseRecordByStatusAndIds(Integer status, Long[] ids) {
        return cdjhsExerciseRecordMapper.selectCdjhsExerciseRecordByStatusAndIds(status, ids);
    }
}
