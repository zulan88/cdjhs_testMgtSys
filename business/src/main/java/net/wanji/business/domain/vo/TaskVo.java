package net.wanji.business.domain.vo;

import java.util.List;

import lombok.Data;
import net.wanji.business.domain.PartConfigSelect;

/**
 * @author: guowenhao
 * @date: 2023/8/31 19:16
 * @description:
 */
@Data
public class TaskVo {

    private String taskName;

    private Integer caseCount;

    private String caseIds;

    private List<PartConfigSelect> dataConfigs;

}
