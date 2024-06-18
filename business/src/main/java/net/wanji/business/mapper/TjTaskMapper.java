package net.wanji.business.mapper;

import net.wanji.business.domain.dto.TaskDto;
import net.wanji.business.domain.vo.TaskListVo;
import net.wanji.business.domain.vo.TaskReportVo;
import net.wanji.business.entity.TjTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
* @author guowenhao
* @description 针对表【tj_task(测试任务表)】的数据库操作Mapper
* @createDate 2023-08-31 17:39:15
* @Entity net.wanji.business.entity.TjTask
*/
public interface TjTaskMapper extends BaseMapper<TjTask> {

    /**
     * 任务数量统计
     * @return
     */
    List<Map<String, String>> selectCountByStatus(TaskDto taskDto);

    List<TaskListVo> getPageList(TaskDto taskDto);

    List<TaskReportVo> getExportList(Integer taskId);

    void saveCustomScenarioWeight(@Param("taskId") String task_id, @Param("weights") String weights, @Param("weightsType") String weightsType);

    void deleteCustomScenarioWeightByTaskId(@Param("taskId") String task_id);
}




