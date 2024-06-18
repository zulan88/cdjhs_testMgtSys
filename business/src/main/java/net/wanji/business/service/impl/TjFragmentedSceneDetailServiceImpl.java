package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.common.Constants;
import net.wanji.business.common.Constants.ChannelBuilder;
import net.wanji.business.common.Constants.ColumnName;
import net.wanji.business.common.Constants.ContentTemplate;
import net.wanji.business.common.Constants.PartRole;
import net.wanji.business.common.Constants.PartType;
import net.wanji.business.common.Constants.PlaybackAction;
import net.wanji.business.common.Constants.PointTypeEnum;
import net.wanji.business.common.Constants.SysType;
import net.wanji.business.common.Constants.YN;
import net.wanji.business.domain.bo.ParticipantTrajectoryBo;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.business.domain.bo.TrajectoryDetailBo;
import net.wanji.business.domain.dto.SceneDebugDto;
import net.wanji.business.domain.dto.SceneQueryDto;
import net.wanji.business.domain.dto.TjDeviceDetailDto;
import net.wanji.business.domain.dto.TjFragmentedSceneDetailDto;
import net.wanji.business.domain.param.GeneralizeScene;
import net.wanji.business.domain.param.TessParam;
import net.wanji.business.domain.param.TestStartParam;
import net.wanji.business.domain.vo.DeviceDetailVo;
import net.wanji.business.domain.vo.FragmentedScenesDetailVo;
import net.wanji.business.domain.vo.SceneDetailVo;
import net.wanji.business.entity.*;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjCaseMapper;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.business.mapper.TjFragmentedSceneDetailMapper;
import net.wanji.business.mapper.TjFragmentedScenesMapper;
import net.wanji.business.schedule.PlaybackSchedule;
import net.wanji.business.service.*;
import net.wanji.business.trajectory.RedisTrajectory2Consumer;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.CounterUtil;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.bean.BeanUtils;
import net.wanji.system.service.ISysDictDataService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 片段式场景定义 服务实现类
 * </p>
 *
 * @author wj
 * @since 2023-06-27
 */
