package net.wanji.business.pdf.enums;

/**
 * @author: jenny
 * @create: 2024-06-28 1:36 下午
 */
public enum PdfTitleEnum {
    TITLE1(1, "长安大学季后赛"),
    TITLE2(2, "在线练习平台"),
    TITLE3(3, "《在线练习测试报告》");

    PdfTitleEnum(Integer order, String title){
        this.order = order;
        this.title = title;
    }

    private final String title;

    private final Integer order;

    public String getTitle(){
        return title;
    }

    public Integer getOrder(){
        return order;
    }
}
