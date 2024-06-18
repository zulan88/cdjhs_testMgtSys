package net.wanji.business.domain.bo;

import lombok.Data;

/**
 * @ClassName SaveTaskSchemeBo
 * @Description
 * @Author liruitao
 * @Date 2023-11-15
 * @Version 1.0
 **/
@Data
public class SaveTaskSchemeBo {

    /**
     * 测试任务id
     */
    private String taskId;

    /**
     * 指标方案id
     */
    private Integer indexSchemeId;

    /**
     * 场景方案id
     */
    private Integer senceSchemeId;

}
