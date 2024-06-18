package net.wanji.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: guanyuduo
 * @date: 2023/10/19 19:31
 * @descriptoin:
 */
@Data
public class CaseSceneVo {

    @ApiModelProperty("场景主键")
    private Integer sceneDetailId;

    @ApiModelProperty("场景编号")
    private String sceneNumber;

    @ApiModelProperty("示意图")
    private String imgUrl;

    @ApiModelProperty("场景描述")
    private String testSceneDesc;

    @JsonIgnore
    private String label;

    @ApiModelProperty("场景标签")
    private Object labelDetail;

    @JsonIgnore
    @ApiModelProperty("场景点位配置信息")
    private String trajectoryInfo;

}
