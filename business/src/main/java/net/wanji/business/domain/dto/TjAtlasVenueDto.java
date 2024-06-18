package net.wanji.business.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.wanji.business.common.Constants.QueryGroup;
import net.wanji.business.common.Constants.InsertGroup;
import net.wanji.business.common.Constants.DeleteGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("测试场地信息")
public class TjAtlasVenueDto {

    @ApiModelProperty(value = "id，修改时传")
    @NotNull(message = "请选择一个节点", groups = {DeleteGroup.class})
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称",required = true)
    @NotBlank(message = "测试场地名称不可为空", groups = {InsertGroup.class})
    private String name;

    /**
     * 地图树id
     */
    @ApiModelProperty(value = "地图分组id",required = true)
    @NotNull(message = "请选择地图分组", groups = {InsertGroup.class, QueryGroup.class})
    private Integer treeId;

    /**
     * 是否支持实地测试（0：不支持；1支持）
     */
    @ApiModelProperty(value = "是否支持实地测试（0：不支持；1支持）",required = true)
    @NotNull(message = "请选择是否支持实地测试", groups = {InsertGroup.class})
    private Integer isField;

    @NotNull(message = "请选择场景类型", groups = {InsertGroup.class})
    private Integer sceneType;

    /**
     * geoJson文件地址
     */
    @ApiModelProperty(value = "geoJson文件路径")
    private String geoJsonPath;

    /**
     * openDrive文件地址
     */
    @ApiModelProperty(value = "openDrive文件路径")
    private String openDrivePath;

    /**
     * 场地实景图片地址
     */
    @ApiModelProperty(value = "场地实景图片文件路径")
    private String fieldImgPath;

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

}
