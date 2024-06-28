package net.wanji.business.domain.evaluation;

import lombok.Data;

import java.util.Map;

/**
 * @author: jenny
 * @create: 2024-06-27 10:01 上午
 */
@Data
public class TestOverview {
    private Map<Integer, Long> security;

    private Map<Integer, Long> efficency;

    private Map<Integer, Long> comfort;
}
