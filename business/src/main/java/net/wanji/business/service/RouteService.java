package net.wanji.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.wanji.business.common.Constants.ColumnName;
import net.wanji.business.common.Constants.Extension;
import net.wanji.business.common.Constants.TaskCaseStatusEnum;
import net.wanji.business.common.Constants.TaskStatusEnum;
import net.wanji.business.common.Constants.TestingStatusEnum;
import net.wanji.business.domain.InElement;
import net.wanji.business.domain.bo.CaseTrajectoryDetailBo;
import net.wanji.business.domain.bo.ParticipantTrajectoryBo;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.business.domain.bo.TrajectoryDetailBo;
import net.wanji.business.domain.vo.ParticipantTrajectoryVo;
import net.wanji.business.domain.vo.TrajectoryDetailVo;
import net.wanji.business.entity.TjCase;
import net.wanji.business.entity.TjCaseRealRecord;
import net.wanji.business.entity.TjTask;
import net.wanji.business.entity.TjTaskCase;
import net.wanji.business.entity.TjTaskCaseRecord;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjCaseMapper;
import net.wanji.business.mapper.TjCaseRealRecordMapper;
import net.wanji.business.mapper.TjTaskCaseMapper;
import net.wanji.business.mapper.TjTaskCaseRecordMapper;
import net.wanji.business.mapper.TjTaskMapper;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.common.RealTestTrajectoryDto;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.GeoUtil;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.file.FileUploadUtils;
import net.wanji.common.utils.file.FileUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/7 13:05
 * @Descriptoin:
 */
@Service
public class RouteService {

    private static final Logger log = LoggerFactory.getLogger("business");

    @Autowired
    private TjTaskMapper taskMapper;

    @Autowired
    private TjCaseMapper tjCaseMapper;

    @Autowired
    private TjTaskCaseMapper taskCaseMapper;

    @Autowired
    private TjCaseRealRecordMapper caseRealRecordMapper;

    @Autowired
    private TjTaskCaseRecordMapper taskCaseRecordMapper;

