package net.wanji.business.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * scenelib对象 tj_scenelib
 * 
 * @author wanji
 * @date 2023-10-31
 */
@Data
@TableName("tj_scenelib")
public class TjScenelib
{
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 树形ID */
    @TableField("tree_id")
    private Long treeId;

    /** 编码 */
    @TableField("number")
    private String number;

    /** 标签 */
    @TableField("labels")
    private String labels;

    /**
     * 场景来源
     * 0 - 自定义创建 1 - 车端采集 2 - 路侧采集 3 - 标准场景 4 - 模拟器采集 5 - 无限里程测试 6 - onsite场景
     * */
    @TableField("scene_source")
    private Integer sceneSource;

    /** 场景状态 */
    @TableField("scene_status")
    private Integer sceneStatus;

    @TableField("geojson_path")
    private String geojsonPath;

    /** 压缩包目录 */
    @TableField("zip_path")
    private String zipPath;

    /** 地图目录 */
    @TableField("xodr_path")
    private String xodrPath;

    /** 场景脚本目录 */
    @TableField("xosc_path")
    private String xoscPath;

    /** 场景描述 */
    @TableField("scene_desc")
    private String sceneDesc;

    @TableField("create_datetime")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDatetime;

    @TableField("update_datetime")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDatetime;

    @TableField("create_by")
    private String createBy;

    @TableField("update_by")
    private String updateBy;

    /** 图片目录 */
    @TableField("img_path")
    private String imgPath;

    /** 视频目录 */
    @TableField("video_path")
    private String videoPath;

    /** 全树形标签 */
    @TableField("all_stage_labels")
    private String allStageLabels;

    @TableField("scene_detail_id")
    private Integer sceneDetailId;

    /** 生成源
     * 0-原始场景，1-泛化场景
     * */
    @TableField("is_gen")
    private Integer isGen;

    @TableField("evo_num")
    private Integer evoNum;
}
