package net.wanji.business.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jenny
 * @create: 2024-06-23 9:41 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageIssueReqDto {
    private long timestamp;

    private String deviceId;

    private String md5;

    private String imageId;

    private String imgPath;
}
