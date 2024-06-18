package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.domain.bo.CaseTrajectoryDetailBo;
import net.wanji.business.entity.TjCase;

import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/28 13:11
 * @Descriptoin:
 */
@Data
public class CaseSimulationDetailVo extends TjCase {

    private String sceneNumber;

    private String resourceName;

    private List<String> labelList;

    private CaseTrajectoryDetailBo caseDetail;

}
