package net.wanji.business.domain.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.log4j.Log4j2;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.business.entity.TjFragmentedSceneDetail;
import net.wanji.common.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/28 13:20
 * @Descriptoin:
 */
@Log4j2
@ApiModel("场景库详情")
public class FragmentedScenesDetailVo extends TjFragmentedSceneDetail {

    private String typeName;

    /**
     * 资源名称
     */
    @ApiModelProperty(value = "资源名称")
    private String resourcesName;

    /**
     * 资源缩略图
     */
    @ApiModelProperty(value = "资源缩略图")
    private String resourcesImgPath;

    /**
     * 资源文件路径
     */
    @ApiModelProperty(value = "资源文件路径")
    private String filePath;

    /**
     * 地图的geoJson文件路径
     */
    @ApiModelProperty(value = "地图的geoJson文件路径")
    private String geoJsonPath;

    /**
     * 道路类型名称
     */
    @ApiModelProperty(value = "道路类型名称")
    private String roadTypeName;

    private String roadWayName;

    private String roadConditionName;

    private String weatherName;

    private String trafficFlowStatusName;

    /**
     * 场景复杂度名称
     */
    private String sceneComplexityName;

    JSONArray referencePoints;

    public void setReferencePoints(JSONArray referencePoints) {
        this.referencePoints = referencePoints;
    }

    public JSONArray getReferencePoints() {
        if (StringUtils.isNotBlank(this.getReferencePoint())) {
            return JSONArray.parseArray(this.getReferencePoint());
        }
        return referencePoints;
    }

    private String sceneTypeName;

    private SceneTrajectoryBo trajectoryJson;

    private List<String> labelList;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getResourcesName() {
        return resourcesName;
    }

    public void setResourcesName(String resourcesName) {
        this.resourcesName = resourcesName;
    }

    public String getResourcesImgPath() {
        return resourcesImgPath;
    }

    public void setResourcesImgPath(String resourcesImgPath) {
        this.resourcesImgPath = resourcesImgPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getGeoJsonPath() {
        return geoJsonPath;
    }

    public void setGeoJsonPath(String geoJsonPath) {
        this.geoJsonPath = geoJsonPath;
    }

    public String getRoadTypeName() {
        return roadTypeName;
    }

    public void setRoadTypeName(String roadTypeName) {
        this.roadTypeName = roadTypeName;
    }

    public String getRoadWayName() {
        return roadWayName;
    }

    public void setRoadWayName(String roadWayName) {
        this.roadWayName = roadWayName;
    }

    public String getRoadConditionName() {
        return roadConditionName;
    }

    public void setRoadConditionName(String roadConditionName) {
        this.roadConditionName = roadConditionName;
    }

    public String getWeatherName() {
        return weatherName;
    }

    public void setWeatherName(String weatherName) {
        this.weatherName = weatherName;
    }

    public String getTrafficFlowStatusName() {
        return trafficFlowStatusName;
    }

    public void setTrafficFlowStatusName(String trafficFlowStatusName) {
        this.trafficFlowStatusName = trafficFlowStatusName;
    }

    public String getSceneComplexityName() {
        return sceneComplexityName;
    }

    public void setSceneComplexityName(String sceneComplexityName) {
        this.sceneComplexityName = sceneComplexityName;
    }

    public String getSceneTypeName() {
        return sceneTypeName;
    }

    public void setSceneTypeName(String sceneTypeName) {
        this.sceneTypeName = sceneTypeName;
    }

    public SceneTrajectoryBo getTrajectoryJson() {
        if (this.getSimuType()!=null && this.getSimuType()==0){
            try {
                if (StringUtils.isNotEmpty(this.getTrajectoryInfoTime())) {
                    return JSONObject.parseObject(this.getTrajectoryInfoTime(), SceneTrajectoryBo.class);
                }
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("parse error!", e);
                }
            }
        }else {
            try {
                if (StringUtils.isNotEmpty(this.getTrajectoryInfo())) {
                    return JSONObject.parseObject(this.getTrajectoryInfo(), SceneTrajectoryBo.class);
                }
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("parse error!", e);
                }
            }
        }
        return trajectoryJson;
    }

    public void setTrajectoryJson(SceneTrajectoryBo trajectoryJson) {
        this.trajectoryJson = trajectoryJson;
    }


    public List<String> getLabelList() {
        if (StringUtils.isNotEmpty(this.getLabel())) {
            return Arrays.stream(this.getLabel().split(",")).collect(Collectors.toList());
        }
        return labelList;
    }

    public void setLabelList(List<String> labelList) {
        this.labelList = labelList;
    }
}
