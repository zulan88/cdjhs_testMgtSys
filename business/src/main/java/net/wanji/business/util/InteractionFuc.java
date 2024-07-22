package net.wanji.business.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.SitePoint;
import net.wanji.business.domain.bo.ParticipantTrajectoryBo;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.business.domain.bo.TrajectoryDetailBo;
import net.wanji.business.domain.dto.TaskDto;
import net.wanji.business.domain.vo.SceneDetailVo;
import net.wanji.business.domain.vo.TaskCaseVo;
import net.wanji.business.domain.vo.TaskListVo;
import net.wanji.business.entity.TjFragmentedSceneDetail;
import net.wanji.business.entity.TjTask;
import net.wanji.business.exercise.dto.evaluation.ScenePos;
import net.wanji.business.exercise.dto.evaluation.SceneSitePoint;
import net.wanji.business.exercise.enums.OperationTypeEnum;
import net.wanji.business.exercise.dto.evaluation.StartPoint;
import net.wanji.business.exercise.dto.simulation.*;
import net.wanji.business.schedule.SceneLabelMap;
import net.wanji.business.service.TjFragmentedSceneDetailService;
import net.wanji.business.service.TjTaskService;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.utils.GeoUtil;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.bean.BeanUtils;
import net.wanji.common.utils.file.FileUploadUtils;
import net.wanji.common.utils.file.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InteractionFuc {

    @Autowired
    private TjTaskService tjTaskService;

    @Autowired
    private TjFragmentedSceneDetailService tjFragmentedSceneDetailService;

    @Autowired
    private SceneLabelMap sceneLabelMap;

    /**
     * 根据任务ID查询场景详情。
     *
     * @param taskId 任务ID
     * @return 场景详情列表
     */
    public List<SceneDetailVo> findSceneDetail(Integer taskId) {
        // 创建任务DTO对象，并设置任务ID
        TaskDto taskDto = new TaskDto();
        taskDto.setId(taskId);

        // 调用服务获取任务
        TaskListVo taskListVo= tjTaskService.pageList(taskDto).get(0);

        // 如果任务的案例列表为空，则直接返回null
        if (taskListVo.getTaskCaseVos().size()==0) {
            return null;
        }
        Gson gson = new Gson();

        // 获取任务的案例列表
        List<TaskCaseVo> taskCaseVos = taskListVo.getTaskCaseVos();

        List<SceneDetailVo> list = new ArrayList<>();

        for (TaskCaseVo taskCaseVo : taskCaseVos) {
            TjFragmentedSceneDetail sceneDetail = tjFragmentedSceneDetailService.getById(taskCaseVo.getSceneDetailId());
            SceneDetailVo sceneDetailVo = new SceneDetailVo();
            BeanUtils.copyProperties(sceneDetail, sceneDetailVo);
            List<SitePoint> connect = gson.fromJson(taskCaseVo.getConnectInfo(), new TypeToken<List<SitePoint>>(){}.getType());
            sceneDetailVo.setStartPoint(connect.get(0));
            sceneDetailVo.setEndPoint(connect.get(connect.size()-1));
            sceneDetailVo.setEvoNum(Integer.valueOf(taskCaseVo.getEvaNum()));
            if (sceneDetailVo.getMapId() != null && sceneDetailVo.getMapId() == 21) {
                sceneDetailVo.setMapBounds(MapBoundary.CHANGAN.getValues());
            }
            sceneDetailVo.setEndTarget(gson.fromJson(taskCaseVo.getTestTarget(), new TypeToken<List<SitePoint>>(){}.getType()));
            list.add(sceneDetailVo);
        }

        list.forEach(sceneDetailVo -> {
            String labels = sceneDetailVo.getLabel();
            // 如果标签为空，则跳过当前元素
            if (labels == null) return;
            // 初始化标签显示字符串
            StringBuilder labelshows = new StringBuilder();
            // 处理标签字符串，转换为场景分类标签
            Arrays.stream(labels.split(","))
                    .map(str -> {
                        try {
                            long intValue = Long.parseLong(str);
                            return sceneLabelMap.getSceneLabel(intValue);
                        } catch (NumberFormatException e) {
                            // 忽略无效的整数字符串
                            return null;
                        }
                    })
                    // 过滤掉null值，保留有效的标签显示
                    .filter(Objects::nonNull)
                    .forEach(labelshow -> {
                        if (labelshows.length() > 0) {
                            labelshows.append(",").append(labelshow);
                        } else {
                            labelshows.append(labelshow);
                        }
                    });
            // 设置场景分类显示
            sceneDetailVo.setSceneSort(labelshows.toString());
        });
        // 返回场景详情列表
        return list;
    }

    /**
     * 根据场景ID获取并解析场景轨迹信息。
     *
     * @param sceneId 场景唯一标识ID。
     * @return SceneTrajectoryBo 包含场景轨迹信息的对象，若无则返回null。
     * @throws IOException 文件读取或JSON解析时发生的IO异常。
     */
    public SceneTrajectoryBo getSceneTrajectory(Integer sceneId) throws IOException {
        // 查询场景详细信息
        TjFragmentedSceneDetail detail = tjFragmentedSceneDetailService.getById(sceneId);
        if (detail == null || detail.getTrajectoryInfo() == null) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(detail.getTrajectoryInfo()))) {
            // 使用try-with-resources自动关闭reader
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            // 使用Gson解析JSON字符串为SceneTrajectoryBo对象
            Gson gson = new Gson();
            return gson.fromJson(content.toString(), SceneTrajectoryBo.class);
        }
    }

    public List<SceneDetailVo> getSceneDetails(Integer testId){
        List<SceneDetailVo> sceneDetails = findSceneDetail(testId);
        if(StringUtils.isNotEmpty(sceneDetails)){
            for(SceneDetailVo sceneDetailVo: sceneDetails){
                try {
                    SceneTrajectoryBo sceneTrajectory = getSceneTrajectory(sceneDetailVo.getId());
                    sceneDetailVo.setSceneTrajectoryBo(sceneTrajectory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sceneDetails;
    }

    public List<StartPoint> getSceneStartPoints(Integer testId){
        List<StartPoint> startPoints = new ArrayList<>();
        try {
            List<SceneDetailVo> sceneDetails = findSceneDetail(testId);
            if(StringUtils.isNotEmpty(sceneDetails)){
                for(int i = 0; i < sceneDetails.size(); i++){
                    SceneDetailVo sceneDetailVo = sceneDetails.get(i);
                    if(Objects.nonNull(sceneDetailVo.getStartPoint())){
                        SitePoint sitePoint = sceneDetailVo.getStartPoint();
                        StartPoint startPoint = new StartPoint();
                        startPoint.setSequence(i + 1);
                        startPoint.setSceneId(sceneDetailVo.getId());
                        startPoint.setName(sceneDetailVo.getSceneSort());
                        startPoint.setLongitude(Double.parseDouble(sitePoint.getLongitude()));
                        startPoint.setLatitude(Double.parseDouble(sitePoint.getLatitude()));

                        startPoints.add(startPoint);

                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return startPoints;
    }

    public List<SceneSitePoint> getSceneSitePoints(Integer testId){
        List<SceneSitePoint> sitePoints = new ArrayList<>();
        try {
            List<SceneDetailVo> sceneDetails = findSceneDetail(testId);
            if(StringUtils.isNotEmpty(sceneDetails)){
                for(int i = 0; i < sceneDetails.size(); i++){
                    SceneDetailVo sceneDetailVo = sceneDetails.get(i);
                    if(Objects.nonNull(sceneDetailVo.getStartPoint()) && Objects.nonNull(sceneDetailVo.getEndPoint())){
                        SitePoint startPoint = sceneDetailVo.getStartPoint();
                        SitePoint endPoint = sceneDetailVo.getEndPoint();

                        SceneSitePoint sceneSitePoint = new SceneSitePoint();
                        sceneSitePoint.setSequence(i + 1);

                        ScenePos start = new ScenePos();
                        start.setLongitude(Double.parseDouble(startPoint.getLongitude()));
                        start.setLatitude(Double.parseDouble(startPoint.getLatitude()));
                        sceneSitePoint.setStartPoint(start);

                        ScenePos end = new ScenePos();
                        end.setLongitude(Double.parseDouble(endPoint.getLongitude()));
                        end.setLatitude(Double.parseDouble(endPoint.getLatitude()));
                        sceneSitePoint.setEndPoint(end);

                        sitePoints.add(sceneSitePoint);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sitePoints;
    }

    public SimulationSceneDto getSimulationSceneInfo(Integer testId){
        try {
            //根据测试用例id获取关联场景信息和每个仿真参与者点位信息
            List<SceneDetailVo> sceneDetails = getSceneDetails(testId);
            if(StringUtils.isEmpty(sceneDetails)){
                return null;
            }
            List<SimulationSceneParticipant> simulationScenes = sceneDetails.stream()
                    .map(scene -> {
                        SimulationSceneParticipant simulationSceneParticipant = new SimulationSceneParticipant();
                        simulationSceneParticipant.setCaseId(String.valueOf(scene.getId()));
                        simulationSceneParticipant.setType(scene.getSceneSort());
                        simulationSceneParticipant.setAvPassTime(1);

                        List<ParticipantTrajectoryBo> participantTrajectories = scene.getSceneTrajectoryBo().getParticipantTrajectories();
                        if (StringUtils.isNotEmpty(participantTrajectories)) {
                            List<ParticipantTrajectory> participants = participantTrajectories.stream()
                                    .filter(participant -> !participant.getRole().equals(Constants.PartRole.AV))
                                    .map(participant -> {
                                        ParticipantTrajectory participantTrajectory = new ParticipantTrajectory();
                                        BeanUtils.copyProperties(participant, participantTrajectory);

                                        List<TrajectoryDetailBo> trajectories = participant.getTrajectory();
                                        List<TrajectoryPoint> trajectoryPoints = trajectories.stream()
                                                .map(trajectory -> {
                                                    TrajectoryPoint trajectoryPoint = new TrajectoryPoint();
                                                    BeanUtils.copyProperties(trajectory, trajectoryPoint);
                                                    Double[] postion = new Double[2];
                                                    postion[0] = Double.parseDouble(trajectory.getLongitude());
                                                    postion[1] = Double.parseDouble(trajectory.getLatitude());
                                                    trajectoryPoint.setPosition(postion);

                                                    return trajectoryPoint;
                                                }).collect(Collectors.toList());
                                        participantTrajectory.setTrajectory(trajectoryPoints);

                                        return participantTrajectory;
                                    }).collect(Collectors.toList());

                            simulationSceneParticipant.setParticipantTrajectories(participants);
                        }

                        return simulationSceneParticipant;
                    }).collect(Collectors.toList());

            SimulationSceneDto simulationSceneDto = new SimulationSceneDto();
            simulationSceneDto.setType(OperationTypeEnum.PREPARE_STATUS_REQ.getType());
            simulationSceneDto.setTimestamp(System.currentTimeMillis());
            SimulationSceneParam params = new SimulationSceneParam();
            params.setParam1(simulationScenes);
            simulationSceneDto.setParams(params);

            return simulationSceneDto;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public SimulationSceneParticipant createSceneEndReq(String type){
        SimulationSceneParticipant simulationSceneParticipant = new SimulationSceneParticipant();
        simulationSceneParticipant.setCaseId(UUID.randomUUID().toString());
        simulationSceneParticipant.setAvPassTime(1);
        simulationSceneParticipant.setType(type);
        simulationSceneParticipant.setParticipantTrajectories(new ArrayList<>());

        return simulationSceneParticipant;
    }

    @Async
    public void dualwithMainTrace(Integer taskId){
        TjTask tjTask = tjTaskService.getById(taskId);
        if (tjTask.getMainPlanFile()==null){
            return;
        }
        List<SceneDetailVo> sceneDetails = findSceneDetail(taskId);
        String routeFile = FileUploadUtils.getAbsolutePathFileName(tjTask.getMainPlanFile());
        List<SimulationTrajectoryDto> trajectories = FileUtils.readOriTrajectory(routeFile);

        trajectories.forEach(trajectory -> {
            if (!trajectory.getValue().isEmpty()) {
                trajectory.getValue().get(0).setSiteType(Constants.MainTraceType.PASSWAY);
            }
        });

        for (SceneDetailVo sceneDetailVo : sceneDetails) {
            findNearestPoint(sceneDetailVo.getStartPoint(), trajectories, Constants.MainTraceType.START);
            findNearestPoint(sceneDetailVo.getEndPoint(), trajectories, Constants.MainTraceType.END);
        }

        try {
            FileUtils.writeRouteAbs(trajectories, tjTask.getMainPlanFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void findNearestPoint(SitePoint target, List<SimulationTrajectoryDto> points, Integer type) {
        int index = 0;
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < points.size(); i++) {
            TrajectoryValueDto point = points.get(i).getValue().get(0);
            double distance = GeoUtil.calculateDistance(Double.parseDouble(target.getLatitude()), Double.parseDouble(target.getLongitude()), point.getLatitude(), point.getLongitude());
            if (distance < minDistance) {
                minDistance = distance;
                index = i;
            }
        }
        points.get(index).getValue().get(0).setSiteType(type);
    }

}
