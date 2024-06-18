package net.wanji.business.evaluation;

import lombok.*;

/**
 * @author hcy
 * @version 1.0
 * @className ContextDto
 * @description TODO
 * @date 2024/3/26 15:23
 **/
@Data
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class EvalContext {
  private final Integer taskId;
  private final Integer caseId;
  /**
   * 测试类型：0：单用例测试（实车试验）；1：连续性场景测试（多场景任务一次启停）；2：批量测试（n场景任务n次启停；）3：无限里程
   */
  private final Integer testType;
  /**
   * 测试记录id
   */
  private final Integer recordId;

  /**
   * websocket 客户端的key
   */
  private String wsClientKey;
}
