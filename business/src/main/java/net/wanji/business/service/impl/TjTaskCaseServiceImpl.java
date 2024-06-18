package net.wanji.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.common.Constants;
import net.wanji.business.common.Constants.ChannelBuilder;
import net.wanji.business.common.Constants.ColumnName;
import net.wanji.business.common.Constants.ContentTemplate;
import net.wanji.business.common.Constants.PartRole;
import net.wanji.business.common.Constants.PartType;
import net.wanji.business.common.Constants.PlaybackAction;
import net.wanji.business.common.Constants.PointTypeEnum;
import net.wanji.business.common.Constants.RedisMessageType;
import net.wanji.business.common.Constants.SysType;
import net.wanji.business.common.Constants.TaskCaseStatusEnum;
import net.wanji.business.common.Constants.TaskStatusEnum;
import net.wanji.business.common.Constants.TestMode;
import net.wanji.business.common.Constants.TestingStatusEnum;
import net.wanji.business.common.Constants.YN;
import net.wanji.business.component.DeviceStateToRedis;
import net.wanji.business.domain.Label;
import net.wanji.business.domain.RealWebsocketMessage;
import net.wanji.business.domain.bo.*;
import net.wanji.business.domain.dto.TessngEvaluateDto;
import net.wanji.business.domain.dto.device.DeviceReadyStateParam;
import net.wanji.business.domain.dto.device.ParamsDto;
import net.wanji.business.domain.param.*;
import net.wanji.business.domain.vo.*;
import net.wanji.business.entity.*;
import net.wanji.business.entity.evaluation.TessngEvaluationState;
import net.wanji.business.entity.infity.TjInfinityTask;
import net.wanji.business.evaluation.EvalContext;
import net.wanji.business.evaluation.EvaluationRedisData;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.listener.KafkaCollector;
import net.wanji.business.mapper.*;
import net.wanji.business.schedule.RealPlaybackSchedule;
import net.wanji.business.schedule.TwinsPlayback;
import net.wanji.business.service.*;
import net.wanji.business.service.evaluation.TessngEvaluationStateService;
import net.wanji.business.service.evaluation.TjTestSingleSceneScoreService;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.business.trajectory.TaskChainFactory;
import net.wanji.business.util.RedisLock;
import net.wanji.business.util.ToBuildOpenX;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.system.service.ISysDictDataService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author guowenhao
 * @description 针对表【tj_task_case(测试任务-用例详情表)】的数据库操作Service实现
 * @createDate 2023-08-31 17:39:16
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TjTaskCaseServiceImpl
    extends ServiceImpl<TjTaskCaseMapper, TjTaskCase>
    implements TjTaskCaseService {
  private final TjDeviceDetailService deviceDetailService;
  private final RestService restService;
  private final RouteService routeService;
  private final TjCaseService caseService;
  private final ILabelsService labelsService;
  private final TjTaskCaseRecordService taskCaseRecordService;
  private final ISysDictDataService dictDataService;
  private final TjCaseTreeService caseTreeService;
  private final KafkaCollector kafkaCollector;
  private final RedisLock redisLock;
  private final TjInfinityTaskService tjInfinityTaskService;
  private final ToBuildOpenX toBuildOpenX;
  private final TwinsPlayback twinsPlayback;
  private final TaskChainFactory taskChainFactory;
  private final KafkaProducer kafkaProducer;
  private final DeviceStateToRedis deviceStateToRedis;
  private final TessngEvaluationStateService tessngEvaluationStateService;
  private final EvaluationRedisData evaluationRedisData;
  private final TjTestSingleSceneScoreService tjTestSingleSceneScoreService;

  @Resource
  private TjTaskMapper taskMapper;
  @Resource
  private TjTaskCaseMapper taskCaseMapper;
  @Resource
  private TjTaskCaseRecordMapper taskCaseRecordMapper;
  @Resource
  private TjTaskDataConfigMapper taskDataConfigMapper;

  @Override
  public TaskCaseVerificationPageVo getStatus(TjTaskCase param, String user,
      boolean hand) throws BusinessException {
    log.info("查询任务的设备状态>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    if (ObjectUtils.isEmpty(param.getTaskId())) {
      throw new BusinessException("参数异常");
    }
    // 1.查询用例详情
    TjTask tjTask = taskMapper.selectById(param.getTaskId());
    if (ObjectUtils.isEmpty(tjTask)) {
      throw new BusinessException("查询失败，请检查任务是否存在");
    }
    List<TaskCaseInfoBo> taskCaseInfos = taskCaseMapper.selectTaskCaseByCondition(
        param);
    if (CollectionUtils.isEmpty(taskCaseInfos)) {
      throw new BusinessException("查询失败，请检查用例是否存在");
    }
    // 筛选任务用例
    // 非连续性测试（即自动化）任务，必须使用任务用例ID
    // 若任务用例ID不为空，按正常单任务用例测试流程进行
    // 若参数中任务用例ID为空时，若存在任务链，则使用任务链中当前节点的任务用例ID，否则，默认从第一个任务用例开始
    String taskChainNumber = ChannelBuilder.buildTaskDataChannel(user,
        tjTask.getId());
    if (!tjTask.isContinuous() && ObjectUtils.isEmpty(param.getId())) {
      if (taskChainFactory.hasChain(taskChainNumber) && !hand) {
        Integer currentNodeId = taskChainFactory.getCurrentNodeId(
            taskChainNumber);
        taskCaseInfos = taskCaseInfos.stream()
            .filter(t -> t.getId().equals(currentNodeId))
            .collect(Collectors.toList());
      } else {
        taskCaseMapper.reset(taskCaseInfos.stream().map(TaskCaseInfoBo::getId)
            .collect(Collectors.toList()));
        taskChainFactory.createTaskChain(taskChainNumber, user, tjTask.getId())
            .confirmState(taskChainNumber);
        taskCaseInfos = taskCaseInfos.stream().filter(t -> t.getSort() == 1)
            .collect(Collectors.toList());
        log.info("非连续性测试任务，使用第一个任务用例：{}",
            taskCaseInfos.get(0).getId());
      }
    }
    List<Integer> taskCaseIds = taskCaseInfos.stream()
        .map(TaskCaseInfoBo::getCaseId).collect(Collectors.toList());
    // 2.数据填充
    List<TaskCaseConfigBo> allTaskCaseConfigs = new ArrayList<>();
    Map<Integer, Map<String, String>> caseBusinessIdAndRoleMap = new HashMap<>();
    Map<Integer, Integer> caseMainSize = new HashMap<>();
    Map<String, String> startMap = new HashMap<>();
    Map<String, List<SimulationTrajectoryDto>> mainTrajectoryMap = new HashMap<>();
    Map<Integer, TaskCaseInfoBo> taskCaseInfoMap = new HashMap<>();
    fillStatusParam(tjTask, param.getId(), taskCaseInfos, allTaskCaseConfigs,
        caseBusinessIdAndRoleMap, caseMainSize, startMap, mainTrajectoryMap,
        taskCaseInfoMap);
    // 3.重复设备过滤
    List<TaskCaseConfigBo> distTaskCaseConfigs = filterConfigs(
        allTaskCaseConfigs);
    // 校验设备类型
    TaskCaseConfigBo avConfig = distTaskCaseConfigs.stream()
        .filter(t -> PartRole.AV.equals(t.getType())).findFirst()
        .orElseThrow(() -> new BusinessException("未配置被测设备"));
    TaskCaseConfigBo simulationConfig = distTaskCaseConfigs.stream().filter(
        t -> (PartRole.MV_SIMULATION.equals(t.getType()) || PartRole.SP.equals(
            t.getType()))).findFirst().orElse(null);
    TaskCaseConfigBo trackingConfig = distTaskCaseConfigs.stream()
        .filter(t -> t.getType().equals(PartRole.SV_TRACKING)).findFirst()
        .orElse(null);
    List<DeviceConnInfo> deviceConnInfos = distTaskCaseConfigs.stream()
        .filter(t -> t.getType().equals(PartRole.SV_TRACKING)).map(
            t -> new DeviceConnInfo(t.getDeviceId().toString(),
                t.getCommandChannel(), t.getDataChannel()))
        .collect(Collectors.toList());
    List<TaskCaseConfigBo> filteredTaskCaseConfigs = distTaskCaseConfigs.stream()
        .filter(t -> !(PartRole.MV_SIMULATION.equals(t.getType())
            || PartRole.SP.equals(t.getType()))).collect(Collectors.toList());
    if (hand) {
      if (param.getCaseId() == null) {
        param.setCaseId(taskCaseInfos.get(0).getCaseId());
      }
      handServer(param, tjTask, simulationConfig, filteredTaskCaseConfigs, user,
          taskCaseIds, deviceConnInfos, trackingConfig);
    }
    // 5.状态查询
    for (TaskCaseConfigBo taskCaseConfigBo : distTaskCaseConfigs) {
      // 查询设备状态
      Integer status = hand ?
          deviceDetailService.handDeviceState(taskCaseConfigBo.getDeviceId(),
              getCommandChannelByRoleTW(taskCaseConfigBo, user), false) :
          deviceDetailService.selectDeviceState(taskCaseConfigBo.getDeviceId(),
              getCommandChannelByRoleTW(taskCaseConfigBo, user), false);
      taskCaseConfigBo.setStatus(status);
      // 查询设备准备状态
      DeviceReadyStateParam stateParam = new DeviceReadyStateParam(
          taskCaseConfigBo.getDeviceId(),
          getCommandChannelByRoleTW(taskCaseConfigBo, user));
      if (PartRole.AV.equals(taskCaseConfigBo.getType())) {
        // av车需要主车全部轨迹
        stateParam.setParams(new ParamsDto("1", mainTrajectoryMap.get("main")));
      } else if (PartRole.MV_SIMULATION.equals(taskCaseConfigBo.getType())
          || PartRole.SP.equals(taskCaseConfigBo.getType())) {
        stateParam.setParams(
            buildTessStateParam(param.getTaskId(), taskCaseInfoMap,
                caseBusinessIdAndRoleMap, caseMainSize));
      } else {
        stateParam.setParams(new ParamsDto(taskCaseConfigBo.getParticipatorId(),
            mainTrajectoryMap.get(taskCaseConfigBo.getParticipatorId())));
      }
      Integer readyStatus = hand ?
          deviceDetailService.handDeviceReadyState(
              taskCaseConfigBo.getDeviceId(),
              getReadyStatusChannelByTypeTW(taskCaseConfigBo, user), stateParam,
              false) :
          deviceDetailService.selectDeviceReadyState(
              taskCaseConfigBo.getDeviceId(),
              getReadyStatusChannelByTypeTW(taskCaseConfigBo, user), stateParam,
              false);
      taskCaseConfigBo.setPositionStatus(readyStatus);
    }
    // 6.构建页面结果集
    TaskCaseVerificationPageVo result = buildPageVo(param, startMap,
        allTaskCaseConfigs, distTaskCaseConfigs);
    if (!tjTask.isContinuous()) {
      if (result.getStatusMap().entrySet().stream().allMatch(
          e -> e.getValue().stream().allMatch(
              t -> t.getStatus() == 1 && t.getPositionStatus() == 1))) {
        taskChainFactory.confirmComplete(taskChainNumber);
      }
    }
    return result;
  }

  private void handServer(TjTaskCase param, TjTask tjTask,
      TaskCaseConfigBo simulationConfig,
      List<TaskCaseConfigBo> filteredTaskCaseConfigs, String user,
      List<Integer> caseIds, List<DeviceConnInfo> deviceConnInfos,
      TaskCaseConfigBo trackingConfig) throws BusinessException {
    List<String> mapList = new ArrayList<>();
    for (Integer caseId : caseIds) {
      TjCase caseInfo = caseService.getById(caseId);
      if (caseInfo.getMapId() != null) {
        mapList.add(String.valueOf(caseInfo.getMapId()));
      } else {
        mapList.add("10");
      }
    }
    TessParam tessParam = buildTessServerParam(1, tjTask.getCreatedBy(),
        param.getTaskId(), mapList);
    // 先停止
    stop(param.getTaskId(), param.getId(), user);
    // 4.唤醒仿真服务
    if (simulationConfig != null) {
      int res = restService.startServer(simulationConfig.getIp(),
          Integer.valueOf(simulationConfig.getServiceAddress()), tessParam);
      if (res == 0) {
        throw new BusinessException("唤醒仿真服务失败");
      } else if (res == 2) {
        throw new BusinessException("仿真程序忙，请稍后再试");
      }
    } else {
      // 无tessng时发送评价信息到tessng数据库用于评价
      sendTessngEvaluationState(tessParam, param.getTaskId());
    }
    if (trackingConfig != null) {
      TjCase caseInfo = caseService.getById(param.getCaseId());
      TessTrackParam tessTrackParam = new TessTrackParam(param.getCaseId(),
          caseInfo.getMapId(), deviceConnInfos.size(), deviceConnInfos);
      int res = restService.takeSvServer(trackingConfig.getIp(),
          Integer.valueOf(trackingConfig.getServiceAddress()), tessTrackParam,
          1);
      if (0 == res) {
        throw new BusinessException("唤起云控车仿真服务失败");
      } else if (2 == res) {
        throw new BusinessException("云控车仿真服务忙，请稍后再试");
      }
    }
    for (TaskCaseConfigBo taskCaseConfigBo : filteredTaskCaseConfigs) {
      if (!redisLock.tryLock("task_" + taskCaseConfigBo.getDataChannel(),
          user)) {
        throw new BusinessException(
            taskCaseConfigBo.getDeviceName() + "设备正在使用中，请稍后再试，使用者："
                + user);
      }
    }
    if (param.getTaskId() != null) {
      redisLock.setUser("tw_" + param.getTaskId(), user);
    }
  }

  private String getCommandChannelByRole(TaskCaseConfigBo taskCaseConfigBo) {
    return PartRole.MV_SIMULATION.equals(taskCaseConfigBo.getSupportRoles()) ?
        ChannelBuilder.buildTaskControlChannel(SecurityUtils.getUsername(),
            taskCaseConfigBo.getTaskId()) :
        taskCaseConfigBo.getCommandChannel();
  }

  private String getReadyStatusChannelByType(
      TaskCaseConfigBo taskCaseConfigBo) {
    return PartRole.MV_SIMULATION.equals(taskCaseConfigBo.getSupportRoles()) ?
        ChannelBuilder.buildTaskStatusChannel(SecurityUtils.getUsername(),
            taskCaseConfigBo.getTaskId()) :
        ChannelBuilder.DEFAULT_STATUS_CHANNEL;
  }

  private String getCommandChannelByRoleTW(TaskCaseConfigBo taskCaseConfigBo,
      String userName) {
    return PartRole.MV_SIMULATION.equals(taskCaseConfigBo.getSupportRoles())
        || PartRole.SP.equals(taskCaseConfigBo.getSupportRoles()) ?
        ChannelBuilder.buildTaskControlChannel(userName,
            taskCaseConfigBo.getTaskId()) :
        taskCaseConfigBo.getCommandChannel();
  }

  private String getReadyStatusChannelByTypeTW(
      TaskCaseConfigBo taskCaseConfigBo, String userName) {
    return PartRole.MV_SIMULATION.equals(taskCaseConfigBo.getSupportRoles())
        || PartRole.SP.equals(taskCaseConfigBo.getSupportRoles()) ?
        ChannelBuilder.buildTaskStatusChannel(userName,
            taskCaseConfigBo.getTaskId()) :
        ChannelBuilder.DEFAULT_STATUS_CHANNEL;
  }

  /**
   * 构建唤醒tess服务的参数
   *
   * @param roadNum
   * @param taskId
   * @return
   */
  private TessParam buildTessServerParam(Integer roadNum, String username,
      Integer taskId, List<String> mapList) {
    return new TessParam().buildTaskParam(roadNum,
        ChannelBuilder.buildTaskDataChannel(username, taskId),
        ChannelBuilder.buildTaskControlChannel(username, taskId),
        ChannelBuilder.buildTaskEvaluateChannel(username, taskId),
        ChannelBuilder.buildTaskStatusChannel(username, taskId), mapList);
  }

  /**
   * 构建页面结果集数据
   *
   * @param taskCase
   * @param startMap
   * @param allTaskCaseConfigs
   * @param distTaskCaseConfigs
   * @return
   */
  private TaskCaseVerificationPageVo buildPageVo(TjTaskCase taskCase,
      Map<String, String> startMap, List<TaskCaseConfigBo> allTaskCaseConfigs,
      List<TaskCaseConfigBo> distTaskCaseConfigs) {
    for (TaskCaseConfigBo taskCaseConfigBo : allTaskCaseConfigs) {
      if (PartRole.AV.equals(taskCaseConfigBo.getType())
          && !ObjectUtils.isEmpty(startMap)) {
        String[] position = startMap.get(taskCaseConfigBo.getParticipatorId())
            .split(",");
        taskCaseConfigBo.setStartLongitude(Double.parseDouble(position[0]));
        taskCaseConfigBo.setStartLatitude(Double.parseDouble(position[1]));
      }
    }
    TaskCaseVerificationPageVo result = new TaskCaseVerificationPageVo();
    result.setTaskId(taskCase.getTaskId());
    result.setTaskCaseId(taskCase.getId());
    result.setStatusMap(distTaskCaseConfigs.stream()
        .collect(Collectors.groupingBy(TaskCaseConfigBo::getType)));
    result.setViewMap(allTaskCaseConfigs.stream()
        .collect(Collectors.groupingBy(TaskCaseConfigBo::getType)));
    validStatus(result);
    return result;
  }

  /**
   * 筛选重复设备
   *
   * @param taskCaseConfigs
   * @return
   */
  private List<TaskCaseConfigBo> filterConfigs(
      List<TaskCaseConfigBo> taskCaseConfigs) {
    return taskCaseConfigs.stream()
        .filter(info -> !ObjectUtils.isEmpty(info.getDeviceId())).collect(
            Collectors.collectingAndThen(Collectors.toCollection(
                    () -> new TreeSet<>(
                        Comparator.comparing(TaskCaseConfigBo::getDeviceId))),
                ArrayList::new));
  }

  /**
   * 获取状态接口的数据填充
   *
   * @param tjTask
   * @param taskCaseId
   * @param taskCaseInfos
   * @param taskCaseConfigs
   * @param caseBusinessIdAndRoleMap
   * @param caseMainSize
   * @param startMap
   * @param mainTrajectoryMap
   * @param taskCaseInfoMap
   * @throws BusinessException
   */
  private void fillStatusParam(TjTask tjTask, Integer taskCaseId,
      List<TaskCaseInfoBo> taskCaseInfos,
      List<TaskCaseConfigBo> taskCaseConfigs,
      Map<Integer, Map<String, String>> caseBusinessIdAndRoleMap,
      Map<Integer, Integer> caseMainSize, Map<String, String> startMap,
      Map<String, List<SimulationTrajectoryDto>> mainTrajectoryMap,
      Map<Integer, TaskCaseInfoBo> taskCaseInfoMap) throws BusinessException {
    List<SimulationTrajectoryDto> mainTrajectories = new ArrayList<>();
    // 8.taskCaseId为空时，则按整体任务进行。连续性任务使用主车规划路径；非连续性任务使用拼接的仿真验证的轨迹
    if (ObjectUtils.isEmpty(taskCaseId) && tjTask.isContinuous()) {
      try {
        List<SimulationTrajectoryDto> main = routeService.readOriRouteFile(
            tjTask.getMainPlanFile());
        mainTrajectories.addAll(main);
      } catch (IOException e) {
        log.error("主车规划路径文件读取失败：{}", e.getMessage());
        throw new BusinessException("读取主车已规划路径文件失败");
      } catch (NullPointerException v2) {
        throw new BusinessException("主车轨迹未规划");
      }
    }
    for (TaskCaseInfoBo taskCaseInfoBo : taskCaseInfos) {
      // 2.数据校验
      validConfig(taskCaseInfoBo);
      // 3.轨迹详情(使用实车试验的点位配置)
      SceneTrajectoryBo trajectoryDetail = JSONObject.parseObject(
          taskCaseInfoBo.getDetailInfo(), SceneTrajectoryBo.class);
      if (taskCaseInfoBo.getSort() == 1) {
        // 4.参与者开始点位
        for (ParticipantTrajectoryBo trajectoryBo : CollectionUtils.emptyIfNull(
            trajectoryDetail.getParticipantTrajectories())) {
          startMap.put(trajectoryBo.getId(),
              CollectionUtils.emptyIfNull(trajectoryBo.getTrajectory()).stream()
                  .filter(t -> PointTypeEnum.START.getPointType()
                      .equals(t.getType())).findFirst()
                  .orElse(new TrajectoryDetailBo()).getPosition());
        }
      }
      if (CollectionUtils.isEmpty(taskCaseInfoBo.getDataConfigs())) {
        log.error("任务 {} 用例 {} 配置信息异常", taskCaseInfoBo.getTaskId(),
            taskCaseInfoBo.getCaseId());
        throw new BusinessException("任务用例配置信息异常");
      }
      // 5.用例配置
      taskCaseConfigs.addAll(taskCaseInfoBo.getDataConfigs());
      // 6.参与者ID和参与者名称匹配map
      Map<String, String> businessIdAndRoleMap = taskCaseInfoBo.getDataConfigs()
          .stream().collect(
              Collectors.toMap(TaskCaseConfigBo::getParticipatorId,
                  TaskCaseConfigBo::getType));
      caseBusinessIdAndRoleMap.put(taskCaseInfoBo.getCaseId(),
          businessIdAndRoleMap);

      // 7.用例对应主车轨迹长度(使用仿真验证的轨迹)
      try {
        List<SimulationTrajectoryDto> trajectories = routeService.readOriRouteFile(
            taskCaseInfoBo.getRouteFile());

        // 若taskCaseId不为空，则按用例进行，使用用例仿真验证的轨迹
        if ((!tjTask.isContinuous() || !ObjectUtils.isEmpty(taskCaseId))) {
          mainTrajectories.addAll(trajectories);
        }
        caseMainSize.put(taskCaseInfoBo.getCaseId(), trajectories.size());
      } catch (IOException e) {
        log.error("用例轨迹文件读取失败：{}", e.getMessage());
      }
      // 8、其余设备轨迹
      taskCaseConfigs = taskCaseInfoBo.getDataConfigs();
      taskCaseConfigs.forEach(taskCaseConfigBo -> {
        if (!PartRole.MV_SIMULATION.equals(taskCaseConfigBo.getSupportRoles())
            && !PartRole.AV.equals(taskCaseConfigBo.getSupportRoles())
            || !PartRole.SP.equals(taskCaseConfigBo.getSupportRoles())) {
          try {
            mainTrajectoryMap.put(taskCaseConfigBo.getParticipatorId(),
                routeService.mainTrajectory(taskCaseInfoBo.getRouteFile(),
                    taskCaseConfigBo.getParticipatorId()));
          } catch (BusinessException e) {
            throw new RuntimeException(e);
          }
        }
      });
    }

    mainTrajectories = mainTrajectories.stream().filter(
        item -> !CollectionUtils.isEmpty(item.getValue()) && item.getValue()
            .stream().anyMatch(p -> p.getDriveType() == 1)).peek(
        s -> s.setValue(s.getValue().stream().filter(p -> p.getDriveType() == 1)
            .collect(Collectors.toList()))).collect(Collectors.toList());
    mainTrajectoryMap.put("main", mainTrajectories);
    for (TaskCaseInfoBo caseInfoBo : taskCaseInfos) {
      taskCaseInfoMap.put(caseInfoBo.getCaseId(), caseInfoBo);
    }
  }

  /**
   * 创建tess准备状态参数
   *
   * @param taskCaseInfoMap
   * @param caseBusinessIdAndRoleMap
   * @param caseMainSize
   * @return
   */
  private Map<String, Object> buildTessStateParam(Integer taskId,
      Map<Integer, TaskCaseInfoBo> taskCaseInfoMap,
      Map<Integer, Map<String, String>> caseBusinessIdAndRoleMap,
      Map<Integer, Integer> caseMainSize) {
    Map<String, Object> tessParams = new HashMap<>();

    List<Map<String, Object>> param1 = new ArrayList<>();
    for (Entry<Integer, TaskCaseInfoBo> caseInfoEntry : taskCaseInfoMap.entrySet()) {
      Integer caseId = caseInfoEntry.getKey();
      TaskCaseInfoBo taskCaseInfoBo = caseInfoEntry.getValue();
      Map<String, String> businessIdAndRoleMap = caseBusinessIdAndRoleMap.get(
          caseId);

      Map<String, Object> caseParam = new HashMap<>();
      caseParam.put("caseId", caseId);
      caseParam.put("sort", taskCaseInfoBo.getSort());
      caseParam.put("avPassTime", caseMainSize.get(caseId));
      Label label = new Label();
      label.setParentId(2L);
      List<Label> sceneTypeLabelList = labelsService.selectLabelsList(label);
      List<String> sceneTypes = new ArrayList<>();
      if (StringUtils.isNotEmpty(taskCaseInfoBo.getAllStageLabel())) {
        String[] labels = taskCaseInfoBo.getAllStageLabel().split(",");
        for (String labelId : labels) {
          for (Label sceneTypeLabel : sceneTypeLabelList) {
            if (sceneTypeLabel.getId() == Long.parseLong(labelId)) {
              sceneTypes.add(sceneTypeLabel.getName());
            }
          }
        }
      }
      caseParam.put("type", String.join(",", sceneTypes));
      SceneTrajectoryBo sceneTrajectoryBo = JSONObject.parseObject(
          taskCaseInfoBo.getDetailInfo(), SceneTrajectoryBo.class);
      List<Map<String, Object>> simulationTrajectories = new ArrayList<>();
      for (ParticipantTrajectoryBo participantTrajectory : sceneTrajectoryBo.getParticipantTrajectories()) {
        if (PartType.MAIN.equals(participantTrajectory.getType())) {
          tessParams.put("avId", participantTrajectory.getId());
          tessParams.put("avName", participantTrajectory.getName());
          continue;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", participantTrajectory.getId());
        map.put("model", participantTrajectory.getModel());
        map.put("name", caseId + "_" + participantTrajectory.getName());
        map.put("role",
            businessIdAndRoleMap.get(participantTrajectory.getId()));
        map.put("trajectory",
            participantTrajectory.getTrajectory().stream().map(item -> {
              Map<String, Object> t = new HashMap<>();
              t.put("type", item.getType());
              t.put("time", item.getTime());
              t.put("lane", item.getLane());
              t.put("speed", item.getSpeed());
              String[] pos = item.getPosition().split(",");
              t.put("position", Arrays.asList(pos[0], pos[1]));
              return t;
            }).collect(Collectors.toList()));
        simulationTrajectories.add(map);
      }
      caseParam.put("participantTrajectories", simulationTrajectories);
      param1.add(caseParam);
    }
    param1.sort(Comparator.comparingInt(t -> (int) t.get("sort")));
    tessParams.put("param1", param1);
    tessParams.put("taskId", taskId);
    return tessParams;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public CaseRealTestVo prepare(TjTaskCase param, String user)
      throws BusinessException {
    log.info("准备>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    // 1.用例详情
    TjTask tjTask = taskMapper.selectById(param.getTaskId());
    if (ObjectUtils.isEmpty(tjTask)) {
      throw new BusinessException("测试任务不存在");
    }
    List<TaskCaseInfoBo> taskCaseInfos = taskCaseMapper.selectTaskCaseByCondition(
        param);
    if (CollectionUtils.isEmpty(taskCaseInfos)) {
      throw new BusinessException("测试任务用例不存在");
    }
    Integer recordId = getRecordId(taskCaseInfos);
    // 非连续任务，执行任务链当前用例
    if (!tjTask.isContinuous()) {
      String taskChainNumber = ChannelBuilder.buildTaskDataChannel(user,
          tjTask.getId());
      if (!taskChainFactory.hasChain(taskChainNumber)) {
        throw new BusinessException("任务链不存在");
      }
      taskChainFactory.prepare(taskChainNumber);
      if (ObjectUtils.isEmpty(param.getId())) {
        Integer currentNodeId = taskChainFactory.getCurrentNodeId(
            taskChainNumber);
        taskCaseInfos = taskCaseInfos.stream()
            .filter(t -> t.getId().equals(currentNodeId))
            .collect(Collectors.toList());
      }
    }
    List<Integer> resetList = new ArrayList<>();
    List<Integer> deleteIdList = new ArrayList<>();
    List<TjTaskCaseRecord> addList = new ArrayList<>();
    for (TaskCaseInfoBo taskCaseInfoBo : taskCaseInfos) {
      // 2.校验数据
      validConfig(taskCaseInfoBo);
      // 3.轨迹详情
      CaseTrajectoryDetailBo caseTrajectoryDetailBo = JSONObject.parseObject(
          taskCaseInfoBo.getDetailInfo(), CaseTrajectoryDetailBo.class);
      // 4.角色配置信息
      Map<String, List<TaskCaseConfigBo>> partMap = taskCaseInfoBo.getDataConfigs()
          .stream()
          .collect(Collectors.groupingBy(TaskCaseConfigBo::getSupportRoles));
      // 5.各角色数量
      int avNum = partMap.containsKey(PartRole.AV) ?
          partMap.get(PartRole.AV).size() :
          0;
      int simulationNum = partMap.containsKey(PartRole.MV_SIMULATION) ?
          partMap.get(PartRole.MV_SIMULATION).size() :
          0;
      int pedestrianNum = partMap.containsKey(PartRole.SP) ?
          partMap.get(PartRole.SP).size() :
          0;
      caseTrajectoryDetailBo.setSceneForm(
          StringUtils.format(ContentTemplate.SCENE_FORM_TEMPLATE, avNum,
              simulationNum, pedestrianNum));
      caseTrajectoryDetailBo.setSceneDesc(taskCaseInfoBo.getSceneName());
      // 6.删除空记录
      List<TjTaskCaseRecord> caseRecords = taskCaseRecordService.list(
          new LambdaQueryWrapper<TjTaskCaseRecord>().eq(
                  TjTaskCaseRecord::getTaskId, taskCaseInfoBo.getTaskId())
              .eq(TjTaskCaseRecord::getCaseId, taskCaseInfoBo.getCaseId())
              .isNull(TjTaskCaseRecord::getRouteFile)
              .select(TjTaskCaseRecord::getId));
      Optional.of(caseRecords).filter(CollectionUtils::isNotEmpty)
          .ifPresent(records -> records.forEach(record -> {
            deleteIdList.add(record.getId());
          }));
      // 7.新增记录
      TjTaskCaseRecord addRecord = new TjTaskCaseRecord();
      addRecord.setTaskId(taskCaseInfoBo.getTaskId());
      addRecord.setCaseId(taskCaseInfoBo.getCaseId());
      addRecord.setDetailInfo(JSONObject.toJSONString(
          routeService.resetTrajectoryProp(caseTrajectoryDetailBo)));
      addRecord.setStatus(TestingStatusEnum.NO_PASS.getCode());
      addRecord.setCreatedBy(user);
      addRecord.setCreatedDate(LocalDateTime.now());
      addList.add(addRecord);
      // 8.重置测试用例
      resetList.add(taskCaseInfoBo.getId());
    }
    // 9.删除空记录
    Optional.of(deleteIdList).filter(CollectionUtils::isNotEmpty)
        .ifPresent(deleteIds -> taskCaseRecordService.removeByIds(deleteIds));
    // 10.新增记录
    Optional.of(addList).filter(CollectionUtils::isNotEmpty)
        .ifPresent(records -> {
          taskCaseRecordService.saveBatch(records);
          Integer rId = recordId;
          // 设置测试记录ID
          if (null == rId) {
            rId = -1;
          }
          for (TjTaskCaseRecord record : records) {
            record.setRecordId(rId);
          }
          taskCaseRecordService.updateBatchById(records);

        });
    // 11.测试用例准备
    Optional.of(resetList).filter(CollectionUtils::isNotEmpty)
        .ifPresent(resets -> taskCaseMapper.prepare(resets));
    // 12.前端结果集
    CaseRealTestVo caseRealTestVo = new CaseRealTestVo();
    caseRealTestVo.setTaskId(param.getTaskId());
    caseRealTestVo.setTaskCaseId(param.getId());
    return caseRealTestVo;
  }

  @Transactional(rollbackFor = Exception.class)
  @Override
  public CaseRealTestVo controlTask(Integer taskId, Integer taskCaseId,
      Integer action, String user, String taskChainNumber)
      throws BusinessException {
    log.info("平台测试开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    // 1.任务用例测试记录详情
    TjTask tjTask = taskMapper.selectById(taskId);
    if (ObjectUtils.isEmpty(tjTask)) {
      throw new BusinessException("任务不存在");
    }
    TjTaskCase param = new TjTaskCase();
    param.setTaskId(taskId);
    param.setId(taskCaseId);
    List<TaskCaseInfoBo> taskCaseInfos = taskCaseMapper.selectTaskCaseByCondition(
        param);
    if (CollectionUtils.isEmpty(taskCaseInfos)) {
      throw new BusinessException("任务用例不存在");
    }
    if (!tjTask.isContinuous()) {
      taskChainNumber = StringUtils.isEmpty(taskChainNumber) ?
          ChannelBuilder.buildTaskDataChannel(user, tjTask.getId()) :
          taskChainNumber;
      if (!taskChainFactory.hasChain(taskChainNumber)) {
        throw new BusinessException("任务链不存在");
      }
      if (ObjectUtils.isEmpty(taskCaseId)) {
        Integer currentNodeId = taskChainFactory.getCurrentNodeId(
            taskChainNumber);
        taskCaseInfos = taskCaseInfos.stream()
            .filter(t -> t.getId().equals(currentNodeId))
            .collect(Collectors.toList());
      }
    }
    if (!TaskStatusEnum.RUNNING.getCode().equals(tjTask.getStatus())) {
      tjTask.setStatus(TaskStatusEnum.RUNNING.getCode());
      if (taskMapper.updateById(tjTask) < 1) {
        throw new BusinessException("任务状态更新失败");
      }
    }

    CaseTrajectoryParam caseTrajectoryParam = new CaseTrajectoryParam();
    for (TaskCaseInfoBo taskCaseInfo : taskCaseInfos) {
      for (TaskCaseConfigBo caseConfig : taskCaseInfo.getDataConfigs()) {
        if (caseConfig.getType().equals(PartRole.AV)) {
          caseTrajectoryParam.setDataChannel(caseConfig.getDataChannel());
          caseTrajectoryParam.setControlChannel(caseConfig.getCommandChannel());
          Map<String, String> vehicleTypeMap = new HashMap<>();
          vehicleTypeMap.put(caseConfig.getType(),
              caseConfig.getParticipatorId());
          caseTrajectoryParam.setVehicleIdTypeMap(vehicleTypeMap);
          break;
        }
      }
    }
    if (ObjectUtils.isEmpty(caseTrajectoryParam.getDataChannel())
        || ObjectUtils.isEmpty(caseTrajectoryParam.getControlChannel())) {
      throw new BusinessException("任务数据配置异常");
    }
    caseTrajectoryParam.setTaskId(taskId);
    caseTrajectoryParam.setTestMode(
        tjTask.isContinuous() ? TestMode.CONTINUOUS_TEST : TestMode.BATCH_TEST);
    caseTrajectoryParam.setCaseId(taskCaseId);

    List<CaseSSInfo> caseSSInfos = new ArrayList<>();
    List<ParticipantTrajectoryBo> mainPoints = new ArrayList<>();
    for (TaskCaseInfoBo taskCase : taskCaseInfos) {
      CaseSSInfo caseSSInfo = new CaseSSInfo();
      caseSSInfo.setCaseId(taskCase.getCaseId());
      if (StringUtils.isEmpty(taskCase.getDetailInfo())) {
        throw new BusinessException(
            StringUtils.format("用例{}轨迹不存在", taskCase.getCaseNumber()));
      }
      SceneTrajectoryBo sceneTrajectoryBo = JSONObject.parseObject(
          taskCase.getDetailInfo(), SceneTrajectoryBo.class);
      CollectionUtils.emptyIfNull(
              sceneTrajectoryBo.getParticipantTrajectories()).stream()
          .filter(p -> PartType.MAIN.equals(p.getType())).findFirst()
          .ifPresent(f -> {

            caseSSInfo.setTrajectoryPoints(f.getTrajectory().stream().map(t -> {
              Map<String, Object> map = new HashMap<>();
              map.put("latitude", t.getLatitude());
              map.put("longitude", t.getLongitude());
              return map;
            }).collect(Collectors.toList()));
            caseTrajectoryParam.setCaseTrajectorySSVoList(
                Collections.singletonList(caseSSInfo));

            Map<String, String> vehicleTypeMap = new HashMap<>();
            vehicleTypeMap.put(PartRole.AV, f.getId());
            caseTrajectoryParam.setVehicleIdTypeMap(vehicleTypeMap);

            mainPoints.add(f);
          });
      caseSSInfos.add(caseSSInfo);
    }
    caseTrajectoryParam.setCaseTrajectorySSVoList(caseSSInfos);

    Map<String, Object> context = new HashMap<>();
    context.put(Constants.MasterContext.USERNAME, user);
    context.put(Constants.MasterContext.TASK_CHAIN_NUMBER, taskChainNumber);
    // 添加recordId
    setRecordId(taskId, taskCaseInfos.get(0).getCaseId(), context);
    caseTrajectoryParam.setContext(context);
    // 更新kafka收集器
    String key = ChannelBuilder.buildTaskDataChannel(user, taskId);
    kafkaCollector.remove(key, null);

    if (!tjTask.isContinuous()) {
      taskChainFactory.prepareComplete(key);
    }
    // 向主控发送主车信息
    if (!restService.sendCaseTrajectoryInfo(caseTrajectoryParam)) {
      throw new BusinessException("向主控发送主车信息失败");
    }

    // 前端结果集
    CaseRealTestVo caseRealTestVo = new CaseRealTestVo();
    caseRealTestVo.setTaskId(taskId);
    caseRealTestVo.setTaskCaseId(taskCaseId);
    caseRealTestVo.setStartTime(DateUtils.getTime());
    List<TrajectoryDetailBo> trajectoryDetailBos = new ArrayList<>();
    for (ParticipantTrajectoryBo mainPoint : mainPoints) {
      trajectoryDetailBos.addAll(mainPoint.getTrajectory());
    }
    Map<String, List<TrajectoryDetailBo>> mainTrajectoryMap = new HashMap<>();
    mainTrajectoryMap.put("1", trajectoryDetailBos);
    caseRealTestVo.setMainTrajectories(mainTrajectoryMap);
    return caseRealTestVo;
  }

  private void setRecordId(Integer taskId, Integer taskCaseId,
      Map<String, Object> context) {
    QueryWrapper<TjTaskCaseRecord> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(ColumnName.TASK_ID, taskId);
    queryWrapper.eq(ColumnName.CASE_ID_COLUMN, taskCaseId);
    queryWrapper.orderByDesc(ColumnName.CREATED_DATE_COLUMN);
    Page<TjTaskCaseRecord> recordPage = new Page<>(0, 1);
    Page<TjTaskCaseRecord> page = taskCaseRecordService.page(recordPage,
        queryWrapper);
    List<TjTaskCaseRecord> records = page.getRecords();
    if (CollectionUtils.isNotEmpty(records)) {
      context.put(Constants.MasterContext.RECORD_ID,
          records.get(0).getRecordId());
    } else {
      if (log.isErrorEnabled()) {
        log.error("taskId[{}] caseId[{}] missing!", taskId, taskCaseId);
      }
    }
  }

  @Override
  public CaseRealTestVo getTaskInfo(Integer taskId) throws BusinessException {
    TjTaskCase taskCase = taskCaseMapper.selectOne(
        new LambdaQueryWrapper<TjTaskCase>().eq(TjTaskCase::getTaskId, taskId)
            .eq(TjTaskCase::getStatus, TaskCaseStatusEnum.RUNNING.getCode()));
    CaseRealTestVo caseRealTestVo = new CaseRealTestVo();
    caseRealTestVo.setTaskId(taskId);
    caseRealTestVo.setTaskCaseId(taskCase.getCaseId());
    CaseInfoBo caseInfoBo = caseService.getCaseDetail(taskCase.getCaseId());
    caseRealTestVo.setTestTypeName(caseInfoBo.getTestScene());
    return caseRealTestVo;
  }

  //    @Transactional(rollbackFor = Exception.class)
  @Override
  public CaseRealTestVo caseStartEnd(Integer taskId, Integer caseId,
      Integer action, boolean taskEnd, Map<String, Object> context,
      Integer testModel) throws BusinessException {
    log.info("任务ID:{} 用例ID:{} action:{}, 上下文：{}", taskId, caseId, action,
        JSONObject.toJSONString(context));
    TjTask tjTask = taskMapper.selectById(taskId);
    if (ObjectUtils.isEmpty(tjTask)) {
      throw new BusinessException("任务不存在");
    }
    // 1.任务用例测试记录详情
    TjTaskCaseRecord taskCaseRecord = ssGetTjTaskCaseRecord(taskId, caseId,
        action);
    // 2.任务用例详情
    TjTaskCase taskCase = getTjTaskCaseByTaskCaseRecord(taskId, caseId);
    TaskCaseInfoBo taskCaseInfoBo = taskCaseMapper.selectTaskCaseByCondition(
        taskCase).get(0);
    // 3.校验数据
    validConfig(taskCaseInfoBo);
    Optional<TaskCaseConfigBo> first = taskCaseInfoBo.getDataConfigs().stream()
        .filter(e -> PartRole.AV.equals(e.getType())).findFirst();
    if (!first.isPresent()) {
      throw new BusinessException("未查询到主车配置信息");
    }
    log.info("向主控发送规则向主控发送规则");
    // 4.向主控发送控制请求
    List<TaskCaseConfigBo> filterConfigs = taskCaseInfoBo.getDataConfigs()
        .stream().filter(distinctByKey(TjTaskDataConfig::getDeviceId))
        .collect(Collectors.toList());
    TaskCaseConfigBo mainConfig = first.get();
    TessParam tessParam = buildTessServerParam(1,
        (String) context.get(Constants.MasterContext.USERNAME), taskId, null);
    Map<Integer, List<TessngEvaluateDto>> tessngEvaluateAVs = createTessngEvaluateAVs(
        filterConfigs);

    Object recordId = context.get(Constants.MasterContext.RECORD_ID);
    if (!restService.sendRuleUrl(
        new CaseRuleControl(System.currentTimeMillis(), taskId, caseId,
            action > 0 ? action : 0, generateDeviceConnRules(filterConfigs,
            tessParam.getCommandChannel(), tessParam.getDataChannel(), recordId, tessngEvaluateAVs),
            mainConfig.getCommandChannel(), taskEnd))) {
      throw new BusinessException("主控响应异常");
    }
    // 6.业务处理
    String taskChainNumber = (String) context.get("taskChainNumber");
    String user = (String) context.get("user");
    if (1 == action) {
      if (!tjTask.isContinuous()) {
        taskChainFactory.start(taskChainNumber);
      }
      ssCaseResultUpdate(action, taskCaseRecord, mainConfig, taskCase, null,
          null);
    } else {
      String key = ChannelBuilder.buildTaskDataChannel(user, tjTask.getId());
      String duration = null;
      try {
        List<List<ClientSimulationTrajectoryDto>> trajectories = kafkaCollector.take(
            key, caseId);
        duration = DateUtils.secondsToDuration((int) Math.floor((double) (
            CollectionUtils.isEmpty(trajectories) ?
                0 :
                trajectories.size()) / 10));
        ssCaseResultUpdate(action, taskCaseRecord, mainConfig, taskCase,
            duration, trajectories);
        log.info("开始进行openX场景数据保存");
        toBuildOpenX.casetoOpenX(trajectories, taskId, caseId, null);
        log.info("openX场景数据保存结束");
      } finally {
        if (taskEnd) {
          log.info("任务{}终止 发送ws结束消息", taskId);
          duration = DateUtils.secondsToDuration(
              (int) Math.floor((double) kafkaCollector.getSize(key) / 10));
          if (tjTask.isContinuous() || ObjectUtils.isEmpty(
              taskChainFactory.next(taskChainNumber))) {
            RealWebsocketMessage endMsg = new RealWebsocketMessage(
                RedisMessageType.END, null, null, duration);
            WebSocketManage.sendInfo(key, JSON.toJSONString(endMsg));
            log.info("移除kafka收集器key：{}", key);
            kafkaCollector.remove(key, null);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("taskId", taskId);
            jsonObject.put("caseId", caseId);
            jsonObject.put("status", "finish");
            kafkaProducer.sendMessage("tj_task_tw_status",
                jsonObject.toJSONString());
            log.info("更新任务{}状态 -> 已完成", taskId);
            QueryWrapper<TjTaskCase> queryMapper = new QueryWrapper<>();
            queryMapper.eq(ColumnName.TASK_ID, taskCaseRecord.getTaskId());
            List<TjTaskCase> tjTaskCases = taskCaseMapper.selectList(
                new LambdaQueryWrapper<TjTaskCase>().eq(TjTaskCase::getTaskId,
                    taskCaseRecord.getTaskId()));
            tjTask.setEndTime(new Date());
            tjTask.setStatus(TaskStatusEnum.FINISHED.getCode());
            tjTask.setTestTotalTime(DateUtils.secondsToDuration(
                tjTaskCases.stream().mapToInt(
                        caseObj -> Integer.parseInt(caseObj.getTestTotalTime()))
                    .sum()));
            taskMapper.updateById(tjTask);

            log.info("更新任务{}中非完成状态且已存在测试评价的用例状态",
                taskId);
            tjTaskCases.stream().filter(c ->
                    !TaskCaseStatusEnum.FINISHED.getCode().equals(c.getStatus())
                        && StringUtils.isNotEmpty(c.getEvaluatePath()))
                .forEach(v -> {
                  log.info("更新任务{}用例{}状态 -> 已完成", v.getTaskId(),
                      v.getCaseId());
                  v.setStatus(TaskCaseStatusEnum.FINISHED.getCode());
                  this.updateById(v);
                });
          } else {
            taskChainFactory.confirmState(taskChainNumber);
            try {
              taskChainFactory.selectState(taskChainNumber);
            } catch (Exception e) {
              log.error("任务{}终止:{}", taskId, e);
              manualTermination(taskId, 0, testModel);
            }

          }
        }
      }
    }
    // 评价信息处理
    evaluationProcess(taskId, caseId, action, user,
        Integer.parseInt(String.valueOf(recordId)), tjTask.isContinuous());
    // 7.结果集
    log.info("构建结果集");
    return ssGetCaseRealTestVo(taskCaseRecord);
  }

  @Override
  public void playback(Integer taskId, Integer caseId, Integer recordId,
      Integer action) throws BusinessException, IOException {
    String key = ChannelBuilder.buildTaskPreviewChannel(
        SecurityUtils.getUsername(), taskId, caseId);
    switch (action) {
      case PlaybackAction.START:
        // 1.任务用例测试记录
        TjTaskCase param = new TjTaskCase();
        param.setTaskId(taskId);
        param.setCaseId(caseId);
        List<TaskCaseInfoBo> taskCaseInfos = taskCaseMapper.selectTaskCaseByCondition(
            param);
        List<List<ClientSimulationTrajectoryDto>> trajectories = new ArrayList<>();
        for (TaskCaseInfoBo taskCaseInfoBo : taskCaseInfos) {
          TjTaskCaseRecord tjTaskCaseRecord = getTjTaskCaseRecord(recordId,
              taskCaseInfoBo);
          if (null == tjTaskCaseRecord) {
            continue;
          }
          trajectories.addAll(routeService.readRealTrajectoryFromRouteFile2(
              tjTaskCaseRecord.getRouteFile()));
        }
        // 2.数据校验
        if (CollectionUtils.isEmpty(trajectories)) {
          throw new BusinessException("未查询到任何可用轨迹文件，请先进行试验");
        }
        TjTaskDataConfig dataConfig = taskDataConfigMapper.selectOne(
            new LambdaQueryWrapper<TjTaskDataConfig>().eq(
                    TjTaskDataConfig::getTaskId, taskId)
                .eq(TjTaskDataConfig::getType, PartRole.AV));
        TjDeviceDetail avDevice = deviceDetailService.getById(
            dataConfig.getDeviceId());
        RealPlaybackSchedule.startSendingData(key, avDevice.getDataChannel(),
            trajectories);
        break;
      case PlaybackAction.SUSPEND:
        RealPlaybackSchedule.suspend(key);
        break;
      case PlaybackAction.CONTINUE:
        RealPlaybackSchedule.goOn(key);
        break;
      case PlaybackAction.STOP:
        RealPlaybackSchedule.stopSendingData(key);
        break;
      default:
        break;
    }
  }

  private TjTaskCaseRecord getTjTaskCaseRecord(Integer recordId,
      TaskCaseInfoBo taskCaseInfoBo) {
    QueryWrapper<TjTaskCaseRecord> tjTaskCaseRecordQueryWrapper = new QueryWrapper<>();
    tjTaskCaseRecordQueryWrapper.eq(ColumnName.TASK_ID,
        taskCaseInfoBo.getTaskId());
    tjTaskCaseRecordQueryWrapper.eq(ColumnName.CASE_ID_COLUMN,
        taskCaseInfoBo.getCaseId());
    //        tjTaskCaseRecordQueryWrapper.eq("status", TestingStatusEnum.PASS.getCode());
    if (null != recordId) {
      tjTaskCaseRecordQueryWrapper.eq("record_id", recordId);
    } else {
      tjTaskCaseRecordQueryWrapper.orderByDesc("start_time");
    }
    List<TjTaskCaseRecord> list = taskCaseRecordService.list(
        tjTaskCaseRecordQueryWrapper);
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  @Override
  public void playbackTW(Integer taskId, Integer caseId, String topic)
      throws BusinessException, IOException {
    TjTaskCase param = new TjTaskCase();
    param.setTaskId(taskId);
    param.setCaseId(caseId);
    List<TaskCaseInfoBo> taskCaseInfos = taskCaseMapper.selectTaskCaseByCondition(
        param);

    List<List<ClientSimulationTrajectoryDto>> trajectories = new ArrayList<>();
    for (TaskCaseInfoBo taskCaseInfoBo : taskCaseInfos) {
      Optional<TjTaskCaseRecord> optional = CollectionUtils.emptyIfNull(
              taskCaseInfoBo.getRecords()).stream()
          .filter(r -> TestingStatusEnum.PASS.getCode().equals(r.getStatus()))
          .findFirst();
      if (!optional.isPresent()) {
        continue;
      }
      TjTaskCaseRecord tjTaskCaseRecord = optional.get();
      trajectories.addAll(routeService.readRealTrajectoryFromRouteFile2(
          tjTaskCaseRecord.getRouteFile()));
    }
    try {
      twinsPlayback.sendTwinsPlayback(topic, trajectories);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Object getEvaluation(Integer taskId, Integer id)
      throws BusinessException {
    JSONObject jsonObject = restService.getCarTestResult(taskId);
    try {
      if (id != -1) {
        TjTask task = taskMapper.selectById(taskId);
        Duration duration = Duration.between(task.getStartTime().toInstant(),
            task.getEndTime().toInstant());
        jsonObject.put("taskId", taskId);
        jsonObject.put("id", id);
        jsonObject.put("time", duration.toMillis() / 1000);
        jsonObject.put("startTime",
            DateUtils.dateToString(task.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
        jsonObject.put("endTime",
            DateUtils.dateToString(task.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
      } else {
        TjInfinityTask task = tjInfinityTaskService.getById(taskId);
        Date end = new Date();
        Duration duration = Duration.between(
            task.getTestStartTime().toInstant(), end.toInstant());
        jsonObject.put("taskId", taskId);
        jsonObject.put("id", 0);
        jsonObject.put("time", duration.toMillis() / 1000);
        jsonObject.put("startTime",
            DateUtils.dateToString(task.getTestStartTime(),
                "yyyy-MM-dd HH:mm:ss"));
        jsonObject.put("endTime",
            DateUtils.dateToString(end, "yyyy-MM-dd HH:mm:ss"));
      }
    } catch (Exception e) {
      throw new BusinessException("获取评价信息失败");
    } finally {
      //            unLock(taskId);
    }
    return jsonObject;
  }

  @Override
  public List<RealTestResultVo> getResult(Integer taskId, Integer id)
      throws BusinessException {
    TjTask task = taskMapper.selectById(taskId);
    if (ObjectUtils.isEmpty(task)) {
      throw new BusinessException("未查询到任务信息");
    }
    TjTaskCase param = new TjTaskCase();
    param.setTaskId(taskId);
    param.setId(id);
    List<TaskCaseInfoBo> taskCaseInfos = taskCaseMapper.selectTaskCaseByCondition(
        param);
    if (CollectionUtils.isEmpty(taskCaseInfos)) {
      throw new BusinessException("未查询到任务用例信息");
    }
    List<RealTestResultVo> result = new ArrayList<>();
    for (TaskCaseInfoBo taskCaseInfoBo : taskCaseInfos) {
      Optional<TjTaskCaseRecord> optional = CollectionUtils.emptyIfNull(
              taskCaseInfoBo.getRecords()).stream()
          .filter(r -> TestingStatusEnum.PASS.getCode().equals(r.getStatus()))
          .findFirst();
      if (!optional.isPresent()) {
        continue;
      }
      TjTaskCaseRecord taskCaseRecord = optional.get();
      CaseTrajectoryDetailBo caseTrajectoryDetailBo = JSONObject.parseObject(
          taskCaseRecord.getDetailInfo(), CaseTrajectoryDetailBo.class);
      List<ParticipantTrajectoryBo> trajectoryBos = caseTrajectoryDetailBo.getParticipantTrajectories()
          .stream().filter(item -> PartType.MAIN.equals(item.getType()))
          .collect(Collectors.toList());
      caseTrajectoryDetailBo.setParticipantTrajectories(trajectoryBos);
      RealTestResultVo realTestResultVo = new RealTestResultVo();
      BeanUtils.copyProperties(caseTrajectoryDetailBo, realTestResultVo);
      realTestResultVo.setTestTypeName(
          dictDataService.selectDictLabel(SysType.TEST_TYPE,
              task.getTestType()));
      realTestResultVo.setSceneName(caseTrajectoryDetailBo.getSceneDesc());
      realTestResultVo.setId(id);
      realTestResultVo.setTaskId(taskId);
      realTestResultVo.setRecordId(taskCaseRecord.getId());
      realTestResultVo.setStartTime(taskCaseRecord.getStartTime());
      realTestResultVo.setEndTime(taskCaseRecord.getEndTime());
      result.add(realTestResultVo);
    }
    unLock(taskId);
    return result;
  }

  @Override
  public CommunicationDelayVo communicationDelayVo(Integer taskId, Integer id)
      throws BusinessException {
    List<Map<String, Object>> infos = taskCaseRecordMapper.recordPartInfo(id);
    CommunicationDelayVo communicationDelayVo = new CommunicationDelayVo();
    List<String> type = new ArrayList<>();
    Date startTime = null;
    Date endTime = null;
    for (Map<String, Object> info : infos) {
      if (null == startTime) {
        startTime = Date.from(((LocalDateTime) info.get("START_TIME")).atZone(
            ZoneId.systemDefault()).toInstant());
      }
      if (null == endTime) {
        endTime = Date.from(((LocalDateTime) info.get("END_TIME")).atZone(
            ZoneId.systemDefault()).toInstant());
      }
      String role = String.valueOf(info.get("TYPE"));
      type.add(role);
    }
    if (startTime == null | endTime == null) {
      return null;
    }
    communicationDelayVo.setType(type);
    List<String> times = delayTimes(startTime, endTime);
    communicationDelayVo.setTime(times);

    ArrayList<List<Integer>> delay = new ArrayList<>();
    for (String t : communicationDelayVo.getType()) {
      List<Integer> typeDelay = new ArrayList<>();
      delay.add(typeDelay);
      for (String time : times) {
        typeDelay.add((int) (Math.random() * 100));
      }
    }

    communicationDelayVo.setDelay(delay);

    return communicationDelayVo;
  }

  @Override
  public List<TaskReportVo> getReport(Integer taskId, Integer taskCaseId) {

    return new ArrayList<>();
  }

  @Override
  public void stop(Integer taskId, Integer taskCaseId, String user)
      throws BusinessException {
    TjTaskCase taskCase = new TjTaskCase();
    taskCase.setTaskId(taskId);
    taskCase.setId(taskCaseId);
    List<TaskCaseInfoBo> taskCaseInfoBos = taskCaseMapper.selectTaskCaseByCondition(
        taskCase);
    TjTask tjTask = taskMapper.selectById(taskId);
    if (ObjectUtils.isEmpty(tjTask) || CollectionUtils.isEmpty(
        taskCaseInfoBos)) {
      throw new BusinessException("未查询到任务信息");
    }
    List<TaskCaseConfigBo> filterConfigs = taskCaseInfoBos.stream()
        .map(TaskCaseInfoBo::getDataConfigs).flatMap(List::stream)
        .filter(distinctByKey(TjTaskDataConfig::getDeviceId))
        .collect(Collectors.toList());
    for (TaskCaseConfigBo filterConfig : filterConfigs) {
      deviceStateToRedis.delete(filterConfig.getDeviceId(),
          DeviceStateToRedis.DEVICE_READY_STATE_PREFIX);
    }
    //        Optional<TaskCaseConfigBo> first = CollectionUtils.emptyIfNull(filterConfigs)
    //                .stream().filter(e -> PartRole.AV.equals(e.getType())).findFirst();
    //        if (!first.isPresent()) {
    //            throw new BusinessException("未查询到主车配置信息");
    //        }
    //        TessParam tessParam = buildTessServerParam(1, user, taskId);
    //        if (!restService.sendRuleUrl(
    //                new CaseRuleControl(System.currentTimeMillis(),
    //                        taskId, 0, 0,
    //                        generateDeviceConnRules(filterConfigs, tessParam.getCommandChannel(), tessParam.getDataChannel()),
    //                        first.get().getCommandChannel(), true))) {
    //            throw new BusinessException("主控响应异常");
    //        }
  }

  @Override
  public void manualTermination(Integer taskId, Integer taskCaseId,
      Integer testModel) throws BusinessException {
    TjTask tjTask = taskMapper.selectById(taskId);
    if (ObjectUtils.isEmpty(tjTask)) {
      throw new BusinessException("未查询到任务信息");
    }
    Integer caseId;
    if (!ObjectUtils.isEmpty(taskCaseId) && taskCaseId > 0) {
      TjTaskCase taskCase = this.getById(taskCaseId);
      if (ObjectUtils.isEmpty(taskCase)) {
        throw new BusinessException("未查询到任务用例信息");
      }
      caseId = taskCase.getCaseId();
    } else {
      TjTaskCase currentNodeCase = taskChainFactory.getCurrentNodeCase(
          ChannelBuilder.buildTaskDataChannel(SecurityUtils.getUsername(),
              taskId));
      if (ObjectUtils.isEmpty(currentNodeCase) || ObjectUtils.isEmpty(
          currentNodeCase.getCaseId())) {
        throw new BusinessException("未查询到任务链当前用例信息");
      }
      caseId = currentNodeCase.getCaseId();
    }
    if (!restService.sendManualTermination(taskId, caseId,
        tjTask.isContinuous() ?
            TestMode.CONTINUOUS_TEST :
            TestMode.BATCH_TEST)) {
      throw new BusinessException("任务终止失败");
    }
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("taskId", taskId);
    jsonObject.put("caseId", caseId);
    jsonObject.put("status", "break");
    kafkaProducer.sendMessage("tj_task_tw_status", jsonObject.toJSONString());
  }

  @Override
  public void stopSvTrack(Integer caseId, TessTrackParam tessTrackParam) {

  }

  @Override
  public List<CaseTreeVo> selectTree(String type, Integer taskId) {
    List<CaseTreeVo> caseTree = caseTreeService.selectTree(type, null);
    TjTaskCase taskCase = new TjTaskCase();
    taskCase.setTaskId(taskId);
    List<TaskCaseVo> taskCaseList = taskCaseMapper.selectByCondition(taskCase);
    Map<Integer, Long> caseCountMap = CollectionUtils.emptyIfNull(taskCaseList)
        .stream().collect(Collectors.groupingBy(TaskCaseVo::getTreeId,
            Collectors.counting()));
    for (CaseTreeVo treeVo : CollectionUtils.emptyIfNull(caseTree)) {
      treeVo.setNumber(caseCountMap.containsKey(treeVo.getId()) ?
          caseCountMap.get(treeVo.getId()).intValue() :
          0);
    }
    return caseTree;
  }

  @Override
  public boolean addTaskCase(@NotNull Integer taskId,
      @NotNull List<Integer> caseIds) throws BusinessException {
    if (CollectionUtils.isEmpty(caseIds)) {
      return false;
    }
    List<TjTaskCase> addedList = list(
        new QueryWrapper<TjTaskCase>().eq(ColumnName.TASK_ID, taskId)
            .in(ColumnName.CASE_ID_COLUMN, caseIds));
    List<Integer> addedIdList = CollectionUtils.emptyIfNull(addedList).stream()
        .map(TjTaskCase::getCaseId).collect(Collectors.toList());
    caseIds.removeAll(addedIdList);

    List<TjCase> cases = caseService.listByIds(caseIds);
    Map<Integer, TjCase> caseMap = CollectionUtils.emptyIfNull(cases).stream()
        .collect(Collectors.toMap(TjCase::getId, Function.identity()));

    List<TjTaskCase> saveList = new ArrayList<>();
    for (Integer caseId : caseIds) {
      if (!caseMap.containsKey(caseId) || StringUtils.isEmpty(
          caseMap.get(caseId).getRouteFile())) {
        throw new BusinessException(
            StringUtils.format("未查询到用例{}可用轨迹文件", caseId));
      }
      TjTaskCase taskCase = new TjTaskCase();
      taskCase.setTaskId(taskId);
      taskCase.setCaseId(caseId);
      taskCase.setSort(0);
      taskCase.setCreateTime(new Date());
      taskCase.setStatus(TaskCaseStatusEnum.WAITING.getCode());
      taskCase.setRouteFile(caseMap.get(caseId).getRouteFile());
      taskCase.setDetailInfo(caseMap.get(caseId).getDetailInfo());
      saveList.add(taskCase);
    }
    return saveBatch(saveList);
  }

  @Override
  public boolean deleteTaskCase(@NotNull Integer taskId,
      @NotNull List<Integer> caseIds) {
    return remove(new QueryWrapper<TjTaskCase>().eq(ColumnName.TASK_ID, taskId)
        .in(ColumnName.CASE_ID_COLUMN, caseIds));
  }

  @Override
  public TaskCaseVerificationPageVo getStatustw(TjTaskCase param)
      throws BusinessException {
    if (ObjectUtils.isEmpty(param.getTaskId())) {
      throw new BusinessException("参数异常");
    }
    // 1.查询用例详情
    // taskId为空时 单个用例测试
    // caseId为空时 连续性测试
    List<TaskCaseInfoBo> taskCaseInfos = taskCaseMapper.selectTaskCaseByCondition(
        param);
    if (CollectionUtils.isEmpty(taskCaseInfos)) {
      throw new BusinessException("查询失败，请检查用例是否存在");
    }
    TjTask tjTask = taskMapper.selectById(param.getTaskId());
    if (ObjectUtils.isEmpty(tjTask)) {
      throw new BusinessException("查询失败，请检查任务是否存在");
    }
    String userName = redisLock.getUser("tw_" + param.getTaskId());
    if (userName == null) {
      throw new BusinessException("查询失败，请检查任务是否正在执行");
    }
    // 2.数据填充
    List<TaskCaseConfigBo> allTaskCaseConfigs = new ArrayList<>();
    Map<Integer, Map<String, String>> caseBusinessIdAndRoleMap = new HashMap<>();
    Map<Integer, Integer> caseMainSize = new HashMap<>();
    Map<String, String> startMap = new HashMap<>();
    Map<String, List<SimulationTrajectoryDto>> mainTrajectoryMap = new HashMap<>();
    Map<Integer, TaskCaseInfoBo> taskCaseInfoMap = new HashMap<>();
    fillStatusParam(tjTask, param.getId(), taskCaseInfos, allTaskCaseConfigs,
        caseBusinessIdAndRoleMap, caseMainSize, startMap, mainTrajectoryMap,
        taskCaseInfoMap);
    // 3.重复设备过滤
    List<TaskCaseConfigBo> distTaskCaseConfigs = filterConfigs(
        allTaskCaseConfigs);
    // 5.状态查询
    for (TaskCaseConfigBo taskCaseConfigBo : distTaskCaseConfigs) {
      // 查询设备状态
      Integer status = deviceDetailService.selectDeviceState(
          taskCaseConfigBo.getDeviceId(),
          getReadyStatusChannelByTypeTW(taskCaseConfigBo, userName), false);
      taskCaseConfigBo.setStatus(status);
      // 查询设备准备状态
      DeviceReadyStateParam stateParam = new DeviceReadyStateParam(
          taskCaseConfigBo.getDeviceId(),
          getCommandChannelByRoleTW(taskCaseConfigBo, userName));
      if (PartRole.AV.equals(taskCaseConfigBo.getType())) {
        // av车需要主车全部轨迹
        stateParam.setParams(new ParamsDto("1", mainTrajectoryMap.get("main")));
      } else if (PartRole.MV_SIMULATION.equals(taskCaseConfigBo.getType())
          || PartRole.SP.equals(taskCaseConfigBo.getType())) {
        stateParam.setParams(
            buildTessStateParam(param.getTaskId(), taskCaseInfoMap,
                caseBusinessIdAndRoleMap, caseMainSize));
      } else {
        stateParam.setParams(new ParamsDto(taskCaseConfigBo.getParticipatorId(),
            mainTrajectoryMap.get(taskCaseConfigBo.getParticipatorId())));
      }

      Integer readyStatus = deviceDetailService.selectDeviceReadyState(
          taskCaseConfigBo.getDeviceId(),
          getReadyStatusChannelByTypeTW(taskCaseConfigBo, userName), stateParam,
          false);
      taskCaseConfigBo.setPositionStatus(readyStatus);
    }
    // 6.构建页面结果集
    return buildPageVo(param, startMap, allTaskCaseConfigs,
        distTaskCaseConfigs);
  }

  private void validStatus(TaskCaseVerificationPageVo pageVo) {
    Map<String, List<TaskCaseConfigBo>> statusMap = pageVo.getStatusMap();
    List<TaskCaseConfigBo> configs = new ArrayList<>();
    statusMap.values().stream().flatMap(List::stream).forEach(configs::add);
    StringBuilder messageBuilder = new StringBuilder();
    for (TaskCaseConfigBo config : configs) {
      if (ObjectUtils.isEmpty(config.getStatus())
          || YN.Y_INT != config.getStatus()) {
        messageBuilder.append(
            StringUtils.format(ContentTemplate.DEVICE_OFFLINE_TEMPLATE,
                config.getDeviceName()));
      }
      if (ObjectUtils.isEmpty(config.getPositionStatus())
          || YN.Y_INT != config.getPositionStatus()) {
        messageBuilder.append(
            StringUtils.format(ContentTemplate.DEVICE_POS_ERROR_TEMPLATE,
                config.getDeviceName()));
      }
    }
    String message = messageBuilder.toString();
    if (StringUtils.isNotEmpty(message)) {
      pageVo.setMessage(message);
    } else {
      pageVo.setCanStart(true);
    }
  }

  /**
   * 校验用例信息
   *
   * @param taskCaseInfoBo
   * @throws BusinessException
   */
  private void validConfig(TaskCaseInfoBo taskCaseInfoBo)
      throws BusinessException {
    if (ObjectUtils.isEmpty(taskCaseInfoBo)) {
      throw new BusinessException("任务异常：查询任务用例失败");
    }
    if (CollectionUtils.isEmpty(taskCaseInfoBo.getDataConfigs())) {
      throw new BusinessException("任务异常：任务未进行设备配置");
    }
    if (taskCaseInfoBo.getDataConfigs().stream()
        .allMatch(config -> ObjectUtils.isEmpty(config.getDeviceId()))) {
      throw new BusinessException("用例异常：用例未进行设备配置");
    }
    if (StringUtils.isEmpty(taskCaseInfoBo.getDetailInfo())) {
      throw new BusinessException("用例异常：无路径配置信息");
    }
    if (StringUtils.isEmpty(taskCaseInfoBo.getRouteFile())) {
      throw new BusinessException("用例异常：未进行仿真验证");
    }
  }

  private List<DeviceConnRule> generateDeviceConnRules(
      List<TaskCaseConfigBo> configs, String commandChannel, String dataChannel,
      Object recordId,
      Map<Integer, List<TessngEvaluateDto>> tessngEvaluateAVs) {
    List<DeviceConnRule> rules = new ArrayList<>();
    for (int i = 0; i < configs.size(); i++) {
      TaskCaseConfigBo sourceDevice = configs.get(i);
      for (int j = 0; j < configs.size(); j++) {
        if (j == i) {
          continue;
        }
        Map<String, Object> sourceParams = new HashMap<>();
        Map<String, Object> targetParams = new HashMap<>();

        TaskCaseConfigBo targetDevice = configs.get(j);

        DeviceConnRule rule = new DeviceConnRule();
        rule.setSource(createConnInfo(sourceDevice, commandChannel, dataChannel,
            sourceParams));

        extendParams(tessngEvaluateAVs, recordId, sourceDevice, sourceParams,
            targetDevice, targetParams);

        rule.setTarget(createConnInfo(targetDevice, commandChannel, dataChannel,
            targetParams));
        // 主车接收tessng过滤后数据通道
        // avDevcieDataChannelChange(sourceDevice, targetDevice, rule);
        rules.add(rule);
      }
    }
    return rules;
  }

  private static void avDevcieDataChannelChange(TaskCaseConfigBo sourceDevice,
      TaskCaseConfigBo targetDevice, DeviceConnRule rule) {
    if (PartRole.AV.equals(sourceDevice.getSupportRoles())
        && PartRole.MV_SIMULATION.equals(targetDevice.getSupportRoles())) {
      rule.getTarget().setChannel(sourceDevice.getDataChannel()
          + Constants.TessngInteraction.NEARBY_DATA_CHANNEL_SUFFIX);
    }
  }

  private static void extendParams(
      Map<Integer, List<TessngEvaluateDto>> tessngEvaluateAVs, Object recordId,
      TaskCaseConfigBo sourceDevice, Map<String, Object> sourceParams,
      TaskCaseConfigBo targetDevice, Map<String, Object> targetParams) {
    if (PartRole.MV_SIMULATION.equals(sourceDevice.getSupportRoles())) {
      sourceParams.put(Constants.TessngInteraction.RECORD_ID, recordId);
    }

    if (null != tessngEvaluateAVs
        && tessngEvaluateAVs.get(targetDevice.getDeviceId()) != null) {
      targetParams.put(Constants.TessngInteraction.EVALUATION_INFOS,
          tessngEvaluateAVs.get(targetDevice.getDeviceId()));
      // tessng额外上传主车相邻的背景车数据通道
      if (PartRole.MV_SIMULATION.equals(
          sourceDevice.getSupportRoles())) {
        targetParams.put(Constants.TessngInteraction.NEARBY_DATA_CHANNEL,
            targetDevice.getDataChannel()
                + Constants.TessngInteraction.NEARBY_DATA_CHANNEL_SUFFIX);
      }
    }
  }

  private static DeviceConnInfo createConnInfo(TaskCaseConfigBo config,
      String commandChannel, String dataChannel, Map<String, Object> params) {
    return PartRole.MV_SIMULATION.equals(config.getSupportRoles()) ?
        createSimulationConnInfo(String.valueOf(config.getDeviceId()),
            commandChannel, dataChannel, params) :
        new DeviceConnInfo(String.valueOf(config.getDeviceId()),
            config.getCommandChannel(), config.getDataChannel(),
            config.getSupportRoles(), params);
  }

  private static DeviceConnInfo createSimulationConnInfo(String deviceId,
      String commandChannel, String dataChannel, Map<String, Object> params) {
    return new DeviceConnInfo(deviceId, commandChannel, dataChannel,
        PartRole.MV_SIMULATION, params);
  }

  private static List<String> delayTimes(Date startTime, Date endTime) {
    ArrayList<String> time = new ArrayList<>();
    long seconds = Duration.between(startTime.toInstant(), endTime.toInstant())
        .getSeconds();
    for (int i = 1; i < seconds + 1; i++) {
      long hours = TimeUnit.SECONDS.toHours(i) % 24;
      long minutes = TimeUnit.SECONDS.toMinutes(i) % 60;
      long second = i % 60;
      time.add(String.format("%02d:%02d:%02d", hours, minutes, second));
    }

    return time;
  }

  private TjTaskCaseRecord ssGetTjTaskCaseRecord(Integer taskId, Integer caseId,
      Integer action) throws BusinessException {
    List<TjTaskCaseRecord> tjTaskCaseRecords = taskCaseRecordMapper.selectList(
        new LambdaQueryWrapper<TjTaskCaseRecord>().eq(
                TjTaskCaseRecord::getTaskId, taskId)
            .eq(TjTaskCaseRecord::getCaseId, caseId)
            .isNull(TjTaskCaseRecord::getRouteFile));
    Optional<TjTaskCaseRecord> max = CollectionUtils.emptyIfNull(
            tjTaskCaseRecords).stream()
        .max(Comparator.comparing(TjTaskCaseRecord::getCreatedDate));
    return max.orElse(null);
  }

  private TjTaskCase getTjTaskCaseByTaskCaseRecord(Integer taskId,
      Integer caseId) {
    return taskCaseMapper.selectOne(
        new LambdaQueryWrapper<TjTaskCase>().eq(TjTaskCase::getCaseId, caseId)
            .eq(TjTaskCase::getTaskId, taskId));
  }

  private void ssCaseResultUpdate(Integer action,
      TjTaskCaseRecord taskCaseRecord, TaskCaseConfigBo mainConfig,
      TjTaskCase taskCase, String duration,
      List<List<ClientSimulationTrajectoryDto>> trajectories)
      throws BusinessException {
    if (1 == action) {
      Date date = new Date();
      LocalDateTime now = DateUtils.dateToLDT(date);
      taskCaseRecord.setStartTime(now);
      int i = taskCaseRecordMapper.updateById(taskCaseRecord);
      log.info("更新测试记录{}：{}", taskCaseRecord.getId(), i);
      taskCase.setStartTime(date);
      taskCase.setStatus(TaskCaseStatusEnum.RUNNING.getCode());
      boolean b = this.updateById(taskCase);
      log.info("更新测试任务用例{} -> 测试中：{}", taskCase.getId(), b);
    } else {
      try {
        routeService.checkMain(trajectories, mainConfig.getDataChannel());
        routeService.saveTaskRouteFile2(taskCaseRecord, trajectories, action);
      } catch (Exception e) {
        log.error("保存轨迹文件失败:{}", e);
        throw new BusinessException("保存轨迹文件失败");
      }
      taskCase.setTestTotalTime(
          String.valueOf(DateUtils.durationToSeconds(duration)));
      taskCase.setEndTime(new Date());
      taskCase.setStatus(TaskCaseStatusEnum.FINISHED.getCode());
      taskCase.setPassingRate(0 == action ? "100%" : "0%");
      // todo 评价文件：kafka：从收集器控制收发存；redis：新增监听器；写在saveTaskRouteFile2？
      if (StringUtils.isNotEmpty(taskCaseRecord.getEvaluatePath())) {
        log.info("替换测试任务{}用例{} 评价文件：{}", taskCase.getTaskId(),
            taskCase.getCaseId(), taskCaseRecord.getEvaluatePath());
        taskCase.setEvaluatePath(taskCaseRecord.getEvaluatePath());
      }
      boolean b = this.updateById(taskCase);
      log.info("更新测试任务用例{} -> 已完成：{}", taskCase.getId(), b);
    }
  }

  private static CaseRealTestVo ssGetCaseRealTestVo(
      TjTaskCaseRecord taskCaseRecord) {
    CaseRealTestVo caseRealTestVo = new CaseRealTestVo();
    BeanUtils.copyProperties(taskCaseRecord, caseRealTestVo);
    caseRealTestVo.setStartTime(DateUtils.getTime());
    SceneTrajectoryBo sceneTrajectoryBo = JSONObject.parseObject(
        taskCaseRecord.getDetailInfo(), SceneTrajectoryBo.class);
    Map<String, List<TrajectoryDetailBo>> mainTrajectoryMap = sceneTrajectoryBo.getParticipantTrajectories()
        .stream().filter(item -> PartType.MAIN.equals(item.getType())).collect(
            Collectors.toMap(ParticipantTrajectoryBo::getId,
                ParticipantTrajectoryBo::getTrajectory));
    caseRealTestVo.setMainTrajectories(mainTrajectoryMap);
    return caseRealTestVo;
  }

  private void unLock(Integer taskId) {
    TjTaskCase taskCase = new TjTaskCase();
    taskCase.setTaskId(taskId);
    List<TaskCaseInfoBo> taskCaseInfos = taskCaseMapper.selectTaskCaseByCondition(
        taskCase);
    List<TaskCaseConfigBo> taskCaseConfigs = new ArrayList<>();
    for (TaskCaseInfoBo taskCaseInfo : taskCaseInfos) {
      taskCaseConfigs.addAll(taskCaseInfo.getDataConfigs());
    }
    taskCaseConfigs = filterConfigs(taskCaseConfigs);
    List<TaskCaseConfigBo> filteredTaskCaseConfigs = taskCaseConfigs.stream()
        .filter(t -> !PartRole.MV_SIMULATION.equals(t.getType()))
        .collect(Collectors.toList());
    for (TaskCaseConfigBo taskCaseConfigBo : filteredTaskCaseConfigs) {
      redisLock.releaseLock("task_" + taskCaseConfigBo.getDataChannel(),
          SecurityUtils.getUsername());
    }
  }

  public static <T> Predicate<T> distinctByKey(
      Function<? super T, Object> keyExtractor) {
    Map<Object, Boolean> map = new ConcurrentHashMap<>();
    return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
  }

  @Override
  public void twStop(Integer taskId, Integer caseId, String status) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("taskId", taskId);
    jsonObject.put("caseId", caseId);
    jsonObject.put("status", status);
    kafkaProducer.sendMessage("tj_task_tw_status", jsonObject.toJSONString());
  }

  @Override
  public TjScenelib tasktolib(Integer taskId, Integer caseId) {
    TjTaskCase param = new TjTaskCase();
    param.setTaskId(taskId);
    param.setCaseId(caseId);
    List<TaskCaseInfoBo> taskCaseInfos = taskCaseMapper.selectTaskCaseByCondition(
        param);
    if (CollectionUtils.isNotEmpty(taskCaseInfos)) {
      TjScenelib tjScenelib = new TjScenelib();
      TaskCaseInfoBo taskCaseInfoBo = taskCaseInfos.get(0);
      tjScenelib.setXodrPath(taskCaseInfoBo.getXodrPath());
      tjScenelib.setXoscPath(taskCaseInfoBo.getXoscPath());
      tjScenelib.setZipPath(taskCaseInfoBo.getZipPath());
      return tjScenelib;
    }
    return null;
  }

  private Integer getRecordId(List<TaskCaseInfoBo> taskCaseInfos) {
    TaskCaseInfoBo taskCaseInfoBo = listLastElement(taskCaseInfos);
    List<TjTaskCaseRecord> records = taskCaseInfoBo.getRecords();
    if (CollectionUtils.isNotEmpty(records)) {
      return records.get(0).getId();
    }
    return null;
  }

  private <T> T listLastElement(List<T> list) {
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(list.size() - 1);
    }
    if (log.isWarnEnabled()) {
      log.warn("the parameter of listLastElement is empty!");
    }
    return null;
  }

  private static Map<Integer, List<TessngEvaluateDto>> createTessngEvaluateAVs(
      List<TaskCaseConfigBo> tjTaskDataConfigs) {
    return tjTaskDataConfigs.stream()
        .filter(e -> PartRole.AV.equals(e.getType())).map(
            e -> new TessngEvaluateDto(e.getDeviceId(), e.getParticipatorName(),
                1, e.getDeviceId()))
        .collect(Collectors.groupingBy(TessngEvaluateDto::getDeviceId));
  }

  private void sendTessngEvaluationState(TessParam tessParam, Integer taskId) {
    TessngEvaluationState tessngEvaluationState = new TessngEvaluationState();
    tessngEvaluationState.setTaskID(taskId);
    tessngEvaluationState.setValue(
        JSONObject.toJSONString(JSONObject.toJSONString(tessParam)));
    tessngEvaluationStateService.save(tessngEvaluationState);
  }

  /**
   * 评价数据处理
   *
   * @param taskId
   * @param caseId
   * @param state  <= 0:停止，>0:开始
   */
  private void evaluationProcess(Integer taskId, Integer caseId, Integer state,
      String username, Integer recordId, boolean isContinuous) {
    String evaluateChannel = ChannelBuilder.buildTaskEvaluateChannel(username,
        taskId);
    if (state > 0) {
      EvalContext evalContext = new EvalContext(taskId, caseId,
          true == isContinuous ? 1 : 2, recordId);
      String wsClientKey = ChannelBuilder.buildTaskDataChannel(username,
          taskId);
      evalContext.setWsClientKey(wsClientKey);
      // 监听
      evaluationRedisData.subscribe(evaluateChannel,
          tjTestSingleSceneScoreService, evalContext);
    } else {
      evaluationRedisData.unsubscribe(evaluateChannel);
    }
  }
}




