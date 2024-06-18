package net.wanji.makeanappointment.domain.vo;

import lombok.Data;
import net.wanji.common.core.page.PageDomain;

import java.util.Date;

/**
 * @ClassName TesteeObjectVo
 * @Description
 * @Author liruitao
 * @Date 2023-12-05
 * @Version 1.0
 **/
@Data
public class TestObjectVo extends PageDomain {

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 被测对象名称
     */
    private String testeeObjectName;

    /**
     * 被测对象类型
     */
    private String testeeObjectType;

    /**
     * 自动驾驶等级
     */
    private String automaticDrivingLevel;

    /**
     * 车辆品牌
     */
    private String vehicleBrand;

    /**
     * 车辆型号
     */
    private String vehicleModel;

    /**
     * 车辆类型
     */
    private String vehicleType;

    /**
     * 车牌号码
     */
    private String vehicleLicense;

    /**
     * 对接人
     */
    private String contactPerson;

    /**
     * 联系方式
     */
    private String phoneNumber;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（0-正常 1-删除）
     */
    private Integer status;

    /**
     * 被测对象照片
     */
    private String testeeObjectPic;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateBy;

    // 数据通道
    private String dataChannel;
    // 指令通道
    private String commandChannel;
}
