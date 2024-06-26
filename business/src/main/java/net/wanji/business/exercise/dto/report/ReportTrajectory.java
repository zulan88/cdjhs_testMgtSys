package net.wanji.business.exercise.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @author hcy
 * @version 1.0
 * @className ReportTrajectory
 * @description 设备上报轨迹
 * @date 2023/10/23 10:42
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportTrajectory {
  /**
   * 时间戳类型
   */
  private String timestampType;
  /**
   * 运行时时间戳
   */
  private String timestamp;
  /**
   * 轨迹详情
   */
  private List<ReportCurrentPointInfo> value;
}
