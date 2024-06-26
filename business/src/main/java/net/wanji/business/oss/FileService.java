package net.wanji.business.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.domain.CdjhsMirrorMgt;
import net.wanji.business.mapper.CdjhsMirrorMgtMapper;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: jenny
 * @create: 2024-06-21 10:25 上午
 */
@Service
@Slf4j
public class FileService {
    @Autowired
    private OssConfig ossConfig;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CdjhsMirrorMgtMapper cdjhsMirrorMgtMapper;

    public Map<String, String> initMultipartUpload(String fileName){
        Map<String, String> map = new HashMap<>();
        String objectName = getObjectName(fileName);
        map.put("objectName", objectName);

        String uploadId = getUploadId(objectName);
        if(StringUtils.isNotEmpty(uploadId)){
            map.put("uploadId", uploadId);
        }
        return map;
    }

    public Map<String, String> upload(MultipartFile multipartFile, String requestId){
        Map<String, String> map = new HashMap<>();
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndPoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        InputStream inputStream = null;
        try {
            byte[] bytes = multipartFile.getBytes();
            inputStream = multipartFile.getInputStream();
            String filename = multipartFile.getOriginalFilename();
            assert filename != null;
            String objectName = getObjectName(filename);

            PutObjectRequest request = new PutObjectRequest(ossConfig.getBucketName(), objectName, inputStream);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentMD5(BinaryUtil.toBase64String(BinaryUtil.calculateMd5(bytes)));
            request.withProgressListener(new PutObjectProgressListener(requestId, redisCache));

            PutObjectResult result = ossClient.putObject(request);
            String eTag = result.getETag();
            map.put("imageId", eTag);
            String domainName = getDomainName();
            String cloudFilePath = domainName + objectName;
            map.put("cloudFilePath", cloudFilePath);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ossClient.shutdown();
            if(inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    public static String getObjectName(String fileName) {
        String datePath = DateUtils.getDatePath();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        //oss对象存储完整路径
        String username = SecurityUtils.getUsername();
        return username + File.separator + datePath + File.separator + uuid + extension;
    }

    public String getUploadId(String objectName) {
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndPoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try {
            InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(ossConfig.getBucketName(), objectName);
            InitiateMultipartUploadResult result = ossClient.initiateMultipartUpload(request);
            return result.getUploadId();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ossClient.shutdown();
        }
        return null;
    }

    public boolean uploadPart(String uploadId, String objectName, long totalSize, int totalChunks, MultipartFile multipartFile, long chunkSize, int chunkIndex) {
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndPoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream stream1 = null;
        try {
            byte[] bytes = multipartFile.getBytes();
            baos = cloneInputstream(multipartFile.getInputStream());
            assert baos != null;
            stream1 = new ByteArrayInputStream(baos.toByteArray());
            UploadPartRequest request = new UploadPartRequest();
            request.setBucketName(ossConfig.getBucketName());
            request.setKey(objectName);
            request.setUploadId(uploadId);
            request.setInputStream(stream1);
            request.setPartSize(chunkSize);
            request.setPartNumber(chunkIndex + 1);
            //分片md5校验
            String md5 = BinaryUtil.toBase64String(BinaryUtil.calculateMd5(bytes));
            request.setMd5Digest(md5);
            //分片上传添加进度监听器
            request.setProgressListener(new UploadMultipartProgressListener(uploadId, redisCache, totalSize, totalChunks, chunkSize, chunkIndex));

            UploadPartResult uploadPartResult = ossClient.uploadPart(request);
            PartETag partETag = uploadPartResult.getPartETag();
            String key = RedisKeyUtils.getOssMultipartUploadKey(uploadId);
            redisCache.setCacheMapValue(key, String.valueOf(chunkIndex), partETag);
            redisCache.expire(key, 1, TimeUnit.DAYS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            ossClient.shutdown();

            if (stream1 != null) {
                try {
                    stream1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Map<String, String> chunkMerge(String objectName, String uploadId){
        Map<String, String> map = new HashMap<>();
        String key = RedisKeyUtils.getOssMultipartUploadKey(uploadId);
        Map<String, PartETag> cacheMap = redisCache.getCacheMap(key);
        List<PartETag> partETags = new ArrayList<>(cacheMap.values());
        redisCache.deleteObject(key);//删除缓存key
        CompleteMultipartUploadResult result = completeMultipartUpload(objectName, uploadId, partETags);
        if(null != result){
            String eTag = result.getETag();
            map.put("imageId", eTag);
            String domainName = getDomainName();
            String cloudFilePath = domainName + objectName;
            map.put("cloudFilePath", cloudFilePath);
        }
        return map;
    }

    public CompleteMultipartUploadResult completeMultipartUpload(String objectName, String uploadId, List<PartETag> partETags) {
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndPoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try {
            CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(ossConfig.getBucketName(), objectName, uploadId, partETags);
            return ossClient.completeMultipartUpload(request);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            ossClient.shutdown();
        }
    }

    //将oss文件下载到本地并且存储md5
    @Async("fileDownloadHandlePool")
    public void saveLocalFile(CdjhsMirrorMgt cdjhsMirrorMgt){
        try {
            String pathCloud = cdjhsMirrorMgt.getMirrorPathCloud();
            String domainName = getDomainName();
            String objectName = pathCloud.substring(domainName.length());
            String localFilePath = download(objectName);
            if(StringUtils.isNotEmpty(localFilePath)){
                //更新本地存储路径
                cdjhsMirrorMgt.setMirrorPathLocal(localFilePath);
                FileInputStream inputStream = new FileInputStream(localFilePath);
                String md5 = DigestUtils.md5DigestAsHex(inputStream);
                //更新本地md5值
                cdjhsMirrorMgt.setMd5(md5);
                cdjhsMirrorMgtMapper.updateMirrorPathLocalInt(cdjhsMirrorMgt.getId(), localFilePath, md5);
            }
        } catch (Exception e){
            log.error("更新镜像列表-{}本地存储路径和md5发生错误", cdjhsMirrorMgt.getId());
            e.printStackTrace();
        }
    }

    public String getDomainName() {
        return "https://" +
                ossConfig.getBucketName() +
                "." +
                ossConfig.getEndPoint() +
                "/";
    }

    public String download(String objectName){
        //String downloadPath = WanjiConfig.getDownloadPath();
        String downloadPath = "/Users/jennydediannao/Desktop/download/";
        //本地文件完整路径
        String filePath = downloadPath + ossConfig.getBucketName() + File.separator + objectName;
        File parentFile = new File(filePath).getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndPoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try {
            DownloadFileRequest downloadFileRequest = new DownloadFileRequest(ossConfig.getBucketName(), objectName);
            downloadFileRequest.setDownloadFile(filePath);
            downloadFileRequest.setPartSize(10 * 1024 * 1024);
            downloadFileRequest.setTaskNum(5);
            downloadFileRequest.setEnableCheckpoint(true);
            //设置断点记录文件地址 下载完成后，该文件会被删除
            String checkPointFilePath = filePath + ".dcp";
            downloadFileRequest.setCheckpointFile(checkPointFilePath);
            //下载文件
            DownloadFileResult result = ossClient.downloadFile(downloadFileRequest);
            //下载成功 会返回文件元数据
            ObjectMetadata metadata = result.getObjectMetadata();
            return filePath;
        } catch (Throwable e){
            log.error("从阿里云下载文件-{}到本地失败", objectName);
            e.printStackTrace();
            return null;
        } finally {
            ossClient.shutdown();
        }
    }

    public static ByteArrayOutputStream cloneInputstream(InputStream inputStream){
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1){
                baos.write(buffer, 0, length);
            }
            baos.flush();
            return baos;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUploadFileProgress(String requestId){
        String key = RedisKeyUtils.getOssProgressDetailKey(requestId);
        Map<String, Long> map = redisCache.getCacheMap(key);
        if(Objects.isNull(map)){
            return "0%";
        }
        long totalBytes = map.get(RedisKeyUtils.TOTAL_BYTES);
        long uploaded = map.get(RedisKeyUtils.UPLOADED);
        return String.format("%.1f%%", (uploaded * 100.0 / totalBytes));
    }

    public boolean check(String pathLocal, String pathCloud) {
        String domainName = getDomainName();
        String localDir = WanjiConfig.getDownloadPath() + ossConfig.getBucketName() + File.separator;
        String localObjectName = pathLocal.substring(localDir.length());
        String cloudObjectName = pathCloud.substring(domainName.length());
        return localObjectName.equals(cloudObjectName);
    }
}
