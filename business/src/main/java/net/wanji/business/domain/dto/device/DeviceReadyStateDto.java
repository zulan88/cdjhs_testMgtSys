package net.wanji.business.domain.dto.device;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author glace
 * @version 1.0
 * @className DeviceReadyStateDto
 * @description TODO
 * @date 2023/10/7 9:32
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceReadyStateDto extends DeviceStateDto{
}
