package net.wanji.business.service.impl;

import com.alibaba.fastjson2.JSONObject;
import net.wanji.business.annotion.DeviceReport;
import net.wanji.business.component.DeviceStateToRedis;
import net.wanji.business.domain.dto.device.DeviceReadyStateDto;
import net.wanji.business.service.DeviceReportService;
import net.wanji.business.service.StatusManage;
import org.springframework.stereotype.Service;

/**
 * @author glace
 * @version 1.0
 * @className DeviceReadyStateReportImpl
 * @description TODO
 * @date 2023/10/7 10:24
 **/
@DeviceReport("1")
@Service
public class DeviceReadyStateReportImpl
    implements DeviceReportService<JSONObject> {

  private final DeviceStateToRedis deviceStateToRedis;

  public DeviceReadyStateReportImpl(DeviceStateToRedis deviceStateToRedis) {
    this.deviceStateToRedis = deviceStateToRedis;
  }

  @Override
  public void dataProcess(JSONObject jsonObject) {
    DeviceReadyStateDto deviceReadyStateDto = JSONObject.parseObject(String.valueOf(jsonObject), DeviceReadyStateDto.class);
    deviceStateToRedis.save(deviceReadyStateDto.getDeviceId(), deviceReadyStateDto.getState(),
            DeviceStateToRedis.DEVICE_READY_STATE_PREFIX, jsonObject.getString("channel"));

    String key =  DeviceStateToRedis.DEVICE_READY_STATE_PREFIX + "_" + deviceReadyStateDto.getDeviceId() + "_" + jsonObject.getString("channel");
    StatusManage.countDown(key, deviceReadyStateDto.getState());
  }
}
