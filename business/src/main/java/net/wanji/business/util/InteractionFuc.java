package net.wanji.business.util;

import com.google.gson.Gson;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.business.domain.dto.TaskDto;
import net.wanji.business.domain.vo.SceneDetailVo;
import net.wanji.business.domain.vo.TaskCaseVo;
import net.wanji.business.domain.vo.TaskListVo;
import net.wanji.business.entity.TjFragmentedSceneDetail;
import net.wanji.business.entity.TjFragmentedScenes;
import net.wanji.business.schedule.SceneLabelMap;
import net.wanji.business.service.TjFragmentedSceneDetailService;
import net.wanji.business.service.TjTaskService;
import net.wanji.common.utils.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

        // 获取任务的案例列表
        List<TaskCaseVo> taskCaseVos = taskListVo.getTaskCaseVos();

        // 将案例ID转换为List，用于后续查询场景详情
        List<Integer> mid = taskCaseVos.stream().map(TaskCaseVo::getSceneDetailId).collect(Collectors.toList());

        // 根据案例ID列表查询场景详情
        List<TjFragmentedSceneDetail> sceneDetails = tjFragmentedSceneDetailService.listByIds(mid);
        // 将场景详情转换为VO对象列表
        List<SceneDetailVo> list = sceneDetails.stream().map(item -> {
            SceneDetailVo sceneDetailVo = new SceneDetailVo();
            BeanUtils.copyProperties(item, sceneDetailVo);
            return sceneDetailVo;
        }).collect(Collectors.toList());

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




}
