package net.wanji.approve.entity.vo;

import lombok.Data;
import net.wanji.approve.entity.TjDeviceDetail;

@Data
public class TjDeviceDetailVo extends TjDeviceDetail {

    /**
     * 占用状态
     */
    Integer occstatus;

    Integer isSelect;

}
