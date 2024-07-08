package net.wanji.business.exercise.enums;

/**
 * @author: jenny
 * @create: 2024-07-08 5:37 下午
 */
public enum CheckResultEnum {
    FAILURE(1, "校验失败"),
    SUCCESS(0, "校验通过");

    CheckResultEnum(Integer result, String name){
        this.result = result;
        this.name = name;
    }

    private final Integer result;

    private final String name;

    public Integer getResult() {
        return result;
    }

    public String getName() {
        return name;
    }
}
