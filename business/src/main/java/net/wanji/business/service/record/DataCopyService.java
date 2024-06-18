package net.wanji.business.service.record;

import oshi.annotation.concurrent.NotThreadSafe;

/**
 * @author hcy
 * @version 1.0
 * @interfaceName DataCopyService
 * @description TODO
 * @date 2024/4/15 8:48
 **/
@NotThreadSafe
public interface DataCopyService {
  /**
   * 逐条拷贝
   *
   * @param data
   */
  void data(String data);

  /**
   * 拷贝进度，百分比
   *
   * @param progress
   */
  void progress(int progress);

  /**
   * 数据拷贝完成
   *
   * @param e 拷贝过程中未出现异常，这里是null
   */
  void stop(Exception e);
}
