package net.wanji.business.common;

/**
 * @author hcy
 * @version 1.0
 * @enumName TaskType
 * @description TODO
 * @date 2024/3/11 10:02
 **/
public enum TaskType {
  /**
   * 无线里程
   * 时间为停止条件
   */
  INFINITE,
  /**
   * 连续测试
   * 主车不停，从车启停
   */
  CONTINUOUS,
  /**
   * 批量测试
   * 多测试用例独立运行
   */
  BATCH
}
