package net.wanji.business.common;

/**
 * @author hcy
 * @version 1.0
 * @enumName DeviceStatus
 * @description TODO
 * @date 2024/3/14 14:33
 **/
public enum DeviceStatus {
  OFFLINE(0, "离线"),
  ONLINE(1, "在线"),
  ARRIVED(2, "已到达"),
  NOT_ARRIVED(3, "等待到达"),
  BUSY(4, "使用中"),
  IDLE(5, "空闲");

  /**
   * 状态码
   */
  private int status;
  /**
   * 状态名称
   */
  private String name;

  DeviceStatus(int status, String name) {
    this.status = status;
    this.name = name;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
