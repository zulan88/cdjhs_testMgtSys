package net.wanji.business.exercise.dto.evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-06-27 9:07 上午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluationOutputReq {
    private Long taskId;

    private String fusionFilePath;

    private List<StartPoint> startPoints;

    private String signFilePath;

    private String mainChannel;

    private Integer pointsNum;
}
