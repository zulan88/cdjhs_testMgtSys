package net.wanji.business.domain;

import lombok.Data;

import java.util.List;

@Data
public class DeparturePoint {

    Long id;

    String name;

    Float weight;

    List<SitePoint> route;

}
