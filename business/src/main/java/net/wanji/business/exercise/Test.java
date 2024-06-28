package net.wanji.business.exercise;

import net.wanji.business.exercise.dto.ImageListReportReq;

import java.io.File;

/**
 * @author: jenny
 * @create: 2024-06-23 7:17 下午
 */
public class Test {
    public static void main(String[] args){
        String fileName = "0/489/687fadac-731b-4a57-b51a-ada508419659";
        String path = "/Users/jennydediannao/Desktop/download";
        File file = new File(path, fileName);
        System.out.println(file.getName());
        System.out.println(file.getParentFile().getAbsolutePath());
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

    }
}
