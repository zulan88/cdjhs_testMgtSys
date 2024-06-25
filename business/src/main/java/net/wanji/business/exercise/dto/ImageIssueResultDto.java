package net.wanji.business.exercise.dto;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-06-23 9:57 下午
 */
@Data
public class ImageIssueResultDto {
    private Long timestamp;

    private String deviceId;

    private String imageId;

    //校验状态 0-失败 1-成功
    private Integer imageStatus;
}
