package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wj
 * @since 2024-01-17
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tj_generalize_scene")
public class TjGeneralizeScene implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 原始场景id
     */
    @TableField("scene_id")
    private Integer sceneId;

    /**
     * 编码
     */
    @TableField("number")
    private String number;

    /**
     * 标签
     */
    @TableField("label")
    private String label;

    /**
     * 轨迹信息
     */
    @TableField("trajectory_info")
    private String trajectoryInfo;

    /**
     * 轨迹文件
     */
    @TableField("route_file")
    private String routeFile;

    /**
     * 全树形标签
     */
    @TableField("all_stage_label")
    private String allStageLabel;

    /**
     * 场景描述
     */
    @TableField("test_scene_desc")
    private String testSceneDesc;


}
