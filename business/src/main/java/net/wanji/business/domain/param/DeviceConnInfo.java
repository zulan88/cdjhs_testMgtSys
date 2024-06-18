package net.wanji.business.domain.param;

import lombok.Data;

import java.util.Map;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/23 17:43
 * @Descriptoin:
 */
@Data
public class DeviceConnInfo {
    /**
     * 设备id
     */
    private String id;
    /**
     * 控制频道
     */
    private String controlChannel;
    /**
     * 通信频道
     */
    private String channel;

    private Map<String, Object> params;
    
    private String role;

    public DeviceConnInfo(String id, String controlChannel, String channel, String role, Map<String, Object> params) {
        this.id = id;
        this.controlChannel = controlChannel;
        this.channel = channel;
        this.role = role;
        this.params = params;
    }

    public DeviceConnInfo(String id, String controlChannel, String datachannel) {
        this.id = id;
        this.controlChannel = controlChannel;
        this.channel = datachannel;
    }
}
