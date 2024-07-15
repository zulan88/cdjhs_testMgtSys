package net.wanji.business.exercise.dto.evaluation;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-07-14 14:24
 */
@Data
public class SceneSitePoint {
    private Integer sequence;

    private ScenePos startPoint;

    private ScenePos endPoint;
}
