package net.wanji.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.wanji.business.entity.TjTaskCase;

import java.util.Date;

/**
 * @author: guowenhao
 * @date: 2023/8/31 17:50
 * @description: 测试任务-用例页面使用
 */
@ApiModel(value = "测试任务-用例展示实体类", description = "测试任务-用例页面使用")
@Data
public class TaskCaseVo extends TjTaskCase {

    /**
     * 排期日期
     */
    @ApiModelProperty(value = "排期日期", required = true, dataType = "Date", example = "2023-05-25", position = 1)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date planDate;

    /**
     * 用例编号
     */
    @ApiModelProperty(value = "用例编号", required = true, dataType = "String", example = "CASE11111", position = 2)
    private String caseNumber;

    /**
     * 关联场景ID
     */
    private Integer sceneDetailId;
    /**
     * 场景分类
     */
    @ApiModelProperty(value = "场景分类", required = true, dataType = "String", example = "场景分类", position = 3)
    private String sceneSort;

    /**
     * 状态名称
     */
    @ApiModelProperty(value = "测试状态名称", required = true, dataType = "String", example = "状态名称", position = 5)
    private String statusName;

    @JsonIgnore
    private String detailInfo;

    /**
     * 角色配置简述
     */
    @ApiModelProperty(value = "角色配置简述", required = true, dataType = "String", example = "角色配置简述", position = 4)
    private String roleConfigSort;

    @ApiModelProperty(value = "所属文件", dataType = "Integer", example = "1", position = 6)
    private Integer treeId;

    private String mapFile;

    private Integer mapId;



}
