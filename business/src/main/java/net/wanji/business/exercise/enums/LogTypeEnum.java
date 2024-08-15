package net.wanji.business.exercise.enums;

/**
 * @author: jenny
 * @create: 2024-08-15 10:49 上午
 */
public enum  LogTypeEnum {
    COMMAND("command"),

    MAIN_CAR_TRAJECTORY("main_car_trajectory"),

    FUSION_TRAJECTORT("fusion_trajectory_data");

    LogTypeEnum(String name){
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
