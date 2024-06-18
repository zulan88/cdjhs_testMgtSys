package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.wanji.common.annotation.Excel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 
 * @TableName tj_case_real_record
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value ="tj_case_real_record")
public class TjCaseRealRecord implements Serializable {
    /**
     * 实车验证记录ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

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
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 开始时间
     */
    @TableField("start_time")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}