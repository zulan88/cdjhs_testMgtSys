package net.wanji.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wanji.business.entity.TjInfinityTaskDataConfig;

import java.util.List;

public interface TjInfinityTaskDataConfigMapper extends BaseMapper<TjInfinityTaskDataConfig> {

    List<TjInfinityTaskDataConfig> selectByCondition(TjInfinityTaskDataConfig in);
}
