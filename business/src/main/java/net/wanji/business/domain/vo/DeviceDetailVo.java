package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.entity.TjDeviceDetail;
import net.wanji.common.annotation.Excel;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/29 17:23
 * @Descriptoin:
 */
@Data
public class DeviceDetailVo extends TjDeviceDetail {

    @Excel(name = "状态名称")
    private String statusName;
    @Excel(name = "支持角色")
    private String supportRolesName;

    private boolean selected;
}
