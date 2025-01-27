package net.wanji.common.utils.file;

import com.alibaba.fastjson.JSONObject;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.common.RealTestTrajectoryDto;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.uuid.IdUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件处理工具类
 *
 * @author ruoyi
 */
public class FileUtils {
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.close(os);
            IOUtils.close(fis);
        }
    }

    /**
     * 写数据到文件中
     *
     * @param data 数据
     * @return 目标文件
     * @throws IOException IO异常
     */
    public static String writeImportBytes(byte[] data) throws IOException {
        return writeBytes(data, WanjiConfig.getImportPath());
    }


    /**
     * 写数据到文件中
     *
     * @param data      数据
     * @param uploadDir 目标文件
     * @return 目标文件
     * @throws IOException IO异常
     */
    public static String writeBytes(List<byte[]> data, String uploadDir, String extension) throws IOException {
        FileOutputStream fos = null;
        String pathName = "";
        try {
            pathName = DateUtils.datePath() + "/" + IdUtils.fastUUID() + "." + extension;
            File file = FileUploadUtils.getAbsoluteFile(uploadDir, pathName);
            fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            for (byte[] bytes : data) {
                writer.write(new String(bytes));
            }
        } finally {
            IOUtils.close(fos);
        }
        return FileUploadUtils.getPathFileName(uploadDir, pathName);
    }

    public static String writeRoute(List<?> data, String uploadDir, String extension) throws IOException {
        String pathName = "";
        PrintWriter writer = null;
        try {
            pathName = DateUtils.datePath() + "/" + IdUtils.fastUUID() + "." + extension;
            File file = FileUploadUtils.getAbsoluteFile(uploadDir, pathName);
            writer = new PrintWriter(file, "utf-8");
            CopyOnWriteArrayList<?> objects = new CopyOnWriteArrayList<>(data);
            for (Object content : objects) {
                writer.println(JSONObject.toJSONString(content));
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
        return FileUploadUtils.getPathFileName(uploadDir, pathName);
    }

    public static void writeRouteAbs(List<?> data, String filePath) throws IOException {
        String pathName = "";
        PrintWriter writer = null;
        try {
            pathName = FileUploadUtils.getAbsolutePathFileName(filePath);
            File file = new File(pathName);
            writer = new PrintWriter(file, "utf-8");
            CopyOnWriteArrayList<?> objects = new CopyOnWriteArrayList<>(data);
            for (Object content : objects) {
                writer.println(JSONObject.toJSONString(content));
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }


    /**
     * 写数据到文件中
     *
     * @param data      数据
     * @param uploadDir 目标文件
     * @return 目标文件
     * @throws IOException IO异常
     */
    public static String writeBytes(byte[] data, String uploadDir) throws IOException {
        FileOutputStream fos = null;
        String pathName = "";
        try {
            String extension = getFileExtendName(data);
            pathName = DateUtils.datePath() + "/" + IdUtils.fastUUID() + "." + extension;
            File file = FileUploadUtils.getAbsoluteFile(uploadDir, pathName);
            fos = new FileOutputStream(file);
            fos.write(data);
        } finally {
            IOUtils.close(fos);
        }
        return FileUploadUtils.getPathFileName(uploadDir, pathName);
    }

    public static List<List<TrajectoryValueDto>> readTrajectory(String filePath) {
        List<List<TrajectoryValueDto>> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                SimulationTrajectoryDto data = JSONObject.parseObject(line, SimulationTrajectoryDto.class);
                List<TrajectoryValueDto> mapList = data.getValue();
                list.add(mapList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<SimulationTrajectoryDto> readOriTrajectory(String filePath) {
        List<SimulationTrajectoryDto> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(JSONObject.parseObject(line, SimulationTrajectoryDto.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<RealTestTrajectoryDto> readRealRouteFile(String filePath) {
        List<RealTestTrajectoryDto> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                RealTestTrajectoryDto data = JSONObject.parseObject(line, RealTestTrajectoryDto.class);
                list.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<List<ClientSimulationTrajectoryDto>> readRealRouteFile2(String filePath) {
        List<List<ClientSimulationTrajectoryDto>> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.length() > 0){
                    List<ClientSimulationTrajectoryDto> data = JSONObject.parseArray(line, ClientSimulationTrajectoryDto.class);
                    list.add(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String readFile(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 删除文件
     *
     * @param filePath 文件
     * @return
     */
    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename) {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 检查文件是否可下载
     *
     * @param resource 需要下载的文件
     * @return true 正常 false 非法
     */
    public static boolean checkAllowDownload(String resource) {
        // 禁止目录上跳级别
        if (StringUtils.contains(resource, "..")) {
            return false;
        }

        // 检查允许下载的文件规则
        if (ArrayUtils.contains(MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION, FileTypeUtils.getFileType(resource))) {
            return true;
        }

        // 不在允许下载的文件规则
        return false;
    }

    /**
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains("Chrome")) {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }

    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     */
    public static void setAttachmentResponseHeader(HttpServletResponse response, String realFileName) throws UnsupportedEncodingException {
        String percentEncodedFileName = percentEncode(realFileName);

        StringBuilder contentDispositionValue = new StringBuilder();
        contentDispositionValue.append("attachment; filename=")
                .append(percentEncodedFileName)
                .append(";")
                .append("filename*=")
                .append("utf-8''")
                .append(percentEncodedFileName);

        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("Content-disposition", contentDispositionValue.toString());
        response.setHeader("download-filename", percentEncodedFileName);
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    public static String percentEncode(String s) throws UnsupportedEncodingException {
        String encode = URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        return encode.replaceAll("\\+", "%20");
    }

    /**
     * 获取图像后缀
     *
     * @param photoByte 图像数据
     * @return 后缀名
     */
    public static String getFileExtendName(byte[] photoByte) {
        String strFileExtendName = "jpg";
        if ((photoByte[0] == 71) && (photoByte[1] == 73) && (photoByte[2] == 70) && (photoByte[3] == 56)
                && ((photoByte[4] == 55) || (photoByte[4] == 57)) && (photoByte[5] == 97)) {
            strFileExtendName = "gif";
        } else if ((photoByte[6] == 74) && (photoByte[7] == 70) && (photoByte[8] == 73) && (photoByte[9] == 70)) {
            strFileExtendName = "jpg";
        } else if ((photoByte[0] == 66) && (photoByte[1] == 77)) {
            strFileExtendName = "bmp";
        } else if ((photoByte[1] == 80) && (photoByte[2] == 78) && (photoByte[3] == 71)) {
            strFileExtendName = "png";
        }
        return strFileExtendName;
    }

    /**
     * 获取文件名称 /profile/upload/2022/04/16/ruoyi.png -- ruoyi.png
     *
     * @param fileName 路径名称
     * @return 没有文件路径的名称
     */
    public static String getName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int lastUnixPos = fileName.lastIndexOf('/');
        int lastWindowsPos = fileName.lastIndexOf('\\');
        int index = Math.max(lastUnixPos, lastWindowsPos);
        return fileName.substring(index + 1);
    }

    /**
     * 获取不带后缀文件名称 /profile/upload/2022/04/16/ruoyi.png -- ruoyi
     *
     * @param fileName 路径名称
     * @return 没有文件路径和后缀的名称
     */
    public static String getNameNotSuffix(String fileName) {
        if (fileName == null) {
            return null;
        }
        String baseName = FilenameUtils.getBaseName(fileName);
        return baseName;
    }

    public static List<String> readZipFile(String path) throws IOException {
        List<String> result = new ArrayList<>();
        ZipEntry zipEntry = null;
        File file = new File(path);
        //判断文件是否存在
        if (file.exists()) {
            //解决包内文件存在中文时的中文乱码问题
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(path), Charset.forName("GBK"));
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                //遇到文件夹就跳过
                if (!zipEntry.isDirectory()) {
                    result.add(zipEntry.getName().substring(zipEntry.getName().lastIndexOf("/") + 1));
                }
            }
        }
        return result;

    }

}
