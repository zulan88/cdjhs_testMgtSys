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
 * 测试预约申请-被测对象列表
 * </p>
 *
 * @author wj
 * @since 2023-12-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tj_testee_object_info")
public class TjTesteeObjectInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 被测对象名称
     */
    @TableField("testee_object_name")
    private String testeeObjectName;

    /**
     * 被测对象类型
     */
    @TableField("testee_object_type")
    private String testeeObjectType;

    /**
     * 自动驾驶等级
     */
    @TableField("automatic_driving_level")
    private String automaticDrivingLevel;

    /**
     * 车辆品牌
     */
    @TableField("vehicle_brand")
    private String vehicleBrand;

    /**
     * 车辆型号
     */
    @TableField("vehicle_model")
    private String vehicleModel;

    /**
     * 车辆类型
     */
    @TableField("vehicle_type")
    private String vehicleType;

    /**
     * 车牌号码
     */
    @TableField("vehicle_license")
    private String vehicleLicense;

    /**
     * 对接人
     */
    @TableField("contact_person")
    private String contactPerson;

    /**
     * 联系方式
     */
    @TableField("phone_number")
    private String phoneNumber;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 删除标志（0-正常 1-删除）
     */
    @TableField("status")
    private String status;

    @TableField("testee_object_pic")
    private String testeeObjectPic;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    @TableField("createBy")
    private String createBy;

    /**
     * 修改时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 修改人
     */
    @TableField("updateBy")
    private String updateBy;

    @TableField("device_id")
    private Integer deviceId;



}
