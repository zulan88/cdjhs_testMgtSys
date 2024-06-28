package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.entity.TjFragmentedSceneDetail;

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

}
