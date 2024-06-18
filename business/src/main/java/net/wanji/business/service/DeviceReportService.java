package net.wanji.business.service;

/**
 * @author glace
 * @version 1.0
 * @interfaceName DeviceReportService
 * @description TODO
 * @date 2023/10/7 10:10
 **/
public interface DeviceReportService<T> {
  /**
   * 数据处理
   *
   * @param t
   */
  void dataProcess(T t);

}
