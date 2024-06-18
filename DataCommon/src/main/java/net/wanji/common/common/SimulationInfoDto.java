package net.wanji.common.common;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/9 9:40
 * @Descriptoin:
 */
public class SimulationInfoDto {
    private String sceneDesc;
    private String sceneForm;
    private String evaluationVerify;

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
}
