package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.entity.TjCaseOp;

import java.util.List;

@Data
public class CaseOpVo {

    List<TjCaseOp> caseOpList;

    Integer nowMapId;

}
