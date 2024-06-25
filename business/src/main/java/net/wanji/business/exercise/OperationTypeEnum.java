package net.wanji.business.exercise;

/**
 * @author: jenny
 * @create: 2024-06-23 5:23 下午
 */
public enum OperationTypeEnum {
    TEST_CONTROL_REQ(2),

    IMAGE_LIST_REPORT_REQ(4),

    TEST_ISSUE_REQ(5);

    OperationTypeEnum(Integer type){
        this.type = type;
    }

    private final Integer type;

    public Integer getType() {
        return type;
    }
}
