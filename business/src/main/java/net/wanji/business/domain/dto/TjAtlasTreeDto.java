package net.wanji.business.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.wanji.business.common.Constants.DeleteGroup;
import net.wanji.business.common.Constants.InsertGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 地图库模块地图分组dto
 */
@Data
@ApiModel("地图树信息")
public class TjAtlasTreeDto {

    @ApiModelProperty(value = "id，修改时传")
    @NotNull(message = "请选择一个节点", groups = {DeleteGroup.class})
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称，查询可模糊查询",required = true)
    @NotBlank(message = "名称不可为空", groups = { InsertGroup.class })
    private String name;

    private String type;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 自定义字段1
     */
    private String attribute1;

    /**
     * 自定义字段2
     */
    private String attribute2;

    /**
     * 自定义字段3
     */
    private String attribute3;

    /**
     * 自定义字段4
     */
    private String attribute4;

    /**
     * 自定义字段5
     */
    private String attribute5;

}
