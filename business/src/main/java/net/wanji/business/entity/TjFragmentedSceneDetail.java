package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 片段式场景定义
 * </p>
 *
 * @author wj
 * @since 2023-06-27
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tj_fragmented_scene_detail")
public class TjFragmentedSceneDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 片段式场景id
     */
    @TableField("fragmented_scene_id")
    private Integer fragmentedSceneId;

    /**
     * 道路渠化.车道类型
     * 子场景编号
     */
    @TableField("road_way_type")
    private String roadWayType;

    /**
     * 子场景编号
     */
    @TableField("number")
    private String number;

    /**
     * 道路渠化.车道类型
     * 车道数
     */
    @TableField("lane_num")
    private Integer laneNum;

    /**
     * 地图id
     */
    @TableField("map_id")
    private Integer mapId;

    /**
     * 场地地图
     */
    @TableField("map_file")
    private String mapFile;

    /**
     * 场景描述
     */
    @TableField("test_scene_desc")
    private String testSceneDesc;

    /**
     * 场景来源
     */
    @TableField("scene_source")
    private String sceneSource;

    /**
     * 场景复杂度（字典：scene_complexity）
     */
    @TableField("scene_complexity")
    private String sceneComplexity;


    /**
     * 交通流状态（字典：traffic_flow_status）
     */
    @TableField("traffic_flow_status")
    private String trafficFlowStatus;

    /**
     * 危险指数
     */
    @TableField("hazard_index")
    private Integer hazardIndex;

    /**
     * 道路类型(字典：road_type)
     */
    @TableField("road_type")
    private String roadType;

    /**
     * 路面状况（字典：road_condition）
     */
    @TableField("road_condition")
    private String roadCondition;

    /**
     * 天气（字典：weather）
     */
    @TableField("weather")
    private String weather;

    /**
     * 轨迹信息-计划时间
     */
    @TableField("trajectory_info_time")
    private String trajectoryInfoTime;

    /**
     * 参考点
     */
    @TableField("reference_point")
    private String referencePoint;


    /**
     * 仿真类型（0-计划时间，1-计划速度）
     */
    @TableField("simu_type")
    private Integer simuType;


    /**
     * 是否完成（0：未完成；1：已完成）
     */
    @TableField("finished")
    private boolean finished;

    /**
     * 收藏状态（未收藏：0；已收藏：1；）
     */
    @TableField("collect_status")
    private boolean collectStatus;

    /**
     * 标签
     */
    @TableField("label")
    @JsonIgnore
    private String label;

    /**
     * 轨迹信息-计划速度
     */
    @TableField("trajectory_info")
    private String trajectoryInfo;

    /**
     * 轨迹文件
     */
    @TableField("route_file")
    private String routeFile;

    /**
     * 图片路径
     */
    @TableField("img_url")
    private String imgUrl;

    /**
     * 场景类型（字典：scene_type）
     */
    @TableField("scene_type")
    private String sceneType;

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
     * 场景状态(0-停用，1-启用)
     */
    @TableField("scene_status")
    private Integer sceneStatus;

    @TableField("all_stage_label")
    private String allStageLabel;

    @TableField("camera_info")
    private String cameraInfo;


}
