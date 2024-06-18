package net.wanji.business.domain.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.wanji.business.domain.vo.CaseContinuousVo;
import net.wanji.business.entity.TjTaskDataConfig;
import net.wanji.business.entity.TjTaskDc;

import javax.validation.constraints.NotNull;

/**
 * @author: guowenhao
 * @date: 2023/8/31 19:46
 * @description:
 */
@ApiModel(value = "TaskBo", description = "保存测试任务实体类")
@Data
public class TaskBo {

    @ApiModelProperty(value = "节点")
    @NotNull(message = "请确认当前流程节点")
    private Integer processNode;

    @ApiModelProperty(value = "任务ID（创建任务信息）", example = "1")
    private Integer id;

    @ApiModelProperty(value = "委托单位（创建任务信息）", example = "xxx公司")
    private String client;

    @ApiModelProperty(value = "委托人（创建任务信息）", example = "张三")
    private String consigner;

    @ApiModelProperty(value = "联系方式（创建任务信息）", example = "133****9839")
    private String contract;

    @ApiModelProperty(value = "测试类型（创建任务信息）", example = "virtualRealFusion")
    private String testType;

    @ApiModelProperty(value = "测试设备ID集合（创建任务信息）", example = "[1,2,3]")
    private List<Integer> avDeviceIds;

    public List<Integer> getAvDeviceIds() {
        if (avDeviceIds == null) {
            avDeviceIds = new ArrayList<>();
        }
        return avDeviceIds;
    }

    @ApiModelProperty(value = "开始时间（创建任务信息）", example = "2023-10-19 18:40:55")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty(value = "结束时间（创建任务信息）", example = "2024-10-19 18:40:55")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "连续性配置（测试连续性配置）")
    private List<CaseContinuousVo> cases;

    @ApiModelProperty(value = "主车轨迹文件（测试连续性配置）")
    private String routeFile;

    @ApiModelProperty(value = "任务指标")
    private List<TjTaskDc> diadynamicCriterias;

    private Integer isInner;

    private Integer apprecordId;

    private Integer measurandId;

    /**
     * 是否连续性测试 0否 1是
     */
    private Integer isContinuity;

    public Integer getIsContinuity() {
        if (isContinuity == null) {
            isContinuity = 0;
        }
        return isContinuity;
    }
}
