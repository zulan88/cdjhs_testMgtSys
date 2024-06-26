package net.wanji.business.exercise.dto.report;

import lombok.Data;

/**
 * @author hcy
 * @version 1.0
 * @className LLPoint
 * @description 经纬度
 * @date 2023/11/29 9:58
 **/
@Data
public class LLPoint {
  /**
   * 分辨率1e-7°，东经为正，西经为负
   */
  private double longitude;
  /**
   * 分辨率1e-7°，北纬为正，南纬为负
   */
  private double latitude;

}
