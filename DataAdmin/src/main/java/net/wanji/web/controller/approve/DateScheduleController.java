package net.wanji.web.controller.approve;

import net.wanji.approve.entity.AppointmentRecord;
import net.wanji.approve.entity.dto.ScheduleDto;
import net.wanji.approve.entity.vo.DateScheduleVo;
import net.wanji.approve.entity.vo.DeviceListVo;
import net.wanji.approve.service.AppointmentRecordService;
import net.wanji.approve.service.TjDateScheduleService;
import net.wanji.approve.service.TjDeviceDetailApService;
import net.wanji.business.exception.BusinessException;
import net.wanji.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/dateSchedule")
public class DateScheduleController {

    @Autowired
    private TjDateScheduleService dateScheduleService;

    @Autowired
    private TjDeviceDetailApService tjDeviceDetailApService;

    @Autowired
    private AppointmentRecordService appointmentRecordService;

    @GetMapping("/getschedule")
    public AjaxResult getSchedule(String year){
        List<DateScheduleVo> list = dateScheduleService.takeDateScheduleByDate(year,null);
        return AjaxResult.success(list);
    }

    @GetMapping("/getscheduleQuarter")
    public AjaxResult getScheduleQuarter(String year, Integer quarter){
        List<DateScheduleVo> list = dateScheduleService.takeDateScheduleByDateOld(year,quarter);
        return AjaxResult.success(list);
    }

    @PostMapping("/getdevice")
    public AjaxResult getDevice(@RequestBody ScheduleDto scheduleDto){
        Set<Integer> set = dateScheduleService.takeDeviceIds(scheduleDto);
        List<DeviceListVo> devices = tjDeviceDetailApService.getDevices(set,scheduleDto.getRecordId());
        List<Integer> deviceIds = appointmentRecordService.getdeviceIdsByCase(scheduleDto.getRecordId());
        AppointmentRecord appointmentRecord = appointmentRecordService.getById(scheduleDto.getRecordId());
        List<DeviceListVo> list = tjDeviceDetailApService.addInfo(devices,deviceIds,appointmentRecord);
        return AjaxResult.success(list);
    }

    @PostMapping("/commit")
    public AjaxResult commit(@RequestBody ScheduleDto scheduleDto) throws BusinessException {
        dateScheduleService.deleteSchedule(scheduleDto.getRecordId());
        dateScheduleService.commitSchedule(scheduleDto);
        return AjaxResult.success();
    }


}
