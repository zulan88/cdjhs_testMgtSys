package net.wanji.business.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.wanji.business.domain.SitePoint;
import net.wanji.business.domain.vo.CaseContinuousVo;

import java.util.List;

/**
 * @author: guanyuduo
 * @date: 2023/11/14 15:10
 * @descriptoin:
 */
@ApiModel(value = "RoutingPlanDto", description = "路径规划参数")
@Data
public class RoutingPlanDto {

    @ApiModelProperty("任务ID")
    private Integer taskId;

    @ApiModelProperty("连续性配置信息")
    private List<CaseContinuousVo> cases;

    private String mapId;

    private SitePoint allStartPoint;
}
