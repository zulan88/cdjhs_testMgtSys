package net.wanji.business.exercise.dto.luansheng;

/**
 * @author: jenny
 * @create: 2024-08-29 4:54 下午
 */
public class CommonField {
    private Long lastTimestamp;
    private boolean speedExceedLimit;
    private Long speedStartTime;
    private boolean lonAccExceedLimit;
    private Long lonAccStartTime;
    private boolean lonAcc2ExceedLimit;
    private Long lonAcc2StartTime;
    private boolean latAccExceedLimit;
    private Long latAccStartTime;
    private boolean latAcc2ExceedLimit;
    private Long latAcc2StartTime;
    private boolean angularVelocityXExceedLimit;
    private Long  angularVelocityXStartTime;

    public Long getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(Long lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public boolean isSpeedExceedLimit() {
        return speedExceedLimit;
    }

    public void setSpeedExceedLimit(boolean speedExceedLimit) {
        this.speedExceedLimit = speedExceedLimit;
    }

    public Long getSpeedStartTime() {
        return speedStartTime;
    }

    public void setSpeedStartTime(Long speedStartTime) {
        this.speedStartTime = speedStartTime;
    }

    public boolean isLonAccExceedLimit() {
        return lonAccExceedLimit;
    }

    public void setLonAccExceedLimit(boolean lonAccExceedLimit) {
        this.lonAccExceedLimit = lonAccExceedLimit;
    }

    public Long getLonAccStartTime() {
        return lonAccStartTime;
    }

    public void setLonAccStartTime(Long lonAccStartTime) {
        this.lonAccStartTime = lonAccStartTime;
    }

    public boolean isLonAcc2ExceedLimit() {
        return lonAcc2ExceedLimit;
    }

    public void setLonAcc2ExceedLimit(boolean lonAcc2ExceedLimit) {
        this.lonAcc2ExceedLimit = lonAcc2ExceedLimit;
    }

    public Long getLonAcc2StartTime() {
        return lonAcc2StartTime;
    }

    public void setLonAcc2StartTime(Long lonAcc2StartTime) {
        this.lonAcc2StartTime = lonAcc2StartTime;
    }

    public boolean isLatAccExceedLimit() {
        return latAccExceedLimit;
    }

    public void setLatAccExceedLimit(boolean latAccExceedLimit) {
        this.latAccExceedLimit = latAccExceedLimit;
    }

    public Long getLatAccStartTime() {
        return latAccStartTime;
    }

    public void setLatAccStartTime(Long latAccStartTime) {
        this.latAccStartTime = latAccStartTime;
    }

    public boolean isLatAcc2ExceedLimit() {
        return latAcc2ExceedLimit;
    }

    public void setLatAcc2ExceedLimit(boolean latAcc2ExceedLimit) {
        this.latAcc2ExceedLimit = latAcc2ExceedLimit;
    }

    public Long getLatAcc2StartTime() {
        return latAcc2StartTime;
    }

    public void setLatAcc2StartTime(Long latAcc2StartTime) {
        this.latAcc2StartTime = latAcc2StartTime;
    }

    public boolean isAngularVelocityXExceedLimit() {
        return angularVelocityXExceedLimit;
    }

    public void setAngularVelocityXExceedLimit(boolean angularVelocityXExceedLimit) {
        this.angularVelocityXExceedLimit = angularVelocityXExceedLimit;
    }

    public Long getAngularVelocityXStartTime() {
        return angularVelocityXStartTime;
    }

    public void setAngularVelocityXStartTime(Long angularVelocityXStartTime) {
        this.angularVelocityXStartTime = angularVelocityXStartTime;
    }
}
