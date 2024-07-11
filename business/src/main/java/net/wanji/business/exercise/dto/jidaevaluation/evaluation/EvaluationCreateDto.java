package net.wanji.business.exercise.dto.jidaevaluation.evaluation;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-07-09 22:43
 */
@Data
public class EvaluationCreateDto {
    private String timestamp;

    private EvaluationCreateData data;
}
