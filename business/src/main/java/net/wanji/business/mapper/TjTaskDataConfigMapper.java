package net.wanji.business.mapper;

import net.wanji.business.entity.TjTaskDataConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author guowenhao
* @description 针对表【tj_task_data_config(测试任务-数据配置表)】的数据库操作Mapper
* @createDate 2023-08-31 17:39:16
* @Entity net.wanji.business.entity.TjTaskDataConfig
*/
public interface TjTaskDataConfigMapper extends BaseMapper<TjTaskDataConfig> {

    /**
     * 条件查询
     * @param in
     * @return
     */
    List<TjTaskDataConfig> selectByCondition(TjTaskDataConfig in);

}




