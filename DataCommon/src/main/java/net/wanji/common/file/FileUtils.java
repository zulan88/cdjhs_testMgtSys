package net.wanji.common.file;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import net.wanji.common.utils.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author guodejun
 * @createTime 2022/9/20 14:28
 * @description
 */
@Log4j2
public class FileUtils {

    /**
     * 文件压缩，接收一批文件路径，压缩完成后返回文件路径
     *
     * @param filePaths
     * @return
     */
    public static String fileCompression(List<String> filePaths, String targetFile) {

        try (FileOutputStream fileOut = new FileOutputStream(targetFile);
             CheckedOutputStream cos = new CheckedOutputStream(fileOut, new CRC32());
             ZipOutputStream zipOut = new ZipOutputStream(cos)) {
            String baseDir = "";
            for (String filePath : filePaths) {
                File sourceFile = CompressUtil.validateSourcePath(filePath);
                CompressUtil.compressToZip(sourceFile, zipOut, baseDir);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetFile;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return deleteFile(file);
    }

    /**
     * 删除文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (!file.exists()) {
            System.out.println("the file is not exist, file name: " + file.getName());
            throw new RuntimeException("the file is not exist");
        }
        return file.delete();
    }


    /**
     * 上传文件存储
     *
     * @param file
     * @param fileName
     * @return
     */
    public static boolean upload(MultipartFile file, String fileName) {
        try {
            File dest = new File(fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


//    @PostMapping("/upload")
//    public AjaxResult upload(@RequestParam MultipartFile file, HttpServletResponse response) {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        if (file.isEmpty()) {
//            return AjaxResult.error(StatusCode.FILE_MISSING.getCode(), StatusCode.FILE_MISSING.getMessage());
//        }
//        if (file.getSize() > 1024 * 1024 * 500) {// 500M
//            return AjaxResult.error(StatusCode.FILE_TOO_LARGE.getCode(), StatusCode.FILE_TOO_LARGE.getMessage());
//        }
//        String originalFilename = file.getOriginalFilename();
//        if (null == originalFilename) {
//            return AjaxResult.error(StatusCode.FILE_NAME_MISSING.getCode(), StatusCode.FILE_NAME_MISSING.getMessage());
//        }
//        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
//        if (!"jar".equals(suffix)) {
//            return AjaxResult.error(StatusCode.FILE_SUFFIX_NOT_MATCH.getCode(),
//                    StatusCode.FILE_SUFFIX_NOT_MATCH.getMessage());
//        }
//        String fileName = System.currentTimeMillis() + "_" + originalFilename;
//        if (!sdkService.upload(file, fileName)) {
//            return AjaxResult.error(StatusCode.UPLOAD_FAILED.getCode(), StatusCode.UPLOAD_FAILED.getMessage());
//        }
//        JSONObject data = new JSONObject();
//        data.put("fileName", fileName);
//        System.out.println(globalData.getSdkBasePath());
//        return AjaxResult.success(data);
//    }

}
