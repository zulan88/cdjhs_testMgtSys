package net.wanji.business.exercise.dto;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-06-23 9:18 下午
 */
@Data
public class ImageDelResultDto {
    private Long timestamp;

    private String deviceId;

    private String imageId;

    //删除状态 0-失败 1-成功
    private Integer imageStatus;
}
