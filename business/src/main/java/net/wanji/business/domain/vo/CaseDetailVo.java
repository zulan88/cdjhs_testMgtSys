package net.wanji.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.wanji.business.domain.PartConfigSelect;
import net.wanji.business.entity.TjCasePartConfig;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: guanyuduo
 * @date: 2023/10/19 19:17
 * @descriptoin:
 */
@Data
public class CaseDetailVo extends CaseSceneVo {
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("所属文件夹")
    private Integer treeId;

    @ApiModelProperty("用例编号")
    private String caseNumber;

    @ApiModelProperty("测试说明")
    private String testTarget;

    @ApiModelProperty("用例状态")
    private String status;

    @ApiModelProperty("场景轨迹文件（复制）")
    private String routeFile;

    @JsonIgnore
    @ApiModelProperty("用例点位配置及校验信息")
    private String detailInfo;

    @ApiModelProperty("用例状态名称")
    private String statusName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("修改日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;

    @ApiModelProperty("角色配置")
    @JsonIgnore
    private List<TjCasePartConfig> partConfigs;

    @ApiModelProperty("角色配置选择")
    private List<PartConfigSelect> partConfigSelects;
}
