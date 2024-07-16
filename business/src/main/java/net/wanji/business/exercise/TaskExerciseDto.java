package net.wanji.business.exercise;

import java.util.concurrent.Future;

/**
 * @author: jenny
 * @create: 2024-07-12 9:15 上午
 */
public class TaskExerciseDto {
    private TaskExercise taskExercise;

    private Future<?> future;

    public TaskExerciseDto(TaskExercise taskExercise, Future<?> future){
        this.taskExercise = taskExercise;
        this.future = future;
    }

    public TaskExercise getTaskExercise() {
        return taskExercise;
    }

    public void setTaskExercise(TaskExercise taskExercise) {
        this.taskExercise = taskExercise;
    }

    public Future<?> getFuture() {
        return future;
    }

    public void setFuture(Future<?> future) {
        this.future = future;
    }
}
