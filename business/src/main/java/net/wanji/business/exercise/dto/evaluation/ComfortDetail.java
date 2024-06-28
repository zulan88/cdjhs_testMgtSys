package net.wanji.business.exercise.dto.evaluation;

import lombok.Data;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-06-27 9:24 上午
 */
@Data
public class ComfortDetail {
    private int rapidAcceleration;

    private int rapidDeceleration;

    private int steeringWheel;

    private List<IndexDetail> comfortIndexDetails;
}