@Service
public class TjFragmentedSceneDetailServiceImpl
        extends ServiceImpl<TjFragmentedSceneDetailMapper, TjFragmentedSceneDetail>
        implements TjFragmentedSceneDetailService {

    @Autowired
    private TjFragmentedScenesService scenesService;

    @Autowired
    private TjFragmentedScenesMapper scenesMapper;

    @Autowired
    private TjFragmentedSceneDetailMapper sceneDetailMapper;

    @Autowired
    private TjCaseMapper caseMapper;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private TjResourcesDetailService tjResourcesDetailService;

    @Autowired
    private RestService restService;

    @Autowired
    private RedisTrajectory2Consumer redisTrajectoryConsumer;

    @Autowired
    private RouteService routeService;

    @Autowired
    private TjDeviceDetailMapper deviceDetailMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    TjGeneralizeSceneService generalizeSceneService;

    @Autowired
    private ITjAtlasVenueService tjAtlasVenueService;

    @Override
    public FragmentedScenesDetailVo getDetailVo(Integer id, Integer type) throws BusinessException {
        QueryWrapper<TjFragmentedSceneDetail> queryWrapper = new QueryWrapper();
        queryWrapper.eq(ColumnName.SCENE_DETAIL_ID_COLUMN, id);
        TjFragmentedSceneDetail detail = this.getOne(queryWrapper);
        FragmentedScenesDetailVo detailVo = new FragmentedScenesDetailVo();
        if (null != detail) {
            if (!ObjectUtils.isEmpty(detail)) {
                BeanUtils.copyBeanProp(detailVo, detail);
                detailVo.setTypeName("集群目标物测试场景");
                if(type != null){
                    detailVo.setSimuType(type);
                }
            }
        }
        TjAtlasVenue atlasVenue = tjAtlasVenueService.getById(detail.getMapId());
        detailVo.setResourcesName(atlasVenue.getName());
        return detailVo;
    }

    @Override
    public void playback(Integer id, String participantId, int action) throws BusinessException, IOException {
        FragmentedScenesDetailVo scenesDetailVo = this.getDetailVo(id, null);
        String key = ChannelBuilder.buildScenePreviewChannel(SecurityUtils.getUsername(), id);
        switch (action) {
            case PlaybackAction.START:
                if (StringUtils.isEmpty(scenesDetailVo.getRouteFile())) {
                    throw new BusinessException("未进行仿真验证");
                }
                List<List<TrajectoryValueDto>> routeList = routeService.readTrajectoryFromRouteFile(
                        scenesDetailVo.getRouteFile(), participantId);
                if (CollectionUtils.isEmpty(routeList)) {
                    throw new BusinessException("轨迹文件读取异常");
                }
                PlaybackSchedule.startSendingData(key, routeList);
                break;
            case PlaybackAction.SUSPEND:
                PlaybackSchedule.suspend(key);
                break;
            case PlaybackAction.CONTINUE:
                PlaybackSchedule.goOn(key);
                break;
            case PlaybackAction.STOP:
                PlaybackSchedule.stopSendingData(key);
                break;
            default:
                break;

        }

    }

    @Override
    public List<List<TrajectoryValueDto>> getroutelist(Integer id, String participantId, int action) throws IOException, BusinessException {
        if (action == 0) {
            FragmentedScenesDetailVo caseInfoBo = this.getDetailVo(id, null);
            return routeService.readTrajectoryFromRouteFile(
                    caseInfoBo.getRouteFile(), participantId);
        }else {
            TjGeneralizeScene generalizeScene = generalizeSceneService.getById(id);
            return routeService.readTrajectoryFromRouteFile(
                    generalizeScene.getRouteFile(), participantId);
        }
    }

    @Override
    public Boolean saveSceneDebug(SceneDebugDto sceneDebugDto) throws BusinessException{
        List<ParticipantTrajectoryBo> list = sceneDebugDto.getTrajectoryJson().getParticipantTrajectories().stream()
                .filter(t -> PartType.MAIN.equals(t.getType()))
                .filter(p -> ObjectUtils.isEmpty(p.getTrajectory().get(0).getPass())
                        || ObjectUtils.isEmpty(p.getTrajectory().get(p.getTrajectory().size() - 1).getPass())
                        || !p.getTrajectory().get(0).getPass()
                        || !p.getTrajectory().get(p.getTrajectory().size() - 1).getPass())
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(list)) {
            return null;
        }

        if(sceneDebugDto.getIsGen()==null || sceneDebugDto.getIsGen().equals(0)) {
            TjFragmentedSceneDetail tjFragmentedSceneDetail = new TjFragmentedSceneDetail();
            tjFragmentedSceneDetail.setId(sceneDebugDto.getId());
            tjFragmentedSceneDetail.setRouteFile(sceneDebugDto.getRouteFile());
            if (sceneDebugDto.getSimuType()!=null&&sceneDebugDto.getSimuType().equals(0)) {
                tjFragmentedSceneDetail.setTrajectoryInfoTime(!ObjectUtils.isEmpty(sceneDebugDto.getTrajectoryJson())
                        ? sceneDebugDto.getTrajectoryJson().buildId().toJsonString()
                        : null);
            }
            return this.updateById(tjFragmentedSceneDetail);
        }else {
            TjGeneralizeScene detail = new TjGeneralizeScene();
            detail.setId(sceneDebugDto.getId());
            detail.setRouteFile(sceneDebugDto.getRouteFile());
            return generalizeSceneService.updateById(detail);
        }
    }

    @Override
    public Integer sortCount(GeneralizeScene generalizeScene) throws BusinessException {
        checkGereneralizeScene(generalizeScene);
        FragmentedScenesDetailVo detailVo = getDetailVo(generalizeScene.getId(), null);
        List<ParticipantTrajectoryBo> participantTrajectoryBos = detailVo.getTrajectoryJson().getParticipantTrajectories().stream()
                .filter(t -> PartType.MAIN.equals(t.getType())).collect(Collectors.toList());
        Optional.of(participantTrajectoryBos)
                .filter(CollectionUtils::isNotEmpty)
                .filter(list -> list.size() == 1)
                .map(list -> list.get(0))
                .map(ParticipantTrajectoryBo::getTrajectory)
                .filter(trajectorys -> !trajectorys.isEmpty())
                .filter(trajectorys -> {
                    int count = 0;
                    for (TrajectoryDetailBo trajectoryDetailBo : trajectorys){
                        if (trajectoryDetailBo.getType().equals("conflict")){
                            count++;
                        }
                    }
                    return count == 1;
                })
                .orElseThrow(() -> new BusinessException("未找到有效的参与者轨迹信息 (需配置一辆主车并配置且仅配置一个冲突点)"));
        List<TrajectoryDetailBo> trajectorys = participantTrajectoryBos.get(0).getTrajectory();
        double conflictspeed = trajectorys.stream()
                .filter(trajectoryDetailBo -> "conflict".equals(trajectoryDetailBo.getType()))
                .map(TrajectoryDetailBo::getSpeed)
                .findFirst().get();

        double minSpeed = generalizeScene.getMinSpeed();
        double maxSpeed = generalizeScene.getMaxSpeed();
        int step = generalizeScene.getStep();
        if (step > (conflictspeed - minSpeed) && step > (maxSpeed - conflictspeed)) {
            throw new BusinessException("速度间隔过大，无法依据规则泛化");
        }
        if (conflictspeed > maxSpeed || conflictspeed < minSpeed) {
            throw new BusinessException("冲突点速度超出速度区间");
        }
        int num = detailVo.getTrajectoryJson().getParticipantTrajectories().size();
        List<Integer> numList = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            numList.add(i);
        }
        List<List<Integer>> combinations = findCombinations(numList, generalizeScene.getType());
        for (List<Integer> combination : combinations) {
            Collections.sort(combination);
        }
        Set<List<Integer>> combinationset = new HashSet<>(combinations);
        int down = (int) ((conflictspeed - minSpeed) / step);
        int up = (int) ((maxSpeed - conflictspeed) / step);
        return (down+up)*combinationset.size();
    }

    @Override
    public boolean stopSence(Integer id) {
        TjFragmentedSceneDetail fragmentedSceneDetail = this.getById(id);
        String channel = Constants.ChannelBuilder.buildSimulationChannel(SecurityUtils.getUsername(), fragmentedSceneDetail.getNumber());
        TjDeviceDetailDto deviceDetailDto = new TjDeviceDetailDto();
        deviceDetailDto.setSupportRoles(Constants.PartRole.MV_SIMULATION);
        List<DeviceDetailVo> deviceDetailVos = deviceDetailMapper.selectByCondition(deviceDetailDto);
        if (CollectionUtils.isEmpty(deviceDetailVos)) {
            return false;
        }
        DeviceDetailVo detailVo = deviceDetailVos.get(0);
        return restService.stopTessNg(detailVo.getIp(), detailVo.getServiceAddress(),channel,1);
    }

    void checkGereneralizeScene(GeneralizeScene sceneDetailDto) throws BusinessException {
        Optional.ofNullable(sceneDetailDto.getId())
                .orElseThrow(() -> new BusinessException("参数不完整"));
        Optional.ofNullable(sceneDetailDto.getStep())
                .orElseThrow(() -> new BusinessException("参数不完整"));
        Optional.ofNullable(sceneDetailDto.getMaxSpeed())
                .orElseThrow(() -> new BusinessException("参数不完整"));
        Optional.ofNullable(sceneDetailDto.getMinSpeed())
                .orElseThrow(() -> new BusinessException("参数不完整"));
        if(sceneDetailDto.getMinSpeed()>=sceneDetailDto.getMaxSpeed()){
            throw new BusinessException("最小速度不能大于等于最大速度");
        }
    }

    @Override
    public List<String> getalllabel(String id) {
        return sceneDetailMapper.getalllabel(id);
    }

    @Override
    public List<SceneDetailVo> selectTjSceneDetailListBylabels(List<List<Integer>> lists) {
        return sceneDetailMapper.selectTjSceneDetailListBylabels(lists);
    }

    @Override
    public List<SceneDetailVo> selectTjSceneDetailListAnd(List<Integer> labellist, Integer fragmentedSceneId) {
        return sceneDetailMapper.selectTjSceneDetailListAnd(labellist, fragmentedSceneId);
    }

    @Override
    public List<SceneDetailVo> selectTjSceneDetailListOr(List<Integer> labellist, Integer fragmentedSceneId) {
        return sceneDetailMapper.selectTjSceneDetailListOr(labellist, fragmentedSceneId);
    }

    private void detailVoTranslate(FragmentedScenesDetailVo detailVo) {
        // 地图
//        TjResourcesDetail tjResourcesDetail = tjResourcesDetailService.getById(
//                detailVo.getResourcesDetailId());
//        detailVo.setResourcesName(tjResourcesDetail.getName());
//        detailVo.setFilePath(tjResourcesDetail.getFilePath());
//        detailVo.setGeoJsonPath(tjResourcesDetail.getAttribute4());
        // 道路类型
        detailVo.setRoadTypeName(dictDataService.selectDictLabel(SysType.ROAD_TYPE
                , detailVo.getRoadType()));
        // 场景复杂度
        detailVo.setSceneComplexityName(dictDataService.selectDictLabel(SysType.SCENE_COMPLEXITY
                , detailVo.getSceneComplexity()));
        // 交通流状态
        detailVo.setTrafficFlowStatusName(dictDataService.selectDictLabel(SysType.TRAFFIC_FLOW_STATUS
                , detailVo.getTrafficFlowStatus()));
        // 路面状况
        detailVo.setRoadConditionName(dictDataService.selectDictLabel(SysType.ROAD_CONDITION
                , detailVo.getRoadCondition()));
        // 天气
        detailVo.setWeatherName(dictDataService.selectDictLabel(SysType.WEATHER
                , detailVo.getWeather()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSceneDetail(TjFragmentedSceneDetailDto sceneDetailDto) throws BusinessException {
        TjFragmentedSceneDetail detail = new TjFragmentedSceneDetail();
        BeanUtils.copyBeanProp(detail, sceneDetailDto);
        if (ObjectUtils.isEmpty(sceneDetailDto.getId())) {
            detail.setNumber(StringUtils.isEmpty(sceneDetailDto.getNumber()) ? buildSceneNumber() : sceneDetailDto.getNumber());
            detail.setCreatedBy(SecurityUtils.getUsername());
            detail.setCreatedDate(LocalDateTime.now());
        } else {
            detail.setUpdatedBy(SecurityUtils.getUsername());
            detail.setUpdatedDate(LocalDateTime.now());
        }
        if(!ObjectUtils.isEmpty(sceneDetailDto.getMapId())){
            TjAtlasVenue tjAtlasVenue = tjAtlasVenueService.getById(sceneDetailDto.getMapId());
            detail.setMapFile(tjAtlasVenue.getGeoJsonPath());
        }
        if(sceneDetailDto.getTrajectoryJson()!=null) {
            if (sceneDetailDto.getTrajectoryJson().getParticipantTrajectories().stream().filter(item -> item.getType().equals(PartType.MAIN)).count() > 1) {
                throw new BusinessException("一个场景中只能有一个AV车");
            }
        }
        List<String> labellist = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sceneDetailDto.getLabelList())) {
            for (String id : sceneDetailDto.getLabelList()) {
                labellist.addAll(this.getalllabel(id));
            }
        }
        if (Integer.valueOf(1).equals(sceneDetailDto.getFinished())) {
            detail.setFinished(true);
        }
        detail.setLabel(CollectionUtils.isNotEmpty(sceneDetailDto.getLabelList())
                ? String.join(",", sceneDetailDto.getLabelList())
                : null);
        detail.setAllStageLabel(CollectionUtils.isNotEmpty(labellist)
                ? labellist.stream().distinct().collect(Collectors.joining(","))
                : null);
        if (sceneDetailDto.getSimuType()!=null&&sceneDetailDto.getSimuType().equals(0)) {
            detail.setTrajectoryInfoTime(!ObjectUtils.isEmpty(sceneDetailDto.getTrajectoryJson())
                    ? sceneDetailDto.getTrajectoryJson().buildId().toJsonString()
                    : null);
        } else {
            detail.setTrajectoryInfo(!ObjectUtils.isEmpty(sceneDetailDto.getTrajectoryJson())
                    ? sceneDetailDto.getTrajectoryJson().buildId().toJsonString()
                    : null);
        }
        if (!ObjectUtils.isEmpty(sceneDetailDto.getReferencePoints())){
            detail.setReferencePoint(sceneDetailDto.getReferencePoints().toJSONString());
        }
        boolean flag = this.saveOrUpdate(detail);
        sceneDetailDto.setId(detail.getId());
        return flag;
    }

    @Override
    public boolean saveGeneralScene(TjFragmentedSceneDetailDto sceneDetailDto) throws BusinessException {
        TjGeneralizeScene detail = new TjGeneralizeScene();
        detail.setId(sceneDetailDto.getId());

        List<String> labellist = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(sceneDetailDto.getLabelList())) {
            for (String id : sceneDetailDto.getLabelList()) {
                labellist.addAll(this.getalllabel(id));
            }
        }
        if (!StringUtils.isEmpty(sceneDetailDto.getRouteFile())) {
            detail.setRouteFile(sceneDetailDto.getRouteFile());
        }
        detail.setLabel(CollectionUtils.isNotEmpty(sceneDetailDto.getLabelList())
                ? String.join(",", sceneDetailDto.getLabelList())
                : null);
        detail.setAllStageLabel(CollectionUtils.isNotEmpty(labellist)
                ? labellist.stream().distinct().collect(Collectors.joining(","))
                : null);
        detail.setTrajectoryInfo(!ObjectUtils.isEmpty(sceneDetailDto.getTrajectoryJson())
                ? sceneDetailDto.getTrajectoryJson().buildId().toJsonString()
                : null);
        return generalizeSceneService.updateById(detail);
    }

    /**
     * 对场景进行泛化
     *
     * @param sceneDetailDto 场景详细信息
     * @throws BusinessException 业务异常
     */
    @Override
    public void generalizeScene(GeneralizeScene sceneDetailDto) throws BusinessException {
        checkGereneralizeScene(sceneDetailDto);
        FragmentedScenesDetailVo detailVo = getDetailVo(sceneDetailDto.getId(), null);
        List<ParticipantTrajectoryBo> participantTrajectoryBos = detailVo.getTrajectoryJson().getParticipantTrajectories().stream()
                .filter(t -> PartType.MAIN.equals(t.getType())).collect(Collectors.toList());
        Optional.of(participantTrajectoryBos)
                .filter(CollectionUtils::isNotEmpty)
                .filter(list -> list.size() == 1)
                .map(list -> list.get(0))
                .map(ParticipantTrajectoryBo::getTrajectory)
                .filter(trajectorys -> !trajectorys.isEmpty())
                .filter(trajectorys -> {
                    int count = 0;
                    for (TrajectoryDetailBo trajectoryDetailBo : trajectorys){
                        if (trajectoryDetailBo.getType().equals("conflict")){
                            count++;
                        }
                    }
                    return count == 1;
                })
                .orElseThrow(() -> new BusinessException("未找到有效的参与者轨迹信息 (需配置一辆主车并配置且仅配置一个冲突点)"));
        List<TrajectoryDetailBo> trajectorys = participantTrajectoryBos.get(0).getTrajectory();
        double conflictspeed = trajectorys.stream()
                .filter(trajectoryDetailBo -> "conflict".equals(trajectoryDetailBo.getType()))
                .map(TrajectoryDetailBo::getSpeed)
                .findFirst().get();

        double minSpeed = sceneDetailDto.getMinSpeed();
        double maxSpeed = sceneDetailDto.getMaxSpeed();
        int step = sceneDetailDto.getStep();

        if (step > (conflictspeed - minSpeed) && step > (maxSpeed - conflictspeed)) {
            throw new BusinessException("速度间隔过大，无法依据规则泛化");
        }

        if (conflictspeed > maxSpeed || conflictspeed < minSpeed) {
            throw new BusinessException("冲突点速度超出速度区间");
        }

        QueryWrapper<TjGeneralizeScene> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("scene_id", sceneDetailDto.getId());
        generalizeSceneService.remove(queryWrapper);

        int num = detailVo.getTrajectoryJson().getParticipantTrajectories().size();
        List<Integer> numList = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            numList.add(i);
        }
        List<List<Integer>> combinations = findCombinations(numList, sceneDetailDto.getType());
        for (List<Integer> combination : combinations) {
            Collections.sort(combination);
        }
        Set<List<Integer>> combinationset = new HashSet<>(combinations);

        for (List<Integer> combination : combinationset) {
            //向下泛化
            int down = (int) ((conflictspeed - minSpeed) / step);
            double proStep = step / conflictspeed;
            for (int i = 1; i <= down; i++) {
                SceneTrajectoryBo caseTrajectoryDetailBo = detailVo.getTrajectoryJson();
                int finalI = i;
                AtomicInteger index = new AtomicInteger(1);
                caseTrajectoryDetailBo.getParticipantTrajectories().forEach(participantTrajectoryBoList -> {
                    if (combination.contains(index.get())) {
                        participantTrajectoryBoList.getTrajectory().forEach(trajectoryDetailBo -> {
                            Double speed = trajectoryDetailBo.getSpeed();
                            if (speed != null) {
                                trajectoryDetailBo.setSpeed(speed * (1 - finalI * proStep));
                            }
                        });
                    }
                    index.getAndIncrement();
                });
                TjGeneralizeScene generalizeScene = new TjGeneralizeScene();
                generalizeScene.setSceneId(sceneDetailDto.getId());
                generalizeScene.setNumber(buildSceneNumber());
                generalizeScene.setLabel(detailVo.getLabel());
                generalizeScene.setTrajectoryInfo(caseTrajectoryDetailBo.buildId().toJsonString());
                generalizeScene.setAllStageLabel(detailVo.getAllStageLabel());
                generalizeScene.setTestSceneDesc(detailVo.getTestSceneDesc());
                generalizeSceneService.save(generalizeScene);
            }

            //向上泛化
            int up = (int) ((maxSpeed - conflictspeed) / step);
            for (int i = 1; i <= up; i++) {
                SceneTrajectoryBo caseTrajectoryDetailBo = detailVo.getTrajectoryJson();
                int finalI = i;
                AtomicInteger index = new AtomicInteger(1);
                caseTrajectoryDetailBo.getParticipantTrajectories().forEach(participantTrajectoryBoList -> {
                    if (combination.contains(index.get())) {
                        participantTrajectoryBoList.getTrajectory().forEach(trajectoryDetailBo -> {
                            Double speed = trajectoryDetailBo.getSpeed();
                            if (speed != null) {
                                trajectoryDetailBo.setSpeed(speed * (1 + finalI * proStep));
                            }
                        });
                    }
                    index.getAndIncrement();
                });
                TjGeneralizeScene generalizeScene = new TjGeneralizeScene();
                generalizeScene.setSceneId(sceneDetailDto.getId());
                generalizeScene.setNumber(buildSceneNumber());
                generalizeScene.setLabel(detailVo.getLabel());
                generalizeScene.setTrajectoryInfo(caseTrajectoryDetailBo.buildId().toJsonString());
                generalizeScene.setAllStageLabel(detailVo.getAllStageLabel());
                generalizeScene.setTestSceneDesc(detailVo.getTestSceneDesc());
                generalizeSceneService.save(generalizeScene);
            }
        }
    }

    private static List<List<Integer>> findCombinations(List<Integer> nums, Integer type) {
        List<List<Integer>> result = new ArrayList<>();
        if (type == 0){
            result.add(nums);
            return result;
        }else {
            backtracking(nums, new ArrayList<>(), result);
            return result;
        }
    }

    private static void backtracking(List<Integer> nums, List<Integer> tempResult, List<List<Integer>> result) {
        if (!tempResult.isEmpty()) {
            result.add(new ArrayList<>(tempResult));
        }

        for (int i = 0; i < nums.size(); i++) {
            tempResult.add(nums.get(i));
            List<Integer> remainingNums = new ArrayList<>(nums);
            remainingNums.remove(i);
            backtracking(remainingNums, tempResult, result);
            tempResult.remove(tempResult.size() - 1);
        }
    }

    public synchronized String buildSceneNumber() {
        return StringUtils.format(ContentTemplate.SCENE_NUMBER_TEMPLATE, DateUtils.getNowDayString(),
                CounterUtil.getRandomChar());
    }

    @Override
    public List<FragmentedScenesDetailVo> selectScene(SceneQueryDto queryDto) throws BusinessException {
        TjFragmentedScenes scenes = scenesMapper.selectById(queryDto.getFragmentedSceneId());
        if (ObjectUtils.isEmpty(scenes)) {
            throw new BusinessException("场景不存在");
        }
        String typeName = dictDataService.selectDictLabel(SysType.SCENE_TREE_TYPE, scenes.getType());

        List<FragmentedScenesDetailVo> detailVos = null;
        if (YN.Y_INT == scenes.getIsFolder()) {
            List<TjFragmentedScenes> collector = new ArrayList<>();
            scenesService.selectChildrenFromFolder(scenes.getId(), collector);
            List<Integer> sceneIds = collector.stream().map(TjFragmentedScenes::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(sceneIds)) {
                queryDto.setFragmentedSceneId(null);
                queryDto.setFragmentedSceneIds(sceneIds);
            }
        }
        detailVos = sceneDetailMapper.selectByCondition(queryDto);
        CollectionUtils.emptyIfNull(detailVos).forEach(detail -> {
            detail.setTypeName(typeName);
            detail.setRoadTypeName(dictDataService.selectDictLabel(SysType.ROAD_TYPE, detail.getRoadType()));
            detail.setRoadWayName(dictDataService.selectDictLabel(SysType.ROAD_WAY_TYPE, detail.getRoadWayType()));
            detail.setRoadConditionName(dictDataService.selectDictLabel(SysType.ROAD_CONDITION, detail.getRoadCondition()));
            detail.setWeatherName(dictDataService.selectDictLabel(SysType.WEATHER, detail.getWeather()));
            detail.setTrafficFlowStatusName(dictDataService.selectDictLabel(SysType.TRAFFIC_FLOW_STATUS,
                    detail.getTrafficFlowStatus()));
            detail.setSceneComplexityName(dictDataService.selectDictLabel(SysType.SCENE_COMPLEXITY,
                    detail.getSceneComplexity()));
            detail.setSceneTypeName(dictDataService.selectDictLabel(SysType.SCENE_TYPE, detail.getSceneType()));
        });
        return detailVos;
    }

    @Override
    public boolean deleteSceneDetail(Integer id) throws BusinessException {
        int count = caseMapper.selectCountBySceneDetailIds(Collections.singletonList(id));
        if (count > 0) {
            throw new BusinessException("当前子场景下存在测试用例");
        }
        return removeById(id);
    }

    @Override
    public void debugging(SceneDebugDto sceneDebugDto) throws BusinessException {
        String key = ChannelBuilder.buildSimulationChannel(SecurityUtils.getUsername(), sceneDebugDto.getNumber());
        switch (sceneDebugDto.getAction()) {
            case PlaybackAction.START:
                validDebugParam(sceneDebugDto);
                sceneDebugDto.getTrajectoryJson().buildId();
//                TjFragmentedScenes scenes = scenesService.getById(sceneDebugDto.getFragmentedSceneId());
//                if (ObjectUtils.isEmpty(scenes)) {
//                    throw new BusinessException("场景节点不存在");
//                }
                TjDeviceDetailDto deviceDetailDto = new TjDeviceDetailDto();
                deviceDetailDto.setSupportRoles(PartRole.MV_SIMULATION);
                List<DeviceDetailVo> deviceDetailVos = deviceDetailMapper.selectByCondition(deviceDetailDto);
                if (CollectionUtils.isEmpty(deviceDetailVos)) {
                    throw new BusinessException("当前无可用仿真程序");
                }
                sceneDebugDto.getTrajectoryJson().setSceneDesc("集群目标物测试场景");

                TestStartParam testStartParam = buildStartParam(sceneDebugDto);
                sceneDebugDto.getTrajectoryJson().setSceneForm(StringUtils.format(ContentTemplate.SCENE_FORM_TEMPLATE,
                        testStartParam.getAvNum(), testStartParam.getSimulationNum(), testStartParam.getPedestrianNum()));
                redisTrajectoryConsumer.subscribeAndSend(sceneDebugDto);

                List<String> mapList = new ArrayList<>();
                if (ObjectUtils.isEmpty(sceneDebugDto.getMapId())) {
                    mapList.add("10");
                }else {
                    mapList.add(String.valueOf(sceneDebugDto.getMapId()));
                }

                DeviceDetailVo detailVo = deviceDetailVos.get(0);
                int start = restService.startServer(detailVo.getIp(), Integer.valueOf(detailVo.getServiceAddress()),
                        new TessParam().buildSimulationParam(1, testStartParam.getChannel(), testStartParam, mapList));
                if (0==start) {
                    String repeatKey = "DEBUGGING_SCENE_" + sceneDebugDto.getNumber();
                    redisCache.deleteObject(repeatKey);
                    throw new BusinessException("仿真程序连接失败");
                }else if (2==start) {
                    String repeatKey = "DEBUGGING_SCENE_" + sceneDebugDto.getNumber();
                    redisCache.deleteObject(repeatKey);
                    throw new BusinessException("仿真程序忙，请稍后再试");
                }
                break;
            case PlaybackAction.SUSPEND:
                PlaybackSchedule.suspend(key);
                break;
            case PlaybackAction.CONTINUE:
                PlaybackSchedule.goOn(key);
                break;
            case PlaybackAction.STOP:
                redisTrajectoryConsumer.removeListener(key);
                break;
            default:
                break;

        }
    }

    /**
     * 参数校验
     * @param sceneDebugDto
     * @throws BusinessException
     */
    private void validDebugParam(SceneDebugDto sceneDebugDto) throws BusinessException {
        List<ParticipantTrajectoryBo> participantTrajectories = sceneDebugDto.getTrajectoryJson().getParticipantTrajectories();
        if (CollectionUtils.isEmpty(participantTrajectories)) {
            throw new BusinessException("参与者轨迹不能为空");
        }
        for (ParticipantTrajectoryBo participantTrajectory : participantTrajectories) {
            List<TrajectoryDetailBo> trajectory = participantTrajectory.getTrajectory();
            if (CollectionUtils.isEmpty(trajectory)) {
                throw new BusinessException("参与者轨迹不能为空");
            }
            if (trajectory.size() < 2) {
                throw new BusinessException("参与者轨迹点不能少于2个");
            }
            if (!PointTypeEnum.START.getPointType().equals(trajectory.get(0).getType())) {
                throw new BusinessException("参与者轨迹点必须以起点开始");
            }
            if (!PointTypeEnum.END.getPointType().equals(trajectory.get(trajectory.size() - 1).getType())) {
                throw new BusinessException("参与者轨迹点必须以终点结束");
            }
            if (trajectory.stream().anyMatch(t -> StringUtils.isEmpty(t.getPosition()))) {
                throw new BusinessException("参与者轨迹点位置不能为空");
            }
        }
    }

    /**
     * 构建仿真验证实际参数
     * @param sceneDebugDto
     * @return
     */
    private TestStartParam buildStartParam(SceneDebugDto sceneDebugDto) {
        SceneTrajectoryBo sceneTrajectoryBo = sceneDebugDto.getTrajectoryJson();
        // 计算caseInfo.getCaseConfigs()中各个角色的数量
        long avNum = (int) sceneTrajectoryBo.getParticipantTrajectories().stream().filter(trajectoryBo ->
                PartType.MAIN.equals(trajectoryBo.getType())).count();
        long simulationNum = (int) sceneTrajectoryBo.getParticipantTrajectories().stream().filter(trajectoryBo ->
                PartType.SLAVE.equals(trajectoryBo.getType())).count();
        long pedestrianNum = (int) sceneTrajectoryBo.getParticipantTrajectories().stream().filter(trajectoryBo ->
                PartType.PEDESTRIAN.equals(trajectoryBo.getType())).count();
        for (ParticipantTrajectoryBo participantTrajectory : sceneTrajectoryBo.getParticipantTrajectories()) {
            for (TrajectoryDetailBo trajectoryDetailBo : participantTrajectory.getTrajectory()) {
                String[] pos = trajectoryDetailBo.getPosition().split(",");
                if (!ObjectUtils.isEmpty(pos)) {
                    trajectoryDetailBo.setLongitude(pos[0]);
                    trajectoryDetailBo.setLatitude(pos[1]);
                }
            }
        }
        return new TestStartParam(
                ChannelBuilder.buildSimulationChannel(SecurityUtils.getUsername(), sceneDebugDto.getNumber()),
                (int) avNum, (int) simulationNum, (int) pedestrianNum, sceneTrajectoryBo.getParticipantTrajectories());
    }

    @Override
    public List<SceneDetailVo> selectTjFragmentedSceneDetailList(SceneDetailVo sceneDetailVo) {
        String user = SecurityUtils.getUsername();
        if (!user.equals("admin")) {
            sceneDetailVo.setCreatedBy(user);
        }
        return sceneDetailMapper.selectTjFragmentedSceneDetailList(sceneDetailVo);
    }

    @Override
    public boolean deleteSceneByIds(Integer[] ids) throws BusinessException {
        int count = caseMapper.selectCountBySceneDetailIds(Arrays.asList(ids));
        if (count > 0) {
            throw new BusinessException("当前子场景下存在测试用例");
        }
        return this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public boolean updateOne(TjFragmentedSceneDetail sceneDetail) {
        return this.update().update(sceneDetail);
    }


}
