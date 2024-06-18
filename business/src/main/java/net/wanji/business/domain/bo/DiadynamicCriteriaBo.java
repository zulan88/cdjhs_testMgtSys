package net.wanji.business.domain.bo;

import lombok.Data;

/**
 * @author: guowenhao
 * @date: 2023/8/30 16:52
 * @description:
 */
@Data
public class DiadynamicCriteriaBo extends SceneTrajectoryBo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 指标描述
     */
    private String indexDescribe;

    /**
     * 计算参数
     */
    private String designConditions;

    /**
     * 类型
     */
    private String type;

}
