package net.wanji.business.exercise.enums;

/**
 * @author: jenny
 * @create: 2024-07-19 11:08 上午
 */
public enum  SimulationStatusEnum {
    WAIT("wait"),

    START("start"),

    STOP("stop");

    SimulationStatusEnum(String status){
        this.status = status;
    }

    private final String status;

    public String getStatus() {
        return status;
    }
}
