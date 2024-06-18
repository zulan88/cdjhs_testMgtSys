package net.wanji.common.common;

import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/9 9:40
 * @Descriptoin:
 */
public class SimulationTrajectoryDto {

//    private String source;

    /**
     * 时间戳类型（创建时间：CREATE_TIME）
     */
    private String timestampType;

    /**
     * 实际值（TrajectoryValueDto.class）
     */
    private List<TrajectoryValueDto> value;

    /**
     * 时间戳
     */
    private String timestamp;

    public String getTimestampType() {
        return timestampType;
    }

    public void setTimestampType(String timestampType) {
        this.timestampType = timestampType;
    }

    public List<TrajectoryValueDto> getValue() {
        return value;
    }

    public void setValue(List<TrajectoryValueDto> value) {
        this.value = value;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
