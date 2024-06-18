package net.wanji.business.enumeration;

/**
 * @author hcy
 * @version 1.0
 * @className EvaluationItemType
 * @description TODO
 * @date 2024/3/27 17:29
 **/
public enum EvaluationItemType {
  /**
   * 效率
   */
  EFFICIENCY("efficiency"),

  /**
   * 舒适
   */
  COMFORTABLE("comfortable"),

  /**
   * 安全
   */
  SAFE("safe");

  private String type;

  EvaluationItemType(String type) {
    this.type = type;
  }
}
