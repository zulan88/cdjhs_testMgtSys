package net.wanji.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.wanji.business.entity.TjCaseRealRecord;

import java.util.List;

/**
 * @author: guanyuduo
 * @date: 2023/10/19 16:18
 * @descriptoin:
 */
@Api("测试用例分页实体类")
@Data
@JsonInclude(value= JsonInclude.Include.USE_DEFAULTS)
public class CasePageVo extends CaseDetailVo {

    @ApiModelProperty("场景分类")
    private String sceneSort;

    @ApiModelProperty("角色配置简述")
    private String roleConfigSort;

    @ApiModelProperty("角色配置详情")
    private Object roleConfigDetail;

    @ApiModelProperty("实车试验测试记录")
    private List<TjCaseRealRecord> caseRealRecords;

    private Boolean selected;

    private String mapFile;

    private Integer mapId;
}
