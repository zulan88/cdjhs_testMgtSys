package net.wanji.business.exercise.dto.strategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hcy
 * @version 1.0
 * @className TaskStrategy
 * @description TODO
 * @date 2023/10/23 15:02
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CaseStrategy implements Serializable {

  private static final long serialVersionUID = -3370103674701873596L;

  /**
   * 任务ID
   */
  private int taskId;
  /**
   * 场景ID
   */
  private int caseId;
  /**
   * 0:结束，1：开始
   */
  private Integer state;

  private Strategy strategy;
  /**
   * 任务是否结束
   */
  private boolean taskEnd;
}
