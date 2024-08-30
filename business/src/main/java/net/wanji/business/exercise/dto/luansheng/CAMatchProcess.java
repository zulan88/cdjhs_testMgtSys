package net.wanji.business.exercise.dto.luansheng;

import lombok.Data;
import net.wanji.business.exercise.enums.TWProcessStatusEnum;

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

    public static CAMatchProcess buildRunning(Long taskId, Long teamId){
        CAMatchProcess process = new CAMatchProcess();
        process.setStatus(TWProcessStatusEnum.RUNNING.getStatus());
        process.setDatetime(System.currentTimeMillis());
        process.setTaskId(taskId);
        process.setTeamId(teamId);
        return process;
    }

    public static CAMatchProcess buildFinished(Long taskId, Long teamId, Integer objectivePercent, Integer subjectivePercent){
        CAMatchProcess process = new CAMatchProcess();
        process.setStatus(TWProcessStatusEnum.FINISHED.getStatus());
        process.setDatetime(System.currentTimeMillis());
        process.setTaskId(taskId);
        process.setTeamId(teamId);
        process.setObjectivePercent(objectivePercent);
        process.setSubjectivePercent(subjectivePercent);
        return process;
    }

    public static CAMatchProcess buildSorceFinished(Long taskId, Long teamId, Integer objectivePercent, Double objectiveScore, Double subjectiveScore, Double totalScore){
        CAMatchProcess process = new CAMatchProcess();
        process.setStatus(TWProcessStatusEnum.SCORE_COMPLETED.getStatus());
        process.setDatetime(System.currentTimeMillis());
        process.setTaskId(taskId);
        process.setTeamId(teamId);
        process.setObjectivePercent(objectivePercent);
        process.setSubjectivePercent(100-objectivePercent);
        process.setObjectiveScore(objectiveScore);
        process.setSubjectiveScore(subjectiveScore);
        process.setTotalScore(totalScore);
        return process;
    }
}
