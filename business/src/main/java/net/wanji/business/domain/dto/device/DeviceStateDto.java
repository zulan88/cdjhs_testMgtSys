package net.wanji.business.domain.dto.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author glace
 * @version 1.0
 * @className DeviceStateDto
 * @description TODO
 * @date 2023/10/7 9:32
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceStateDto {
  /**
   * 当前时间戳 ms 级
   */
  private Long timestamp;
  /**
   * 操作类型，0：设备状态；1：准备状态查询
   */
  private Integer type;
  /**
   * 规控算法状态，1.占用 2：空闲
   */
  private Integer state;
  /**
   * 平台设备ID
   */
  private Integer deviceId;

  /**
   * 设备唯一标识
   */
  private String uniques;

  /**
   * 设备状态 1:在线
   */
  private Integer deviceStatus;
}
