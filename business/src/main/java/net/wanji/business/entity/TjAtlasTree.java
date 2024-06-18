package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 地图库地图分类表
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tj_atlas_tree")
public class TjAtlasTree implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     */
    @TableField("type")
    private String type;

    /**
     * 状态 0：使用中；1：已删除
     */
    @TableField("status")
    private Integer status;

    /**
     * 父级id
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 级别
     */
    @TableField("level")
    private Integer level;

    /**
     * 自定义字段1
     */
    @TableField("attribute1")
    private String attribute1;

    /**
     * 自定义字段2
     */
    @TableField("attribute2")
    private String attribute2;

    /**
     * 自定义字段3
     */
    @TableField("attribute3")
    private String attribute3;

    /**
     * 自定义字段4
     */
    @TableField("attribute4")
    private String attribute4;

    /**
     * 自定义字段5
     */
    @TableField("attribute5")
    private String attribute5;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建日期
     */
    @TableField("created_date")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    /**
     * 修改人
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 修改日期
     */
    @TableField("updated_date")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;

    /**
     * 子集节点
     */
    @TableField(exist = false)
    private List<TjAtlasTree> children;

}
