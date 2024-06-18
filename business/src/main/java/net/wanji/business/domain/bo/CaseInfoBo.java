package net.wanji.business.domain.bo;

import lombok.Data;
import net.wanji.business.entity.TjCase;
import net.wanji.business.entity.TjCaseRealRecord;

import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/24 13:36
 * @Descriptoin:
 */
@Data
public class CaseInfoBo extends TjCase {

    private String label;

    private String allStageLabel;

    private String filePath;

    private String geoJsonPath;

    private TjCaseRealRecord caseRealRecord;

    private List<CaseConfigBo> caseConfigs;

    private String number;
}
