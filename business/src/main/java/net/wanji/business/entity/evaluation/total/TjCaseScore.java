package net.wanji.business.entity.evaluation.total;

/**
 * @author hcy
 * @version 1.0
 * @className TjCaseScore
 * @description TODO
 * @date 2024/3/27 14:05
 **/

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("tj_case_score")
public class TjCaseScore {
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
  /**
   * 任务ID
   */
  @TableField("task_id")
  private int taskId;
  /**
   * 场景ID
   */
  @TableField("case_id")
  private int caseId;
  /**
   * 测试类型
   */
  @TableField("test_type")
  private int testType;
  /**
   * 测试记录ID
   */
  @TableField("record_id")
  private int recordId;
  /**
   * 测试开始时间戳
   */
  @TableField("start_time")
  private int startTime;
  /**
   * 测试结束时间戳
   */
  @TableField("end_time")
  private int endTime;
  /**
   * 测试总时长,单位秒
   */
  @TableField("test_duration")
  private int testDuration;
  /**
   * 测试危险总时长占比 例子52%
   */
  @TableField("danger_time_proportion")
  private float dangerTimeProportion;
  /**
   * 场景总得分
   */
  @TableField("all_sense_score")
  private float allSenseScore;

  /**
   * 仅用于json转换
   * 详细得分
   */
  @TableField(exist = false)
  private List<TjCaseSceneScore> testScene;
}
