package net.wanji.business.exercise.dto.jidaevaluation.network;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-07-09 21:18
 */
@Data
public class NetworkData {
    private String name = "大道";

    private RegionalWeight regionalWeight;

    private ProjectWeight projectWeight;
}
