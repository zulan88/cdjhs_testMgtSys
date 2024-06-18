package net.wanji.business.entity.evaluation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author hcy
 * @version 1.0
 * @className TessngEvaluationState
 * @description TODO
 * @date 2024/6/5 13:11
 **/
@Data
@TableName("evaluationState")
public class TessngEvaluationState {
  @TableId(value = "taskID_main", type = IdType.AUTO)
  private Integer id;

  @TableField("taskID")
  private Integer taskID;

  @TableField("value")
  private String value;

  // enum('实车验证','离线轨迹','无限里程')
  @TableField("taskType")
  private String taskType = "实车验证";

  @TableField("created_time")
  private Date created_time;

  @TableField("updated_time")
  private Date updated_time;
  /**
   * init：未绑定权重方案,new ：排队待评价,evaluating：评价中,evaluated：评价完成,error：评价异常
   */
  @TableField("currentState")
  private String currentState;

  @TableField("evaluationResult")
  private String evaluationResult;

  @TableField("isDelete")
  private Boolean isDelete;
}
