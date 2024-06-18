package net.wanji.business.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hcy
 * @version 1.0
 * @className TjTessngShardingChangeDto
 * @description TODO
 * @date 2024/4/1 14:42
 **/
@Data
public class TjTessngShardingChangeDto implements Serializable {

  private static final long serialVersionUID = 3703299697098543921L;
  private Integer taskId;
  private Integer caseId;
  /**
   * 历史记录ID
   */
  private Integer recordId;
  /**
   * 参与者id
   */
  private Integer participantId;
  /**
   * 参与者名称
   */
  private String participantName;
  /**
   * 分片ID
   */
  private Integer shardingId;
  /**
   * 0：出，1：进入
   */
  private Integer state;

  private Long timestamp = System.currentTimeMillis();
  private Integer type = 4;
  private String username;

  public void setParticipantId(String participantId) {
    this.participantId = Integer.parseInt(participantId);
  }
}
