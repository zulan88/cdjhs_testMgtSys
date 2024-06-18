package net.wanji.business.entity.evaluation.total;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import net.wanji.business.enumeration.EvaluationItemType;

/**
 * @author hcy
 * @version 1.0
 * @className TjCaseSceneItemScore
 * @description TODO
 * @date 2024/3/27 14:05
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("tj_case_scene_item_score")
public class TjCaseSceneItemScore {
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;
  /**
   * 场景评分ID
   */
  @TableField("case_scene_score")
  private int caseSceneScoreId;
  /**
   * 指标：任务完成、任务耗时
   */
  @TableField("index")
  private String index;
  /**
   * 30/30
   */
  @TableField("score")
  private String score;
  /**
   * a、J
   */
  @TableField("type")
  private String type;
  /**
   * 超出时长：20
   */
  @TableField("time")
  private Integer time;
  /**
   * efficiency:效率，comfortable：舒适，safe：安全
   */
  @TableField("item_type")
  private EvaluationItemType itemType;
}
