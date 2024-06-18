package net.wanji.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wanji.business.domain.dto.TaskDto;
import net.wanji.business.entity.infity.TjInfinityTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author hcy
 * @version 1.0
 * @interfaceName TjInfinityMapper
 * @description TODO
 * @date 2024/3/11 13:07
 **/
public interface TjInfinityMapper extends BaseMapper<TjInfinityTask> {

    List<Map<String, String>> selectCountByStatus(TaskDto taskDto);

    List<Map<String, Object>> getPageList(TaskDto taskDto);

    void saveCustomScenarioWeight(@Param("taskId") String task_id, @Param("weights") String weights, @Param("weightsType") String weightsType);

    int saveTask(Map<String, Object> task);


}
