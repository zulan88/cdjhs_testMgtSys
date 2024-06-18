package net.wanji.onsite.mapper;

import net.wanji.onsite.entity.TjOnsiteCase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2024-06-04
 */
public interface TjOnsiteCaseMapper extends BaseMapper<TjOnsiteCase> {

    TjOnsiteCase getOnsiteCaseByNum(@Param("onsiteNumber") String onsiteNumber);

}
