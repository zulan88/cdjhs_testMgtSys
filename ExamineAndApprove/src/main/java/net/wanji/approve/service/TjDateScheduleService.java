package net.wanji.approve.service;

import net.wanji.approve.entity.TjDateSchedule;
import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.approve.entity.dto.ScheduleDto;
import net.wanji.approve.entity.vo.DateScheduleVo;
import net.wanji.business.exception.BusinessException;

import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2023-12-07
 */
public interface TjDateScheduleService extends IService<TjDateSchedule> {

    List<DateScheduleVo> takeDateScheduleByDate(String year, Integer quarter);

    List<DateScheduleVo> takeDateScheduleByDateOld(String year, Integer quarter);

    Set<Integer> takeDeviceIds(ScheduleDto scheduleDto);

    void commitSchedule(ScheduleDto scheduleDto);

    void deleteSchedule(Integer recordId) throws BusinessException;

}
