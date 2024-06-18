package net.wanji.business.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/4 10:53
 * @Descriptoin:
 */
@Data
public class TreeTypeDto {

    /** 字典编码 */
    private Long dictCode;

    /** 字典标签 */
    @NotBlank(message = "请设置类型名称")
    private String dictLabel;

    /** 样式属性（其他样式扩展） */
    @NotBlank(message = "请选择图标")
    private String cssClass;

    /**
     * 描述
     */
    private String remark;
}
