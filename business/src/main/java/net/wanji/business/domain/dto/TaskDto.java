package net.wanji.business.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author: guowenhao
 * @date: 2023/8/31 17:58
 * @description:
 */
@ApiModel(value = "测试任务列表页展示实体类", description = "测试任务列表页使用")
@Data
public class TaskDto {

    @ApiModelProperty(value = "页码", required = true, dataType = "Integer", example = "1", position = 1)
    @NotNull(message = "请选择页码")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "页大小", required = true, dataType = "Integer", example = "10", position = 2)
    @NotNull(message = "请选择页大小")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "流程编号", dataType = "String", example = "2023-11", position = 3)
    private String taskCode;

    @ApiModelProperty(value = "车牌号", dataType = "String", example = "京A****", position = 4)
    private String plateNumber;

    @ApiModelProperty(value = "唯一标识", dataType = "String", example = "CHXResult", position = 5)
    private String sign;

    @ApiModelProperty(value = "委托单位", dataType = "String", example = "万集科技", position = 6)
    private String client;

    @ApiModelProperty(value = "创建开始时间", dataType = "Date", example = "2023-05-25 11:01:02", position = 7)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startCreateTime;

    @ApiModelProperty(value = "创建结束时间", dataType = "Date", example = "2023-05-25  11:01:03", position = 8)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endCreateTime;

    @ApiModelProperty(value = "排期开始日期", dataType = "Date", example = "2023-05-25", position = 9)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startPlanDate;

    @ApiModelProperty(value = "排期结束日期", dataType = "Date", example = "2023-05-25", position = 10)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endPlanDate;

    @ApiModelProperty(value = "测试类型", dataType = "String", example = "测试类型", position = 11)
    private String testType;

    @ApiModelProperty(value = "测试对象类型", dataType = "String", example = "测试对象", position = 12)
    private String objectType;

    @ApiModelProperty(value = "任务状态", dataType = "String", example = "任务状态", position = 13)
    private String status;

    @ApiModelProperty(value = "主键ID", dataType = "Integer", example = "1", position = 14)
    private Integer id;

    private String createdBy;

    private Integer isInner;

    private Integer opStatus;
}
