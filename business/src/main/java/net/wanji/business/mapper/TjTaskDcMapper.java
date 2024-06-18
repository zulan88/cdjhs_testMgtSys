package net.wanji.business.mapper;

import net.wanji.business.domain.vo.TaskDcVo;
import net.wanji.business.entity.TjTaskDc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author guowenhao
* @description 针对表【tj_task_dc(测试任务-指标配置表)】的数据库操作Mapper
* @createDate 2023-08-31 17:39:16
* @Entity net.wanji.business.entity.TjTaskDc
*/
public interface TjTaskDcMapper extends BaseMapper<TjTaskDc> {

    /**
     * 查询任务指标
     * @param taskId
     * @return
     */
    List<TaskDcVo> selectDcByTaskId(Integer taskId);

}




