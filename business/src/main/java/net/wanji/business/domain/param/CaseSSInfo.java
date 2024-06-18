package net.wanji.business.domain.param;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author: guanyuduo
 * @date: 2023/11/15 18:10
 * @descriptoin: 用例起止点信息
 */
@Data
public class CaseSSInfo {
    private Integer caseId;
    private Integer shardingId;
    private List<Map<String, Object>> trajectoryPoints;
}
