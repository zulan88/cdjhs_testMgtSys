package net.wanji.business.domain.vo.task.infinity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.wanji.business.common.DeviceStatus;

/**
 * @author hcy
 * @version 1.0
 * @className DeviceInfo
 * @description TODO
 * @date 2024/3/14 14:31
 **/
@ApiModel
@Data
public class DeviceInfo {
  @ApiModelProperty(value = "设备名称")
  private String name;
  @ApiModelProperty(value = "设备类型,av,mvReal,mvSimulation,sp")
  private String type;
  @ApiModelProperty(value = "设备ID")
  private Integer id;
  @ApiModelProperty(value = "设备状态-枚举，OFFLINE：离线，ONLINE：在线，ARRIVED：已到达，NOT_ARRIVED：等待到达，BUSY：使用中，IDLE：空闲")
  private DeviceStatus deviceStatus;
  @ApiModelProperty(value = "设备状态-名称")
  private String statusName;

  @JsonIgnore
  private String commandChannel;

  public void setDeviceStatus(DeviceStatus deviceStatus) {
    this.deviceStatus = deviceStatus;
    this.statusName = deviceStatus.getName();
  }
}
