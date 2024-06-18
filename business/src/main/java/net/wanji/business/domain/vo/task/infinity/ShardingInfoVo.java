package net.wanji.business.domain.vo.task.infinity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * @author hcy
 * @version 1.0
 * @className ShardingInfoVo
 * @description TODO
 * @date 2024/3/14 14:09
 **/
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShardingInfoVo {
  @ApiModelProperty(value = "分片ID", required = true)
  @NotNull(message = "分片ID")
  private Integer id;
  @ApiModelProperty(value = "分片点位", required = true)
  @NotNull(message = "分片点位")
  private List<Point2D.Double> points;
}
