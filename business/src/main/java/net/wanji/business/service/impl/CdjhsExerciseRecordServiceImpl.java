package net.wanji.business.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.domain.CdjhsTeamInfo;
import net.wanji.business.domain.evaluation.*;
import net.wanji.business.domain.vo.CdjhsErSort;
import net.wanji.business.entity.TjDeviceDetail;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.exercise.BindingConfig;
import net.wanji.business.exercise.ExerciseHandler;
import net.wanji.business.exercise.dto.evaluation.*;
import net.wanji.business.exercise.dto.luansheng.StatDto;
import net.wanji.business.exercise.dto.luansheng.StatResult;
import net.wanji.business.exercise.dto.luansheng.StatThresoldEnum;
import net.wanji.business.exercise.dto.luansheng.TWPlaybackSchedule;
import net.wanji.business.exercise.enums.TaskStatusEnum;
import net.wanji.business.mapper.CdjhsExerciseRecordMapper;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.business.pdf.enums.IndexTypeEnum;
import net.wanji.business.schedule.RealPlaybackSchedule;
import net.wanji.business.service.*;
import net.wanji.business.util.InteractionFuc;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.core.domain.entity.SysUser;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.file.FileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private InteractionFuc interactionFuc;

    @Autowired
    private RestService restService;

    @Value("${download.proxy}")
    private String downloadProxy;

    @Value("${trajectory.radius}")
    private Double radius;

    @Autowired
    private BindingConfig bindingConfig;

    @Autowired
    private ExerciseHandler exerciseHandler;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    CdjhsRefereeScoringService refereeScoringService;

    @Autowired
    ICdjhsTeamInfoService teamInfoService;

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
        cdjhsExerciseRecord.setIsCompetition(0);//测试练习记录
        SysUser user = SecurityUtils.getLoginUser().getUser();
        boolean isStudent = SecurityUtils.isStudent(user);
        if(isStudent){
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
    @Transactional(rollbackFor = {BusinessException.class})
    @Override
    public int insertCdjhsExerciseRecord(CdjhsExerciseRecord cdjhsExerciseRecord) throws BusinessException {
        cdjhsExerciseRecord.setUserName(SecurityUtils.getUsername());
        cdjhsExerciseRecord.setCreateTime(DateUtils.getNowDate());
        cdjhsExerciseRecord.setIsCompetition(0);//测试任务
        //镜像地址改成代理地址
        String mirrorPath = cdjhsExerciseRecord.getMirrorPath();
        String downloadPath = WanjiConfig.getDownloadPath();
        String proxyUrl = downloadProxy + mirrorPath.substring(downloadPath.length());
        cdjhsExerciseRecord.setMirrorPath(proxyUrl);

        //镜像名称
        String mirrorName = cdjhsExerciseRecord.getMirrorName();
        boolean enabled = bindingConfig.getEnabled();
        Map<String, String> relationship = bindingConfig.getRelationship();
        if(enabled && relationship.containsKey(mirrorName)){
            String uniques = relationship.get(mirrorName);
            ReentrantLock lock = ExerciseHandler.binedLockMap.get(uniques);
            lock.lock();
            try {
                int size = ExerciseHandler.bindedTaskQueue.get(uniques).size();
                cdjhsExerciseRecord.setWaitingNum(size);
                cdjhsExerciseRecord.setStatus(TaskStatusEnum.WAITING.getStatus());
                int i = cdjhsExerciseRecordMapper.insertCdjhsExerciseRecord(cdjhsExerciseRecord);
                ExerciseHandler.bindedTaskQueue.get(uniques).add(cdjhsExerciseRecord);
                return i;
            }catch (Exception e){
                e.printStackTrace();
                throw new BusinessException("向绑定域控的任务队列中添加任务失败");
            }finally {
                lock.unlock();
            }
        }else{
            //任务入队
            ExerciseHandler.lock.lock();
            try {
                int size = ExerciseHandler.taskQueue.size();
                cdjhsExerciseRecord.setWaitingNum(size);
                cdjhsExerciseRecord.setStatus(TaskStatusEnum.WAITING.getStatus());
                int i = cdjhsExerciseRecordMapper.insertCdjhsExerciseRecord(cdjhsExerciseRecord);
                ExerciseHandler.taskQueue.add(cdjhsExerciseRecord);
                return i;
            }catch (Exception e){
                e.printStackTrace();
                throw new BusinessException("向队列中添加任务失败");
            }finally {
                ExerciseHandler.lock.unlock();
            }
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
        List<CdjhsExerciseRecord> unexecutedRecords = selectCdjhsExerciseRecordByStatusAndIds(TaskStatusEnum.WAITING.getStatus(), ids);
        if(!unexecutedRecords.isEmpty()){
            boolean enabled = bindingConfig.getEnabled();
            Map<String, String> relationship = bindingConfig.getRelationship();
            if(enabled && !relationship.isEmpty()){
                Map<String, List<CdjhsExerciseRecord>> map = unexecutedRecords.stream()
                        .filter(record -> relationship.containsKey(record.getMirrorName()))
                        .collect(Collectors.groupingBy(record -> relationship.get(record.getMirrorName())));
                for(Map.Entry<String, List<CdjhsExerciseRecord>> entry: map.entrySet()){
                    String uniques = entry.getKey();
                    List<CdjhsExerciseRecord> records = entry.getValue();
                    ReentrantLock lock = ExerciseHandler.binedLockMap.get(uniques);
                    lock.lock();
                    try {
                        LinkedBlockingQueue<CdjhsExerciseRecord> taskQueue = ExerciseHandler.bindedTaskQueue.get(uniques);
                        List<CdjhsExerciseRecord> list = new ArrayList<>();
                        int waiting = 0;
                        Iterator<CdjhsExerciseRecord> iterator = taskQueue.iterator();
                        while (iterator.hasNext()){
                            CdjhsExerciseRecord next = iterator.next();
                            boolean existed = records.stream()
                                    .anyMatch(item -> item.getId().compareTo(next.getId()) == 0);
                            if(existed){
                                iterator.remove();
                                continue;
                            }
                            next.setWaitingNum(waiting);
                            list.add(next);
                            waiting++;
                        }
                        //批量更新
                        if(!list.isEmpty()){
                            cdjhsExerciseRecordMapper.updateBatch(list);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        lock.unlock();
                    }
                    unexecutedRecords.removeAll(records);
                }

            }
            ExerciseHandler.lock.lock();
            try {
                LinkedBlockingQueue<CdjhsExerciseRecord> taskQueue = ExerciseHandler.taskQueue;
                List<CdjhsExerciseRecord> list = new ArrayList<>();
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
                    list.add(next);
                    waiting++;
                }
                //批量更新
                if(!list.isEmpty()){
                    cdjhsExerciseRecordMapper.updateBatch(list);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                ExerciseHandler.lock.unlock();
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
            if(securityIndexMap.containsKey(IndexTypeEnum.COLLAPSE.getIndexType())){
                securityAnalysis.setCollapse(securityIndexMap.get(IndexTypeEnum.COLLAPSE.getIndexType()).intValue());
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
                //场景起点列表
                List<StartPoint> sceneStartPoints = interactionFuc.getSceneStartPoints(record.getTestId().intValue());
                //查询练习设备的数据通道
                TjDeviceDetail avDevice = tjDeviceDetailMapper.selectByUniques(record.getDeviceId());
                //开始场景回放
                RealPlaybackSchedule.startSendingData(key, avDevice.getDataChannel(), trajectories, sceneStartPoints, radius);
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
    public List<CdjhsExerciseRecord> selectCdjhsExerciseRecordByStatusAndIds(Integer status, Long[] ids) {
        return cdjhsExerciseRecordMapper.selectCdjhsExerciseRecordByStatusAndIds(status, ids);
    }

    @Override
    public int updateBatch(List<CdjhsExerciseRecord> list) {
        return cdjhsExerciseRecordMapper.updateBatch(list);
    }


    @Override
    public String queryEvaluationStatus(Long id, String evaluationUrl) {
        int index = evaluationUrl.lastIndexOf("=");
        String taskId = evaluationUrl.substring(index + 1);
        String json = restService.queryEvalutionTaskStatus(taskId);
        if(StringUtils.isNotEmpty(json)){
            JSONArray jsonArray = JSONArray.parseArray(json);
            JSONObject result = jsonArray.getJSONObject(0);
            String status = result.getString("status");

            CdjhsExerciseRecord record = new CdjhsExerciseRecord();
            record.setId(id);
            record.setEvaluationTaskStatus(status);
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
            return status;
        }
        return null;
    }

    @Override
    public List<CdjhsExerciseRecord> selectCdjhsCompetitionRecordList(CdjhsExerciseRecord cdjhsExerciseRecord) {
        cdjhsExerciseRecord.setIsCompetition(1);//比赛记录
        SysUser user = SecurityUtils.getLoginUser().getUser();
        boolean student = SecurityUtils.isStudent(user);
        if(student){
            String username = SecurityUtils.getUsername();
            cdjhsExerciseRecord.setUserName(username);
        }
        return cdjhsExerciseRecordMapper.selectCdjhsExerciseRecordList(cdjhsExerciseRecord);
    }

    @Override
    public List<CdjhsExerciseRecord> selectCdjhsCompetitionRecordListTW(CdjhsExerciseRecord cdjhsExerciseRecord){
        cdjhsExerciseRecord.setIsCompetition(1);
        return cdjhsExerciseRecordMapper.selectCdjhsExerciseRecordList(cdjhsExerciseRecord);
    }



    @Transactional(rollbackFor = {Exception.class})
    @Override
    public int createCompetitionRecord(CdjhsExerciseRecord cdjhsExerciseRecord) {
        cdjhsExerciseRecord.setCreateTime(DateUtils.getNowDate());
        cdjhsExerciseRecord.setIsCompetition(1);//比赛任务
        cdjhsExerciseRecord.setStatus(TaskStatusEnum.WAITING.getStatus());
        int i = cdjhsExerciseRecordMapper.insertCdjhsExerciseRecord(cdjhsExerciseRecord);
        //任务下发域控
        exerciseHandler.run(cdjhsExerciseRecord, cdjhsExerciseRecord.getDeviceId());
        //告知主观评分
        CdjhsTeamInfo teamInfo = new CdjhsTeamInfo();
        teamInfo.setTeamName(cdjhsExerciseRecord.getTeamName());
        List<CdjhsTeamInfo> teamInfoList = teamInfoService.selectCdjhsTeamInfoList(teamInfo);
        if(CollectionUtils.isNotEmpty(teamInfoList)){
            teamInfo = teamInfoList.get(0);
            refereeScoringService.buildScoreData(Math.toIntExact(cdjhsExerciseRecord.getId()), Math.toIntExact(teamInfo.getId()),cdjhsExerciseRecord.getTeamName(),teamInfo.getSequence());
        }
        return i;
    }

    @Override
    public int deleteCompetitionRecordByIds(Long[] ids) {
        return cdjhsExerciseRecordMapper.deleteCdjhsExerciseRecordByIds(ids);
    }

    @Override
    public List<CdjhsErSort> selectSortByScore(CdjhsExerciseRecord cdjhsExerciseRecord) {
        return cdjhsExerciseRecordMapper.selectSortByScore(cdjhsExerciseRecord);
    }

    @Override
    public StatResult stat(Long taskId) {
        String thresoldKey = RedisKeyUtils.getCdjhsLuanshengStatThresoldKey(taskId.intValue());
        StatResult result = redisCache.getCacheObject(thresoldKey);
        if(Objects.isNull(result)){
            result = new StatResult();
        }
        String statKey = RedisKeyUtils.getCdjhsLuanshengStatKey(taskId.intValue());
        Set<ZSetOperations.TypedTuple<TrajectoryValueDto>> typedTuples = redisCache.rangeWithScores(statKey, 0, -1);
        if(StringUtils.isNotEmpty(typedTuples)){
            List<ZSetOperations.TypedTuple<TrajectoryValueDto>> list = new ArrayList<>(typedTuples);
            long timestamp = Objects.requireNonNull(list.get(list.size() - 1).getScore()).longValue();
            for (ZSetOperations.TypedTuple<TrajectoryValueDto> tuple : list) {
                long score = Objects.requireNonNull(tuple.getScore()).longValue();
                int diff = Math.toIntExact(timestamp - score);
                TrajectoryValueDto current = tuple.getValue();
                assert current != null;
                result.getSpeed().add(new StatDto(diff, current.getSpeed().doubleValue()));//速度
                result.getLonAcc().add(new StatDto(diff, current.getLonAcc()));//纵向加速度
                result.getLatAcc().add(new StatDto(diff, current.getLatAcc()));//横向加速度
                result.getAngularVelocityX().add(new StatDto(diff, current.getAngularVelocityX()));//横摆角速度
                result.getLonAcc2().add(new StatDto(diff, current.getLonAcc2()));
                result.getLatAcc2().add(new StatDto(diff, current.getLatAcc2()));
            }
        }
        return result;
    }

    @Override
    public void playbackTW(Long taskId, String topic, Integer action) throws BusinessException, IOException {
        switch (action) {
            case Constants.PlaybackAction.START:
                CdjhsExerciseRecord record = cdjhsExerciseRecordMapper.selectCdjhsExerciseRecordById(taskId);
                if(Objects.isNull(record) || StringUtils.isEmpty(record.getFusionFilePath())){
                    throw new BusinessException("未查询到练习记录或任何可用的轨迹文件");
                }
                String fusionFilePath = record.getFusionFilePath();
                List<List<ClientSimulationTrajectoryDto>> trajectories = FileUtils.readRealRouteFile2(fusionFilePath);
                // 2.数据校验
                if (CollectionUtils.isEmpty(trajectories)) {
                    throw new BusinessException("未查询到任何可用轨迹文件，请先进行试验");
                }
                //场景起点列表
                List<StartPoint> sceneStartPoints = interactionFuc.getSceneStartPoints(record.getTestId().intValue());
                //开始场景回放
                String statKey = RedisKeyUtils.getCdjhsLuanshengStatKey(taskId.intValue());
                redisCache.deleteObject(statKey);
                TWPlaybackSchedule.startSendingData(taskId, topic, trajectories, sceneStartPoints, radius, kafkaProducer);
                break;
            case Constants.PlaybackAction.STOP:
                TWPlaybackSchedule.stopSendingData(taskId);
                break;
            default:
                break;
        }
    }
}
