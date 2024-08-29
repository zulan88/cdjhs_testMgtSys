package net.wanji.business.exercise.dto.luansheng;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jenny
 * @create: 2024-08-29 11:20 上午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCacheDto {
    private Long taskId;

    private Long teamId;

    //比赛状态 1:待开始 2:进行中 3:已完成
    private Integer status;

    //0: 打分未完成 1: 打分已完成
    private Integer scoreStatus;
}
