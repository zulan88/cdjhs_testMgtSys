package net.wanji.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import net.wanji.business.entity.TjCase;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/29 17:23
 * @Descriptoin:
 */
@ApiModel("测试用例")
public class CaseVo extends TjCase {

    @JsonIgnore
    private String label;

    /**
     * 场景分类
     */
    private String sceneSort;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSceneSort() {
        return sceneSort;
    }

    public void setSceneSort(String sceneSort) {
        this.sceneSort = sceneSort;
    }
}
