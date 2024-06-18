package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.entity.TjScenelib;

@Data
public class ScenelibVo extends TjScenelib {

    private String sceneSort;

    private String startDate;

    private String endDate;
}
