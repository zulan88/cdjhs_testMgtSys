package net.wanji.approve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wj
 * @since 2023-12-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tj_device_detail")
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
    private String ip;

    /**
     * 数据服务器地址
     */
    @TableField("service_address")
    private String serviceAddress;

    /**
     * 数据通道
     */
    @TableField("data_channel")
    private String dataChannel;

    /**
     * 指令通道
     */
    @TableField("command_channel")
    private String commandChannel;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 最后上线时间
     */
    @TableField("last_online_date")
    private LocalDateTime lastOnlineDate;

    /**
     * 自定义字段1（仿真路径规划通道）
     */
    @TableField("attribute1")
    private String attribute1;

    /**
     * 自定义字段2（仿真验证绑定设备）
     */
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
    private LocalDateTime updatedDate;

    @TableField("is_inner")
    private Integer isInner;

}
