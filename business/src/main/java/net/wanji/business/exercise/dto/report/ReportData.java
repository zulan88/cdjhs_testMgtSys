package net.wanji.business.exercise.dto.report;

import lombok.Data;

/**
 * @author hcy
 * @version 1.0
 * @className ReportData
 * @description TODO
 * @date 2023/10/31 18:05
 **/
@Data
public class ReportData {
  private String type;
  private ReportTrajectory value;
}
