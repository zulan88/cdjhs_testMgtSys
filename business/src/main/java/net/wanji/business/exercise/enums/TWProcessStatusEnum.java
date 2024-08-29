package net.wanji.business.exercise.enums;

/**
 * @author: jenny
 * @create: 2024-08-29 10:46 上午
 */
public enum TWProcessStatusEnum {
    RUNNING(0, "比赛开始"),
    FINISHED(1, "比赛完成"),
    SCORE_COMPLETED(2, "打分完成");

    TWProcessStatusEnum(Integer status, String name){
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
