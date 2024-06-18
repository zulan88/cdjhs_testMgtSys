package net.wanji.business.entity.evaluation.total;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author hcy
 * @version 1.0
 * @className TjCaseSceneScore
 * @description TODO
 * @date 2024/3/27 14:05
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("tj_case_scene_score")
public class TjCaseSceneScore {

  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
  /**
   * 用例评分id
   */
  @TableField("case_score_id")
  private int caseScoreId;
  /**
   * 场景id
   */
  @TableField("sence_id")
  private int senceID;
  /**
   * 任务是否完成 1:已完成 0:未完成
   */
  @TableField("mission_accomplish")
  private int missionAccomplish;
  /**
   * 场景得分
   */
  @TableField("sense_score")
  private int senseScore;
  /**
   * 场景总分
   */
  @TableField("sense_agg_score")
  private int senseAggScore;
  /**
   * 场景权重
   */
  @TableField("sense_weight")
  private int senseWeight;

  /**
   * 仅用于json转换
   * 详细得分
   */
  private Map<String, List<TjCaseSceneItemScore>> info;
}
