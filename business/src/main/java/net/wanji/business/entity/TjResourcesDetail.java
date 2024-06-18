package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 资源详情表
 * </p>
 *
 * @author wj
 * @since 2023-06-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tj_resources_detail")
public class TjResourcesDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属资源id
     */
    @TableField("resources_id")
    private Integer resourcesId;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 编号
     */
    @TableField("code")
    private String code;

    /**
     * 格式 opendrive；
     */
    @TableField("format")
    private String format;

    /**
     * 源文件存储路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 示意图存储路径
     */
    @TableField("img_path")
    private String imgPath;

    /**
     * 收藏状态（未收藏：0；已收藏：1；）
     */
    @TableField("collect_status")
    private Integer collectStatus;

    /**
     * 自定义字段1（地图：道路类型scene_tree_type；）
     */
    @TableField("attribute1")
    private String attribute1;

    /**
     * 自定义字段2（地图：道路属性road_way_type；）
     */
    @TableField("attribute2")
    private String attribute2;

    /**
     * 自定义字段3（地图：车道数；）
     */
    @TableField("attribute3")
    private String attribute3;

    /**
     * 自定义字段4（地图：geojson文件；）
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


}
