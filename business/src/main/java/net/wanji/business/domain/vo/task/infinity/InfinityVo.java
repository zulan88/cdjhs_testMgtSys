package net.wanji.business.domain.vo.task.infinity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hcy
 * @version 1.0
 * @className InfinityVo
 * @description TODO
 * @date 2024/3/11 13:38
 **/

@ApiModel
@Data
public class InfinityVo implements Serializable {
  private static final long serialVersionUID = 2134844207273740138L;

  @ApiModelProperty(value = "流程单号", dataType = "double")
  private String orderNumber;

  // 排期开始时间

  @ApiModelProperty(value = "计划测试时长", required = true, dataType = "long")
  private Long planTestTime;

  @ApiModelProperty(value = "测试开始时间",
      dataType = "Date",
      example = "2023-05-25 11:01:02")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date testStartTime;

  @ApiModelProperty(value = "测试结束时间",
      dataType = "Date",
      example = "2023-05-25 11:01:02")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Data testEndTime;

  @ApiModelProperty(value = "测试场景名称",
      required = true,
      dataType = "string")
  private String caseName;

  @ApiModelProperty(value = "被测试对象类型",
      required = true,
      dataType = "string")
  private String testedType;

  @ApiModelProperty(value = "被测试对象名称",
      required = true,
      dataType = "string")
  private String testedName;

  @ApiModelProperty(value = "委托单位", required = true, dataType = "string")
  private String entrustOrg;

  @ApiModelProperty(value = "委托人", required = true, dataType = "string")
  private String entruster;

  @ApiModelProperty(value = "联系方式", required = true, dataType = "string")
  private String entrusterContact;

  @ApiModelProperty(value = "委托信息", required = true, dataType = "string")
  private String entrustContent;

  /**
   * 任务状态（save：待提交；waiting：待测试；prepping: 准备中； running：进行中；finished：已完成；past_due：逾期）
   */
  @ApiModelProperty(value = "委托信息", dataType = "string")
  private String status;
}
