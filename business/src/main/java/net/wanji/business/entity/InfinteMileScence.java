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
 * 
 * </p>
 *
 * @author wj
 * @since 2024-02-23
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("infinte_mile_scence")
public class InfinteMileScence implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 场景名称
     */
    @TableField("name")
    private String name;

    /**
     * 场景编号
     */
    @TableField("view_id")
    private String viewId;

    /**
     * 测试难度 1-简单 2-较简单 3-中等 4-较困难 5-困难
     */
    @TableField("difficulty")
    private Integer difficulty;

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
     * 元素
     */
    @TableField("element")
    private String element;

    /**
     * 交通流
     */
    @TableField("traffic_flow")
    private String trafficFlow;

    @TableField("traffic_flow_config")
    private String trafficFlowConfig;

    /**
     * 场地切片
     */
    @TableField("site_slice")
    private String siteSlice;

    /**
     *  主车轨迹
     */
    @TableField("route_file")
    private String routeFile;

    @TableField("camera_info")
    private String cameraInfo;

    /**
     * 测试说明
     */
    @TableField("test_explain")
    private String testExplain;

    /**
     * 图片路径
     */
    @TableField("img_url")
    private String imgUrl;

    /**
     * 场景切片截图
     */
    @TableField(value = "slice_img", select = false)
    private String sliceImg;

    /**
     * 场景状态
     */
    @TableField("status")
    private Integer status;

    @TableField("create_date")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @TableField("update_date")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;


}
