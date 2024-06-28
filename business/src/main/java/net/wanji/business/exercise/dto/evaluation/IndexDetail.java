package net.wanji.business.exercise.dto.evaluation;

import lombok.Data;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-06-27 9:21 上午
 */
@Data
public class IndexDetail {
    //安全指标类型
    private Integer index;

    private Integer deductPoints;

    private Integer duration;

    private Integer count;

    private List<TrendChange> trendOfChange;
}
