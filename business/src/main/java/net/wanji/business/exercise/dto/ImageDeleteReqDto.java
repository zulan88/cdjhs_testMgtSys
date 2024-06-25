package net.wanji.business.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jenny
 * @create: 2024-06-23 9:07 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDeleteReqDto {
    private Long timestamp;

    private String deviceId;

    private String imageId;
}
