package net.wanji.business.domain;

import lombok.Data;

import java.util.List;

@Data
public class InfiniteTessParm {

    List<InElement> inElements;

    List<TrafficFlow> trafficFlows;

    List<SiteSlice> siteSlices;

    List<TrafficFlowConfig> trafficFlowConfigs;

    int taskId;

    Long planTestTime;

}
