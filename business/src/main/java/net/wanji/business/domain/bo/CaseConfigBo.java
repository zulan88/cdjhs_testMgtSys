package net.wanji.business.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.wanji.business.entity.TjCasePartConfig;

import java.time.LocalDateTime;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/24 13:38
 * @Descriptoin:
 */
@Data
public class CaseConfigBo extends TjCasePartConfig {
    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备类型节点
     */
    private String deviceType;

    /**
     * 支持角色
     */
    private String supportRoles;

    /**
     * IP
     */
    private String ip;

    /**
     * 数据服务器地址
     */
    private String serviceAddress;

    /**
     * 数据通道
     */
    private String dataChannel;

    /**
     * 指令通道
     */
    private String commandChannel;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 最后上线时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastOnlineDate;

    /**
     * 开始位置经度
     */
    private Double startLongitude;
    /**
     * 开始位置纬度
     */
    private Double startLatitude;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 航向角
     */
    private Double courseAngle;
    /**
     * 是否到达指定位置
     */
    private Integer positionStatus;
    /**
     * 车辆运行状态，0：空闲，1：运行
     */
    private Integer running;
}
