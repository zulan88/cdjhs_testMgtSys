package net.wanji.business.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jenny
 * @create: 2024-06-23 10:12 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestReqDto {
    private Long timestamp;

    private Integer type;

    private TestParams params;
}
