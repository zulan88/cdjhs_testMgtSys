package net.wanji.business.domain.evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jenny
 * @create: 2024-06-27 9:35 上午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SceneInfo {
    private Integer sequence;

    private String sceneCode;

    private String sceneCategory;

    private String duration;

    private Double securityScore;

    private Double efficencyScore;

    private Double comfortScore;

    private Double sceneScore;
}
