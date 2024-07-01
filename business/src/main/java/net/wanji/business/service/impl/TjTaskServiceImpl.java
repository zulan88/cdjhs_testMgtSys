package net.wanji.business.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import net.wanji.business.common.Constants;
import net.wanji.business.common.Constants.ChannelBuilder;
import net.wanji.business.common.Constants.ContentTemplate;
import net.wanji.business.common.Constants.PartRole;
import net.wanji.business.common.Constants.PartType;
import net.wanji.business.common.Constants.SysType;
import net.wanji.business.common.Constants.TaskCaseStatusEnum;
import net.wanji.business.common.Constants.TaskProcessNode;
import net.wanji.business.common.Constants.TaskStatusEnum;
import net.wanji.business.common.Constants.TestType;
import net.wanji.business.domain.SitePoint;
import net.wanji.business.domain.bo.*;
import net.wanji.business.domain.dto.CaseQueryDto;
import net.wanji.business.domain.dto.RoutingPlanDto;
import net.wanji.business.domain.dto.TaskDto;
import net.wanji.business.domain.dto.TjDeviceDetailDto;
import net.wanji.business.domain.dto.device.TaskSaveDto;
import net.wanji.business.domain.param.TessParam;
import net.wanji.business.domain.vo.CaseContinuousVo;
import net.wanji.business.domain.vo.CaseDetailVo;
import net.wanji.business.domain.vo.CasePageVo;
import net.wanji.business.domain.vo.DeviceDetailVo;
import net.wanji.business.domain.vo.SceneIndexSchemeVo;
import net.wanji.business.domain.vo.TaskCaseVo;
import net.wanji.business.domain.vo.TaskListVo;
import net.wanji.business.domain.vo.TaskReportVo;
import net.wanji.business.domain.vo.TaskTargetVehicleVo;
import net.wanji.business.entity.*;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjCaseMapper;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.business.mapper.TjTaskCaseMapper;
import net.wanji.business.mapper.TjTaskDataConfigMapper;
import net.wanji.business.mapper.TjTaskMapper;
import net.wanji.business.schedule.SceneLabelMap;
import net.wanji.business.service.*;
import net.wanji.business.trajectory.RoutingPlanConsumer;
import net.wanji.business.util.CustomMergeStrategy;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.core.domain.SimpleSelect;
import net.wanji.common.core.domain.entity.SysDictData;
import net.wanji.common.core.page.TableDataInfo;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.CounterUtil;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.bean.BeanUtils;
import net.wanji.common.utils.file.FileUploadUtils;
import net.wanji.common.utils.file.FileUtils;
import net.wanji.system.service.ISysDictDataService;
import net.wanji.system.service.ISysDictTypeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author guowenhao
 * @description 针对表【tj_task(测试任务表)】的数据库操作Service实现
 * @createDate 2023-08-31 17:39:16
 */