    @Async
    public void saveRouteFile(Integer caseId, List<SimulationTrajectoryDto> data) throws ExecutionException, InterruptedException {
        log.info(StringUtils.format("保存{}路径文件", caseId));
        TjCase tjCase = new TjCase();
        tjCase.setId(caseId);
        // 保存本地文件
        try {
            String path = FileUtils.writeRoute(data, WanjiConfig.getRoutePath(), Extension.TXT);
            log.info("saveRouteFile routePath:{}", path);
            tjCase.setRouteFile(path);
            tjCase.setUpdatedDate(LocalDateTime.now());
            tjCaseMapper.updateById(tjCase);
            log.info("更新完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveRealRouteFile(Integer recordId, List<RealTestTrajectoryDto> data) throws ExecutionException, InterruptedException {
        log.info(StringUtils.format("保存实车测试{}路径文件", recordId));
        TjCaseRealRecord caseRealRecord = new TjCaseRealRecord();
        caseRealRecord.setId(recordId);
        // 保存本地文件
        try {
            String path = FileUtils.writeRoute(data, WanjiConfig.getRoutePath(), Extension.TXT);
            log.info("saveRealRouteFile routePath:{}", path);
            caseRealRecord.setRouteFile(path);
            caseRealRecord.setStatus(TestingStatusEnum.PASS.getCode());
            caseRealRecord.setEndTime(LocalDateTime.now());
            caseRealRecordMapper.updateById(caseRealRecord);
            log.info("更新完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean saveRealRouteFile2(TjCaseRealRecord caseRealRecord, int action, List<List<ClientSimulationTrajectoryDto>> data) throws Exception {
        // 保存本地文件
        String path = null;
        try {
            path = FileUtils.writeRoute(data, WanjiConfig.getRoutePath(), Extension.TXT);
            caseRealRecord.setRouteFile(path);
            caseRealRecord.setStatus(action < 0 ? TestingStatusEnum.NO_PASS.getCode() : TestingStatusEnum.PASS.getCode());
            caseRealRecord.setEndTime(LocalDateTime.now());
            CaseTrajectoryDetailBo originalTrajectory = JSONObject.parseObject(caseRealRecord.getDetailInfo(), CaseTrajectoryDetailBo.class);
            updateRecordDetailInfo(caseRealRecord.getId(), originalTrajectory, data);
            originalTrajectory.setDuration(DateUtils.secondsToDuration((int) Math.floor((double) data.size() / 10)));
            caseRealRecord.setDetailInfo(JSON.toJSONString(originalTrajectory));
            log.info("保存用例 {} 实车测试记录 {} 路径文件 : {}, 轨迹长度：{}", caseRealRecord.getCaseId(), caseRealRecord.getId(), path, data.size());
        } catch (Exception e) {
            log.error("保存用例 {} 实车测试记录 {} 路径文件失败", caseRealRecord.getCaseId(), caseRealRecord.getId(), e);
        }
        int result = caseRealRecordMapper.updateById(caseRealRecord);
        return result > 0;
    }

    @Deprecated
    public void saveTaskRouteFile(Integer recordId, List<RealTestTrajectoryDto> data,
                                  CaseTrajectoryDetailBo originalTrajectory, Integer action)
            throws ExecutionException, InterruptedException {
        log.info(StringUtils.format("保存任务用例测试{}路径文件", recordId));
        TjTaskCaseRecord taskCaseRecord = taskCaseRecordMapper.selectById(recordId);
        // 保存本地文件
        try {
            String duration = DateUtils.secondsToDuration(
                    (int) Math.floor((double) (data.size()) / 10));
            originalTrajectory.setDuration(duration);

            String path = FileUtils.writeRoute(data, WanjiConfig.getRoutePath(), Extension.TXT);
            log.info("save task case record routePath:{}", path);
            taskCaseRecord.setRouteFile(path);
            taskCaseRecord.setStatus(0 == action ? TestingStatusEnum.PASS.getCode() : TestingStatusEnum.NO_PASS.getCode());
            taskCaseRecord.setEndTime(LocalDateTime.now());
            taskCaseRecordMapper.updateById(taskCaseRecord);

            log.info("save task case info");
            TjTaskCase taskCase = new TjTaskCase();
            taskCase.setTestTotalTime(String.valueOf(DateUtils.durationToSeconds(originalTrajectory.getDuration())));
            taskCase.setEndTime(new Date());
            taskCase.setStatus(TaskCaseStatusEnum.FINISHED.getCode());
            taskCase.setPassingRate(0 == action ? "100%" : "0%");
            QueryWrapper<TjTaskCase> updateMapper = new QueryWrapper<>();
            updateMapper.eq(ColumnName.TASK_ID, taskCaseRecord.getTaskId()).eq(ColumnName.CASE_ID_COLUMN,
                    taskCaseRecord.getCaseId());
            taskCaseMapper.update(taskCase, updateMapper);

            QueryWrapper<TjTaskCase> queryMapper = new QueryWrapper<>();
            queryMapper.eq(ColumnName.TASK_ID, taskCaseRecord.getTaskId());
            List<TjTaskCase> tjTaskCases = taskCaseMapper.selectList(queryMapper);
            if (CollectionUtils.emptyIfNull(tjTaskCases).stream().allMatch(item ->
                    TaskCaseStatusEnum.FINISHED.getCode().equals(item.getStatus()))) {
                log.info("任务{}下所有用例已完成", taskCaseRecord.getTaskId());
                TjTask tjTask = new TjTask();
                tjTask.setId(tjTaskCases.get(0).getTaskId());
                tjTask.setEndTime(new Date());
                tjTask.setTestTotalTime(DateUtils.secondsToDuration(tjTaskCases.stream().mapToInt(caseObj ->
                        Integer.parseInt(caseObj.getTestTotalTime())).sum()));
                tjTask.setStatus(TaskStatusEnum.FINISHED.getCode());
                taskMapper.updateById(tjTask);
            }
            log.info("更新完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean saveTaskRouteFile2(TjTaskCaseRecord taskCaseRecord, List<List<ClientSimulationTrajectoryDto>> data, Integer action) throws Exception {
        log.info("保存任务 {} 用例 {} 测试记录 {} 轨迹长度:{} action:{}", taskCaseRecord.getTaskId(),
                taskCaseRecord.getCaseId(), taskCaseRecord.getId(), data.size(), action);
        String path = FileUtils.writeRoute(data, WanjiConfig.getRoutePath(), Extension.TXT);
        log.info("路径文件:{}", path);
        log.info("评价文件:{}", path);
        CaseTrajectoryDetailBo trajectoryDetailBo = JSONObject.parseObject(taskCaseRecord.getDetailInfo(), CaseTrajectoryDetailBo.class);
        updateRecordDetailInfo(taskCaseRecord.getId(), trajectoryDetailBo, data);
        String duration = DateUtils.secondsToDuration((int) Math.floor((data.size())) / 10);
        trajectoryDetailBo.setDuration(duration);
        taskCaseRecord.setRouteFile(path);
        // todo 评价文件取存方式待修改
        taskCaseRecord.setEvaluatePath(path);
        taskCaseRecord.setStatus(0 == action ? TestingStatusEnum.PASS.getCode() : TestingStatusEnum.NO_PASS.getCode());
        taskCaseRecord.setEndTime(LocalDateTime.now());
        int result = taskCaseRecordMapper.updateById(taskCaseRecord);
        log.info("测试记录{}更新完毕:{}", taskCaseRecord.getId(), result);
        return result > 0;
    }

    public void checkMain(List<List<ClientSimulationTrajectoryDto>> data, String mainSource) {
        if (StringUtils.isBlank(mainSource)) {
            return;
        }
        for (List<ClientSimulationTrajectoryDto> trajectory : CollectionUtils.emptyIfNull(data)) {
            for (ClientSimulationTrajectoryDto dto : CollectionUtils.emptyIfNull(trajectory)) {
                if (mainSource.equals(dto.getSource())) {
                    dto.setMain(Boolean.TRUE);
                }
            }
        }
    }

    /**
     * 仿真轨迹点位检查
     *
     * @param oldDetail
     * @param data
     */
    public List<ParticipantTrajectoryVo> checkSimulaitonRoute2(CaseTrajectoryDetailBo oldDetail, List<TrajectoryValueDto> data, Long time) throws ParseException {
        if (CollectionUtils.isEmpty(data) || ObjectUtils.isEmpty(oldDetail)
                || CollectionUtils.isEmpty(oldDetail.getParticipantTrajectories())) {
            return null;
        }
        List<ParticipantTrajectoryVo> res = new ArrayList<>();
        checkSimulationSingle(oldDetail, data);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        for (ParticipantTrajectoryBo trajectoryBo : oldDetail.getParticipantTrajectories()) {
            ParticipantTrajectoryVo participantTrajectoryVo = new ParticipantTrajectoryVo();
            participantTrajectoryVo.setId(trajectoryBo.getId());
            for (TrajectoryDetailBo trajectoryDetailBo : trajectoryBo.getTrajectory()) {
                Double restime = 0D;
                if (trajectoryDetailBo.getDate() != null) {
                    restime = (dateFormat.parse(trajectoryDetailBo.getDate()).getTime() - time) / 1000D;
                    if (restime < 0.9) {
                        restime = 0D;
                    }
                }
                TrajectoryDetailVo trajectoryDetailVo = new TrajectoryDetailVo(trajectoryDetailBo.getFrameId(), trajectoryDetailBo.getPass(), trajectoryDetailBo.getSpeed(), String.valueOf(restime));
                participantTrajectoryVo.addtrajectory(trajectoryDetailVo);
            }
            res.add(participantTrajectoryVo);
        }
        return res;
    }

    /**
     * 更新任务用例测试记录点位详情
     * @param recordId
     * @param oldDetail
     * @param data
     */
    public void updateRecordDetailInfo(Integer recordId, CaseTrajectoryDetailBo oldDetail, List<List<ClientSimulationTrajectoryDto>> data) {
        if (ObjectUtils.isEmpty(recordId) || ObjectUtils.isEmpty(oldDetail) || CollectionUtils.isEmpty(data)) {
            return;
        }
        checkRealTraSingle(oldDetail, data);
    }

    /**
     * 检验仿真验证轨迹点位
     * @param oldDetail
     * @param data
     * @return
     */
    public boolean checkSimulationSingle(CaseTrajectoryDetailBo oldDetail, List<TrajectoryValueDto> data) {
        boolean update = false;
        for (TrajectoryValueDto trajectory : data) {
            for (ParticipantTrajectoryBo trajectoryBo : oldDetail.getParticipantTrajectories()) {
                if (!StringUtils.equals(trajectoryBo.getName(), trajectory.getName())) {
                    continue;
                }
                List<TrajectoryDetailBo> points = trajectoryBo.getTrajectory();
                for (TrajectoryDetailBo trajectoryDetailBo : points) {
                    if (!ObjectUtils.isEmpty(trajectoryDetailBo.getPass()) && trajectoryDetailBo.getPass()) {
                        continue;
                    }
                    String[] positionArray = trajectoryDetailBo.getPosition().split(",");
                    double longitude = Double.parseDouble(positionArray[0]);
                    double latitude = Double.parseDouble(positionArray[1]);
                    double instance = GeoUtil.calculateDistance(latitude, longitude,
                            trajectory.getLatitude(), trajectory.getLongitude());
                    if (instance <= 3) {
                        trajectoryDetailBo.setPass(true);
                        trajectoryDetailBo.setReason("已校验完成");
                        trajectoryDetailBo.setDate(trajectory.getTimestamp());
                        trajectoryDetailBo.setSpeed(Double.valueOf(trajectory.getSpeed()));
                    } else {
                        trajectoryDetailBo.setPass(false);
                        trajectoryDetailBo.setReason("未经过该点位3米范围区域");
                    }
                    update = true;
                }
            }
        }
        return update;
    }

    public void checkinfinite(Map<String, Boolean> mainId, List<TrajectoryValueDto> data, List<InElement> inElements){
        for (TrajectoryValueDto value : data) {
            if (mainId.containsKey(value.getId())){
                if (mainId.get(value.getId())) {
                    for (InElement inElement : inElements) {
                        if (StringUtils.equals(value.getId(), inElement.getId().toString())) {
                            double longitude = Double.parseDouble(inElement.getRoute().get(0).getLongitude());
                            double latitude = Double.parseDouble(inElement.getRoute().get(0).getLatitude());
                            double instance = GeoUtil.calculateDistance(latitude, longitude,
                                    value.getLatitude(), value.getLongitude());
                            if (instance <= 2){
                                mainId.put(value.getId(), false);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 检验实车测试过程中的轨迹点位
     * @param oldDetail
     * @param data
     * @return
     */
    public boolean checkRealTraSingle(CaseTrajectoryDetailBo oldDetail, List<List<ClientSimulationTrajectoryDto>> data) {
        boolean update = false;
        for (List<ClientSimulationTrajectoryDto> trajectoryDtoList : data) {
            for (ClientSimulationTrajectoryDto clientTrajectoryDto : trajectoryDtoList) {
                for (TrajectoryValueDto trajectory : clientTrajectoryDto.getValue()) {
                    for (ParticipantTrajectoryBo trajectoryBo : oldDetail.getParticipantTrajectories()) {
                        if (!StringUtils.equals(trajectoryBo.getId(), trajectory.getId())) {
                            continue;
                        }
                        List<TrajectoryDetailBo> points = trajectoryBo.getTrajectory();
                        for (TrajectoryDetailBo trajectoryDetailBo : points) {
                            if (!ObjectUtils.isEmpty(trajectoryDetailBo.getPass()) && trajectoryDetailBo.getPass()) {
                                continue;
                            }
                            String[] positionArray = trajectoryDetailBo.getPosition().split(",");
                            double longitude = Double.parseDouble(positionArray[0]);
                            double latitude = Double.parseDouble(positionArray[1]);
                            double instance = GeoUtil.calculateDistance(latitude, longitude,
                                    trajectory.getLatitude(), trajectory.getLongitude());
                            if (instance <= 3) {
                                trajectoryDetailBo.setPass(true);
                                trajectoryDetailBo.setReason("已校验完成");
                                trajectoryDetailBo.setDate(trajectory.getTimestamp());
                                trajectoryDetailBo.setSpeed(Double.valueOf(trajectory.getSpeed()));
                            } else {
                                trajectoryDetailBo.setPass(false);
                                trajectoryDetailBo.setReason("未经过该点位3米范围区域");
                            }
                            update = true;
                        }
                    }
                }
            }
        }
        return update;
    }

    public Map<String, List<Map<String, Double>>> extractRoute(List<List<TrajectoryValueDto>> data) {
        if (CollectionUtils.isEmpty(data)) {
            return null;
        }
        Map<String, List<Map<String, Double>>> result = new HashMap<>();
        try {
            for (List<TrajectoryValueDto> vehicleList : data) {
                Map<String, Map<String, Double>> idAndPoint = vehicleList.stream().collect(Collectors.toMap(
                        item -> String.valueOf(item.getId()),
                        value -> {
                            Map<String, Double> map = new HashMap<>();
                            map.put("longitude", Double.parseDouble(String.valueOf(value.getLongitude())));
                            map.put("latitude", Double.parseDouble(String.valueOf(value.getLatitude())));
                            return map;
                        }, (oldValue, newValue) -> oldValue));
                for (Map.Entry<String, Map<String, Double>> item : idAndPoint.entrySet()) {
                    if (!result.containsKey(item.getKey())) {
                        List<Map<String, Double>> list = new ArrayList<>();
                        list.add(item.getValue());
                        result.put(item.getKey(), list);
                        continue;
                    }
                    List<Map<String, Double>> list = result.get(item.getKey());
                    list.add(item.getValue());
                    result.put(item.getKey(), list);
                }
            }
        } catch (Exception e) {
            log.error("轨迹文件读取异常，请重新进行验证");
        }
        return result;
    }

    /**
     * 读取仿真验证轨迹文件
     *
     * @param fileName
     * @param participantId
     * @return
     * @throws IOException
     */
    public List<List<TrajectoryValueDto>> readTrajectoryFromRouteFile(String fileName, String participantId) throws IOException {
        List<List<TrajectoryValueDto>> data = readRouteFile(fileName);
        return readTrajectoryFromData(data, participantId);
    }

    public List<List<TrajectoryValueDto>> readRouteFile(String fileName) throws IOException {
        String routeFile = FileUploadUtils.getAbsolutePathFileName(fileName);
        return FileUtils.readTrajectory(routeFile);
    }

    public List<List<TrajectoryValueDto>> readTrajectoryFromData(List<List<TrajectoryValueDto>> data, String participantId) {
        return CollectionUtils.emptyIfNull(data).stream().map(item -> filterParticipant(item, participantId))
                .collect(Collectors.toList());
    }

    public List<TrajectoryValueDto> filterParticipant(List<TrajectoryValueDto> data, String participantId) {
        return StringUtils.isNotEmpty(participantId)
                ? CollectionUtils.emptyIfNull(data).stream().filter(route ->
                participantId.equals(route.getId())).collect(Collectors.toList())
                : data;
    }


    public List<SimulationTrajectoryDto> readOriRouteFile(String fileName) throws IOException {
        String routeFile = FileUploadUtils.getAbsolutePathFileName(fileName);
        return FileUtils.readOriTrajectory(routeFile);
    }

    public List<SimulationTrajectoryDto> mainTrajectory(String fileName,String id)
        throws BusinessException {
        List<SimulationTrajectoryDto> participantTrajectories = null;
        try {
            participantTrajectories = this.readOriRouteFile(fileName);
            participantTrajectories = participantTrajectories.stream()
                .filter(item -> !ObjectUtils.isEmpty(item.getValue())
                    && item.getValue().stream().anyMatch(p -> id.equals(p.getId())))
                .peek(s -> s.setValue(s.getValue().stream().filter(p -> id.equals(p.getId()))
                    .collect(Collectors.toList())))
                .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("mainTrajectory!", e);
            throw new BusinessException("查询主车轨迹失败");
        }
        if (CollectionUtils.isEmpty(participantTrajectories)) {
            throw new BusinessException("查询主车轨迹失败");
        }

        return participantTrajectories;
    }

    public List<SimulationTrajectoryDto> readOriTrajectoryFromData(List<SimulationTrajectoryDto> data, String participantId) {
        for (SimulationTrajectoryDto simulationTrajectoryDto : data) {
            simulationTrajectoryDto.setValue(filterParticipant(simulationTrajectoryDto.getValue(), participantId));
        }
        return data;
    }

    /**
     * 从仿真验证轨迹文件中读取主车轨迹
     * @param fileName
     * @return
     * @throws IOException
     */
    public List<TrajectoryValueDto> readMainTrajectoryFromOriRoute(String fileName) throws IOException {
        List<SimulationTrajectoryDto> participantTrajectories = readOriRouteFile(fileName);
        return participantTrajectories.stream()
                .map(SimulationTrajectoryDto::getValue)
                .filter(value -> !ObjectUtils.isEmpty(value))
                .flatMap(List::stream)
                .filter(a -> 1 == a.getDriveType())
                .collect(Collectors.toList());
    }


    /**
     * 读取实车验证轨迹文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public List<RealTestTrajectoryDto> readRealTrajectoryFromRouteFile(String fileName) {
        return readRealRouteFile(fileName);
    }

    public List<RealTestTrajectoryDto> readRealRouteFile(String fileName) {
        String routeFile = FileUploadUtils.getAbsolutePathFileName(fileName);
        return FileUtils.readRealRouteFile(routeFile);
    }


    /**
     * 读取实车验证轨迹文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public List<List<ClientSimulationTrajectoryDto>> readRealTrajectoryFromRouteFile2(String fileName) {
        return readRealRouteFile2(fileName);
    }

    public List<List<ClientSimulationTrajectoryDto>> readRealRouteFile2(String fileName) {
        String routeFile = FileUploadUtils.getAbsolutePathFileName(fileName);
        return FileUtils.readRealRouteFile2(routeFile);
    }

    public SceneTrajectoryBo resetTrajectoryProp(CaseTrajectoryDetailBo caseTrajectoryDetailBo) {
        try {
            for (ParticipantTrajectoryBo participantTrajectoryBo : caseTrajectoryDetailBo.getParticipantTrajectories()) {
                participantTrajectoryBo.setDuration("00:00");
                for (TrajectoryDetailBo trajectoryDetailBo : participantTrajectoryBo.getTrajectory()) {
                    trajectoryDetailBo.setReason("等待校验");
                    trajectoryDetailBo.setPass(Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            log.error("重置轨迹属性异常", e);
        }
        return caseTrajectoryDetailBo;
    }
}
