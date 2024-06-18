package net.wanji.approve.mapper;

import io.lettuce.core.dynamic.annotation.Param;
import net.wanji.approve.entity.RecordRe;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2023-12-07
 */
public interface RecordReMapper extends BaseMapper<RecordRe> {

    List<Integer> selectBydevice(@Param("deviceId") Integer deviceId);

    List<Integer> selectByperson(@Param("personId") Integer personId);

}
