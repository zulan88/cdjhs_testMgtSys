package net.wanji.business.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author hcy
 * @version 1.0
 * @className RecordSimulationTrajectoryDto
 * @description TODO
 * @date 2024/4/3 17:57
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordSimulationTrajectoryDto {
  private String role;
  private String source;
  private Long timestamp;
  private String timestampType;
  private Object value;
}
