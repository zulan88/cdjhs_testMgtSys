package net.wanji.business.domain;

import lombok.Data;

@Data
public class WoPostion {

    public WoPostion(String id, String x, String y, String h, Integer type) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.h = h;
        this.type = type;
    }

    public WoPostion(){}

    String id;

    String x;

    String y;

    String h;

    Integer type;

    String xTarget;

    String yTarget;

    String time;

}
