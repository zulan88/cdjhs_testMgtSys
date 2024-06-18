package net.wanji.business.domain.dto;

import lombok.Data;
import net.wanji.business.common.Constants.DeleteGroup;
import net.wanji.business.common.Constants.InsertGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/28 14:05
 * @Descriptoin:
 */
@Data
public class TjResourcesDto {

    @NotNull(message = "请选择一个节点", groups = {DeleteGroup.class})
    private Integer id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不可为空", groups = { InsertGroup.class })
    private String name;

    /**
     * 资源类型 map：地图；
     */
    @NotBlank(message = "请确认类型", groups = { InsertGroup.class })
    private String type;

    /**
     * 父级id
     */
    @NotNull(message = "请确认父级场景", groups = { InsertGroup.class })
    private Integer parentId;

    /**
     * 级别
     */
    @NotNull(message = "请确认场景等级")
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
