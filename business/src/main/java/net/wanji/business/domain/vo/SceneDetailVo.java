package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.entity.TjFragmentedSceneDetail;

@Data
public class SceneDetailVo extends TjFragmentedSceneDetail {

    private String sceneSort;

    private String startDate;

    private String endDate;

    private String labels;

    private String count;

}
