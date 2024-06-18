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
 * <p>
 * 片段式场景表
 * </p>
 *
 * @author wj
 * @since 2023-06-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tj_fragmented_scenes")
public class TjFragmentedScenes implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 类型类型（基本段：1；T型交叉路口：2；环岛：3；正交交叉口：4；）
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
     * 是否是文件夹（场景：0；文件夹：1）
     */
    @TableField("is_folder")
    private Integer isFolder;

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
    private List<TjFragmentedScenes> children;
}
