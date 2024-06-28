package net.wanji.business.exercise.dto.evaluation;

import lombok.Data;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-06-27 9:17 上午
 */
@Data
public class SceneDetail {
    private Integer sequence;

    //场景用时 单位s
    private Integer duration;

    private Double securityScore;

    private Double efficencyScore;

    private Double comfortScore;

    private Double sceneScore;

    //场景评分输出时主车轨迹时间戳
    private Long timestamp;

    private List<IndexDetail> securityIndexDetails;

    private List<IndexDetail> efficencyIndexDetails;

    private ComfortDetail comfortDetails;

    //场景开始时间戳
    private Long startTime;

    //场景结束时间戳
    private Long endTime;

    //场景期望时长
    private Integer expectDuration;
}
