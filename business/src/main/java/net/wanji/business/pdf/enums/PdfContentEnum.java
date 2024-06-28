package net.wanji.business.pdf.enums;

/**
 * @author: jenny
 * @create: 2024-06-28 1:43 下午
 */
public enum PdfContentEnum {
    USER_NAME(1, "userName", "用户名"),
    MIRROR_NAME(2, "mirrorName", "镜像名称"),
    MIRROR_VERSION(3, "mirrorVersion", "镜像版本"),
    DEVICE_ID(4, "deviceId", "练习设备唯一标识"),
    TEST_CASE_CODE(5, "testCaseCode", "测试用例编号"),
    TEST_CASE_NAME(6, "testCaseName", "测试用例名称"),
    TEST_PAPER_TYPE(7, "testPaperType", "所属试卷类型"),
    SCORE(8, "score", "测试评分"),
    DURATION(9, "duration", "测试总时长"),
    START_TIME(10, "startTime", "测试开始时间"),
    END_TIME(11, "endTime", "测试评价时间");

    PdfContentEnum(Integer order, String fieldName, String name){
        this.order = order;
        this.fieldName = fieldName;
        this.name = name;
    }

    private final Integer order;

    private final String fieldName;

    private final String name;

    public Integer getOrder() {
        return order;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getName() {
        return name;
    }
}
