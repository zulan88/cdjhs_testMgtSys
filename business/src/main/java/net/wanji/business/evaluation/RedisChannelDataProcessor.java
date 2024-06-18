package net.wanji.business.evaluation;

/**
 * @author hcy
 * @version 1.0
 * @className RedisChannelDataProcessor
 * @description TODO
 * @date 2024/3/26 10:32
 **/
public interface RedisChannelDataProcessor {

  /**
   * 订阅信息
   *
   * @param data
   */
  void data(Object data, EvalContext contextDto);

  void data(String channel, Object data, EvalContext contextDto);

  /**
   * 数据id，校验规则
   *
   * @param data
   * @return
   */
  String dataId(Object data);
}
