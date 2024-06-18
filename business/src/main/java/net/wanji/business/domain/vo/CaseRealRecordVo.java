package net.wanji.business.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.wanji.business.entity.TjCaseRealRecord;

/**
 * @author: guanyuduo
 * @date: 2023/11/27 17:26
 * @descriptoin:
 */
@ApiModel("实车试验测试记录")
@Data
public class CaseRealRecordVo extends TjCaseRealRecord {

    @ApiModelProperty("记录状态名称")
    private String statusName;

    @ApiModelProperty("秒数")
    private int seconds;
}
