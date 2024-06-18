package net.wanji.business.domain.vo.task.infinity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hcy
 * @version 1.0
 * @className SelectRecordIdVo
 * @description TODO
 * @date 2024/4/10 8:57
 **/
@ApiModel
@Data
public class SelectRecordIdVo {
  @ApiModelProperty(value = "任务ID", dataType = "Integer")
  private Integer taskId;
  @ApiModelProperty(value = "选中回放记录ID", dataType = "Integer")
  private Integer recordId;
}
