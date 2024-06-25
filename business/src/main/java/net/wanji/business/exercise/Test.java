package net.wanji.business.exercise;

import net.wanji.business.exercise.dto.ImageListReportReq;

/**
 * @author: jenny
 * @create: 2024-06-23 7:17 下午
 */
public class Test {
    public static void main(String[] args){
        ImageListReportReq image = ImageListReportReq.builder()
                .timestamp(System.currentTimeMillis())
                .deviceId("001")
                .type(4)
                .build();

    }
}
