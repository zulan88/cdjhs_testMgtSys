package net.wanji.business.domain.evaluation;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-06-27 10:02 上午
 */
@Data
public class IndexRate {
    //评级
    //0: 优秀
    //1: 良好
    //2: 一般
    //3: 较差
    //4: 很差
    private Integer rate;

    //场景数目
    private Integer value;
}
