package net.wanji.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.wanji.business.common.Constants;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author hcy
 * @version 1.0
 * @className PlatformSSDto
 * @description 平台用例开始、结束控制实体类
 * @date 2023/11/15 16:17
 **/
@ApiModel(value = "平台用例开始、结束控制实体类",
    description = "平台用例开始、结束控制实体类")
@Data
public class PlatformSSDto {
  @ApiModelProperty(value = "任务id",
      required = true,
      dataType = "Integer",
      example = "1",
      position = 1)
  @NotNull(message = "任务id不能为空")
  private int taskId;

  @ApiModelProperty(value = "用例id",
      required = true,
      dataType = "Integer",
      example = "1",
      position = 2)
  @NotNull(message = "用例id不能为空")
  private int caseId;

  @ApiModelProperty(value = "任务控制状态，0：停止，1：开始，-1：异常",
      required = true,
      dataType = "Integer",
      example = "0",
      position = 3,
      notes = "0：停止，1：开始，-1：异常",
      allowableValues = "0：停止，1：开始，-1：异常")
  @NotNull(message = "用例id不能为空")
  private int state;

  @ApiModelProperty(value = "任务状态，True:全部场景停止；False：测试进行中",
      required = true,
      dataType = "Boolean",
      example = "true",
      position = 4)
  @NotNull(message = "任务状态不能为空")
  private boolean taskEnd = false;

  @ApiModelProperty(value = "上下文参数",
      dataType = "Object",
      example = "",
      position = 5)
  private Map<String, Object> context;

  @ApiModelProperty(value = "错误信息",
      dataType = "String",
      example = "",
      position = 6)
  private String message;

  @ApiModelProperty(value = "测试类型",
      dataType = "String",
      example = "Constants.TestMode",
      position = 7)
  private Integer testMode;

  @ApiModelProperty(value = "基准数据频道（主车之一）",
      dataType = "String",
      example = "result",
      position = 7)
  private String benchmarkDataChannel;
}
