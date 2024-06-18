package net.wanji.common.common;

import com.alibaba.fastjson.JSONObject;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/14 17:16
 * @Descriptoin:
 */

public class SimulationMessage {
    /**
     * 类型（1：详情SimulationInfoDto.class；2：轨迹SimulationTrajectoryDto.class; 3.评分）
     */
    private String type;
    private Object value;

    public SimulationMessage() {
    }

    public SimulationMessage(String type, JSONObject value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
