package net.wanji.business.exercise.dto.jidaevaluation.network;

import lombok.Data;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-07-09 21:20
 */
@Data
public class ProjectWeight {
    private List<WeightIndexEvaluation> EvaluationSafeWeights;

    private List<WeightIndexEvaluation> EvaluationClassWeights;

    private List<WeightIndexEvaluation> EvaluationComfortWeights;

    private List<WeightIndexEvaluation> EvaluationEfficiencyWeights;
}
