package net.wanji.business.domain.vo.task.infinity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hcy
 * @version 1.0
 * @className ShardingResultVo
 * @description TODO
 * @date 2024/3/21 16:07
 **/
@Data
public class ShardingResultVo {
  @ApiModelProperty(value = "途径次数",
      required = true,
      dataType = "Integer",
      position = 1)
  private Integer time;
  @ApiModelProperty(value = "场地切片名称",
      required = true,
      dataType = "String",
      position = 2)
  private String shardingName;
  @ApiModelProperty(value = "得分，50/100",
      required = true,
      dataType = "String",
      position = 3)
  private String evaluationScore;

  /**
   * 分片ID
   */
  @JsonIgnore
  private Integer shardingId;

  /**
   * 分片交互状态，0：出，1：进
   */
  @JsonIgnore
  private int state;
}
