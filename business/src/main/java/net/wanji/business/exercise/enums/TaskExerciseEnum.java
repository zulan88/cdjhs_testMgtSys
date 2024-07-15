package net.wanji.business.exercise.enums;

/**
 * @author: jenny
 * @create: 2024-07-12 2:51 下午
 */
public enum TaskExerciseEnum {
    BEFORE_TESS_AWAKENED(1),

    IS_TESS_AWAKENDED(2),

    STARTING_LISTEN_MAIN_TRAJECTORY(3),

    FUSION_STRATEGY_IS_ISSUED(4),

    IS_TASK_STARTED(5),

    TASK_IS_FINISHED(6);

    TaskExerciseEnum(Integer status){
        this.status = status;
    }

    private final Integer status;

    public Integer getStatus() {
        return status;
    }
}
