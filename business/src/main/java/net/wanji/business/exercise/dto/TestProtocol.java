package net.wanji.business.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jenny
 * @create: 2024-06-25 9:26 上午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestProtocol {
    private Integer type;

    private String channel;
}
