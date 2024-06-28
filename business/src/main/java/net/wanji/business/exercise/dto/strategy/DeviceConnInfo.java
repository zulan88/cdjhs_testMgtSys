package net.wanji.business.exercise.dto.strategy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author hcy
 * @version 1.0
 * @className DeviceConnInfo
 * @description TODO
 * @date 2024/1/9 8:51
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceConnInfo implements Serializable {

  private static final long serialVersionUID = -9021972497270928998L;

  private String controlChannel;
  private String channel;
  private String role;

  private Map<String, Object> params;
}
