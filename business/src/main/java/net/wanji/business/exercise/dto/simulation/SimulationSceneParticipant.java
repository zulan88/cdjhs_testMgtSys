package net.wanji.business.exercise.dto.simulation;

import lombok.Data;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-07-02 10:00 上午
 */
@Data
public class SimulationSceneParticipant {
    //用例id 对应长安大学项目中的场景id
    private Integer caseId;

    //场景分类
    private String type;

    //主车通过场景时长（单位0.1s）
    private Integer avPassTime;

    private List<ParticipantTrajectory> participantTrajectories;
}
