package net.wanji.business.exercise.dto;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-06-23 10:33 下午
 */
@Data
public class TestIssueResultDto {
    private Long timestamp;

    private String deviceId;

    //0-失败，1-成功
    private Integer status;

    //异常信息
    private String message;
}
