package net.wanji.business.exercise.dto;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-07-31 10:50 上午
 */
@Data
public class YkscResultDto {
    private Long timestamp;

    //0-域控 1-实车域控
    private Integer type;

    //设备唯一标识
    private String deviceId;

    //镜像唯一标识
    private String imageId;

    //镜像名称
    private String imageName;

    private String teamName;

    private String md5;

    //任务状态（1：占用；2: 空闲 ）
    private Integer status;
}
