package net.wanji.approve.service;

import net.wanji.approve.entity.AppointmentRecord;
import net.wanji.approve.entity.TjDeviceDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.approve.entity.vo.DeviceListVo;

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
public interface TjDeviceDetailApService extends IService<TjDeviceDetail> {

    List<DeviceListVo> getDevices(Set<Integer> set, Integer recordId);

    List<DeviceListVo> addInfo(List<DeviceListVo> list, List<Integer> deviceIds, AppointmentRecord appointmentRecord);

}
