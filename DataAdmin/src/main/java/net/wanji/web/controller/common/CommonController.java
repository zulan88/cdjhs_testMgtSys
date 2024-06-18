package net.wanji.web.controller.common;

import cn.hutool.core.io.FileUtil;
import io.swagger.annotations.ApiOperation;
import net.wanji.business.common.Constants.DefaultLabel;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.constant.Constants;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.file.FileUploadUtils;
import net.wanji.common.utils.file.FileUtils;
import net.wanji.framework.config.ServerConfig;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;


    private static final String FILE_DELIMETER = ",";

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = WanjiConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @ApiOperation("上传")
    @PostMapping("/upload")
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = WanjiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @ApiOperation("上传")
    @PostMapping("/uploadZips")
    public AjaxResult uploadZipFile(@RequestParam("files") List<MultipartFile> files) throws Exception {
        try {
            // 上传文件路径
            String filePath = WanjiConfig.getUploadPath();
            List<Map<String,String>>data = new ArrayList<>();
            // 上传并返回新文件名称
            for(MultipartFile file: files) {
                String res = FileUploadUtils.uploadzip(filePath, file);
                String fileName = res.split(",")[1];
                String absfile = res.split(",")[0];
                String outputFolder = WanjiConfig.getScenelibPath() + File.separator + DateUtils.datePath();
                File folder = new File(outputFolder);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
//                Charset charset = Charset.forName(character);
                ZipFile zipFile = new ZipFile(absfile);
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                String xodrFile = "";
                String xoscFile = "";
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    String entryName = (int) (System.currentTimeMillis() % 1000)+"_"+entry.getName();
                    File entryFile = new File(outputFolder, entryName);

                    if (entry.isDirectory()) {
                        entryFile.mkdirs();
                    } else {
                        InputStream inputStream = zipFile.getInputStream(entry);
                        FileOutputStream outputStream = new FileOutputStream(entryFile);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }

                        inputStream.close();
                        outputStream.close();
                    }
                    String filepath = entryFile.getAbsolutePath();
                    if (filepath.endsWith("xodr")) {
                        xodrFile = filepath;
                    }
                    if (filepath.endsWith("xosc")) {
                        xoscFile = filepath;
                    }
                }
                String url = serverConfig.getUrl() + fileName;
                Map<String,String> ajax=new HashMap<>();
                ajax.put("url", url);
                ajax.put("fileName", fileName);
                ajax.put("newFileName", FileUtils.getName(fileName));
                ajax.put("originalFilename", file.getOriginalFilename());
                ajax.put("xodrFile", xodrFile);
                ajax.put("xoscFile", xoscFile);
                data.add(ajax);
            }
            return AjaxResult.success(data);
        } catch (Exception e) {
            return AjaxResult.error("编码异常，请剔除中文，并修改为UTF-8");
        }
    }

    /**
     * 通用上传请求（单个）
     */
    @ApiOperation("上传Excel")
    @PostMapping("/uploadExcel")
    public AjaxResult uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = WanjiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.uploadExcel(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            ajax.put("url", url);
            ajax.put("fileName", fileName);
            ajax.put("newFileName", FileUtils.getName(fileName));
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }




    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception {
        try {
            // 上传文件路径
            String filePath = WanjiConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files) {
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @PostMapping ("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            if (!FileUtils.checkAllowDownload(resource)) {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = WanjiConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());

        } catch (Exception e) {
            log.error("下载文件失败", e);

        }
    }

    @PostMapping ("/download/makeAnAppointment")
    public void downloadMakeAnAppointment(HttpServletResponse response)
            throws Exception {
        try {
            // 本地资源路径
            String localPath = WanjiConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + "/makeAnAppointment/预约申请表.xlsx";
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());

        } catch (Exception e) {
            log.error("下载文件失败", e);

        }
    }
    @PostMapping("/batchDownload/resource")
    public void  batchDownload(@RequestBody List<String> resourcePaths, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String downloads = WanjiConfig.getDownloadPath();
        File downloadpath = new File(downloads);
        if(!downloadpath.exists()){
            downloadpath.mkdirs();
        }
//        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss"));
        String fileName = "batchdownload"+System.currentTimeMillis()+".zip";
        File zipFile = new File(downloadpath, fileName);
        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(zipFile.toPath()))) {
            for (String resourcePath : resourcePaths) {
                String localPath = WanjiConfig.getProfile();
                String downloadPath = localPath + StringUtils.substringAfter(resourcePath, Constants.RESOURCE_PREFIX);
                File sourceFile = new File(downloadPath);

                // 添加资源到ZIP文件
                zipOut.putNextEntry(new ZipEntry(sourceFile.getName()));
                Files.copy(sourceFile.toPath(), zipOut);
                zipOut.closeEntry();
            }
            zipOut.close();
            FileUtils.setAttachmentResponseHeader(response, fileName);
            FileUtils.writeBytes(zipFile.getPath(), response.getOutputStream());
            FileUtils.deleteFile(downloadpath.getPath());
        }catch (Exception e){
            log.error("下载文件失败", e);
        }


    }

    @GetMapping ("/getDefaultSign")
    public AjaxResult getDefaultSign() {
       return AjaxResult.success(DefaultLabel.getDefaultLabel());
    }

    /**
     * 标注规则上传（单个）
     */
    @PostMapping("/tagupload")
    public AjaxResult taguploadFile(MultipartFile file) throws Exception {
        try {
            // 上传文件路径
            String filePath = WanjiConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            AjaxResult ajax = AjaxResult.success();
            //文件下载地址连接
            ajax.put("url", url);
            //标注存储路径
            ajax.put("tagStoragePath", fileName);
            //标注文件名称
            ajax.put("tagFileName", FileUtils.getName(fileName));
            //原文件名称
            ajax.put("originalFilename", file.getOriginalFilename());
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/downloadZip")
    public void downloadFiles(HttpServletRequest request, HttpServletResponse response, String[] urls) {

        //筛选存在于数据库的图片
        String zipName = null;
        for (String url : urls) {
            String[] split = url.split("/");
            for (String s : split) {
                if (s.contains("G")) {
                    //处理文件
                     zipName = s + ".zip";
                    break;
                }
            }
        }
       
        response.setHeader("content-type", "application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + zipName);
        response.setCharacterEncoding("utf-8");
        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            for (String iUrl : urls) {
                URL url = new URL(iUrl);
                String name = FilenameUtils.getName(iUrl);
                boolean rource = getRource(String.valueOf(url));
                if (!rource) {
                    continue;
                }
                switch (name) {
                    case "0.jpeg":
                        zipOut.putNextEntry(new ZipEntry("车头.jpeg"));
                        break;
                    case "1.jpeg":
                        zipOut.putNextEntry(new ZipEntry("车尾.jpeg"));
                        break;
                    case "2.jpeg":
                        zipOut.putNextEntry(new ZipEntry("车身.jpeg"));
                        break;
                    case "3.jpeg":
                        zipOut.putNextEntry(new ZipEntry("车牌.jpeg"));
                        break;
                    case "4.mp4":
                        zipOut.putNextEntry(new ZipEntry("视频.mp4"));
                        break;
                    default:
                        break;
                }
                //zipOut.putNextEntry(new ZipEntry(FilenameUtils.getName(iUrl)));

                InputStream in = new BufferedInputStream(url.openStream());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer)) != -1) {
                    zipOut.write(buffer, 0, len);
                }
                //zipOut.write(in.readAllBytes());

                zipOut.closeEntry();
                in.close();
            }
        } catch (IOException e) {
            log.error("导出相册压缩包失败", e);
        }
    }
//判断链接是否存在
    public boolean getRource(String source) {
        try {
            URL url = new URL(source);
            URLConnection uc = url.openConnection();
            InputStream in = uc.getInputStream();
            if (source.equalsIgnoreCase(uc.getURL().toString()))
                in.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("/xmlview")
    public ResponseEntity<String> APDownload(String AP) throws Exception {
        Path filePath = Paths.get(AP); // 替换为你要读取的文件的路径

        try {
            String fileContent = new String(Files.readAllBytes(filePath));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE); // 设置Content-Type为适当的值，例如PDF

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .internalServerError().body("未找到文件");
    }

}
