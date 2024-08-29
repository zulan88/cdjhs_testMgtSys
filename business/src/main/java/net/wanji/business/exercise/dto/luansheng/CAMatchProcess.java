package net.wanji.business.exercise.dto.luansheng;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-08-29 9:43 上午
 */
@Data
public class CAMatchProcess {
    //0：比赛开始
    //1：已完成:
    //2：打分完成
    private Integer status;

    private Long datetime;

    private Long taskId;

    private Long teamId;

    //客观占比
    private Integer objectivePercent;

    //主观占比
    private Integer subjectivePercent;

    //客观得分
    private Double objectiveScore;

    //主观得分
    private Double subjectiveScore;

    //总得分
    private Double totalScore;
}
