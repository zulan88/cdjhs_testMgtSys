package net.wanji.business.domain.bo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName SaveCustomScenarioWeightBo
 * @Description
 * @Author liruitao
 * @Date 2023-11-15
 * @Version 1.0
 **/
@Data
public class SaveCustomIndexWeightBo {

    /**
     * 是否自定义标识 ture 为自定义，false 或不传为非自定义
     */
    private boolean userDefineFlag;

    /**
     * 任务id
     */
    private String task_id;

    /**
     * 任务id （自定义标识 ture 时必传）
     */
    private String type;

    /**
     * 方案名称
     */
//    private String name;

    /**
     * 入参：index 指标 或 sence 场景方案
     */
//    private String description;

    /**
     * 权重集合
     */
    private List<Weights> list;

    @Data
    public static class Weights{

        /**
         * 用例id
         */
        private String code;

        /**
         * 权重比
         */
        private Double weight;


    }

}
