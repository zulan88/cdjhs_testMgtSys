package net.wanji.business.exercise.dto.luansheng;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-08-29 11:20 上午
 */
@Data
public class TaskCacheDto {
    private Long taskId;

    //
    private Integer status;

    //0: 打分未完成 1: 打分已完成
    private Integer scoreStatus;
}
