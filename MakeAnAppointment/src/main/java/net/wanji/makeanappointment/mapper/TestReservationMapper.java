package net.wanji.makeanappointment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wanji.makeanappointment.domain.vo.TestTypeVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestReservationMapper extends BaseMapper<TestTypeVo> {
}
