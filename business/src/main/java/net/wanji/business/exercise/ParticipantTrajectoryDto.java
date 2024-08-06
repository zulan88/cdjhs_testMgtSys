package net.wanji.business.exercise;

import net.wanji.common.common.TrajectoryValueDto;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-08-05 4:02 下午
 */
public class ParticipantTrajectoryDto {
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
    private Long timestamp;

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

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
