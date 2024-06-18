package net.wanji.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wanji.business.entity.TjScenelibTree;
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
public interface TjScenelibTreeMapper extends BaseMapper<TjScenelibTree> {

    List<TjScenelibTree>  selectByCondition(@Param("type") String type);
}
