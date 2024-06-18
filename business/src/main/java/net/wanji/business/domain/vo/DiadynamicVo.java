package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.common.annotation.Excel;

import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/5 14:35
 * @Descriptoin:
 */
@Data
public class DiadynamicVo {

    /**
     * 类型
     */
    private String dcType;

    /**
     * 权重
     */
    private String dcWeight;

    @Excel(name = "详情")
    private List<DiadynamicDetailVo> detail;

}
