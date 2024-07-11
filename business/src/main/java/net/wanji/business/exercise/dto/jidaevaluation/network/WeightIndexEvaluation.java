package net.wanji.business.exercise.dto.jidaevaluation.network;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-07-09 21:29
 */
@Data
public class WeightIndexEvaluation {
    private Integer id;

    private Integer weights;

    private String name;

    private String indicatorName;

    private String calculationFormula;

    private String indicatorDescription;
}
