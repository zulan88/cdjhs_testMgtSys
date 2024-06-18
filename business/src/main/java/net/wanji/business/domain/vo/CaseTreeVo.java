package net.wanji.business.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import net.wanji.business.entity.TjCaseTree;

/**
 * @author: guanyuduo
 * @date: 2023/10/19 13:40
 * @descriptoin:
 */
@ApiModel("测试用例树展示实体")
@Getter
@Setter
public class CaseTreeVo {

    private Integer id;

    private String type;

    private String name;

    private Integer parentId;

    private Integer number;

    public CaseTreeVo(TjCaseTree caseTree) {
        this.id = caseTree.getId();
        this.type = caseTree.getType();
        this.name = caseTree.getName();
        this.parentId = caseTree.getParentId();
    }
}
