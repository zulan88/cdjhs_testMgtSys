package net.wanji.business.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.wanji.common.annotation.Excel;

/**
 * 测试任务表
 * @TableName tj_task
 */
@TableName(value ="tj_task")
@Data
public class TjTask implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 委托单位
     */
    @TableField(value = "client")
    @ApiModelProperty(value = "委托单位", required = true, dataType = "String", example = "万集科技", position = 13)
    private String client;

    /**
     * 委托人
     */
    @TableField(value = "consigner")
    private String consigner;

    /**
     * 联系方式
     */
    @TableField(value = "contract")
    private String contract;

    /**
     * 测试类型（字典：test_type）
     */
    @TableField(value = "test_type")
    private String testType;

    /**
     * 任务编号
     */
    @TableField(value = "task_code")
    @ApiModelProperty(value = "流程单号", required = true, dataType = "String", example = "task 2023-05-25 18:30", position = 1)
    private String taskCode;

    /**
     * 排期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField(value = "plan_date")
    @ApiModelProperty(value = "排期开始时间", required = true, dataType = "String", example = "2023-05-25", position = 2)
    private Date planDate;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time")
    @ApiModelProperty(value = "任务创建时间", required = true, dataType = "String", example = "2023-05-25 18:10:11", position = 3)
    private Date createTime;

    /**
     * 测试用例数
     */
    @TableField(value = "case_count")
    @ApiModelProperty(value = "测试用例数", required = true, dataType = "int", example = "3", position = 6)
    private Integer caseCount;

    /**
     * 通过率
     */
    @TableField(value = "passing_rate")
    private String passingRate;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 测试总时长
     */
    @TableField(value = "test_total_time")
    @ApiModelProperty(value = "测试总时长", required = false, dataType = "String", example = "00:15:24", position = 11)
    private String testTotalTime;

    /**
     * 状态（save：待提交；waiting：待测试；prepping: 准备中； running：进行中；finished：已完成；past_due：逾期）
     */
    private String status;

    /**
     * 流程节点（1：创建任务信息；2：选择测试用例；3：测试连续性配置；4：查看评价方案；5.待试验；6.已完成）
     */
    @TableField(value = "process_node")
    private Integer processNode;

    /**
     * 是否连续性测试
     */
    @TableField(value = "continuous")
    private boolean continuous;

    /**
     * 主车轨迹文件
     */
    @TableField(value = "main_plan_file")
    private String mainPlanFile;


    /**
     * 完整轨迹文件
     */
    @TableField(value = "route_file")
    private String routeFile;


    /**
     * 创建人
     */
    @TableField("created_by")
    @Excel(name = "创建人")
    private String createdBy;

    /**
     * 创建日期
     */
    @TableField("created_date")
    @Excel(name = "创建日期")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    /**
     * 修改人
     */
    @TableField("updated_by")
    @Excel(name = "修改人")
    private String updatedBy;

    /**
     * 修改日期
     */
    @TableField("updated_date")
    @Excel(name = "修改日期")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField("apprecord_id")
    private Integer apprecordId;

    @TableField("measurand_id")
    private Integer measurandId;

    /**
     * 运营平台状态： 0：待测试，1：进行中，2：已完成，4：逾期，5：复审通过，6：复审失败，7：已取消
     */
    @TableField("op_status")
    private Integer opStatus;

    @TableField("is_inner")
    private Integer isInner;

    @TableField("remark")
    private String remark;

    @TableField("last_status")
    private String lastStatus;
    /**
     * 选中测试记录ID
     */
    @TableField("selected_record_id")
    private Integer selectedRecordId;
}