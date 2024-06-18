package net.wanji.business.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class TjCasePartRoleVo {

    String type;

    List<CasePartConfigVo> casePartConfigs;

    public List<CasePartConfigVo> getCasePartConfigs() {
        if (casePartConfigs == null) {
            casePartConfigs = new java.util.ArrayList<>();
        }
        return casePartConfigs;
    }
}
