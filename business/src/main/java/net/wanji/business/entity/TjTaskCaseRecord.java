package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.wanji.common.annotation.Excel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 
 * @TableName tj_task_case_record
 */
@TableName(value ="tj_task_case_record")
@Data
public class TjTaskCaseRecord implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 任务ID
     */
    @TableField("task_id")
    private Integer taskId;

    /**
     * 用例ID
     */
    @TableField("case_id")
    private Integer caseId;

    /**
     * 点位详情
     */
    @TableField("detail_info")
    private String detailInfo;

    /**
     * 轨迹文件
     */
    @TableField("route_file")
    private String routeFile;

    /**
     * 评价文件
     */
    @TableField("evaluate_path")
    private String evaluatePath;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 开始时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("end_time")
    private LocalDateTime endTime;

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
     * 测试记录ID
     */
    @TableField("record_id")
    private Integer recordId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}