package net.wanji.business.domain;

import lombok.Data;
import net.wanji.business.domain.vo.CasePartConfigVo;
import net.wanji.common.core.domain.SimpleSelect;

import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/18 16:56
 * @Descriptoin:
 */
@Data
public class PartConfigSelect extends SimpleSelect {

    private List<CasePartConfigVo> parts;

}
