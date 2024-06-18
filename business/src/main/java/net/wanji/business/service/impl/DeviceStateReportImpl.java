package net.wanji.business.service.impl;

import com.alibaba.fastjson2.JSONObject;
import net.wanji.business.annotion.DeviceReport;
import net.wanji.business.component.DeviceStateToRedis;
import net.wanji.business.domain.dto.device.DeviceStateDto;
import net.wanji.business.service.DeviceReportService;
import net.wanji.business.service.StatusManage;
import org.springframework.stereotype.Service;

/**
 * @author glace
 * @version 1.0
 * @className DeviceStateReportImpl
 * @description TODO
 * @date 2023/10/7 10:24
 **/
@DeviceReport("0")
@Service
public class DeviceStateReportImpl
    implements DeviceReportService<JSONObject> {
  private final DeviceStateToRedis deviceStateToRedis;

  public DeviceStateReportImpl(DeviceStateToRedis deviceStateToRedis) {
    this.deviceStateToRedis = deviceStateToRedis;
  }

  @Override
  public void dataProcess(JSONObject jsonObject) {
    DeviceStateDto deviceStateDto = JSONObject.parseObject(String.valueOf(jsonObject), DeviceStateDto.class);
    deviceStateToRedis.save(deviceStateDto.getDeviceId(), deviceStateDto.getState(),
            DeviceStateToRedis.DEVICE_STATE_PREFIX, jsonObject.getString("channel"));
    String key =  DeviceStateToRedis.DEVICE_STATE_PREFIX + "_" + deviceStateDto.getDeviceId() + "_" + jsonObject.getString("channel");
    StatusManage.countDown(key, deviceStateDto.getState());
  }
}
