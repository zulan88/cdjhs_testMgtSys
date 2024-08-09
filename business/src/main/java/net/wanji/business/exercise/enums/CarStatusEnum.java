package net.wanji.business.exercise.enums;

/**
 * @author: jenny
 * @create: 2024-08-02 4:24 下午
 */
public enum CarStatusEnum {
    IDLE(1, "空闲"),
    PREPARE(2, "已就绪"),
    RUNNING(3, "运行中");

    CarStatusEnum(Integer status, String name){
        this.status = status;
        this.name = name;
    }

    private final Integer status;

    private final String name;

    public Integer getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }
}
