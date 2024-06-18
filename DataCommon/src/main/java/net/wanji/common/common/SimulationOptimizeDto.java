package net.wanji.common.common;

import com.alibaba.fastjson.JSONArray;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author: guanyuduo
 * @date: 2023/10/16 17:52
 * @descriptoin:
 */
public class SimulationOptimizeDto {

    private Double distance;

    private List<Double> newPosition;

    private Double speed;

    private String type;

    private List<Double> oriPosition;

    private Integer time;

    private Integer index;

    private String id;

    public SimulationOptimizeDto(String id, String index, Object detail) {
        try {
            this.index = Integer.valueOf(index);
            this.id = id;
            if (ObjectUtils.isEmpty(detail)) {
                return;
            }
            JSONArray array = (JSONArray) detail;
            this.distance = array.getBigDecimal(0).doubleValue();
            this.newPosition = array.getJSONArray(1).toJavaList(Double.class);
            this.speed = array.getBigDecimal(2).doubleValue();
            this.type = array.getString(3);
            this.oriPosition = array.getJSONArray(4).toJavaList(Double.class);
            this.time = array.getInteger(5) / 1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public List<Double> getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(List<Double> newPosition) {
        this.newPosition = newPosition;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getOriPosition() {
        return oriPosition;
    }

    public void setOriPosition(List<Double> oriPosition) {
        this.oriPosition = oriPosition;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
