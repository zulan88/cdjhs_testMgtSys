package net.wanji.business.exercise.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author hcy
 * @version 1.0
 * @className RestReportCurrentPointInfo
 * @description TODO
 * @date 2023/11/14 16:43
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportCurrentPointInfo {
  /**
   * 当前时间
   */
  private String timestamp;
  /**
   * 当前毫秒级时间戳（13位整数）
   */
  private String globalTimeStamp;
  /**
   * 帧号
   */
  private int frameId;
  /**
   * 参与者id
   */
  private String id;
  /**
   * 参与者名称
   */
  private String name;
  /**
   * 车牌号
   */
  private String picLicense;
  /**
   * 原始颜色
   */
  private int originalColor;
  /**
   * 车辆颜色
   */
  private int vehicleColor;
  /**
   * 车辆类型
   */
  private int vehicleType;
  /**
   * 长度（cm）
   */
  private int length;
  /**
   * 宽度（cm）
   */
  private int width;
  /**
   * 高度（cm）
   */
  private int height;
  /**
   * 驾驶类型
   */
  private int driveType;
  /**
   * 经度
   */
  private double longitude;
  /**
   * 纬度
   */
  private double latitude;
  /**
   * 航向角
   */
  private double courseAngle;
  /**
   * 速度（km/h）
   */
  private int speed;
  /**
   * 纵向加速度，单位：m/s2
   */
  private double lonAcc;
  /**
   * 方向盘转转角（示例15.1deg）,单位：deg
   */
  private double steeringWheelAngle;
  /**
   * 油门踏板开度（示例16.4%），单位：%
   */
  private double acceleratorPedal;
  /**
   * 制动踏板开度（示例10.1%），单位：%
   */
  private double braking;
  /**
   * 5G/V2X_OBU通信状态；（是否正常：0异常；1正常）
   */
  private int obuStatus;
  /**
   * 定位状态（是否正常：0异常；1正常）
   */
  private int locationStatus;
  /**
   * 底盘线控状态（是否正常：0异常；1正常）
   */
  private int chassisStatus;
  /**
   * 自动驾驶状态（是否正常：0异常；1正常）
   */
  private int autoStatus;
  /**
   * 转向灯状态（0：关闭 1：左转向 2：右转向）
   */
  private int indicatorStatus;
  /**
   * 双闪灯状态（0：关闭 1：打开）
   */
  private int blinkerStatus;
  /**
   * 车辆未来规划轨迹信息
   */
  private List<LLPoint> futurePlanList;
}
