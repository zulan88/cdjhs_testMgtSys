package net.wanji.business.mapper;

import net.wanji.business.domain.InfinteMileScenceExo;
import net.wanji.business.entity.InfinteMileScence;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2024-02-23
 */
public interface InfinteMileScenceMapper extends BaseMapper<InfinteMileScence> {

    @Select({"<script>",
            "SELECT im.* , mp.name as map_name FROM infinte_mile_scence im LEFT JOIN tj_atlas_venue mp on im.map_id=mp.id",
            "<if test=\"status != null\">",
            "WHERE im.status = #{status}",
            "</if>",
            "ORDER BY im.create_date DESC",
            "</script>"
    })
    List<InfinteMileScenceExo> selectInfinteMileScenceExo(@Param("status") Integer status);

    @Select("SELECT slice_img FROM infinte_mile_scence WHERE id = #{id}")
    String getSliceImage(@Param("id") Integer id);

}
