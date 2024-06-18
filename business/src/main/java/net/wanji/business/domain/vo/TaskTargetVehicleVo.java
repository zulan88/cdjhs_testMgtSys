package net.wanji.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: guanyuduo
 * @date: 2023/11/13 17:04
 * @descriptoin:
 */
@ApiModel(value = "被测车辆", description = "创建任务信息时，被测车辆信息")
@Data
public class TaskTargetVehicleVo {

    @ApiModelProperty(value = "设备ID", dataType = "Integer", example = "1", position = 1)
    private Integer deviceId;

    @ApiModelProperty(value = "被测设备类型", dataType = "String", example = "自动驾驶车辆", position = 2)
    private String objectType;

    @ApiModelProperty(value = "自动驾驶等级", dataType = "String", example = "L3", position = 3)
    private String level;

    @ApiModelProperty(value = "品牌", dataType = "String", example = "畅行", position = 4)
    private String brand;

    @ApiModelProperty(value = "车牌号(型号)", dataType = "String", example = "京A****", position = 5)
    private String plateNumber;

    @ApiModelProperty(value = "类型", dataType = "String", example = "SUV", position = 6)
    private String type;

    @ApiModelProperty(value = "唯一标识", dataType = "String", example = "CHXResult", position = 7)
    private String sign;

    @ApiModelProperty(value = "对接人", dataType = "String", example = "张三", position = 8)
    private String person;

    @ApiModelProperty(value = "联系方式", dataType = "String", example = "139****8392", position = 9)
    private String phone;

    @ApiModelProperty(value = "是否选择", dataType = "Boolean", example = "true", position = 10)
    private boolean selected;
}
