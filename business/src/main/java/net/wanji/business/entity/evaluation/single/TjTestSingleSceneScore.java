package net.wanji.business.entity.evaluation.single;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author hcy
 * @version 1.0
 * @className TjTestSingleSceneIScore
 * @description 单个场景评分
 * @date 2024/4/22 14:21
 **/
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("tj_test_single_scene_score")
public class TjTestSingleSceneScore {
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
  /**
   * 就是你们传过来的taskId基于任务
   */
  @JsonProperty("taskID")
  @TableField("task_id")
  private Integer taskId;
  /**
   * 场景id
   */
  @JsonProperty("senceID")
  @TableField("case_id")
  private Integer caseId;
  /**
   * 测试类型：0：单用例测试（实车试验）；1：连续性场景测试（多场景任务一次启停）；2：批量测试（n场景任务n次启停；）3：无限里程
   */
  @TableField("test_type")
  private Integer testType;
  /**
   * 测试记录ID
   */
  @TableField("record_id")
  private Integer recordId;
  /**
   * 对应分片/场景记录的评价分数
   */
  @TableField("evaluative_id")
  private Integer evaluativeId;
  /**
   * 分片ID
   */
  @TableField("slice_id")
  private Integer sliceId;
  /**
   * 1:已完成 0:测试中
   */
  @TableField("scene_status")
  private Integer senceStatus;
  /**
   * 当senceStatus为1时发送得分情况,例:50/100
   */
  @TableField("scene_score")
  private String senceScore;
  /**
   * 安全性总分
   */
  @TableField("safe_score")
  private Integer safeScore;
  /**
   * 安全性扣分项，没有扣分发送0
   */
  @JsonProperty("TTC")
  @TableField("safe_minus_score")
  private Integer safeMinusScore;
  /**
   * 前向碰撞时间(TTC)
   */
  @TableField("tcc")
  private Integer TTC;
  /**
   * 当前全程的平均速度
   */
  @TableField("avg_speed_all")
  private Float avgSpeedAll;
  /**
   * 从当前场景开始的平均速度
   */
  @TableField("avg_speed")
  private Float avgSpeed;
  /**
   * 效率性总分
   */
  @TableField("efficiency_score")
  private Integer efficiencyScore;
  /**
   * 效率性扣分项，没有扣分发送0
   */
  @TableField("efficiency_minus_score")
  private Integer efficiencyMinusScore;
  /**
   * 任务耗时
   */
  @TableField("task_already_time")
  private Integer taskAlreadyTime;
  /**
   * 期望耗时
   */
  @TableField("task_time")
  private Integer taskTime;
  /**
   * 舒适性总分
   */
  @TableField("comfortable_score")
  private Integer comfortableScore;
  /**
   * 舒适性扣分项，没有扣分发送0
   */
  @TableField("comfortable_minus_score")
  private Integer comfortableMinusScore;
  /**
   * 纵向加速度
   */
  @TableField("vertical_a")
  private Float verticalA;
  /**
   * 纵向加加速度
   */
  @TableField("vertical_a_plus")
  private Float verticalAPlus;
  /**
   * 横向加速度
   */
  @TableField("horizontal_a")
  private Float horizontalA;
  /**
   * 横向加加速度
   */
  @TableField("horizontal_a_plus")
  private Float horizontalAPlus;
  /**
   * 转弯加速度
   */
  @TableField("turn_a")
  private Float turnA;
  /**
   * 转弯加加速度
   */
  @TableField("turn_a_plus")
  private Float turnAPlus;
  /**
   * 也是你们传过来的
   */
  @TableField("av_name")
  private String avName;
}
