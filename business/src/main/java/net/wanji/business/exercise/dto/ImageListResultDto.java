package net.wanji.business.exercise.dto;

import lombok.Data;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-06-23 5:57 下午
 */
@Data
public class ImageListResultDto {
    private Long timestamp;

    private String deviceId;

    private List<String> imageList;
}