@Service
@RequiredArgsConstructor
public class TjTaskServiceImpl extends ServiceImpl<TjTaskMapper, TjTask>
        implements TjTaskService {
    private static final Logger log = LoggerFactory.getLogger("business");

    private final ISysDictTypeService dictTypeService;
    private final ISysDictDataService dictDataService;
    private final TjCaseService tjCaseService;
    private final TjTaskDataConfigService tjTaskDataConfigService;
    private final TjTaskCaseService tjTaskCaseService;
    private final RouteService routeService;
    private final RestService restService;
    private final SceneLabelMap sceneLabelMap;
    private final RoutingPlanConsumer routingPlanConsumer;
    private final RedisCache redisCache;
    private final TjTaskCaseRecordService taskCaseRecordService;
    private final TjCasePartConfigService tjCasePartConfigService;

    @Resource
    private TjCaseMapper caseMapper;
    @Resource
    private TjTaskMapper tjTaskMapper;
    @Resource
    private TjTaskCaseMapper tjTaskCaseMapper;
    @Resource
    private TjTaskDataConfigMapper tjTaskDataConfigMapper;
    @Resource
    private TjDeviceDetailMapper deviceDetailMapper;

    private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

    @Override
    public Map<String, List<SimpleSelect>> initPage() {
        Map<String, List<SimpleSelect>> result = new HashMap<>();
        List<SysDictData> testType = dictTypeService.selectDictDataByType(SysType.TEST_TYPE);
        result.put(SysType.TEST_TYPE, CollectionUtils.emptyIfNull(testType).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        // todo 被测对象类型
        SimpleSelect simpleSelect = new SimpleSelect();
        simpleSelect.setDictValue("域控制器");
        simpleSelect.setDictLabel("域控制器");
        result.put("object_type", Collections.singletonList(simpleSelect));
        // 任务状态
        result.put("task_status", TaskStatusEnum.getSelectList());
        return result;
    }

    @Override
    public Map<String, List<SimpleSelect>> initPageOp() {
        Map<String, List<SimpleSelect>> result = new HashMap<>();
        List<SysDictData> testType = dictTypeService.selectDictDataByType(SysType.TEST_TYPE);
        result.put(SysType.TEST_TYPE, CollectionUtils.emptyIfNull(testType).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        // todo 被测对象类型
        SimpleSelect simpleSelect = new SimpleSelect();
        simpleSelect.setDictValue("域控制器");
        simpleSelect.setDictLabel("域控制器");
        result.put("object_type", Collections.singletonList(simpleSelect));
        // 任务状态
        List<SimpleSelect> taskStatus = new ArrayList<>();
        taskStatus.add(new SimpleSelect("待测试", "0"));
        taskStatus.add(new SimpleSelect("进行中", "1"));
        taskStatus.add(new SimpleSelect("已完成", "2"));
        taskStatus.add(new SimpleSelect("逾期", "4"));
        taskStatus.add(new SimpleSelect("复审通过", "5"));
        taskStatus.add(new SimpleSelect("复审失败", "6"));
        taskStatus.add(new SimpleSelect("已取消", "7"));
        result.put("task_status", taskStatus);
        return result;
    }

    @Override
    public Map<String, Long> selectCount(TaskDto taskDto) {
        List<Map<String, String>> statusMaps = tjTaskMapper.selectCountByStatus(taskDto);
        Map<String, Long> statusCountMap = CollectionUtils.emptyIfNull(statusMaps).stream().map(t -> t.get("status"))
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        Map<String, Long> result = new HashMap<>();
        for (String status : TaskStatusEnum.getPageCountList()) {
            result.put(status, statusCountMap.getOrDefault(status, 0L));
        }
        return result;
    }

    @Override
    public List<TaskListVo> pageList(TaskDto in) {
        List<TaskListVo> pageList = tjTaskMapper.getPageList(in);
        List<DeviceDetailVo> deviceDetails = deviceDetailMapper.selectByCondition(new TjDeviceDetailDto());
        Map<Integer, DeviceDetailVo> deviceMap = CollectionUtils.emptyIfNull(deviceDetails).stream()
                .collect(Collectors.toMap(DeviceDetailVo::getDeviceId, value -> value));
        for (TaskListVo taskVo : CollectionUtils.emptyIfNull(pageList)) {
            // 测试类型名称
            taskVo.setTestTypeName(dictDataService.selectDictLabel(SysType.TEST_TYPE, taskVo.getTestType()));
            // 任务状态
            taskVo.setStatusName(TaskStatusEnum.getValueByCode(taskVo.getStatus()));
            // 已完成用例
            taskVo.setFinishedCaseCount((int) CollectionUtils.emptyIfNull(taskVo.getTaskCaseVos()).stream()
                    .filter(t -> TaskCaseStatusEnum.FINISHED.getCode().equals(t.getStatus())).count());
            // 用例配置
            Map<Integer, List<TjTaskDataConfig>> taskCaseConfigMap = CollectionUtils
                    .emptyIfNull(taskVo.getTaskCaseConfigs()).stream().collect(
                            Collectors.groupingBy(TjTaskDataConfig::getCaseId));
            // 主车数量
            String avNum = StringUtils.format(ContentTemplate.CASE_ROLE_TEMPLATE,
                    dictDataService.selectDictLabel(SysType.PART_ROLE, PartRole.AV),
                    CollectionUtils.emptyIfNull(taskVo.getTaskCaseConfigs()).stream().filter(t ->
                            PartRole.AV.equals(t.getType())).count());
            // 用例
            for (TaskCaseVo taskCaseVo : CollectionUtils.emptyIfNull(taskVo.getTaskCaseVos())) {
                // 排期时间
                taskCaseVo.setPlanDate(taskVo.getPlanDate());
                // 状态
                taskCaseVo.setStatusName(TaskCaseStatusEnum.getValueByCode(taskCaseVo.getStatus()));
                // 场景分类
                if (StringUtils.isNotEmpty(taskCaseVo.getSceneSort())) {
                    StringBuilder labelSort = new StringBuilder();
                    for (String str : taskCaseVo.getSceneSort().split(",")) {
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
                    taskCaseVo.setSceneSort(labelSort.toString());
                }
                // 角色配置
                StringBuilder roleConfigSort = new StringBuilder();
                roleConfigSort.append(avNum);
                List<TjTaskDataConfig> caseConfigs = taskCaseConfigMap.get(taskCaseVo.getCaseId());
                Map<String, List<TjTaskDataConfig>> roleConfigMap = CollectionUtils.emptyIfNull(caseConfigs).stream()
                        .collect(Collectors.groupingBy(TjTaskDataConfig::getType));
                for (Entry<String, List<TjTaskDataConfig>> roleItem : roleConfigMap.entrySet()) {
                    String roleName = dictDataService.selectDictLabel(SysType.PART_ROLE, roleItem.getKey());
                    // 角色配置简述
                    if (roleItem.getValue().stream().anyMatch(item -> !ObjectUtils.isEmpty(item.getDeviceId()))) {
                        Map<Integer, List<TjTaskDataConfig>> deviceGroup = roleItem.getValue().stream().collect(Collectors.groupingBy(TjTaskDataConfig::getDeviceId));
                        for (Entry<Integer, List<TjTaskDataConfig>> deviceEntry : deviceGroup.entrySet()) {
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
                taskCaseVo.setRoleConfigSort(roleConfigSort.toString());
            }

            taskVo.setHistoryRecords( getTaskRecords(taskVo));
        }

        return pageList;
    }

    private List<Map<String, Object>> getTaskRecords(TaskListVo taskVo) {
        List<Map<String, Object>> records = taskCaseRecordService.selectTaskRecordInfo(
            taskVo.getId(), taskVo.getSelectedRecordId());
        for (Map<String, Object> record : records) {
            record.put("isSelected", 1 == Integer.valueOf(
                String.valueOf(record.get("isSelected"))));
        }
        return records;
    }

    @Override
    public List<CasePageVo> getTaskCaseList(Integer taskId) {
        TjTaskCase taskCase = new TjTaskCase();
        taskCase.setTaskId(taskId);
        List<TaskCaseVo> taskCaseVos = tjTaskCaseMapper.selectByCondition(taskCase);
        if (CollectionUtils.isEmpty(taskCaseVos)) {
            return null;
        }
        CaseQueryDto param = new CaseQueryDto();
        param.setSelectedIds(taskCaseVos.stream().map(TaskCaseVo::getCaseId).collect(Collectors.toList()));
        param.setUserName(SecurityUtils.getUsername());
        return tjCaseService.pageList(param, "byUsername");
    }

    @Override
    public Object initProcessed(Integer processNode) {
        Map<String, Object> result = new HashMap<>();
        switch (processNode) {
            case TaskProcessNode.TASK_INFO:
                List<SysDictData> testType = dictTypeService.selectDictDataByType(SysType.TEST_TYPE);
                result.put(SysType.TEST_TYPE, CollectionUtils.emptyIfNull(testType).stream()
                        .map(SimpleSelect::new).collect(Collectors.toList()));

                break;
            case TaskProcessNode.SELECT_CASE:
                List<SysDictData> caseStatus = dictTypeService.selectDictDataByType(SysType.CASE_STATUS);
                result.put(SysType.CASE_STATUS, CollectionUtils.emptyIfNull(caseStatus).stream()
                        .map(SimpleSelect::new).collect(Collectors.toList()));
                break;
            case TaskProcessNode.CONFIG:

                break;
            case TaskProcessNode.VIEW_PLAN:

                break;
            default:
                break;
        }

        return result;
    }

    @Override
    public Object processedInfo(TaskSaveDto taskSaveDto) throws BusinessException {
        switch (taskSaveDto.getProcessNode()) {
            case TaskProcessNode.TASK_INFO:
                return this.getTaskInfo(taskSaveDto);
            case TaskProcessNode.SELECT_CASE:
                return this.selectTaskCaseInfo(taskSaveDto);
            case TaskProcessNode.CONFIG:
                return this.selectTaskContinuousConfig(taskSaveDto);
            case TaskProcessNode.VIEW_PLAN:
                return this.viewPlan(taskSaveDto);
            default:
                break;
        }
        return Maps.newHashMap();
    }

    /**
     * 节点1：查询任务信息
     *
     * @param taskSaveDto
     * @return
     * @throws BusinessException
     */
    private Map<String, Object> getTaskInfo(TaskSaveDto taskSaveDto) throws BusinessException {
        TjDeviceDetailDto deviceDetailDto = new TjDeviceDetailDto();
        deviceDetailDto.setSupportRoles(PartRole.AV);
        deviceDetailDto.setIsInner(0);
        String username = SecurityUtils.getUsername();
        if(!Constants.UserInfo.ADMIN_NAME.equals(username)){
            deviceDetailDto.setCreatedBy(username);
        }
        List<DeviceDetailVo> avDevices = deviceDetailMapper.selectByCondition(deviceDetailDto);
        TjTask task = new TjTask();
        if (ObjectUtil.isNotEmpty(taskSaveDto.getId())) {
            task = this.getById(taskSaveDto.getId());
            // 已存在的设备
            TjTaskDataConfig dataConfig = new TjTaskDataConfig();
            dataConfig.setTaskId(taskSaveDto.getId());
            List<TjTaskDataConfig> dataConfigs = tjTaskDataConfigMapper.selectByCondition(dataConfig);
            CollectionUtils.emptyIfNull(avDevices).forEach(t -> {
                if (CollectionUtils.emptyIfNull(dataConfigs).stream().anyMatch(dc -> dc.getDeviceId().equals(t.getDeviceId()))) {
                    t.setSelected(Boolean.TRUE);
                }
            });
        }
        Map<String, Object> result = new HashMap<>();
        result.put("taskInfo", task);
        result.put("avDevices", avDevices.stream().map(t -> {
            TaskTargetVehicleVo targetVehicleVo = new TaskTargetVehicleVo();
            targetVehicleVo.setDeviceId(t.getDeviceId());
            targetVehicleVo.setObjectType(t.getDeviceType());
            targetVehicleVo.setLevel("L3");
            targetVehicleVo.setBrand(t.getDeviceName());
            targetVehicleVo.setType(t.getDeviceName());
            targetVehicleVo.setPerson("张三");
            targetVehicleVo.setPhone("135****9384");
            targetVehicleVo.setSelected(t.isSelected());
            return targetVehicleVo;
        }).collect(Collectors.toList()));
        return result;
    }

    /**
     * 节点2：查询用例信息
     *
     * @param taskSaveDto
     * @return
     * @throws BusinessException
     */
    private Map<String, Object> selectTaskCaseInfo(TaskSaveDto taskSaveDto) throws BusinessException {
        if (ObjectUtil.isEmpty(taskSaveDto.getId())) {
            throw new BusinessException("请确认任务信息是否已保存");
        }
        if (ObjectUtil.isEmpty(taskSaveDto.getCaseQueryDto())) {
            throw new BusinessException("请确认用例查询条件");
        }
        // 已选择的用例
        TjTaskCase taskCase = new TjTaskCase();
        taskCase.setTaskId(taskSaveDto.getId());
        List<TaskCaseVo> taskCaseVos = tjTaskCaseMapper.selectByCondition(taskCase);
        List<Integer> choiceCaseIds = taskCaseVos.stream().map(TaskCaseVo::getCaseId).collect(Collectors.toList());
        // 分页查询用例
        List<Integer> selectedIds = new ArrayList<>();
        CaseQueryDto caseQueryDto = taskSaveDto.getCaseQueryDto();
        caseQueryDto.setUserName(SecurityUtils.getUsername());
        PageHelper.startPage(caseQueryDto.getPageNum(), caseQueryDto.getPageSize());
        List<CasePageVo> casePageVos = null;
        if (ObjectUtils.isEmpty(caseQueryDto.getShowType()) || 0 == caseQueryDto.getShowType()) {
            caseQueryDto.setSelectedIds(null);
            casePageVos = tjCaseService.pageList(caseQueryDto, "byUsername");
            for (CasePageVo casePageVo : CollectionUtils.emptyIfNull(casePageVos)) {
                if (CollectionUtils.isNotEmpty(taskCaseVos) && choiceCaseIds.contains(casePageVo.getId())) {
                    casePageVo.setSelected(Boolean.TRUE);
                }
            }
        } else {
            // 仅查看选中用例
            caseQueryDto.setSelectedIds(choiceCaseIds);
            casePageVos = tjCaseService.pageList(caseQueryDto, "byUsername");
            CollectionUtils.emptyIfNull(casePageVos).forEach(t -> t.setSelected(Boolean.TRUE));
        }
        // 列表数据+已选择的用例
        TableDataInfo rspData = new TableDataInfo();
        rspData.setData(casePageVos);
        rspData.setTotal(new PageInfo(casePageVos).getTotal());
        Map<String, Object> result = new HashMap<>();
        result.put("tableData", rspData);
        result.put("selectedIds", selectedIds);
        return result;
    }

    /**
     * 节点3：查询任务连续性配置信息
     *
     * @param taskSaveDto
     * @return
     * @throws BusinessException
     */
    private Map<String, Object> selectTaskContinuousConfig(TaskSaveDto taskSaveDto) throws BusinessException {
        if (ObjectUtil.isEmpty(taskSaveDto.getId())) {
            throw new BusinessException("请确认任务");
        }
        TjTask tjTask = this.getById(taskSaveDto.getId());
        if (ObjectUtil.isEmpty(tjTask)) {
            throw new BusinessException("任务查询失败");
        }
        // 处理bug因创建任务中止导致多条主车信息
        TjTaskDataConfig taskAvConfig = getTaskAvConfig(taskSaveDto.getId());
        if (ObjectUtil.isEmpty(taskAvConfig)) {
            throw new BusinessException("请配置被测设备");
        }
        TjDeviceDetail avDetail = deviceDetailMapper.selectById(taskAvConfig.getDeviceId());
        if (ObjectUtils.isEmpty(avDetail)) {
            throw new BusinessException(StringUtils.format("被测设备查询失败:{}", taskAvConfig.getDeviceId()));
        }
        List<Map<String, Double>> mainTrajectories = null;
        if (ObjectUtil.isNotEmpty(tjTask.getMainPlanFile())) {
            try {
                List<List<TrajectoryValueDto>> trajectoryValues = routeService.readTrajectoryFromRouteFile(tjTask.getMainPlanFile(), "1");
                mainTrajectories = trajectoryValues.stream().filter(CollectionUtils::isNotEmpty)
                        .map(item -> item.get(0)).map(t -> {
                            Map<String, Double> map = new HashMap<>();
                            map.put("longitude", t.getLongitude());
                            map.put("latitude", t.getLatitude());
                            map.put("courseAngle", t.getCourseAngle());
                            return map;
                        }).collect(Collectors.toList());
            } catch (Exception e) {
                log.error(StringUtils.format("读取任务{}主车规划后路线文件失败:{}", tjTask.getTaskCode(), e));
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("taskId", taskSaveDto.getId());
        result.put("planRoute", mainTrajectories);
        List<CaseContinuousVo> caseContinuousVos = getCaseContinuousInfo(taskSaveDto.getId());
        result.put("cases", caseContinuousVos);
        Set<Integer> count = new HashSet<>();
        for (CaseContinuousVo caseContinuousVo : CollectionUtils.emptyIfNull(caseContinuousVos)) {
            count.add(caseContinuousVo.getMapId());
        }
        if (count.size() == 1) {
            result.put("plan", true);
        }else{
            result.put("plan", false);
        }
        return result;
    }

    private TjTaskDataConfig getTaskAvConfig(Integer id) {
        List<TjTaskDataConfig> tjTaskDataConfigList = tjTaskDataConfigService.list(
            new LambdaQueryWrapper<TjTaskDataConfig>().eq(
                    TjTaskDataConfig::getTaskId, id)
                .eq(TjTaskDataConfig::getType, PartRole.AV)
                .orderByDesc(TjTaskDataConfig::getId));
        int size = tjTaskDataConfigList.size();
        if (size > 0) {
            if (size > 1) {
                for (int i = 1; i < size; i++) {
                    tjTaskDataConfigService.removeById(
                        tjTaskDataConfigList.get(i));
                }
            }
            return tjTaskDataConfigList.get(0);
        }
        return null;
    }

    /**
     * 节点4：查询任务场景评价信息
     *
     * @param taskSaveDto
     * @return
     * @throws BusinessException
     */
    private Map<String, Object> viewPlan(TaskSaveDto taskSaveDto) throws BusinessException {
        Map<String, Object> result = new HashMap<>();
        List<SceneIndexSchemeVo> sceneIndexSchemeVos = restService.getSceneIndexSchemeList(taskSaveDto);
        result.put("data", sceneIndexSchemeVos);
        return result;
    }


    private List<CaseContinuousVo> getCaseContinuousInfo(Integer taskId) throws BusinessException {
        // 已选择的用例
        TjTaskCase taskCase = new TjTaskCase();
        taskCase.setTaskId(taskId);
        List<TaskCaseVo> taskCaseVos = tjTaskCaseMapper.selectByCondition(taskCase);
        CaseQueryDto param = new CaseQueryDto();
        param.setSelectedIds(CollectionUtils.emptyIfNull(taskCaseVos).stream().map(TaskCaseVo::getCaseId).collect(Collectors.toList()));
        List<CaseDetailVo> caseDetails = caseMapper.selectCases(param);
        Map<Integer, CasePageVo> caseDetailMap = CollectionUtils.emptyIfNull(caseDetails).stream().collect(Collectors.toMap(CaseDetailVo::getId, v -> {
            CasePageVo casePageVo = new CasePageVo();
            BeanUtils.copyBeanProp(casePageVo, v);
            return casePageVo;
        }));
        return CollectionUtils.emptyIfNull(taskCaseVos).stream().map(t -> {
            CaseContinuousVo caseContinuousVo = new CaseContinuousVo();
            BeanUtils.copyBeanProp(caseContinuousVo, t);

            CasePageVo caseDetail = caseDetailMap.get(t.getCaseId());
            // 场景分类
            if (ObjectUtil.isNotEmpty(caseDetail) && StringUtils.isNotEmpty(caseDetail.getLabel())) {
                StringBuilder labelSort = new StringBuilder();
                for (String str : caseDetail.getLabel().split(",")) {
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
                caseContinuousVo.setSceneSort(labelSort.toString());
            }
            // 主车轨迹（仿真验证轨迹）
            try {
                List<TrajectoryValueDto> mainTrajectories = routeService.readMainTrajectoryFromOriRoute(t.getRouteFile());
                caseContinuousVo.setMainTrajectory(mainTrajectories);
                caseContinuousVo.setStartPoint(mainTrajectories.get(0));
                caseContinuousVo.setEndPoint(mainTrajectories.get(mainTrajectories.size() - 1));
            } catch (Exception e) {
                log.error(StringUtils.format("{}主车轨迹信息异常，请检查{}", caseContinuousVo.getCaseNumber(),
                        caseDetail.getRouteFile()));
            }
            // 连接轨迹
            List<SitePoint> trajectoryBos = JSONObject.parseArray(t.getConnectInfo(), SitePoint.class);
            caseContinuousVo.setConnectInfo(trajectoryBos);
            return caseContinuousVo;
        }).sorted(Comparator.comparing(CaseContinuousVo::getSort)).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveTask(TaskBo in) throws BusinessException {
        TjTask tjTask = new TjTask();
        if (ObjectUtil.isNotEmpty(in.getId())) {
            tjTask = this.getById(in.getId());
//            if (!TaskStatusEnum.NO_SUBMIT.getCode().equals(tjTask.getStatus())) {
//                throw new BusinessException("任务已提交，不可修改");
//            }
        }
        switch (in.getProcessNode()) {
            case TaskProcessNode.TASK_INFO:
                this.saveTaskInfo(tjTask, in);
                break;
            case TaskProcessNode.SELECT_CASE:
                this.saveTaskCaseInfo(tjTask, in);
                break;
            case TaskProcessNode.CONFIG:
                this.saveTaskContinuousConfig(tjTask, in);
                break;
            case TaskProcessNode.VIEW_PLAN:
                this.saveTaskEvaluateInfo(tjTask, in);
                break;
            default:
                break;
        }
        return tjTask.getId();
    }

    /**
     * 保存节点1：任务信息
     *
     * @param tjTask
     * @param in
     * @throws BusinessException
     */
    private void saveTaskInfo(TjTask tjTask, TaskBo in) throws BusinessException {
        if (CollectionUtils.isEmpty(in.getAvDeviceIds())) {
            throw new BusinessException("请选择主车被测设备");
        }
        TjTaskDataConfig newAvConfig = new TjTaskDataConfig();
        if (ObjectUtil.isNotEmpty(in.getId())) {
            tjTask.setClient(in.getClient());
            tjTask.setConsigner(in.getConsigner());
            tjTask.setContract(in.getContract());
            tjTask.setStartTime(in.getStartTime());
            tjTask.setEndTime(in.getEndTime());
            tjTask.setUpdatedBy(SecurityUtils.getUsername());
            tjTask.setUpdatedDate(LocalDateTime.now());
            if (in.getIsInner() != null) {
                tjTask.setIsInner(in.getIsInner());
                tjTask.setOpStatus(0);
                tjTask.setMeasurandId(in.getMeasurandId());
                tjTask.setApprecordId(in.getApprecordId());
            }
            TjTaskDataConfig oldAvConfig = tjTaskDataConfigService.getOne(new LambdaQueryWrapper<TjTaskDataConfig>()
                    .eq(TjTaskDataConfig::getTaskId, tjTask.getId())
                    .eq(TjTaskDataConfig::getType, PartRole.AV));
            this.updateById(tjTask);
            BeanUtils.copyBeanProp(newAvConfig, oldAvConfig);
        } else {
            BeanUtils.copyBeanProp(tjTask, in);
            tjTask.setTaskCode("task-" + sf.format(new Date()));
            tjTask.setProcessNode(TaskProcessNode.SELECT_CASE);
            // todo 排期日期
            tjTask.setPlanDate(new Date());
            tjTask.setCreateTime(new Date());
            tjTask.setStatus(TaskStatusEnum.NO_SUBMIT.getCode());
            tjTask.setCreatedBy(SecurityUtils.getUsername());
            tjTask.setCreatedDate(LocalDateTime.now());
            if (in.getIsInner() != null) {
                tjTask.setIsInner(in.getIsInner());
                tjTask.setOpStatus(0);
                tjTask.setMeasurandId(in.getMeasurandId());
                tjTask.setApprecordId(in.getApprecordId());
            }
            tjTask.setLastStatus("0");
            this.save(tjTask);
            newAvConfig.setTaskId(tjTask.getId());
            newAvConfig.setType(PartRole.AV);
            newAvConfig.setParticipatorId("1");
            newAvConfig.setParticipatorName("主车1");
        }
        in.getAvDeviceIds().stream().findFirst().ifPresent(newAvConfig::setDeviceId);
        tjTaskDataConfigService.saveOrUpdate(newAvConfig);
    }

    /**
     * 保存节点2：任务用例信息
     *
     * @param tjTask
     * @param in
     * @throws BusinessException
     */
    private void saveTaskCaseInfo(TjTask tjTask, TaskBo in) throws BusinessException {
        if (ObjectUtil.isEmpty(in.getId())) {
            throw new BusinessException("请选择任务");
        }
        List<TjTaskCase> taskCaseList = tjTaskCaseService.list(new LambdaQueryWrapper<TjTaskCase>()
                .eq(TjTaskCase::getTaskId, in.getId())
                .orderByDesc(TjTaskCase::getCreateTime));
        if (CollectionUtils.isEmpty(taskCaseList)) {
            throw new BusinessException("请选择用例");
        }
        // 默认按创建时间排序
        IntStream.range(0, taskCaseList.size()).forEach(i -> {
            taskCaseList.get(i).setSort(i + 1);
        });
        tjTaskCaseService.updateBatchById(taskCaseList);
        // 查询对应用例
        List<TjCase> cases = tjCaseService.listByIds(taskCaseList.stream().map(TjTaskCase::getCaseId).collect(Collectors.toList()));
        // todo 设备从运营平台查询
        TjDeviceDetailDto deviceDetailDto = new TjDeviceDetailDto();
        deviceDetailDto.setSupportRoles(PartRole.MV_SIMULATION);
        List<DeviceDetailVo> simulationDevices = deviceDetailMapper.selectByCondition(deviceDetailDto);
        if (CollectionUtils.isEmpty(simulationDevices)) {
            throw new BusinessException("无可用的从车设备");
        }
        // 删除老的任务配置
        tjTaskDataConfigService.remove(new LambdaQueryWrapper<TjTaskDataConfig>()
                .eq(TjTaskDataConfig::getTaskId, in.getId())
                .ne(TjTaskDataConfig::getType, PartRole.AV));
        // 添加新的任务配置
        List<TjTaskDataConfig> dataConfigList = new ArrayList<>();
        for (TjCase tjCase : cases) {
            List<TjCasePartConfig> casePartConfigs = tjCasePartConfigService.list(new LambdaQueryWrapper<TjCasePartConfig>()
                    .eq(TjCasePartConfig::getCaseId, tjCase.getId()));
            Map<String, TjCasePartConfig> casePartConfigMap = casePartConfigs.stream().collect(Collectors.toMap(TjCasePartConfig::getBusinessId, Function.identity()));
            SceneTrajectoryBo sceneTrajectoryBo = JSONObject.parseObject(FileUtils.readFile(tjCase.getDetailInfo()), SceneTrajectoryBo.class);
            // 当前所有从车都使用TESSNG
            List<TjTaskDataConfig> slaveConfigs = sceneTrajectoryBo.getParticipantTrajectories().stream()
                    .filter(t -> !PartType.MAIN.equals(t.getType())).map(t -> {
                        TjTaskDataConfig config = new TjTaskDataConfig();
                        config.setTaskId(in.getId());
                        config.setCaseId(tjCase.getId());
                        config.setType(casePartConfigMap.get(t.getId()).getParticipantRole());
                        config.setParticipatorId(t.getId());
                        config.setParticipatorName(t.getName());
                        config.setDeviceId(casePartConfigMap.get(t.getId()).getDeviceId());
                        return config;
                    }).collect(Collectors.toList());
            dataConfigList.addAll(slaveConfigs);
        }
        tjTaskDataConfigService.saveBatch(dataConfigList);
        // 修改任务信息
        tjTask.setProcessNode(TaskProcessNode.CONFIG);
        tjTask.setCaseCount(cases.size());
        updateById(tjTask);
    }

    /**
     * 保存节点3：任务连续性配置信息
     *
     * @param tjTask
     * @param in
     * @throws BusinessException
     */
    private void saveTaskContinuousConfig(TjTask tjTask, TaskBo in) throws BusinessException {
        if (ObjectUtil.isEmpty(in.getId())) {
            throw new BusinessException("请选择任务");
        }
        tjTask.setClient(in.getClient());
        tjTask.setConsigner(in.getConsigner());
        tjTask.setContract(in.getContract());

        if (TestType.VIRTUAL_REAL_FUSION.equals(tjTask.getTestType())) {
            if (CollectionUtils.isEmpty(in.getCases())) {
                throw new BusinessException("请选择用例");
            }
            TjTaskDataConfig taskAvConfig = tjTaskDataConfigService.getOne(new LambdaQueryWrapper<TjTaskDataConfig>()
                    .eq(TjTaskDataConfig::getTaskId, tjTask.getId())
                    .eq(TjTaskDataConfig::getType, PartRole.AV));
            if (ObjectUtil.isEmpty(taskAvConfig)) {
                throw new BusinessException("请配置被测设备");
            }
            TjDeviceDetail avDetail = deviceDetailMapper.selectById(taskAvConfig.getDeviceId());
            if (ObjectUtils.isEmpty(avDetail)) {
                throw new BusinessException(StringUtils.format("被测设备查询失败:{}", taskAvConfig.getDeviceId()));
            }
            if (in.getIsContinuity().equals(1)) {
                if (StringUtils.isEmpty(in.getRouteFile())) {
                    throw new BusinessException("请进行路径规划");
                }
                if (in.getCases().subList(0, in.getCases().size() - 1).stream()
                        .anyMatch(t -> CollectionUtils.isEmpty((List) t.getConnectInfo()))) {
                    throw new BusinessException("请完善用例连接信息");
                }
                tjTask.setContinuous(Boolean.TRUE);
            }
            // 修改任务用例信息
            List<TjTaskCase> tjTaskCases = tjTaskCaseService.listByIds(in.getCases().stream()
                    .map(CaseContinuousVo::getId).collect(Collectors.toList()));
            IntStream.range(0, in.getCases().size()).forEach(i -> {
                in.getCases().get(i).setSort(i + 1);
            });
            Map<Integer, List<SitePoint>> connectMap = in.getCases().stream().collect(Collectors.toMap(CaseContinuousVo::getId,
                    CaseContinuousVo::getConnectInfo));
            Map<Integer, Integer> sortMap = in.getCases().stream().collect(Collectors.toMap(CaseContinuousVo::getId,
                    CaseContinuousVo::getSort));
            for (int i = 0; i < tjTaskCases.size(); i++) {
                TjTaskCase taskCase = tjTaskCases.get(i);
                taskCase.setSort(sortMap.get(taskCase.getId()));
                List<SitePoint> obj = connectMap.get(taskCase.getId());
                taskCase.setConnectInfo(ObjectUtil.isEmpty(obj)
                        ? null
                        : JSONObject.toJSONString(obj));
                tjTaskCaseMapper.updateByCondition(taskCase);
            }
            // 修改连续式场景任务完整轨迹信息
            if (in.getIsContinuity().equals(1)) {
                tjTask.setMainPlanFile(in.getRouteFile());
            }
        }
//        tjTask.setProcessNode(TaskProcessNode.VIEW_PLAN);
        tjTask.setProcessNode(TaskProcessNode.WAIT_TEST);
        tjTask.setStatus(TaskStatusEnum.WAITING.getCode());
        updateById(tjTask);
    }

    /**
     * 保存节点4：任务测试评价信息
     *
     * @param tjTask
     * @param in
     * @throws BusinessException
     */
    private void saveTaskEvaluateInfo(TjTask tjTask, TaskBo in) throws BusinessException {
        if (ObjectUtil.isEmpty(in.getId())) {
            throw new BusinessException("请选择任务");
        }
        tjTask.setProcessNode(TaskProcessNode.WAIT_TEST);
        tjTask.setStatus(TaskStatusEnum.WAITING.getCode());
        updateById(tjTask);
    }

    @Override
    public boolean routingPlan(RoutingPlanDto routingPlanDto) throws BusinessException {
        TjTask task = getById(routingPlanDto.getTaskId());
        if (ObjectUtil.isEmpty(task)) {
            throw new BusinessException("任务不存在");
        }
        List<TjTaskDataConfig> configs = tjTaskDataConfigService.list(new LambdaQueryWrapper<TjTaskDataConfig>()
                .eq(TjTaskDataConfig::getTaskId, task.getId()));
        if (CollectionUtils.isEmpty(configs)) {
            throw new BusinessException("请配置任务被测车辆以及用例");
        }
        TjTaskDataConfig avConfig = configs.stream().filter(t -> t.getType().equals(PartRole.AV))
                .findFirst()
                .orElseThrow(() -> new BusinessException("请配置被测设备"));
//        TjDeviceDetail avDetail = deviceDetailMapper.selectById(avConfig.getDeviceId());
//        if ("域控制器".equals(Optional.of(avDetail)
//                .orElseThrow(() -> new BusinessException(StringUtils.format("被测设备查询失败:{}", avConfig.getDeviceId())))
//                .getDeviceType())) {
//            throw new BusinessException("域控制器无需进行路径规划（连续性配置）");
//        }
        TjTaskDataConfig svConfig = configs.stream().filter(t -> t.getType().equals(PartRole.MV_SIMULATION))
                .findFirst()
                .orElse(null);
        TjDeviceDetail svDeviceDetail = null;
//        if(svConfig != null){
//            svDeviceDetail = deviceDetailMapper.selectById(svConfig.getDeviceId());
//        }else {
        svDeviceDetail = deviceDetailMapper.selectOne(new LambdaQueryWrapper<TjDeviceDetail>()
                    .eq(TjDeviceDetail::getSupportRoles, "mvSimulation"));
//        }
        String channel = ChannelBuilder.buildRoutingPlanChannel(SecurityUtils.getUsername(), task.getId());
        Map<String, Object> params = buildRoutingPlanParam(task.getId(), routingPlanDto.getCases());
        routingPlanConsumer.subscribeAndSend(channel, task.getId(), task.getTaskCode());
        int start = restService.startServer(svDeviceDetail.getIp(), Integer.valueOf(svDeviceDetail.getServiceAddress()),
                new TessParam().buildRoutingPlanParam(21, channel, params, "21"));
        if (start != 1) {
            String repeatKey = "ROUTING_TASK_" + routingPlanDto.getTaskId();
            redisCache.deleteObject(repeatKey);
            throw new BusinessException("路径规划失败");
        }
        return true;
    }

    private Map<String, Object> buildRoutingPlanParam(Integer taskId, List<CaseContinuousVo> caseContinuousInfo) throws BusinessException {
        TjTaskCase param = new TjTaskCase();
        param.setTaskId(taskId);
        List<TaskCaseVo> taskCaseVos = tjTaskCaseMapper.selectByCondition(param);
        Map<Integer, TaskCaseVo> taskCaseMap = CollectionUtils.emptyIfNull(taskCaseVos).stream()
                .collect(Collectors.toMap(TaskCaseVo::getCaseId, v -> v));
        for (int i = 0; i < caseContinuousInfo.size(); i++) {
            CaseContinuousVo caseContinuousVo = caseContinuousInfo.get(i);
            caseContinuousVo.setTaskId(taskId);
            caseContinuousVo.setSort(i + 1);
            TaskCaseVo tc = taskCaseMap.get(caseContinuousVo.getCaseId());
            // 主车轨迹
            try {
//                List<TrajectoryValueDto> res = routeService.readMainTrajectoryFromOriRoute(tc.getRouteFile());
                Integer speed = 15;
//                if(res.size()>0){
//                    speed = res.get(0).getSpeed();
//                }
                TrajectoryValueDto start = buildTrajectoryValueDto(caseContinuousVo.getConnectInfo().get(0),speed);
                List<TrajectoryValueDto> mainTrajectories = new ArrayList<>();
                mainTrajectories.add(start);
                caseContinuousVo.setMainTrajectory(mainTrajectories);
                caseContinuousVo.setStartPoint(start);
                caseContinuousVo.setEndPoint(start);
            } catch (IOException e) {
                log.error(StringUtils.format("{}主车轨迹信息异常，请检查{}", caseContinuousVo.getCaseNumber(),
                        tc.getRouteFile()));
            }
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("taskId", taskId);
        result.put("params", caseContinuousInfo);
        return result;
    }

    private TrajectoryValueDto buildTrajectoryValueDto(SitePoint sitePoint, Integer speed) throws IOException {
        TrajectoryValueDto trajectoryValueDto = new TrajectoryValueDto();
        trajectoryValueDto.setLatitude(Double.valueOf(sitePoint.getLatitude()));
        trajectoryValueDto.setLongitude(Double.valueOf(sitePoint.getLongitude()));
        trajectoryValueDto.setId("1");
        trajectoryValueDto.setName("主车");
        trajectoryValueDto.setSpeed(speed);
        trajectoryValueDto.setFrameId(1);
        return trajectoryValueDto;
    }

    @Override
    public TjTask hasUnSubmitTask() {
        List<TjTask> list = list(new LambdaQueryWrapper<TjTask>()
                .eq(TjTask::getStatus, TaskStatusEnum.NO_SUBMIT.getCode())
                .eq(TjTask::getCreatedBy, SecurityUtils.getUsername()));
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    @Override
    public void export(HttpServletResponse response, Integer taskId) throws IOException {
        List<TaskReportVo> exportList = tjTaskMapper.getExportList(taskId);
        System.out.println(JSONObject.toJSONString(exportList));
        double score = 100;
        DecimalFormat decimalFormat = new DecimalFormat("#.0");

        // 使用DecimalFormat格式化浮点数
        try {
            for (TaskReportVo taskReportVo : CollectionUtils.emptyIfNull(exportList)) {
                if (taskReportVo.getScore().contains("-")) {
                    try {
                        score = score - Double.parseDouble(taskReportVo.getScore());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            for (TaskReportVo taskReportVo : CollectionUtils.emptyIfNull(exportList)) {
                taskReportVo.setScoreTotal(decimalFormat.format(score));
            }
        } catch (Exception e) {
            for (TaskReportVo taskReportVo : CollectionUtils.emptyIfNull(exportList)) {
                taskReportVo.setScoreTotal("100");
            }
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
//        response.setHeader("download-filename", FileUtils.percentEncode(task.getTaskName().concat(".xlsx")));


        //ExcelWriter该对象用于通过POI将值写入Excel
        String templatePath = "template" + File.separator + "TaskTemplate.xlsx";


        //导出模板
        CustomMergeStrategy customMergeStrategy = new CustomMergeStrategy(TaskReportVo.class, 5);
        WriteCellStyle style = new WriteCellStyle();
        style.setHorizontalAlignment(HorizontalAlignment.CENTER); // 设置水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER); // 设置垂直居中
        style.setBorderTop(BorderStyle.THIN); // 设置上边框样式为细线
        style.setBorderBottom(BorderStyle.THIN); // 设置下边框样式为细线
        style.setBorderLeft(BorderStyle.THIN); // 设置左边框样式为细线
        style.setBorderRight(BorderStyle.THIN); // 设置右边框样式为细线
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(new ClassPathResource(templatePath)
                            .getInputStream())
                    .registerWriteHandler(customMergeStrategy)
                    .registerWriteHandler(new HorizontalCellStyleStrategy(style, style))
                    .build();
        } catch (IOException e) {
            log.error("导出文件异常", e);
        }
        //构建excel的sheet
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.write(exportList, writeSheet);
        excelWriter.finish();
        // 关闭流
        excelWriter.finish();

    }

    @Override
    public void saveCustomScenarioWeight(SaveCustomScenarioWeightBo saveCustomScenarioWeightBo) {
        String weights = JSON.toJSONString(saveCustomScenarioWeightBo.getWeights());
        tjTaskMapper.saveCustomScenarioWeight(saveCustomScenarioWeightBo.getTask_id(), weights, "0");
    }

    @Override
    public void saveCustomIndexWeight(SaveCustomIndexWeightBo saveCustomIndexWeightBo) {
        String weights = JSON.toJSONString(saveCustomIndexWeightBo.getList());
        tjTaskMapper.saveCustomScenarioWeight(saveCustomIndexWeightBo.getTask_id(), weights, "1");
    }

    @Value("${tess.testReportOuterChain}")
    private String testReportOuterChain;

    @Override
    public String getTestReportOuterChain(HttpServletRequest request) {
        String requestUrl = StringUtils.isEmpty(testReportOuterChain) ? "" : testReportOuterChain;

//        String url = request.getHeader("X-Forwarded-Host").split(":")[0];
//        if (url == null) {
//            url = request.getHeader("X-Forwarded-For").split(",")[0];
//            if (url == null) {
//                url = request.getRemoteAddr();
//            }
//        }

        return requestUrl;
    }

    public class ExcelMergeCustomerCellHandler implements CellWriteHandler {
        /**
         * 一级合并的列，从0开始算
         */
        private int[] mergeColIndex;

        /**
         * 从指定的行开始合并，从0开始算
         */
        private int mergeRowIndex;

        /**
         * 在单元格上的所有操作完成后调用，遍历每一个单元格，判断是否需要向上合并
         */
        @Override
        public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
            // 获取当前单元格行下标
            int currRowIndex = cell.getRowIndex();
            // 获取当前单元格列下标
            int currColIndex = cell.getColumnIndex();
            // 判断是否大于指定行下标，如果大于则判断列是否也在指定的需要的合并单元列集合中
            if (currRowIndex > mergeRowIndex) {
                for (int i = 0; i < mergeColIndex.length; i++) {
                    if (currColIndex == mergeColIndex[i]) {
                        if (currColIndex <= 18) {
                            // 一级合并唯一标识
                            Object currLevelOneCode = cell.getRow().getCell(0).getStringCellValue();
                            Object preLevelOneCode = cell.getSheet().getRow(currRowIndex - 1).getCell(0).getStringCellValue();
                            // 判断两条数据的是否是同一集合，只有同一集合的数据才能合并单元格
                            if (preLevelOneCode.equals(currLevelOneCode)) {
                                // 如果都符合条件，则向上合并单元格
                                mergeWithPrevRow(writeSheetHolder, cell, currRowIndex, currColIndex);
                                break;
                            }
                        } else {
                            // 一级合并唯一标识
                            Object currLevelOneCode = cell.getRow().getCell(0).getStringCellValue();
                            Object preLevelOneCode = cell.getSheet().getRow(currRowIndex - 1).getCell(0).getStringCellValue();
                            // 二级合并唯一标识
                            Object currLevelTwoCode = cell.getRow().getCell(19).getStringCellValue();
                            Object preLevelTwoCode = cell.getSheet().getRow(currRowIndex - 1).getCell(19).getStringCellValue();
                            if (preLevelOneCode.equals(currLevelOneCode) && preLevelTwoCode.equals(currLevelTwoCode)) {
                                // 如果都符合条件，则向上合并单元格
                                mergeWithPrevRow(writeSheetHolder, cell, currRowIndex, currColIndex);
                                break;
                            }
                        }
                    }
                }
            }
        }

        /**
         * 当前单元格向上合并
         *
         * @param writeSheetHolder 表格处理句柄
         * @param cell             当前单元格
         * @param currRowIndex     当前行
         * @param currColIndex     当前列
         */
        private void mergeWithPrevRow(WriteSheetHolder writeSheetHolder, Cell cell, int currRowIndex, int currColIndex) {
            // 获取当前单元格数值
            Object currData = cell.getCellTypeEnum() == CellType.STRING ? cell.getStringCellValue() : cell.getNumericCellValue();
            // 获取当前单元格正上方的单元格对象
            Cell preCell = cell.getSheet().getRow(currRowIndex - 1).getCell(currColIndex);
            // 获取当前单元格正上方的单元格的数值
            Object preData = preCell.getCellTypeEnum() == CellType.STRING ? preCell.getStringCellValue() : preCell.getNumericCellValue();
            // 将当前单元格数值与其正上方单元格的数值比较
            if (preData.equals(currData)) {
                Sheet sheet = writeSheetHolder.getSheet();
                List<CellRangeAddress> mergeRegions = sheet.getMergedRegions();
                // 当前单元格的正上方单元格是否是已合并单元格
                boolean isMerged = false;
                for (int i = 0; i < mergeRegions.size() && !isMerged; i++) {
                    CellRangeAddress address = mergeRegions.get(i);
                    // 若上一个单元格已经被合并，则先移出原有的合并单元，再重新添加合并单元
                    if (address.isInRange(currRowIndex - 1, currColIndex)) {
                        sheet.removeMergedRegion(i);
                        address.setLastRow(currRowIndex);
                        sheet.addMergedRegion(address);
                        isMerged = true;
                    }
                }
                // 若上一个单元格未被合并，则新增合并单元
                if (!isMerged) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(currRowIndex - 1, currRowIndex, currColIndex, currColIndex);
                    sheet.addMergedRegion(cellRangeAddress);
                }
            }
        }
    }

    public synchronized String buildTaskNumber() {
        return StringUtils.format(ContentTemplate.TASK_NUMBER_TEMPLATE, DateUtils.getNowDayString(),
                CounterUtil.getNextNumber(ContentTemplate.TASK_NUMBER_TEMPLATE));
    }

    @Override
    public TjTask selectOneById(Integer id) {
        return tjTaskMapper.selectById(id);
    }

    @Override
    public boolean taskDelete(Integer taskId) {
        // 配置
        tjTaskDataConfigService.remove(
            new LambdaQueryWrapper<TjTaskDataConfig>().eq(
                TjTaskDataConfig::getTaskId, taskId));
        // 测试用例
        tjTaskCaseService.remove(
            new LambdaQueryWrapper<TjTaskCase>().eq(TjTaskCase::getTaskId,
                taskId));
        // 标签
        tjTaskMapper.deleteCustomScenarioWeightByTaskId(String.valueOf(taskId));

        // 历史记录
        List<TjTaskCaseRecord> tjTaskCaseRecords = taskCaseRecordService.list(
            new LambdaQueryWrapper<TjTaskCaseRecord>().eq(
                TjTaskCaseRecord::getTaskId, taskId));
        // 记录文件
        if (CollectionUtils.isNotEmpty(tjTaskCaseRecords)) {
            for (TjTaskCaseRecord tjTaskCaseRecord : tjTaskCaseRecords) {
                String routeFile = tjTaskCaseRecord.getRouteFile();
                if (StringUtils.isNotEmpty(routeFile)) {
                    FileUtils.deleteFile(
                        FileUploadUtils.getAbsolutePathFileName(routeFile));
                }
                String evaluatePath = tjTaskCaseRecord.getEvaluatePath();
                if (StringUtils.isNotEmpty(evaluatePath)) {
                    FileUtils.deleteFile(
                        FileUploadUtils.getAbsolutePathFileName(evaluatePath));
                }

                taskCaseRecordService.removeById(tjTaskCaseRecord);
            }
        }
        this.removeById(taskId);

        return true;
    }
}




