package net.wanji.approve.entity.vo;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

@Data
public class DeviceListVo {

    String type;

    List<TjDeviceDetailVo> tjDeviceDetailVos;

    Integer caseCount;

    Integer maxDeviceCount;

}
