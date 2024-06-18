package net.wanji.business.domain.dto.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author glace
 * @version 1.0
 * @className DeviceReadyStateDto
 * @description TODO
 * @date 2023/10/7 9:32
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceReadyStateParam extends DeviceReadyStateDto{
  /**
   * 控制通道
   */
  private String controlChannel;
  /**
   * 自定义参数
   */
  private Object params;


  public DeviceReadyStateParam(Integer deviceId, String controlChannel) {
    this.setDeviceId(deviceId);
    this.setTimestamp(System.currentTimeMillis());
    this.setType(1);
    this.controlChannel = controlChannel;
  }
}
