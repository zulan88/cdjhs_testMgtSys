package net.wanji.business.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;
import net.wanji.business.common.Constants;
import net.wanji.business.common.Constants.CaseStatusEnum;
import net.wanji.business.common.Constants.ChannelBuilder;
import net.wanji.business.common.Constants.ColumnName;
import net.wanji.business.common.Constants.ContentTemplate;
import net.wanji.business.common.Constants.ModelEnum;
import net.wanji.business.common.Constants.PartRole;
import net.wanji.business.common.Constants.PlaybackAction;
import net.wanji.business.common.Constants.PointTypeEnum;
import net.wanji.business.common.Constants.SysType;
import net.wanji.business.common.Constants.TestingStatusEnum;
import net.wanji.business.domain.Label;
import net.wanji.business.domain.PartConfigSelect;
import net.wanji.business.domain.bo.CaseConfigBo;
import net.wanji.business.domain.bo.CaseInfoBo;
import net.wanji.business.domain.bo.CaseTrajectoryDetailBo;
import net.wanji.business.domain.bo.ParticipantTrajectoryBo;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.business.domain.dto.CaseQueryDto;
import net.wanji.business.domain.dto.TjCaseDto;
import net.wanji.business.domain.vo.CaseDetailVo;
import net.wanji.business.domain.vo.CasePageVo;
import net.wanji.business.domain.vo.CasePartConfigVo;
import net.wanji.business.domain.vo.CaseVerificationVo;
import net.wanji.business.domain.vo.DeviceDetailVo;
import net.wanji.business.domain.vo.TjCasePartRoleVo;
import net.wanji.business.entity.*;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjCaseMapper;
import net.wanji.business.mapper.TjCasePartConfigMapper;
import net.wanji.business.mapper.TjResourcesDetailMapper;
import net.wanji.business.schedule.PlaybackSchedule;
import net.wanji.business.schedule.SceneLabelMap;
import net.wanji.business.service.*;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.core.domain.SimpleSelect;
import net.wanji.common.core.domain.entity.SysDictData;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.CounterUtil;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.bean.BeanUtils;
import net.wanji.common.utils.file.FileUtils;
import net.wanji.common.utils.spring.SpringUtils;
import net.wanji.system.service.ISysDictDataService;
import net.wanji.system.service.ISysDictTypeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wj
 * @since 2023-06-29
 */
@Service
public class TjCaseServiceImpl extends ServiceImpl<TjCaseMapper, TjCase> implements TjCaseService {

    private static final Logger log = LoggerFactory.getLogger("business");

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private TjFragmentedSceneDetailService sceneDetailService;

    @Autowired
    private TjCasePartConfigService casePartConfigService;

    @Autowired
    private TjDeviceDetailService deviceDetailService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ILabelsService labelsService;

    @Autowired
    private TjCaseRealRecordService caseRealRecordService;

    @Autowired
    private TjCaseMapper caseMapper;

    @Autowired
    private TjResourcesDetailMapper resourcesDetailMapper;

    @Autowired
    private TjCasePartConfigMapper casePartConfigMapper;

    @Autowired
    private SceneLabelMap sceneLabelMap;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private TjGeneralizeSceneService generalizeSceneService;

