package net.wanji.business.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 测试任务-用例详情表
 * @TableName tj_task_case
 */
@TableName(value ="tj_task_case")
@Data
public class TjTaskCase implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 测试任务id
     */
    @TableField(value = "task_id")
    private Integer taskId;

    /**
     * 用例id
     */
    @TableField(value = "case_id")
    private Integer caseId;


    /**
     * 顺序
     */
    @TableField(value = "sort")
    private Integer sort;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 测试状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 通过率
     */
    @ApiModelProperty(value = "通过率", required = true, dataType = "String", example = "100%")
    @TableField(value = "passing_rate")
    private String passingRate;

    @ApiModelProperty(value = "点位详情", dataType = "String")
    @TableField(value = "detail_info")
    private String detailInfo;

    @ApiModelProperty(value = "轨迹文件路径", dataType = "String")
    @TableField(value = "route_file")
    private String routeFile;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = true, dataType = "String", example = "2021-05-25 18:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = true, dataType = "String", example = "2021-05-25 18:30")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 测试总时长
     */
    @TableField(value = "test_total_time")
    private String testTotalTime;

    private String connectInfo;

    /** 压缩包目录 */
    @TableField("zip_path")
    private String zipPath;

    /** 地图目录 */
    @TableField("xodr_path")
    private String xodrPath;

    /** 场景脚本目录 */
    @TableField("xosc_path")
    private String xoscPath;


    /** 评价路径 */
    @TableField("evaluate_path")
    private String evaluatePath;

    private static final long serialVersionUID = 1L;
}