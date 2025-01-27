package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.domain.SitePoint;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.business.entity.TjFragmentedSceneDetail;

import java.util.List;

@Data
public class SceneDetailVo extends TjFragmentedSceneDetail {

    /**
     * 场景分类
     */
    private String sceneSort;

    private String startDate;

    private String endDate;

    private String labels;

    private String count;

    private SceneTrajectoryBo sceneTrajectoryBo;

    private Integer evoNum;

    private String xosc;

    private String weight;

    private List<SitePoint> endTarget;

    private Double[][] mapBounds;

}