    @Override
    public Map<String, List<SimpleSelect>> init() {
        List<SysDictData> caseStatus = dictTypeService.selectDictDataByType(SysType.CASE_STATUS);
        List<SysDictData> testType = dictTypeService.selectDictDataByType(SysType.TEST_TYPE);
        Map<String, List<SimpleSelect>> result = new HashMap<>(2);
        result.put(SysType.CASE_STATUS, CollectionUtils.emptyIfNull(caseStatus).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        result.put(SysType.TEST_TYPE, CollectionUtils.emptyIfNull(testType).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        return result;
    }

    @Override
    public Map<String, List<PartConfigSelect>> initEdit(Integer caseId) {
        Map<String, List<PartConfigSelect>> result = new HashMap<>(1);
        CaseDetailVo caseDetailVo = this.selectCaseDetail(caseId);
        handleCasePartConfig(caseDetailVo);
        for (PartConfigSelect partConfigSelect : CollectionUtils.emptyIfNull(caseDetailVo.getPartConfigSelects())) {
            for (CasePartConfigVo part : CollectionUtils.emptyIfNull(partConfigSelect.getParts())) {
                part.setSelected(false);
            }
        }
        result.put(SysType.PART_ROLE, caseDetailVo.getPartConfigSelects());
        return result;
    }

    @Override
    public List<TjCasePartRoleVo> initEditNew(Integer caseId) throws BusinessException {
        List<TjCasePartRoleVo> roleVos = new ArrayList<>();
        TjCase tjCase = this.getById(caseId);
        if (ObjectUtils.isEmpty(tjCase)) {
            throw new BusinessException("用例异常：用例不存在");
        }
        List<CasePartConfigVo> caseConfigs = casePartConfigService.getConfigInfoByCaseId(caseId);
        if (CollectionUtils.isEmpty(caseConfigs)) {
            throw new BusinessException("用例异常：未进行点位配置");
        }
        Map<String, List<CasePartConfigVo>> configByTypeMap = caseConfigs.stream()
                .peek(c -> c.setDeviceId(null)).collect(Collectors.groupingBy(CasePartConfigVo::getBusinessType));
        for (Map.Entry<String, List<CasePartConfigVo>> configEntry : configByTypeMap.entrySet()) {
            TjCasePartRoleVo roleVo = new TjCasePartRoleVo();
            roleVo.setType(configEntry.getKey());
            roleVo.setCasePartConfigs(configEntry.getValue());
            roleVos.add(roleVo);
        }
        return roleVos;
    }

    @Override
    public List<CasePageVo> pageList(CaseQueryDto caseQueryDto, String selectType, AtomicLong total) {
        // gdj:添加 selectType 作为是否按照用户查询标记
        if ("byUsername".equals(selectType)) {
            caseQueryDto.setUserName(SecurityUtils.getUsername());
        }
        List<CaseDetailVo> caseVos = caseMapper.selectCases(caseQueryDto);
        if(total != null){
            total.set(new PageInfo(caseVos).getTotal());
        }
        handleLabels(caseVos);
        List<TjDeviceDetail> deviceDetails = deviceDetailService.list();
        Map<Integer, TjDeviceDetail> deviceMap = CollectionUtils.emptyIfNull(deviceDetails).stream()
                .collect(Collectors.toMap(TjDeviceDetail::getDeviceId, value -> value));
        return CollectionUtils.emptyIfNull(caseVos).stream().map(t -> {
            CasePageVo casePageVo = new CasePageVo();
            BeanUtils.copyBeanProp(casePageVo, t);
            // 状态名称
            casePageVo.setStatusName(dictDataService.selectDictLabel(SysType.CASE_STATUS, casePageVo.getStatus()));
            // 场景分类
            if (StringUtils.isNotEmpty(casePageVo.getLabel())) {
                StringBuilder labelSort = new StringBuilder();
                for (String str : casePageVo.getLabel().split(",")) {
                    try {
                        long intValue = Long.parseLong(str);
                        String labelName = sceneLabelMap.getSceneLabel(intValue);
                        if (StringUtils.isNotEmpty(labelName)) {
                            if (labelSort.length() > 0) {
                                labelSort.append(",").append(labelName);
                            } else {
                                labelSort.append(labelName);
                            }
                        }
                    } catch (NumberFormatException e) {
                        // 处理无效的整数字符串
                    }
                }
                casePageVo.setSceneSort(labelSort.toString());
            }
            if (CollectionUtils.isNotEmpty(casePageVo.getPartConfigs()) && !CaseStatusEnum.WAIT_CONFIG.getCode().equals(casePageVo.getStatus())) {
                TreeMap<String, List<String>> roleConfigDetail = new TreeMap<>();
                StringBuilder roleConfigSort = new StringBuilder();
                TreeMap<String, List<TjCasePartConfig>> roleGroupMap = casePageVo.getPartConfigs().stream()
                        .collect(Collectors.groupingBy(TjCasePartConfig::getParticipantRole, TreeMap::new,
                                Collectors.toList()));
                for (Entry<String, List<TjCasePartConfig>> roleItem : roleGroupMap.entrySet()) {
                    String roleName = dictDataService.selectDictLabel(SysType.PART_ROLE, roleItem.getKey());
                    // 角色配置详情
                    roleConfigDetail.put(roleName, roleItem.getValue().stream().map(TjCasePartConfig::getName).collect(Collectors.toList()));
                    // 角色配置简述
                    if (roleItem.getValue().stream().anyMatch(item -> !ObjectUtils.isEmpty(item.getDeviceId()))) {
                        Map<Integer, List<TjCasePartConfig>> deviceGroup = roleItem.getValue().stream().collect(Collectors.groupingBy(TjCasePartConfig::getDeviceId));
                        for (Entry<Integer, List<TjCasePartConfig>> deviceEntry : deviceGroup.entrySet()) {
                            if (!ObjectUtils.isEmpty(deviceEntry.getKey()) && deviceMap.containsKey(deviceEntry.getKey())) {
                                roleConfigSort.append(StringUtils.format(ContentTemplate.CASE_ROLE_DEVICE_TEMPLATE, deviceMap.get(deviceEntry.getKey()).getDeviceName(), roleName, deviceEntry.getValue().size()));
                            } else {
                                roleConfigSort.append(StringUtils.format(ContentTemplate.CASE_ROLE_TEMPLATE, roleName, deviceEntry.getValue().size()));
                            }
                        }
                    } else {
                        roleConfigSort.append(StringUtils.format(ContentTemplate.CASE_ROLE_TEMPLATE, roleName, roleItem.getValue().size()));
                    }
                }
                casePageVo.setRoleConfigDetail(roleConfigDetail);
                casePageVo.setRoleConfigSort(roleConfigSort.toString());
            }
            return casePageVo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CasePageVo> pageListByIds(List<Integer> ids, Integer treeId) {
        List<CaseDetailVo> caseVos = caseMapper.selectCasesByIds(ids, treeId);
        handleLabels(caseVos);
        List<TjDeviceDetail> deviceDetails = deviceDetailService.list();
        Map<Integer, TjDeviceDetail> deviceMap = CollectionUtils.emptyIfNull(deviceDetails).stream()
                .collect(Collectors.toMap(TjDeviceDetail::getDeviceId, value -> value));
        return CollectionUtils.emptyIfNull(caseVos).stream().map(t -> {
            CasePageVo casePageVo = new CasePageVo();
            BeanUtils.copyBeanProp(casePageVo, t);
            // 状态名称
            casePageVo.setStatusName(dictDataService.selectDictLabel(SysType.CASE_STATUS, casePageVo.getStatus()));
            // 场景分类
            if (StringUtils.isNotEmpty(casePageVo.getLabel())) {
                StringBuilder labelSort = new StringBuilder();
                for (String str : casePageVo.getLabel().split(",")) {
                    try {
                        long intValue = Long.parseLong(str);
                        String labelName = sceneLabelMap.getSceneLabel(intValue);
                        if (StringUtils.isNotEmpty(labelName)) {
                            if (labelSort.length() > 0) {
                                labelSort.append(",").append(labelName);
                            } else {
                                labelSort.append(labelName);
                            }
                        }
                    } catch (NumberFormatException e) {
                        // 处理无效的整数字符串
                    }
                }
                casePageVo.setSceneSort(labelSort.toString());
            }
            if (CollectionUtils.isNotEmpty(casePageVo.getPartConfigs())) {
                TreeMap<String, List<String>> roleConfigDetail = new TreeMap<>();
                StringBuilder roleConfigSort = new StringBuilder();
                TreeMap<String, List<TjCasePartConfig>> roleGroupMap = casePageVo.getPartConfigs().stream()
                        .collect(Collectors.groupingBy(TjCasePartConfig::getParticipantRole, TreeMap::new,
                                Collectors.toList()));
                for (Entry<String, List<TjCasePartConfig>> roleItem : roleGroupMap.entrySet()) {
                    String roleName = dictDataService.selectDictLabel(SysType.PART_ROLE, roleItem.getKey());
                    // 角色配置详情
                    roleConfigDetail.put(roleName, roleItem.getValue().stream().map(TjCasePartConfig::getName).collect(Collectors.toList()));
                    // 角色配置简述
                    if (roleItem.getValue().stream().anyMatch(item -> !ObjectUtils.isEmpty(item.getDeviceId()))) {
                        Map<Integer, List<TjCasePartConfig>> deviceGroup = roleItem.getValue().stream().collect(Collectors.groupingBy(TjCasePartConfig::getDeviceId));
                        for (Entry<Integer, List<TjCasePartConfig>> deviceEntry : deviceGroup.entrySet()) {
                            if (!ObjectUtils.isEmpty(deviceEntry.getKey()) && deviceMap.containsKey(deviceEntry.getKey())) {
                                roleConfigSort.append(StringUtils.format(ContentTemplate.CASE_ROLE_DEVICE_TEMPLATE, deviceMap.get(deviceEntry.getKey()).getDeviceName(), roleName, deviceEntry.getValue().size()));
                            } else {
                                roleConfigSort.append(StringUtils.format(ContentTemplate.CASE_ROLE_TEMPLATE, roleName, deviceEntry.getValue().size()));
                            }
                        }
                    } else {
                        roleConfigSort.append(StringUtils.format(ContentTemplate.CASE_ROLE_TEMPLATE, roleName, roleItem.getValue().size()));
                    }
                }
                casePageVo.setRoleConfigDetail(roleConfigDetail);
                casePageVo.setRoleConfigSort(roleConfigSort.toString());
            }
            return casePageVo;
        }).collect(Collectors.toList());
    }

    @Override
    public Long takeExpense(List<Integer> ids) {
        return caseMapper.takeExpense(ids);
    }

    @Override
    public CaseDetailVo selectCaseDetail(Integer caseId) {
        CaseQueryDto caseQueryDto = new CaseQueryDto();
        caseQueryDto.setId(caseId);
        List<CaseDetailVo> caseVos = caseMapper.selectCases(caseQueryDto);
        if (CollectionUtils.isNotEmpty(caseVos)) {
            handleLabels(caseVos);
            CaseDetailVo caseDetailVo = caseVos.get(0);
            handleCasePartConfig(caseDetailVo);
            CollectionUtils.emptyIfNull(caseDetailVo.getPartConfigSelects()).removeIf(partConfigSelect ->
                    CollectionUtils.isEmpty(partConfigSelect.getParts()));
            return caseDetailVo;
        }
        return null;
    }

    private void handleLabels(List<CaseDetailVo> caseDetails) {
        List<Label> labelList = labelsService.selectLabelsList(new Label());
        Map<Long, String> sceneMap = new HashMap<>();
        for (Label tlabel : labelList) {
            Long parentId = tlabel.getParentId();
            String prelabel = null;
            if (parentId != null) {
                prelabel = sceneMap.getOrDefault(parentId, null);
            } else {
                continue;
            }
            if (tlabel.getId().equals(2L)) {
                continue;
            }
            if (prelabel == null) {
                sceneMap.put(tlabel.getId(), tlabel.getName());
            } else {
                sceneMap.put(tlabel.getId(), prelabel + "-" + tlabel.getName());
            }
        }
        for (CaseDetailVo caseDetailVo : CollectionUtils.emptyIfNull(caseDetails)) {
            handleLabel(caseDetailVo, sceneMap);
        }
    }

    private void handleLabel(CaseDetailVo caseDetailVo, Map<Long, String> sceneMap) {
        List<String> data = new ArrayList<>();
        if (StringUtils.isNotEmpty(caseDetailVo.getLabel())) {
            List<String> labels = Arrays.stream(caseDetailVo.getLabel().split(",")).collect(Collectors.toList());
            for (String str : labels) {
                try {
                    long intValue = Long.parseLong(str);
                    data.add(sceneMap.get(intValue));
                } catch (NumberFormatException e) {
                    // 处理无效的整数字符串
                }
            }
        }
        caseDetailVo.setLabelDetail(data);
    }

    private void handleCasePartConfig(CaseDetailVo caseDetailVo) {
        CaseTrajectoryDetailBo trajectoryDetailBo = JSONObject.parseObject(FileUtils.readFile(caseDetailVo.getDetailInfo()), CaseTrajectoryDetailBo.class);
        List<PartConfigSelect> configSelect = this.getConfigSelect(caseDetailVo.getId(),trajectoryDetailBo, false);
        caseDetailVo.setPartConfigSelects(configSelect);
    }


    @Override
    public List<PartConfigSelect> getConfigSelect(Integer caseId,
                                                  CaseTrajectoryDetailBo caseTrajectoryDetailBo,
                                                  boolean deviceConfig) {
        // 1.查询用例已存在的参与者角色配置
        Map<String, List<TjCasePartConfig>> caseRoleConfigMap = casePartConfigService.list(
                        new LambdaQueryWrapper<TjCasePartConfig>().eq(TjCasePartConfig::getCaseId, caseId)).stream()
                .filter(item -> !ObjectUtils.isEmpty(item.getParticipantRole()))
                .collect(Collectors.groupingBy(TjCasePartConfig::getParticipantRole));
        // 2.查询设备信息
        Map<String, List<TjDeviceDetail>> devicesMap = deviceDetailService.list()
                .stream().collect(Collectors.groupingBy(TjDeviceDetail::getSupportRoles));
        // 3.将场景参与者转换为配置模板 caseTrajectoryDetailBo.participantTrajectories -> CasePartConfigVo
        Map<String, List<CasePartConfigVo>> configMap = casePartConfigService.trajectory2Config(caseTrajectoryDetailBo);
        // 4.根据参与者角色字典进行匹配
        return CollectionUtils.emptyIfNull(dictTypeService.selectDictDataByType(SysType.PART_ROLE)).stream()
                .map(role -> {
                    // 对应参与者类型的所有需要配置的参与者信息
                    List<CasePartConfigVo> casePartConfigVos = configMap.get(role.getCssClass());
                    List<CasePartConfigVo> parts = new ArrayList<>();
                    for (CasePartConfigVo config : CollectionUtils.emptyIfNull(casePartConfigVos)) {
                        // 如果是用例设备配置，那么该参与者类型下若没有对应的配置则跳过
                        List<TjCasePartConfig> businessConfigs = caseRoleConfigMap.get(role.getDictValue());
                        List<String> businessIds = CollectionUtils.emptyIfNull(businessConfigs).stream()
                                .map(TjCasePartConfig::getBusinessId).collect(Collectors.toList());
                        CasePartConfigVo part = new CasePartConfigVo();
                        if (!businessIds.contains(config.getBusinessId())) {
                            // 未配置对应角色
                            if (deviceConfig) {
                                // 无法配置设备
                                continue;
                            } else {
                                // 初始化角色配置
                                BeanUtils.copyBeanProp(part, config);
                            }
                        } else {
                            // 使用已有的角色配置
                            Map<String, TjCasePartConfig> businessConfigMap = CollectionUtils.emptyIfNull(businessConfigs)
                                    .stream().collect(Collectors.toMap(TjCasePartConfig::getBusinessId, value -> value));
                            BeanUtils.copyBeanProp(part, businessConfigMap.get(config.getBusinessId()));
                            part.setSelected(Boolean.TRUE);
                            if (deviceConfig) {
                                // 对应参与者类型可选择的设备列表
                                List<TjDeviceDetail> devices = devicesMap.get(role.getDictValue());
                                List<DeviceDetailVo> deviceVos = CollectionUtils.emptyIfNull(devices).stream()
                                        .filter(t -> StringUtils.isEmpty(t.getAttribute2())
                                                || SecurityUtils.getUsername().equals(t.getAttribute2()))
                                        .map(device -> {
                                            DeviceDetailVo detailVo = new DeviceDetailVo();
                                            BeanUtils.copyBeanProp(detailVo, device);
                                            if (detailVo.getDeviceId().equals(part.getDeviceId())) {
                                                detailVo.setSelected(Boolean.TRUE);
                                            }
                                            return detailVo;
                                        }).collect(Collectors.toList());
                                part.setDevices(deviceVos);
                            }
                        }
                        part.setModelName(ModelEnum.getDescByCode(part.getModel()));
                        parts.add(part);
                    }
                    PartConfigSelect partConfigSelect = new PartConfigSelect();
                    partConfigSelect.setDictCode(role.getDictCode());
                    partConfigSelect.setDictLabel(role.getDictLabel());
                    partConfigSelect.setDictValue(role.getDictValue());
                    partConfigSelect.setSort(role.getDictSort());
                    partConfigSelect.setCssClass(role.getCssClass());
                    if (CollectionUtils.isNotEmpty(parts)) {
                        partConfigSelect.setParts(parts);
                    }
                    return partConfigSelect;
                }).collect(Collectors.toList());
    }

    @Override
    public List<PartConfigSelect> getConfigSelect(Integer caseId,
                                                  SceneTrajectoryBo sceneTrajectoryBo) {
        // 1.查询用例已存在的参与者角色配置
        QueryWrapper<TjCasePartConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ColumnName.CASE_ID_COLUMN, caseId);
        Map<String, List<TjCasePartConfig>> caseRoleConfigMap = casePartConfigService.list(queryWrapper).stream()
                .collect(Collectors.groupingBy(TjCasePartConfig::getParticipantRole));
        // 2.查询设备信息
        Map<String, List<TjDeviceDetail>> devicesMap = deviceDetailService.list()
                .stream().collect(Collectors.groupingBy(TjDeviceDetail::getSupportRoles));
        // 3.将场景参与者转换为配置模板 sceneTrajectoryBo.participantTrajectories -> CasePartConfigVo
        Map<String, List<CasePartConfigVo>> configMap = casePartConfigService.trajectory2Config(sceneTrajectoryBo);
        // 4.根据参与者角色字典进行匹配
        return CollectionUtils.emptyIfNull(dictTypeService.selectDictDataByType(SysType.PART_ROLE)).stream()
                .map(role -> {
                    // 对应参与者类型的所有需要配置的参与者信息
                    List<CasePartConfigVo> casePartConfigVos = configMap.get(role.getCssClass());
                    // 对应参与者类型可选择的设备列表
                    List<TjDeviceDetail> devices = devicesMap.get(role.getDictValue());
                    List<CasePartConfigVo> parts = new ArrayList<>();
                    for (CasePartConfigVo config : CollectionUtils.emptyIfNull(casePartConfigVos)) {
                        // 如果是用例设备配置，那么该参与者类型下若没有对应的配置则跳过
                        List<TjCasePartConfig> businessConfigs = caseRoleConfigMap.get(role.getDictValue());
                        List<String> businessIds = CollectionUtils.emptyIfNull(businessConfigs).stream()
                                .map(TjCasePartConfig::getBusinessId).collect(Collectors.toList());
                        if (!businessIds.contains(config.getBusinessId())) {
                            continue;
                        }
                        Map<String, TjCasePartConfig> businessConfigMap = CollectionUtils.emptyIfNull(businessConfigs)
                                .stream().collect(Collectors.toMap(TjCasePartConfig::getBusinessId, value -> value));
                        CasePartConfigVo part = new CasePartConfigVo();
                        BeanUtils.copyBeanProp(part, config);
                        part.setModelName(ModelEnum.getDescByCode(part.getModel()));
                        List<DeviceDetailVo> deviceVos = CollectionUtils.emptyIfNull(devices).stream().map(device -> {
                            DeviceDetailVo detailVo = new DeviceDetailVo();
                            BeanUtils.copyBeanProp(detailVo, device);
                            return detailVo;
                        }).collect(Collectors.toList());
                        part.setDevices(deviceVos);
                        parts.add(part);
                    }
                    PartConfigSelect partConfigSelect = new PartConfigSelect();
                    partConfigSelect.setDictCode(role.getDictCode());
                    partConfigSelect.setDictLabel(role.getDictLabel());
                    partConfigSelect.setDictValue(role.getDictValue());
                    partConfigSelect.setSort(role.getDictSort());
                    partConfigSelect.setCssClass(role.getCssClass());
                    if (CollectionUtils.isNotEmpty(parts)) {
                        partConfigSelect.setParts(parts);
                    }
                    return partConfigSelect;
                }).collect(Collectors.toList());
    }

    /**
     * 同一用例只能由一个用户进行测试
     *
     * @param tjCaseDto
     * @return
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveCase(TjCaseDto tjCaseDto) throws BusinessException {
        TjCase tjCase = new TjCase();
        if (ObjectUtils.isEmpty(tjCaseDto.getId())) {
            BeanUtils.copyBeanProp(tjCase, tjCaseDto);
            tjCase.setCaseNumber(this.buildCaseNumber());
            LocalDateTime now = LocalDateTime.now();
            tjCase.setUpdatedDate(now);
            tjCase.setSceneLibId(tjCaseDto.getSceneLibId());
            tjCase.setUpdatedBy(SecurityUtils.getUsername());
            tjCase.setCreatedBy(SecurityUtils.getUsername());
            tjCase.setCreatedDate(now);
            if(tjCaseDto.getIsGen()==null || tjCaseDto.getIsGen().equals(0)){
                TjFragmentedSceneDetail sceneDetail = sceneDetailService.getById(tjCaseDto.getSceneDetailId());
                if (ObjectUtils.isEmpty(sceneDetail)) {
                    throw new BusinessException("创建失败：场景不存在");
                }
                String trajectoryInfoPath = sceneDetail.getTrajectoryInfo();
                if (sceneDetail.getSimuType().equals(0)){
                    trajectoryInfoPath = sceneDetail.getTrajectoryInfoTime();
                }
                //读文件
                CaseTrajectoryDetailBo trajectoryDetailBo = JSONObject.parseObject(FileUtils.readFile(trajectoryInfoPath), CaseTrajectoryDetailBo.class);
                tjCase.setDetailInfo(trajectoryInfoPath);

//                if (StringUtils.isEmpty(sceneDetail.getRouteFile())) {
//                    throw new BusinessException("创建失败：场景未进行仿真验证");
//                }
                tjCase.setRouteFile(sceneDetail.getRouteFile());
                tjCase.setMapFile(sceneDetail.getMapFile());
                tjCase.setMapId(sceneDetail.getMapId());
                StringBuilder labelshows = new StringBuilder();
                for (String str : sceneDetail.getLabel().split(",")) {
                    try {
                        long intValue = Long.parseLong(str);
                        String labelshow = sceneLabelMap.getSceneLabel(intValue);
                        if (labelshow != null) {
                            if (labelshows.length() > 0) {
//                            labelshows.append(",").append(labelshow);
                            } else {
                                labelshows.append(labelshow);
                            }
                        }
                    } catch (NumberFormatException e) {
                        // 处理无效的整数字符串
                    }
                }
                tjCase.setTestScene(labelshows.toString());
                this.save(tjCase);
                // 创建配置
                return createPartConfig(tjCase.getId(), trajectoryDetailBo);
            }else if(tjCaseDto.getIsGen().equals(1)){
                TjGeneralizeScene sceneDetail = generalizeSceneService.getById(tjCaseDto.getSceneDetailId());
                TjFragmentedSceneDetail oldsceneDetail = sceneDetailService.getById(sceneDetail.getSceneId());
                if (ObjectUtils.isEmpty(sceneDetail)) {
                    throw new BusinessException("创建失败：场景不存在");
                }
                if (StringUtils.isEmpty(sceneDetail.getTrajectoryInfo())) {
                    throw new BusinessException("创建失败：未获取到场景点位配置");
                }
                CaseTrajectoryDetailBo trajectoryDetailBo = JSONObject.parseObject(sceneDetail.getTrajectoryInfo(), CaseTrajectoryDetailBo.class);
                tjCase.setDetailInfo(JSONObject.toJSONString(trajectoryDetailBo));
                tjCase.setSceneDetailId(sceneDetail.getSceneId());
                if (StringUtils.isEmpty(sceneDetail.getRouteFile())) {
                    throw new BusinessException("创建失败：场景未进行仿真验证");
                }
                tjCase.setRouteFile(sceneDetail.getRouteFile());
                tjCase.setMapFile(oldsceneDetail.getMapFile());
                tjCase.setMapId(oldsceneDetail.getMapId());
                StringBuilder labelshows = new StringBuilder();
                for (String str : sceneDetail.getLabel().split(",")) {
                    try {
                        long intValue = Long.parseLong(str);
                        String labelshow = sceneLabelMap.getSceneLabel(intValue);
                        if (labelshow != null) {
                            if (labelshows.length() > 0) {
//                            labelshows.append(",").append(labelshow);
                            } else {
                                labelshows.append(labelshow);
                            }
                        }
                    } catch (NumberFormatException e) {
                        // 处理无效的整数字符串
                    }
                }
                tjCase.setTestScene(labelshows.toString());
                this.save(tjCase);
                // 创建配置
                return createPartConfig(tjCase.getId(), trajectoryDetailBo);
            }
        } else {
//            String usingKey = "USING_CASE_" + tjCase.getId();
//            if (redisCache.hasKey(usingKey)) {
//                throw new BusinessException("当前用例正在被使用，请稍后再试");
//            }
//            redisCache.setCacheObject(usingKey, 1, 3, TimeUnit.MINUTES);

            // 业务id和参与者类型的映射（编辑）
            Map<String, String> businessIdAndRoleMap = new HashMap<>();
            // 业务id和设备id的映射（试验）
            Map<String, Integer> businessIdAndDeviceMap = new HashMap<>();
            for (PartConfigSelect partConfigSelect : tjCaseDto.getPartConfigSelects()) {
                for (CasePartConfigVo part : partConfigSelect.getParts()) {
                    if (StringUtils.isNotEmpty(part.getParticipantRole())) {
                        businessIdAndRoleMap.put(part.getBusinessId(), part.getParticipantRole());
                    }
                    if (!ObjectUtils.isEmpty(part.getDeviceId())) {
                        businessIdAndDeviceMap.put(part.getBusinessId(), part.getDeviceId());
                    }
                }
            }
            if (businessIdAndRoleMap.size() < 1) {
                throw new BusinessException("请配置参与者角色");
            }
            tjCase = this.getById(tjCaseDto.getId());
            CaseTrajectoryDetailBo detailInfo = StringUtils.isNotEmpty(tjCase.getDetailInfo())
                    ? JSONObject.parseObject(tjCase.getDetailInfo(), CaseTrajectoryDetailBo.class)
                    : null;
            if(detailInfo != null){
                tjCaseDto.getPartConfigSelects().get(0).getParts().forEach(part -> {
                    if (part.getParticipantRole().equals(PartRole.MV_TRACKING)){
                        detailInfo.getParticipantTrajectories().forEach(trajectory -> {
                            if (trajectory.getId().equals(part.getBusinessId())){
                                trajectory.getTrajectory().forEach(trajectoryPoint -> {
                                    if(trajectoryPoint.getSpeed()>20){
                                        try {
                                            throw new BusinessException("云控寻迹车速度不能超过20km/h,请修改配置或重新选择场景");
                                        } catch (BusinessException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
            if (businessIdAndDeviceMap.size() < 1) {
                if (tjCase.getStatus().equals(CaseStatusEnum.WAIT_CONFIG.getCode())) {
                    tjCase.setStatus(CaseStatusEnum.WAIT_TEST.getCode());
                }
                // 修改用例信息
                tjCase.setTestTarget(tjCaseDto.getTestTarget());
                tjCase.setRemark(tjCaseDto.getRemark());
                tjCase.setUpdatedBy(SecurityUtils.getUsername());
                tjCase.setUpdatedDate(LocalDateTime.now());
                this.updateById(tjCase);
            }
            // 修改角色或设备配置
            updatePartConfig(tjCaseDto.getId(), tjCaseDto.getPartConfigSelects());
        }
        return true;
    }

    /**
     * 可多用户对相同用例进行测试
     *
     * @param tjCaseDto
     * @return
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCase2(TjCaseDto tjCaseDto) throws BusinessException {
        TjCase tjCase = new TjCase();
        if (ObjectUtils.isEmpty(tjCaseDto.getId())) {
            BeanUtils.copyBeanProp(tjCase, tjCaseDto);
            tjCase.setCaseNumber(this.buildCaseNumber());
            TjFragmentedSceneDetail sceneDetail = sceneDetailService.getById(tjCaseDto.getSceneDetailId());
            if (ObjectUtils.isEmpty(sceneDetail)) {
                throw new BusinessException("创建失败：场景不存在");
            }
            SceneTrajectoryBo sceneTrajectoryBo = JSONObject.parseObject(sceneDetail.getTrajectoryInfo(), SceneTrajectoryBo.class);
            if (StringUtils.isEmpty(sceneDetail.getTrajectoryInfo())) {
                throw new BusinessException("创建失败：未获取到场景点位配置");
            }
            tjCase.setDetailInfo(sceneDetail.getTrajectoryInfo());

            if (StringUtils.isEmpty(sceneDetail.getRouteFile())) {
                throw new BusinessException("创建失败：场景未进行仿真验证");
            }
            tjCase.setRouteFile(sceneDetail.getRouteFile());

            StringBuilder labelshows = new StringBuilder();
            for (String str : sceneDetail.getLabel().split(",")) {
                try {
                    long intValue = Long.parseLong(str);
                    String labelshow = sceneLabelMap.getSceneLabel(intValue);
                    if (labelshow != null) {
                        if (labelshows.length() > 0) {
//                            labelshows.append(",").append(labelshow);
                        } else {
                            labelshows.append(labelshow);
                        }
                    }
                } catch (NumberFormatException e) {
                    // 处理无效的整数字符串
                }
            }
            tjCase.setTestScene(labelshows.toString());
            LocalDateTime now = LocalDateTime.now();
            tjCase.setUpdatedDate(now);
            tjCase.setUpdatedBy(SecurityUtils.getUsername());
            tjCase.setCreatedBy(SecurityUtils.getUsername());
            tjCase.setCreatedDate(now);
            return this.save(tjCase);
        } else {
            // 业务id和参与者类型的映射（编辑）
            Map<String, String> businessIdAndRoleMap = new HashMap<>();
            // 业务id和设备id的映射（试验）
            Map<String, Integer> businessIdAndDeviceMap = new HashMap<>();
            for (PartConfigSelect partConfigSelect : tjCaseDto.getPartConfigSelects()) {
                for (CasePartConfigVo part : partConfigSelect.getParts()) {
                    if (StringUtils.isNotEmpty(part.getParticipantRole())) {
                        businessIdAndRoleMap.put(part.getBusinessId(), part.getParticipantRole());
                    }
                    if (!ObjectUtils.isEmpty(part.getDeviceId())) {
                        businessIdAndDeviceMap.put(part.getBusinessId(), part.getDeviceId());
                    }
                }
            }
            tjCase = this.getById(tjCaseDto.getId());
            if (tjCase.getStatus().equals(CaseStatusEnum.WAIT_CONFIG.getCode())) {
                if (businessIdAndRoleMap.size() < 1) {
                    throw new BusinessException("请配置参与者角色");
                }
                tjCase.setStatus(CaseStatusEnum.WAIT_TEST.getCode());
            }
            tjCase.setTestTarget(tjCaseDto.getTestTarget());
            tjCase.setRemark(tjCaseDto.getRemark());
            tjCase.setUpdatedBy(SecurityUtils.getUsername());
            tjCase.setUpdatedDate(LocalDateTime.now());
            this.updateById(tjCase);

            CaseTrajectoryDetailBo trajectoryDetailBo = JSONObject.parseObject(tjCase.getDetailInfo(),
                    CaseTrajectoryDetailBo.class);
            for (ParticipantTrajectoryBo participantTrajectoryBo : trajectoryDetailBo.getParticipantTrajectories()) {
                participantTrajectoryBo.setRole(businessIdAndRoleMap.get(participantTrajectoryBo.getId()));
            }
            // 编辑设备（试验）
            if (businessIdAndDeviceMap.size() > 0) {
                // 查询当前用户创建的且未开始的测试记录
                TjCaseRealRecord notStartRecord = caseRealRecordService
                        .getOne(new LambdaQueryWrapper<TjCaseRealRecord>()
                                .eq(TjCaseRealRecord::getCaseId, tjCase.getId())
                                .eq(TjCaseRealRecord::getCreatedBy, SecurityUtils.getUsername())
                                .isNull(TjCaseRealRecord::getRouteFile));
                if (ObjectUtils.isEmpty(notStartRecord)) {
                    createRecordAndConfig(tjCase.getId(), trajectoryDetailBo, businessIdAndRoleMap, businessIdAndDeviceMap);
                }
            } else {
                // 编辑角色时，删除当前用户创建的且未开始测试记录及配置（保证当前用户下对此用例只有一条未进行的测试记录和配置存在）
                deleteNotStartRecord(tjCase.getId());
                createRecordAndConfig(tjCase.getId(), trajectoryDetailBo, businessIdAndRoleMap, businessIdAndDeviceMap);
            }
        }
        return true;
    }

    /**
     * 删除当前用户创建的且未开始测试记录及配置
     *
     * @param caseId
     */
    private void deleteNotStartRecord(Integer caseId) {
        // 查找符合条件的测试记录
        List<TjCaseRealRecord> notStartRecordList = caseRealRecordService
                .list(new LambdaQueryWrapper<TjCaseRealRecord>()
                        .eq(TjCaseRealRecord::getCaseId, caseId)
                        .eq(TjCaseRealRecord::getCreatedBy, SecurityUtils.getUsername())
                        .isNull(TjCaseRealRecord::getRouteFile));
        if (CollectionUtils.isNotEmpty(notStartRecordList)) {
            List<Integer> removeRecordIds = notStartRecordList.stream()
                    .map(TjCaseRealRecord::getId)
                    .collect(Collectors.toList());
            // 删除测试记录
            caseRealRecordService.removeByIds(removeRecordIds);
            // 删除对应的配置
//            casePartConfigService.remove(new LambdaQueryWrapper<TjCasePartConfig>()
//                    .in(TjCasePartConfig::getRecordId, removeRecordIds));
        }
    }

    /**
     * 创建测试记录和配置
     *
     * @param caseId
     * @param trajectoryDetailBo
     * @param businessIdAndRoleMap
     * @param businessIdAndDeviceMap 编辑（配置角色）时为空，试验（配置设备）时不为空
     * @throws BusinessException
     */
    private void createRecordAndConfig(Integer caseId, CaseTrajectoryDetailBo trajectoryDetailBo,
                                       Map<String, String> businessIdAndRoleMap,
                                       Map<String, Integer> businessIdAndDeviceMap) throws BusinessException {
        // 创建新测试记录（将角色保存在测试记录表的detailInfo中，无轨迹文件、开始结束时间）
        TjCaseRealRecord record = new TjCaseRealRecord();
        record.setCaseId(caseId);
        record.setStatus(TestingStatusEnum.NO_PASS.getCode());
        record.setDetailInfo(JSONObject.toJSONString(trajectoryDetailBo));
        record.setCreatedBy(SecurityUtils.getUsername());
        record.setCreatedDate(LocalDateTime.now());
        caseRealRecordService.save(record);
        createPartConfig(caseId, trajectoryDetailBo);

    }

    /**
     * 创建配置
     *
     * @param caseId
     * @param trajectoryDetailBo
     * @return
     */
    private boolean createPartConfig(Integer caseId, CaseTrajectoryDetailBo trajectoryDetailBo) {
        List<TjCasePartConfig> casePartConfigs = new ArrayList<>();
        for (ParticipantTrajectoryBo participantTrajectoryBo : trajectoryDetailBo.getParticipantTrajectories()) {
            TjCasePartConfig casePartConfig = new TjCasePartConfig();
            casePartConfig.setCaseId(caseId);
            casePartConfig.setBusinessId(participantTrajectoryBo.getId());
            casePartConfig.setBusinessType(participantTrajectoryBo.getType());
            casePartConfig.setName(participantTrajectoryBo.getName());
            casePartConfig.setModel(participantTrajectoryBo.getModel());
            casePartConfig.setParticipantRole(participantTrajectoryBo.getRole());
            participantTrajectoryBo.getTrajectory().stream()
                    .filter(t -> PointTypeEnum.START.getPointType().equals(t.getType()))
                    .findFirst()
                    .ifPresent(p -> casePartConfig.setFristSite(p.getPosition()));
            casePartConfigs.add(casePartConfig);
        }
        return casePartConfigService.saveBatch(casePartConfigs);
    }

    private void updatePartConfig(Integer caseId,
                                  List<PartConfigSelect> partConfigSelects) throws BusinessException {
        // 创建新配置（只有虚拟车在此配置默认设备）
        TjDeviceDetail svDetail = null;
        try {
            svDetail = deviceDetailService.getOne(new LambdaQueryWrapper<TjDeviceDetail>()
                    .eq(TjDeviceDetail::getSupportRoles, PartRole.MV_SIMULATION));
        } catch (TooManyResultsException e) {
            log.error("查询到多个仿真设备");
            throw new BusinessException("查询到多个仿真设备");
        }
        if (ObjectUtils.isEmpty(svDetail)) {
            throw new BusinessException("未检测到可用的仿真设备");
        }
        Integer svId = svDetail.getDeviceId();
        Map<Integer, CasePartConfigVo> selectMap = new HashMap<>();
        for (PartConfigSelect partConfigSelect : partConfigSelects) {
            for (CasePartConfigVo casePartConfigVo : partConfigSelect.getParts()) {
                selectMap.put(casePartConfigVo.getId(), casePartConfigVo);
            }
        }
        List<CasePartConfigVo> configs = casePartConfigService.getConfigInfoByCaseId(caseId);
        for (CasePartConfigVo config : configs) {
            if (!selectMap.containsKey(config.getId())) {
                if (PartRole.MV_SIMULATION.equals(config.getParticipantRole())|| PartRole.SP.equals(config.getParticipantRole())) {
                    config.setDeviceId(svId);
                    casePartConfigMapper.updateDevice(config);
                }
                continue;
            }
            CasePartConfigVo selectConfig = selectMap.get(config.getId());
            if (ObjectUtils.isEmpty(selectConfig.getDeviceId())) {
                casePartConfigMapper.updateRole(selectConfig);
            } else {
                casePartConfigMapper.updateDevice(selectConfig);
            }
        }
    }

    @Override
    public CaseInfoBo getCaseDetail(Integer caseId) throws BusinessException {
        CaseInfoBo caseInfoBo = caseMapper.selectCaseInfo(caseId);
        if (ObjectUtils.isEmpty(caseInfoBo)) {
            throw new BusinessException("用例查询异常");
        }
        List<CaseConfigBo> casePartConfigs = caseInfoBo.getCaseConfigs();
        if (CollectionUtils.isEmpty(casePartConfigs)) {
            throw new BusinessException("用例未进行角色配置");
        }
        return caseInfoBo;
    }


    @Override
    public void playback(Integer id, String participantId, int action) throws BusinessException, IOException {
        CaseInfoBo caseInfoBo = this.getCaseDetail(id);
        // 格式需要修改
        String key = ChannelBuilder.buildScenePreviewChannel(SecurityUtils.getUsername(), id);
        switch (action) {
            case PlaybackAction.START:
                if (StringUtils.isEmpty(caseInfoBo.getRouteFile())) {
                    this.playback(id, participantId, PlaybackAction.CALL);
                    break;
                }
                List<List<TrajectoryValueDto>> routeList = routeService.readTrajectoryFromRouteFile(
                        caseInfoBo.getRouteFile(), participantId);
                if (CollectionUtils.isEmpty(routeList)) {
                    throw new BusinessException("未查询到轨迹");
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean batchDelete(List<Integer> caseIds) throws BusinessException {
        if (CollectionUtils.isEmpty(caseIds)) {
            return false;
        }
        QueryWrapper<TjCasePartConfig> configQueryWrapper = new QueryWrapper<>();
        configQueryWrapper.in(ColumnName.CASE_ID_COLUMN, caseIds);
        List<TjCasePartConfig> tjCasePartConfigs = casePartConfigService.list(configQueryWrapper);
        if (CollectionUtils.isNotEmpty(tjCasePartConfigs)) {
            List<Integer> configIds = tjCasePartConfigs.stream().map(TjCasePartConfig::getId).collect(Collectors.toList());
            boolean removeConfig = casePartConfigService.removeByIds(configIds);
            if (!removeConfig) {
                throw new BusinessException("删除配置失败");
            }
        }
        boolean removeCase = this.removeByIds(caseIds);
        if (!removeCase) {
            throw new BusinessException("删除用例失败");
        }
        return true;
    }

    @Override
    public boolean updateStatus(Integer caseId) throws BusinessException {
        TjCase tjCase = this.getById(caseId);
        if (ObjectUtils.isEmpty(tjCase)) {
            throw new BusinessException("用例不存在！");
        }
        if (CaseStatusEnum.WAIT_CONFIG.getCode().equals(tjCase.getStatus())) {
            throw new BusinessException("用例未进行角色配置，无法进行状态操作！");
        }
        if (CaseStatusEnum.WAIT_TEST.getCode().equals(tjCase.getStatus())
                || CaseStatusEnum.INVALID.getCode().equals(tjCase.getStatus())) {
            tjCase.setStatus(CaseStatusEnum.EFFECTIVE.getCode());
            return this.updateById(tjCase);
        }
        if (CaseStatusEnum.EFFECTIVE.getCode().equals(tjCase.getStatus())) {
            tjCase.setStatus(CaseStatusEnum.INVALID.getCode());
            return this.updateById(tjCase);
        }
        return false;
    }

    @Override
    public boolean batchUpdateStatus(List<Integer> caseIds, Integer action) throws BusinessException {
        List<TjCase> cases = this.listByIds(caseIds);
        if (CollectionUtils.isEmpty(cases)) {
            throw new BusinessException("用例信息异常！");
        }
        if (action == 1) {
            for (TjCase tjCase : cases) {
                if (CaseStatusEnum.WAIT_TEST.getCode().equals(tjCase.getStatus())
                        || CaseStatusEnum.INVALID.getCode().equals(tjCase.getStatus())) {
                    tjCase.setStatus(CaseStatusEnum.EFFECTIVE.getCode());
                }
            }
            return this.updateBatchById(cases);
        }
        if (action == 2) {
            for (TjCase tjCase : cases) {
                if (CaseStatusEnum.WAIT_TEST.getCode().equals(tjCase.getStatus())
                        || CaseStatusEnum.EFFECTIVE.getCode().equals(tjCase.getStatus())) {
                    tjCase.setStatus(CaseStatusEnum.INVALID.getCode());
                }
            }
            return this.updateBatchById(cases);
        }
        return false;
    }


    @Override
    public CaseVerificationVo getSimulationDetail(Integer caseId) throws BusinessException {
        TjCase tjCase = this.getById(caseId);
        if (ObjectUtils.isEmpty(tjCase)) {
            throw new BusinessException("测试用例不存在");
        }
        TjResourcesDetail resourcesDetail = resourcesDetailMapper.selectById(tjCase.getResourcesDetailId());
        if (ObjectUtils.isEmpty(resourcesDetail) || StringUtils.isEmpty(resourcesDetail.getAttribute4())) {
            throw new BusinessException("地图资源不存在");
        }
        CaseVerificationVo result = new CaseVerificationVo(tjCase, resourcesDetail.getFilePath(),
                resourcesDetail.getAttribute4());
        if (ObjectUtils.isEmpty(tjCase.getRouteFile())) {
            return result;
        }
        Map<String, List<Map<String, Double>>> pointMap = null;
        try {
            List<List<TrajectoryValueDto>> trajectoryValue = routeService.readRouteFile(tjCase.getRouteFile());
            pointMap = routeService.extractRoute(trajectoryValue);
        } catch (IOException e) {
            log.error("文件读取异常");
            return result;
        }
        if (!ObjectUtils.isEmpty(pointMap)) {
            for (ParticipantTrajectoryBo trajectoryBo : result.getDetailInfo().getParticipantTrajectories()) {
                if (pointMap.containsKey(trajectoryBo.getId())) {
                    List<Map<String, Double>> list = pointMap.get(trajectoryBo.getId());
                    trajectoryBo.setRoute(list);
                    int sec = (int) Math.ceil((double) list.size() / 10);
                    trajectoryBo.setDuration(DateUtils.secondsToDuration(sec));
                }
            }
        }
        result.setFinished(true);
        return result;
    }

    @Override
    public List<PartConfigSelect> getConfigDetail(Integer caseId) throws BusinessException {
        TjCase tjCase = this.getById(caseId);
        if (ObjectUtils.isEmpty(tjCase)) {
            throw new BusinessException("未查询到对应的测试用例");
        }
        return this.getConfigSelect(caseId, JSONObject.parseObject(tjCase.getDetailInfo(), CaseTrajectoryDetailBo.class), true);
    }

    @Override
    public List<TjCasePartRoleVo> getConfigDetailNew(Integer caseId) throws BusinessException {
        TjCase tjCase = this.getById(caseId);
        if (ObjectUtils.isEmpty(tjCase)) {
            throw new BusinessException("未查询到对应的测试用例");
        }
        LambdaQueryWrapper<TjDeviceDetail> deviceListQW = new LambdaQueryWrapper<TjDeviceDetail>().eq(
            TjDeviceDetail::getIsInner, 0);
        String username = SecurityUtils.getUsername();
        if (!Constants.UserInfo.ADMIN_NAME.equals(username)) {
            deviceListQW.eq(TjDeviceDetail::getCreatedBy,
                SecurityUtils.getUsername());
        }
        List<DeviceDetailVo> deviceDetails = deviceDetailService.list(deviceListQW).stream().map(device -> {
            DeviceDetailVo detailVo = new DeviceDetailVo();
            BeanUtils.copyBeanProp(detailVo, device);
            return detailVo;
        }).collect(Collectors.toList());
        Map<String, List<DeviceDetailVo>> devicesMap = deviceDetails.stream()
                .collect(Collectors.groupingBy(DeviceDetailVo::getSupportRoles));
        List<CasePartConfigVo> configs = casePartConfigService.getConfigInfoByCaseId(caseId);
        SceneTrajectoryBo trajectoryBo = JSONObject.parseObject(tjCase.getDetailInfo(), SceneTrajectoryBo.class);
        if (ObjectUtils.isEmpty(trajectoryBo) || CollectionUtils.isEmpty(trajectoryBo.getParticipantTrajectories())) {
            throw new BusinessException("用例异常：未进行点位配置");
        }
        // 将配置按照业务类型分组（SV仿真车无需进行配置，保存时默认使用tessng）
        Map<String, List<CasePartConfigVo>> configByRoleMap = configs.stream()
                .filter(c -> !(PartRole.MV_SIMULATION.equals(c.getParticipantRole()) || PartRole.SP.equals(c.getParticipantRole())))
                .collect(Collectors.groupingBy(CasePartConfigVo::getParticipantRole));
        List<TjCasePartRoleVo> roleVos = new ArrayList<>();
        for (Map.Entry<String, List<CasePartConfigVo>> configEntry : configByRoleMap.entrySet()) {
            TjCasePartRoleVo tjCasePartRoleVo = new TjCasePartRoleVo();
            tjCasePartRoleVo.setType(configEntry.getKey());
            for (CasePartConfigVo partConfig : configEntry.getValue()) {
                partConfig.setDevices(devicesMap.get(configEntry.getKey()));
            }
            tjCasePartRoleVo.setCasePartConfigs(configEntry.getValue());
            roleVos.add(tjCasePartRoleVo);
        }
        return roleVos;
    }

    @Override
    public boolean deleteRecord(Integer recordId) throws BusinessException {
        return caseRealRecordService.removeById(recordId);
    }

    @Override
    public List<TjCaseOp> selectCaseOp(Integer taskId) {
        return caseMapper.selectCaseOp(taskId);
    }


    public synchronized String buildCaseNumber() {
        return StringUtils.format(ContentTemplate.CASE_NUMBER_TEMPLATE, DateUtils.getNowDayString(),
                CounterUtil.getNextNumber(ContentTemplate.CASE_NUMBER_TEMPLATE));
    }

    private List<SysDictData> getPartRoleList(List<String> filters) {
        return CollectionUtils.emptyIfNull(dictTypeService.selectDictDataByType(SysType.PART_ROLE)).stream()
                .filter(t -> !filters.contains(t.getDictValue())).collect(Collectors.toList());
    }
}
