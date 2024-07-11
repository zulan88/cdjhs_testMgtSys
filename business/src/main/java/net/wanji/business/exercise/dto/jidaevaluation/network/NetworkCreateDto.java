package net.wanji.business.exercise.dto.jidaevaluation.network;

import lombok.Data;
import net.wanji.business.exercise.dto.jidaevaluation.network.NetworkData;

/**
 * @author: jenny
 * @create: 2024-07-09 21:15
 */
@Data
public class NetworkCreateDto {
    private String timestamp;

    private NetworkData data;
}
