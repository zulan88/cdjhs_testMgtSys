package net.wanji.business.exercise.dto.jidaevaluation.trajectory;

import lombok.Data;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-07-09 22:52
 */
@Data
public class RealTimeTrajectory {
    private String source = "auto";

    private String eventId;

    private Long simuTime;

    private List<RealTimeParticipant> data;

}
