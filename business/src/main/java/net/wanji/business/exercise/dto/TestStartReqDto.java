package net.wanji.business.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jenny
 * @create: 2024-06-25 9:14 上午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestStartReqDto {
    //任务控制-任务启停
    private Integer type;

    private Long timestamp;

    private TestStartParams params;
}
