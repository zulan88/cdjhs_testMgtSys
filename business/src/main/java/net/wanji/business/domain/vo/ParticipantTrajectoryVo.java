package net.wanji.business.domain.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParticipantTrajectoryVo {

    /**
     * 参与者ID
     */
    private String id;

    private List<TrajectoryDetailVo> trajectory;

    public void addtrajectory(TrajectoryDetailVo detailVo){
        if(trajectory==null){
            trajectory = new ArrayList<>();
        }
        trajectory.add(detailVo);
    }

}
