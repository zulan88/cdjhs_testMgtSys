package net.wanji.business.domain.bo;

import net.wanji.common.utils.StringUtils;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/5 18:03
 * @Descriptoin:
 */
public class CaseTrajectoryDetailBo extends SceneTrajectoryBo {

    private static final long serialVersionUID = 1L;

    private String sceneDesc;
    private String sceneForm;
    private String evaluationVerify;
    private String duration;
    private String score;


    public String getSceneDesc() {
        return sceneDesc;
    }

    public void setSceneDesc(String sceneDesc) {
        this.sceneDesc = sceneDesc;
    }

    public String getSceneForm() {
        return sceneForm;
    }

    public void setSceneForm(String sceneForm) {
        this.sceneForm = sceneForm;
    }

    public String getEvaluationVerify() {
        return evaluationVerify;
    }

    public void setEvaluationVerify(String evaluationVerify) {
        this.evaluationVerify = evaluationVerify;
    }

    public String getDuration() {
        return StringUtils.isEmpty(duration) ? "00:00" : duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
