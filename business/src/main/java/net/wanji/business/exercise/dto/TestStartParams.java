package net.wanji.business.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-06-25 9:24 上午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestStartParams {
    private Integer taskType;

    private List<TestProtocol> protocols;
}
