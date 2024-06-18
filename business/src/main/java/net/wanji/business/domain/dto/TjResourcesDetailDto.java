package net.wanji.business.domain.dto;

import lombok.Data;
import net.wanji.business.common.Constants.DeleteGroup;
import net.wanji.business.common.Constants.InsertGroup;
import net.wanji.business.common.Constants.OtherGroup;
import net.wanji.business.common.Constants.UpdateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/28 14:05
 * @Descriptoin:
 */
@Data
public class TjResourcesDetailDto {

    @NotNull(message = "请选择资源", groups = {DeleteGroup.class})
    private Integer id;

    /**
     * 所属资源id
     */
    @NotNull(message = "请确认所属资源", groups = {InsertGroup.class, UpdateGroup.class, OtherGroup.class})
    private Integer resourcesId;

    /**
     * 名称
     */
    @NotBlank(message = "请填写名称", groups = {InsertGroup.class, UpdateGroup.class, OtherGroup.class})
    private String name;

    private String code;

    private String format;

    /**
     * 源文件存储路径
     */
    @NotBlank(message = "请上传资源文件", groups = {InsertGroup.class, UpdateGroup.class, OtherGroup.class})
    private String filePath;

    /**
     * 示意图存储路径
     */
    @NotBlank(message = "请上传缩略图", groups = {InsertGroup.class, UpdateGroup.class})
    private String imgPath;

    /**
     * 自定义字段1（地图：道路类型；）
     */
    private String attribute1;

    /**
     * 自定义字段2（地图：道路属性（单双向）；）
     */
    private String attribute2;

    /**
     * 自定义字段3（地图：车道数；）
     */
    private String attribute3;

    /**
     * 自定义字段4（地图：geojson文件；）
     */
    private String attribute4;

    /**
     * 自定义字段5
     */
    private String attribute5;

}
