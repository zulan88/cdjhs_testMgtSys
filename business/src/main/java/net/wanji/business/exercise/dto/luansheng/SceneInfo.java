package net.wanji.business.exercise.dto.luansheng;

/**
 * @author: jenny
 * @create: 2024-08-22 10:09 上午
 */
public class SceneInfo {
    private String sceneName;

    //0: 已触发 1:正在触发 2: 待触发
    private Integer status;

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
