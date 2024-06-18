package net.wanji.business.service.record.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hcy
 * @version 1.0
 * @interfaceName ExtendedDataInterface
 * @description 回放非主车数据
 * @date 2024/4/16 14:08
 **/
@Data
@AllArgsConstructor
public class ExtendedDataWrapper<T> {
  /**
   * 数据时间戳
   */
  private Long timestamp;
  /**
   * 回放数据（非json序列化，防止重复序列化）
   */
  private T data;

}
