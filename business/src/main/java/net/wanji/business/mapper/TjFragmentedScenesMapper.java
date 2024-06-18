package net.wanji.business.mapper;

import net.wanji.business.entity.TjFragmentedScenes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 片段式场景表 Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2023-06-27
 */
public interface TjFragmentedScenesMapper extends BaseMapper<TjFragmentedScenes> {

    List<TjFragmentedScenes>  selectByCondition(@Param("type") String type);
}
