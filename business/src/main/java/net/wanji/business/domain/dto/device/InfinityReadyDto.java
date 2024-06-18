package net.wanji.business.domain.dto.device;

import lombok.Data;
import net.wanji.business.domain.SiteSlice;
import net.wanji.business.domain.TrafficFlow;
import net.wanji.business.domain.TrafficFlowConfig;

import java.util.List;

/**
 * @author hcy
 * @version 1.0
 * @className InfinityReadyDto
 * @description TODO
 * @date 2024/3/15 13:30
 **/
@Data
public class InfinityReadyDto {
  List<TrafficFlow> trafficFlows;

  List<SiteSlice> siteSlices;

  List<TrafficFlowConfig> trafficFlowConfigs;
}
