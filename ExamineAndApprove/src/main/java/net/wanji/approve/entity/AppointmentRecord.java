package net.wanji.approve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 预约记录表
 * </p>
 *
 * @author wj
 * @since 2023-12-05
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("appointment_record")
public class AppointmentRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(notes = "id")
    private Integer id;

    /**
     * 流程单号
     */
    @TableField("record_id")
    @ApiModelProperty(notes = "流程单号")
    private String recordId;

    /**
     * 单位名称
     */
    @TableField("unit_name")
    @ApiModelProperty(notes = "单位名称")
    private String unitName;

    /**
     * 对接人
     */
    @TableField("contact_person")
    @ApiModelProperty(notes = "对接人")
    private String contactPerson;

    /**
     * 联系方式
     */
    @TableField("phone_number")
    @ApiModelProperty(notes = "联系方式")
    private String phoneNumber;

    /**
     * 测试类型
     */
    @TableField("type")
    @ApiModelProperty(notes = "测试类型")
    private String type;

    /**
     * 被测对象ID
     */
    @TableField("measurand_id")
    @ApiModelProperty(notes = "被测对象ID")
    private Integer measurandId;

    /**
     * 被测对象类型
     */
    @TableField("measurand_type")
    @ApiModelProperty(notes = "被测对象类型")
    private String measurandType;

    @TableField("measurand_name")
    private String measurandName;

    /**
     * 用例id
     */
    @TableField("case_ids")
    @ApiModelProperty(notes = "用例id,逗号分割")
    private String caseIds;

    /**
     * 提交时间
     */
    @TableField("commit_date")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "提交时间")
    private String commitDate;

    /**
     * 期望的排期
     */
    @TableField("expected_date")
    @ApiModelProperty(notes = "期望的排期")
    private String expectedDate;

    /**
     * 测试排期
     */
    @TableField("test_schedule")
    @ApiModelProperty(notes = "测试排期")
    private String testSchedule;

    /**
     * 状态  0-待审批 1-审批通过 2-驳回 3-已取消 -1 未提交数据
     */
    @TableField("status")
    @ApiModelProperty(notes = "状态 0-待审批 1-审批通过 2-驳回 3-已取消")
    private Integer status;

    /**
     * 花费
     */
    @TableField("expense")
    @ApiModelProperty(notes = "花费")
    private Integer expense;

    /**
     * 申请表
     */
    @TableField("application_from")
    @ApiModelProperty(notes = "申请表路径")
    private String applicationFrom;

    /**
     * 最后审批时间
     */
    @TableField("last_approval_time")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "最后审批时间")
    private String lastApprovalTime;

    /**
     * 驳回理由
     */
    @TableField("rejection_mes")
    @ApiModelProperty(notes = "驳回理由")
    private String rejectionMes;

    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;

}
