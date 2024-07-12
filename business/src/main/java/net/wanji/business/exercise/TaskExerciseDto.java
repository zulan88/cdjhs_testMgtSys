package net.wanji.business.exercise;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.Future;

/**
 * @author: jenny
 * @create: 2024-07-12 9:15 上午
 */
@Data
@NoArgsConstructor
public class TaskExerciseDto {
    private TaskExercise taskExercise;

    private Future<?> future;

    public TaskExerciseDto(TaskExercise taskExercise, Future<?> future){
        this.taskExercise = taskExercise;
        this.future = future;
    }
}
