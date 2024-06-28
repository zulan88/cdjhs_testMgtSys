package net.wanji.business.exercise.dto.evaluation;

import lombok.Data;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-06-27 9:12 上午
 */
@Data
public class EvaluationOutputResult {
    private Long taskId;

    private Double score;

    private Double avgSpeed;

    private List<SceneDetail> details;
}
