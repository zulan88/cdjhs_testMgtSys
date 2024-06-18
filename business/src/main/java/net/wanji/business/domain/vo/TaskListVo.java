package net.wanji.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.wanji.business.entity.TjTask;
import net.wanji.business.entity.TjTaskCaseRecord;
import net.wanji.business.entity.TjTaskDataConfig;

import java.util.List;
import java.util.Map;

/**
 * @author: guowenhao
 * @date: 2023/8/31 17:44
 * @description: 测试任务列表
 */
@ApiModel(value = "测试任务列表", description = "测试任务列表")
@Data
public class TaskListVo extends TjTask {

    @ApiModelProperty(value = "测试类型名称", required = true, dataType = "String", example = "虚实融合测试", position = 4)
    private String testTypeName;

    @ApiModelProperty(value = "已完成用例", required = true, dataType = "int", example = "0", position = 5)
    private int finishedCaseCount;

    @ApiModelProperty(value = "任务状态名称", required = true, dataType = "String", example = "待提交", position = 12)
    private String statusName;

    @ApiModelProperty(value = "关联主车设备ID", required = true, dataType = "Integer", example = "1")
    private Integer deviceId;

    @ApiModelProperty(value = "被测对象类型", required = true, dataType = "String", example = "域控制器", position = 7)
    private String objectType;

    @ApiModelProperty(value = "品牌", required = true, dataType = "String", example = "万集域控制器", position = 8)
    private String brand;

    @ApiModelProperty(value = "车牌号", required = true, dataType = "String", example = "京A****", position = 9)
    private String plateNumber;

    @ApiModelProperty(value = "唯一标识", required = true, dataType = "String", example = "CHXResult", position = 10)
    private String sign;

    @ApiModelProperty(value = "测试用例列表", required = true, dataType = "List<TaskCaseVo>", position = 11)
    private List<TaskCaseVo> taskCaseVos;

    @ApiModelProperty(value = "历史记录",
        required = true,
        dataType = "List<TjTaskCaseRecord>",
        position = 12)
    private List<Map<String, Object>> historyRecords;

    /**
     * 任务用例配置
     */
    @JsonIgnore
    private List<TjTaskDataConfig> taskCaseConfigs;
}
