package net.wanji.business.domain.vo.task.infinity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author hcy
 * @version 1.0
 * @className PreparedVo
 * @description TODO
 * @date 2024/3/14 14:25
 **/
@ApiModel
@Data
public class InfinityTaskPreparedVo {
  @ApiModelProperty(value = "设备状态")
  private List<DeviceInfo> devicesInfo;

  @ApiModelProperty(value = "是否可以开始，true：可以，false：不可以",
      required = true)
  private boolean canStart = true;

  @ApiModelProperty(value = "状态说明")
  private String message;
}
