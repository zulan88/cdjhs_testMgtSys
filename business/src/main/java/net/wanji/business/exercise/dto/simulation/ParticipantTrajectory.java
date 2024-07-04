package net.wanji.business.exercise.dto.simulation;

import lombok.Data;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-07-02 10:02 上午
 */
@Data
public class ParticipantTrajectory {
    //参与者ID
    private String id;

    //参与者模型（1-小客车；2-大货车；3-大巴车；4-行人；5-自行车）
    private Integer model;

    //参与者名称
    private String name;

    //角色
    //（AV：av；MV-实车：mvReal；MV-仿真车：mvSimulation；SP(行人)：sp）
    private String role;

    private List<TrajectoryPoint> trajectory;

}
