package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.entity.TjCasePartConfig;
import net.wanji.business.entity.TjDeviceDetail;

import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/1 10:03
 * @Descriptoin:
 */
@Data
public class CasePartConfigVo extends TjCasePartConfig {
    /**
     * 参与者名称
     */
    private String modelName;
    /**
     * 参与者角色名称
     */
    private String participantRoleName;
    /**
     * 参与者类型名称
     */
    private String businessTypeName;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 选中状态
     */
    private boolean selected;
    /**
     * 设备列表
     */
    private List<DeviceDetailVo> devices;

}
