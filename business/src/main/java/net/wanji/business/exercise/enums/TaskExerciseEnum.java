package net.wanji.business.exercise.enums;

/**
 * @author: jenny
 * @create: 2024-07-12 2:51 下午
 */
public enum TaskExerciseEnum {
    START_INTERACTION(0),

    IMAGE_ISSUED(1),

    TASK_ISSUED(2),

    IS_TESS_AWAKENDED(3),

    STARTING_LISTEN_MAIN_TRAJECTORY(4),

    FUSION_STRATEGY_IS_ISSUED(5),

    IS_TASK_STARTED(6),

    TASK_IS_FINISHED(7);

    TaskExerciseEnum(Integer status){
        this.status = status;
    }

    private final Integer status;

    public Integer getStatus() {
        return status;
    }
}
