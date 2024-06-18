package net.wanji.business.domain.dto;

import lombok.Data;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/29 15:04
 * @Descriptoin:
 */
@Data
public class CaseConfigDto {

    private Integer caseId;

    private int avNum;

    private int simulationNum;

    private int pedestrianNum;

    private int otherNum;
}
