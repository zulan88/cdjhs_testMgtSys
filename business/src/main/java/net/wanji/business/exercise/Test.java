package net.wanji.business.exercise;

import net.wanji.business.exercise.dto.ImageListReportReq;

import java.io.File;

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

        String objectName = "admin/2024/06/25/b7c7bef754c447e9a293f5420fb179bd.txt";
        String downloadPath = "/Users/jennydediannao/Desktop/download/";

        String filepath = downloadPath + "tess" + File.separator + objectName;
        File parentFile = new File(filepath).getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }

    }
}
