package net.wanji.business.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName SceneIndexSchemeVo
 * @Description
 * @Author liruitao
 * @Date 2023-11-14
 * @Version 1.0
 **/
@Data
public class SceneIndexSchemeVo {

    /**
     * 指标方案ID 或 场景类型方案ID
     */
    private Integer id;

    /**
     * 方案名称
     */
    private String name;

    /**
     * 方案描述
     */
    private String description;

    /**
     * 0 场景分类方案 1 指标方案
     */
    private Integer type;

}
