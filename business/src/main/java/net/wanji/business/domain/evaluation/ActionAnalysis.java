package net.wanji.business.domain.evaluation;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-06-27 9:56 上午
 */
@Data
public class ActionAnalysis {
    private SecurityAnalysis security;

    private EfficencyAnalysis efficency;

    private ComfortAnalysis comfort;
}
