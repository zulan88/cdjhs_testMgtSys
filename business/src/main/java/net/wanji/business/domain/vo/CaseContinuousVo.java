package net.wanji.business.domain.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: guanyuduo
 * @date: 2023/11/9 15:44
 * @descriptoin: 连续性场景展示实体类
 */
@ApiModel("连续性场景展示实体类")
@Data
public class CaseContinuousVo {

    @ApiModelProperty("业务主键")
    private Integer id;
    @ApiModelProperty("所属任务ID")
    private Integer taskId;
    @ApiModelProperty("所属用例ID")
    private Integer caseId;
    @ApiModelProperty("顺序")
    private Integer sort;
    @ApiModelProperty("用例编号")
    private String caseNumber;
    @ApiModelProperty("场景分类")
    private String sceneSort;
    @ApiModelProperty("主车轨迹")
    @JSONField(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
    private Object mainTrajectory;
    @ApiModelProperty("开始点")
    @JSONField(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
    private Object startPoint;
    @ApiModelProperty("结束点")
    @JSONField(serialzeFeatures = {SerializerFeature.DisableCircularReferenceDetect})
    private Object endPoint;
    @ApiModelProperty("连接线")
    private Object connectInfo;
    private String mapFile;
    private Integer mapId;
}
