package net.wanji.business.domain.vo.task.infinity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author hcy
 * @version 1.0
 * @className InfinityTaskInitVo
 * @description TODO
 * @date 2024/3/14 10:49
 **/
@ApiModel
@Data
public class InfinityTaskInitVo {
  @ApiModelProperty(value = "场景ID", required = true)
  private List<ShardingInfoVo> shardingInfos;
  
  @ApiModelProperty(value = "任务运行状态，true：运行中，false：未运行",
      required = true)
  private boolean running;
}

