package net.wanji.business.domain;

import lombok.Data;

import java.util.List;

@Data
public class InElement {

    Long id;

    String name;

    Integer expectedSpeed;

    // 0-被测对象，1-其他对象
    Integer type;

    int carType=0;

    List<SitePoint> route;

}
