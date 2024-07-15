package net.wanji.business.exercise.dto.evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jenny
 * @create: 2024-06-27 9:08 上午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartPoint {
    private Integer sequence;

    private String name;

    private Double longitude;

    private Double latitude;
}
