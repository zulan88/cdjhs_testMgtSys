package net.wanji.business.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jenny
 * @create: 2024-06-23 5:21 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageListReportReq {
    private Long timestamp;

    private String deviceId;

    private Integer type;
}
