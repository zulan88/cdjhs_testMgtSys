package net.wanji.business.domain.vo;

import lombok.Data;

@Data
public class TrajectoryDetailVo {

    public TrajectoryDetailVo(Long frameId, boolean pass, Double speed, String time){
        this.frameId = frameId;
        this.pass = pass;
        this.speed = speed;
        this.time = time;
    }

    private Long frameId;

    private boolean pass;

    private Double speed;

    private String time;

}
