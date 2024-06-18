package net.wanji.approve.mapper;

import io.lettuce.core.dynamic.annotation.Param;
import net.wanji.approve.entity.TjDateSchedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wanji.approve.entity.vo.DateScheduleVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2023-12-07
 */
public interface TjDateScheduleMapper extends BaseMapper<TjDateSchedule> {

    List<TjDateSchedule> selectObjByAppId(@Param("appId") Integer appId);

}
