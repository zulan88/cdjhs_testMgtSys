package net.wanji.business.domain.param;

import lombok.Data;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/23 17:44
 * @Descriptoin:
 */
@Data
public class DeviceConnRule {
    /**
     * 数据提供设备
     */
    private DeviceConnInfo source;
    /**
     * 数据接收设备
     */
    private DeviceConnInfo target;
}
