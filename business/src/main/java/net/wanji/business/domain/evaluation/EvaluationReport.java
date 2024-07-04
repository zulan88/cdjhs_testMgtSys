package net.wanji.business.domain.evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.wanji.business.domain.CdjhsExerciseRecord;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-06-27 9:27 上午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluationReport {
    private Long taskId;

    private CdjhsExerciseRecord taskDetails;

    private Double score;

    private Double avgSpeed;

    private List<SceneInfo> sceneDetails;

    private ActionAnalysis actionAnalysis;

    private TestOverview testOverview;
}
