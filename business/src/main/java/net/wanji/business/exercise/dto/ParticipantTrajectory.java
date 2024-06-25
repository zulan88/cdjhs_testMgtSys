package net.wanji.business.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.wanji.common.common.TrajectoryValueDto;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-06-23 10:20 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantTrajectory {
    private Long timestamp;

    //时间戳类型
    private String timestampType;

    private List<TrajectoryValueDto> value;
}
