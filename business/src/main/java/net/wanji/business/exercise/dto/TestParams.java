package net.wanji.business.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-06-23 10:13 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestParams {
    private String imageId;

    private List<ParticipantTrajectory> participantTrajectories;
}
