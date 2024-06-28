package net.wanji.business.exercise.dto.strategy;

import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * @author glace
 * @version 1.0
 * @className StrategyEntity
 * @description TODO
 * @date 2023/9/19 13:54
 **/
@Data
public class Strategy implements Serializable {

  private static final long serialVersionUID = -3015265078206938512L;

  /**
   * 数据来源channel
   */
  private Set<DeviceConnInfo> sourceDevicesInfo = Collections.synchronizedSet(
      new HashSet<>());
  /**
   * 数据（dataChannel）与执行规则的对应关系
   * (dataChannel,List(Map(ruleId, standard)))
   */
  private Map<String, Map<Integer, Object>> channelRuleMap = new HashMap<>();

  /**
   * 基准数据控制频道
   */
  private String benchmarkDataChannel;

}
