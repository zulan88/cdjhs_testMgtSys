package net.wanji.business.domain.vo;

import lombok.Data;

/**
 * @ClassName SceneWeightDetailsVo
 * @Description
 * @Author liruitao
 * @Date 2023-11-14
 * @Version 1.0
 **/
@Data
public class SceneWeightDetailsVo{

    /**
     * 指标code 或场景分类code
     */
    private String code;

    /**
     * 场景分类名称
     */
    private String categoryName;

    /**
     * 权重值
     */
    private Double weight;

}
