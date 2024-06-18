package net.wanji.business.domain.dto;

import com.alibaba.fastjson2.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.wanji.business.common.Constants;
import net.wanji.business.common.Constants.InsertGroup;
import net.wanji.business.common.Constants.OtherGroup;
import net.wanji.business.common.Constants.QueryGroup;
import net.wanji.business.common.Constants.UpdateGroup;
import net.wanji.business.domain.bo.CaseTrajectoryDetailBo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/28 14:05
 * @Descriptoin:
 */
@ApiModel("片段式场景详情")
@Data
public class TjFragmentedSceneDetailDto {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "场景编号")
    @NotNull(message = "请填写场景编号", groups = {InsertGroup.class, UpdateGroup.class})
    private String number;

    @ApiModelProperty(value = "片段式场景节点id")
//    @NotNull(message = "请确认所属场景", groups = {QueryGroup.class, InsertGroup.class, UpdateGroup.class})
    private Integer fragmentedSceneId;

    @ApiModelProperty(value = "道路渠化.车道类型（道路走向）（已过期）")
    private String roadWayType;

    @ApiModelProperty(value = "车道数（已过期）")
    private Integer laneNum;

    @ApiModelProperty(value = "地图id")
    @NotNull(message = "请填选择地图", groups = {InsertGroup.class, UpdateGroup.class, OtherGroup.class})
    private Integer mapId;

    private String mapFile;

    /**
     * 场景描述
     */
    @ApiModelProperty(value = "场景描述")
    @NotBlank(message = "请填写场景描述", groups = {InsertGroup.class, UpdateGroup.class})
    private String testSceneDesc;

    /**
     * 场景来源
     */
    @ApiModelProperty(value = "场景来源（已过期）")
    private String sceneSource;

    /**
     * 场景复杂度
     */
    @ApiModelProperty(value = "场景复杂度（已过期）")
    private String sceneComplexity;

    /**
     * 交通流状态
     */
    @ApiModelProperty(value = "交通流状态（已过期）")
    private String trafficFlowStatus;

    /**
     * 危险指数
     */
    @ApiModelProperty(value = "危险指数（已过期）")
    private Integer hazardIndex;

    /**
     * 道路类型
     */
    @ApiModelProperty(value = "道路类型（已过期）")
    private String roadType;
    /**
     * 路面状况
     */
    @ApiModelProperty(value = "路面状况（已过期）")
    private String roadCondition;

    /**
     * 天气
     */
    @ApiModelProperty(value = "天气（已过期）")
    private String weather;

    /**
     * 场景类型（字典：scene_type）
     */
    @ApiModelProperty(value = "场景类型")
    @NotBlank(message = "请选择场景类型", groups = {InsertGroup.class, UpdateGroup.class})
    private String sceneType;

    JSONArray referencePoints;

    /**
     * 仿真类型（0-计划时间，1-计划速度）
     */
    private Integer simuType;

    /**
     * 完成标志，区分提交和保存
     */
    private Integer finished;

    /**
     * 标签
     */
    @ApiModelProperty(value = "标签")
    @NotEmpty(message = "请选择标签", groups = {InsertGroup.class, UpdateGroup.class, OtherGroup.class})
    private List<String> labelList;

    /**
     * 图片(路网选点截图)
     */
    @ApiModelProperty(value = "图片(路网选点截图)")
    @NotNull(message = "请截图", groups = {OtherGroup.class})
    private String imgUrl;

    /**
     * 轨迹信息
     */
    @ApiModelProperty(value = "轨迹信息")
    @NotNull(message = "请进行点位标记", groups = {OtherGroup.class, Constants.BatchGroup.class})
    private CaseTrajectoryDetailBo trajectoryJson;

    /**
     * 轨迹文件
     */
    private String routeFile;

    /**
     * 收藏状态（未收藏：0；已收藏：1；）
     */
    private boolean collectStatus;

    private String cameraInfo;

}
