package net.wanji.business.domain.evaluation;

import lombok.Data;

import java.util.Map;

/**
 * @author: jenny
 * @create: 2024-06-27 2:58 下午
 */
@Data
public class SecurityAnalysis {
    private int collapse;

    private int infraction;

    private Map<Integer, Long> stats;
}
