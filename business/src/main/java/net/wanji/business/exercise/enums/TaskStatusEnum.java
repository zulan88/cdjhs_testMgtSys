package net.wanji.business.exercise.enums;

/**
 * @author: jenny
 * @create: 2024-07-08 5:30 下午
 */
public enum TaskStatusEnum {
    WAITING(1, "待开始"),
    RUNNING(2, "进行中"),
    FINISHED(3, "已完成");

    TaskStatusEnum(Integer status, String name){
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
