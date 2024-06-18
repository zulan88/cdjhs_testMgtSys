package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.common.annotation.Excel;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/5 14:35
 * @Descriptoin:
 */
@Data
public class DiadynamicDetailVo {

    /**
     * 指标
     */
    private String diaName;

    /**
     * 分数
     */
    private String score;

    /**
     * 时长
     */
    private String time;


}
