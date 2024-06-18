package net.wanji.business.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hcy
 * @version 1.0
 * @className TessngEvaluateDto
 * @description TODO
 * @date 2024/3/20 15:45
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TessngEvaluateDto {
  /**
   * 参与者ID
   */
  private Integer id;
  /**
   * 参与者名称
   */
  private String name;
  /**
   * 是否需要评价
   * （0：否,1：是）
   */
  private int evaluate;

  /**
   * 设备id
   */
  @JsonIgnore
  private transient int deviceId;

}
