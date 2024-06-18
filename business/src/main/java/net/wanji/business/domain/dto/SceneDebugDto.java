package net.wanji.business.domain.dto;

import lombok.Data;
import net.wanji.business.common.Constants.OtherGroup;
import net.wanji.business.domain.bo.CaseTrajectoryDetailBo;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/26 13:49
 * @Descriptoin:
 */
@Data
public class SceneDebugDto {

    Integer id;

    /**
     * 场景id
     */
//    @NotNull(message = "请确认所属节点", groups = {OtherGroup.class})
    private Integer fragmentedSceneId;
    /**
     * 场景编号
     */
    @NotEmpty(message = "请确认场景编号", groups = {OtherGroup.class})
    private String number;
    /**
     * 轨迹信息
     */
    @NotNull(message = "请进行点位标记", groups = {OtherGroup.class})
    private CaseTrajectoryDetailBo trajectoryJson;

    /**
     * 操作类型
     */
    @NotNull(message = "请选择操作类型", groups = {OtherGroup.class})
    private Integer action;

    private String routePath;
    private String routeFile;

    private Integer mapId;

    private Integer simuType;

    private Integer isGen;

}
