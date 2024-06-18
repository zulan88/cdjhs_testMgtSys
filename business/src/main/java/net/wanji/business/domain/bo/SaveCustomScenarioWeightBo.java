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
public class SaveCustomScenarioWeightBo {

    /**
     * 任务id
     */
    private String task_id;

    /**
     * 权重集合
     */
    private List<Weights> weights;

    @Data
    public static class Weights{

        /**
         * 用例id
         */
        private String case_id;

        /**
         * 场景名称
         */
        private String sence_category_name;

        /**
         * 权重比
         */
        private Double weight;


    }

}
