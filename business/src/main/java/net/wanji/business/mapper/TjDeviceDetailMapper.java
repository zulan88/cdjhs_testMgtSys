package net.wanji.business.mapper;

import net.wanji.business.domain.dto.TjDeviceDetailDto;
import net.wanji.business.domain.vo.DeviceDetailVo;
import net.wanji.business.entity.TjDeviceDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
* @author guanyuduo
* @description 针对表【tj_device】的数据库操作Mapper
* @createDate 2023-08-17 10:56:39
* @Entity net.wanji.business.entity.TjDevice
*/
public interface TjDeviceDetailMapper extends BaseMapper<TjDeviceDetail> {

    /**
     * 条件查询
     * @param deviceDto
     * @return
     */
    List<DeviceDetailVo> selectByCondition(TjDeviceDetailDto deviceDto);
}
