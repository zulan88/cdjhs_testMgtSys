package net.wanji.business.entity.infity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hcy
 * @version 1.0
 * @className TjShardingChangeRecord
 * @description TODO
 * @date 2024/3/12 18:16
 **/

@TableName(value = "tj_sharding_change_record")
@Data
public class TjShardingChangeRecord implements Serializable {
  private static final long serialVersionUID = -216298400435835539L;

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  /**
   * 任务ID
   */
  @TableField("task_id")
  private Integer taskId;

  /**
   * 场景ID
   */
  @TableField("case_id")
  private Integer caseId;

  /**
   * 参与者ID
   */
  @TableField("participant_id")
  private String participantId;

  /**
   * 参与者名称
   */
  @TableField("participant_name")
  private String participantName;

  /**
   * 分片ID
   */
  @TableField("sharding_id")
  private Integer shardingId;

  /**
   * 分片交互状态，0：出，1：进
   */
  @TableField("state")
  private int state;

  @TableField("create_timestamp")
  private Long createTimestamp;

  /**
   * 测试记录ID
   */
  @TableField("record_id")
  private Integer recordId;

  @TableField("evaluation_score")
  private String evaluationScore;
}
