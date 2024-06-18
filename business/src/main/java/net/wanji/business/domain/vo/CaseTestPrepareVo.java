package net.wanji.business.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author: guanyuduo
 * @date: 2023/10/20 18:01
 * @descriptoin:
 */
@Data
public class CaseTestPrepareVo {
    private Integer id;

    private Integer caseId;

    private String testTypeName;

    private List<String> channels;
}
