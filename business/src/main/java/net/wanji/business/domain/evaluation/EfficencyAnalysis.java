package net.wanji.business.domain.evaluation;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-06-27 9:59 上午
 */
@Data
public class EfficencyAnalysis {
    //行驶时长低于测试时长
    private int internal1;

    //行驶时长高于测试时长0-10%
    private int internal2;

    //行驶时长高于测试时长10%-30%
    private int internal3;

    //行驶时长高于测试时长30%及以上
    private int internal4;
}
