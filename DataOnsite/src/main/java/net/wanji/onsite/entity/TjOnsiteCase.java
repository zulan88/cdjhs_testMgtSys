package net.wanji.onsite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wj
 * @since 2024-06-04
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tj_onsite_case")
public class TjOnsiteCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * onsite场景名称
     */
    @TableField("name")
    private String name;

    /**
     * 地图
     */
    @TableField("xodrfile")
    private String xodrfile;

    /**
     * 场景编号
     */
    @TableField("onsite_number")
    private String onsiteNumber;

    /**
     * 场景库数据ID
     */
    @TableField("scenelib_id")
    private Long scenelibId;

    /**
     * caseId
     */
    @TableField("case_id")
    private Integer caseId;

    /**
     * 场景标签
     */
    @TableField("scene_label")
    private String sceneLabel;

    /**
     * 设备ID
     */
    @TableField("device_id")
    private Integer deviceId;

    /**
     * 图片路径
     */
    @TableField("img_url")
    private String imgUrl;

    /**
     * 轨迹文件
     */
    @TableField("routefile")
    private String routefile;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @TableField("created_date")
    private LocalDateTime createdDate;

    /**
     * 更新时间
     */
    @TableField("updated_date")
    private LocalDateTime updatedDate;

    @TableField(exist = false)
    private String channel;


}
