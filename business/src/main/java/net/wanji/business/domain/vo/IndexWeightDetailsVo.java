package net.wanji.business.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @ClassName IndexWeightDetailsVo
 * @Description
 * @Author liruitao
 * @Date 2023-11-14
 * @Version 1.0
 **/
@Data
public class IndexWeightDetailsVo {

    /**
     * 指标code 或场景分类code
     */
    private String code;

    /**
     * 指标名称
     */
    private String indexName;

    /**
     * 权重值
     */
    private Double weight;

    List<IndexWeightDetails> list;

    @Data
    public static class IndexWeightDetails{

        /**
         * 指标code 或场景分类code
         */
        private String code;

        /**
         * 指标名称
         */
        private String indexName;

        /**
         * 指标描述
         */
        private String description;

        /**
         * 公式
         */
        private String formula;

        /**
         * todo 测试目标没有
         */

        /**
         * 权重值
         */
        private String weight;

        /**
         * 父级code
         */
        private String parentCode;
    }

}
