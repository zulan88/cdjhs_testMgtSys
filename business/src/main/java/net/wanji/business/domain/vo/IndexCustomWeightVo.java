package net.wanji.business.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName IndexCustomWeightVo
 * @Description
 * @Author liruitao
 * @Date 2023-11-14
 * @Version 1.0
 **/
@Data
public class IndexCustomWeightVo {

    /**
     * 指标code 或场景分类code
     */
    private String code;

    /**
     * 指标名称
     */
    private String name;

    /**
     * 权重值
     */
    private Double defaultWeight;

    List<IndexCustomWeightVo.IndexWeightDetails> list;

    @Data
    public static class IndexWeightDetails{

        /**
         * id
         */
        private Integer id;

        /**
         * 指标code 或场景分类code
         */
        private String code;

        /**
         * 指标名称
         */
        private String name;

        /**
         * 指标描述
         */
        private String description;

        /**
         * 公式
         */
        private String formula;

        /**
         * 测试目标
         */
        private String goal;

        /**
         * 权重值
         */
        private String defaultWeight;

        /**
         * 父级code
         */
        private String parentCode;
    }

}
