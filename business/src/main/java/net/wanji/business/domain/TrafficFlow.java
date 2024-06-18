package net.wanji.business.domain;

import lombok.Data;

import java.util.List;

@Data
public class TrafficFlow {

    Integer id;

    String name;

    SitePoint startPoint;

    //类型 0-机动车 1-非机动车
    Integer  type;

    //最大车流量
    Integer  maxFlow;

    //最小车流量
    Integer  minFlow;

    //周期(h)
    Double  cycle;

    //梯度
    Integer gradient;

    //小客车权重
    Float smallCarWeight;

    //大客车权重
    Float bigCarWeight;

    List<DeparturePoint> departurePoints;

}
