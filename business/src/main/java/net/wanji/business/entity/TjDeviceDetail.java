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
 * @TableName tj_device
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value ="tj_device_detail")
public class TjDeviceDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 设备ID
     */
    @TableId(value = "device_id", type = IdType.AUTO)
    private Integer deviceId;

    /**
     * 设备名称
     */
    @TableField("device_name")
    @Excel(name = "设备名称")
    private String deviceName;

    /**
     * 设备类型
     */
    @TableField("device_type")
    private String deviceType;

    /**
     * 支持角色
     */
    @TableField("support_roles")
    private String supportRoles;

    /**
     * IP
     */
    @TableField("ip")
    @Excel(name = "IP")
    private String ip;

    /**
     * 数据服务器地址
     */
    @TableField("service_address")
    @Excel(name = "数据服务器")
    private String serviceAddress;

    /**
     * 数据通道
     */
    @TableField("data_channel")
    @Excel(name = "数据通道")
    private String dataChannel;

    /**
     * 指令通道
     */
    @TableField("command_channel")
    @Excel(name = "指令通道")
    private String commandChannel;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 设备使用状态：0：空闲，1：使用中
     */
    @TableField("busy_status")
    private Integer busyStatus;

    /**
     * 最后上线时间
     */
    @TableField("last_online_date")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastOnlineDate;

    @TableField("attribute1")
    private String attribute1;

    @TableField("attribute2")
    private String attribute2;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建日期
     */
    @TableField("created_date")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    /**
     * 修改人
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 修改日期
     */
    @TableField("updated_date")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;

    @TableField("is_inner")
    private Integer isInner;

}
