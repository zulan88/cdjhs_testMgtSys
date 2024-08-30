package net.wanji.business.exercise.dto.luansheng;

/**
 * @author: jenny
 * @create: 2024-08-29 4:54 下午
 */
public class CommonField {
    private Long lastTimestamp;
    private boolean speedExceedLimit;
    private Long speedStarTime;
    private boolean lonAccExceedLimit;
    private Long lonAccStarTime;
    private boolean lonAcc2ExceedLimit;
    private Long lonAcc2StarTime;
    private boolean latAccExceedLimit;
    private Long latAccStarTime;
    private boolean latAcc2ExceedLimit;
    private Long latAcc2StarTime;
    private boolean angularVelocityXExceedLimit;
    private Long  angularVelocityXStarTime;

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

    public Long getSpeedStarTime() {
        return speedStarTime;
    }

    public void setSpeedStarTime(Long speedStarTime) {
        this.speedStarTime = speedStarTime;
    }

    public boolean isLonAccExceedLimit() {
        return lonAccExceedLimit;
    }

    public void setLonAccExceedLimit(boolean lonAccExceedLimit) {
        this.lonAccExceedLimit = lonAccExceedLimit;
    }

    public Long getLonAccStarTime() {
        return lonAccStarTime;
    }

    public void setLonAccStarTime(Long lonAccStarTime) {
        this.lonAccStarTime = lonAccStarTime;
    }

    public boolean isLonAcc2ExceedLimit() {
        return lonAcc2ExceedLimit;
    }

    public void setLonAcc2ExceedLimit(boolean lonAcc2ExceedLimit) {
        this.lonAcc2ExceedLimit = lonAcc2ExceedLimit;
    }

    public Long getLonAcc2StarTime() {
        return lonAcc2StarTime;
    }

    public void setLonAcc2StarTime(Long lonAcc2StarTime) {
        this.lonAcc2StarTime = lonAcc2StarTime;
    }

    public boolean isLatAccExceedLimit() {
        return latAccExceedLimit;
    }

    public void setLatAccExceedLimit(boolean latAccExceedLimit) {
        this.latAccExceedLimit = latAccExceedLimit;
    }

    public Long getLatAccStarTime() {
        return latAccStarTime;
    }

    public void setLatAccStarTime(Long latAccStarTime) {
        this.latAccStarTime = latAccStarTime;
    }

    public boolean isLatAcc2ExceedLimit() {
        return latAcc2ExceedLimit;
    }

    public void setLatAcc2ExceedLimit(boolean latAcc2ExceedLimit) {
        this.latAcc2ExceedLimit = latAcc2ExceedLimit;
    }

    public Long getLatAcc2StarTime() {
        return latAcc2StarTime;
    }

    public void setLatAcc2StarTime(Long latAcc2StarTime) {
        this.latAcc2StarTime = latAcc2StarTime;
    }

    public boolean isAngularVelocityXExceedLimit() {
        return angularVelocityXExceedLimit;
    }

    public void setAngularVelocityXExceedLimit(boolean angularVelocityXExceedLimit) {
        this.angularVelocityXExceedLimit = angularVelocityXExceedLimit;
    }

    public Long getAngularVelocityXStarTime() {
        return angularVelocityXStarTime;
    }

    public void setAngularVelocityXStarTime(Long angularVelocityXStarTime) {
        this.angularVelocityXStarTime = angularVelocityXStarTime;
    }
}
