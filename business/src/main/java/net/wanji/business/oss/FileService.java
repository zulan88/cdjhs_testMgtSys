package net.wanji.business.oss;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.domain.CdjhsMirrorMgt;
import net.wanji.business.domain.dto.TjDeviceDetailDto;
import net.wanji.business.domain.vo.DeviceDetailVo;
import net.wanji.business.entity.TjDeviceDetail;
import net.wanji.business.exercise.dto.ImageDeleteReqDto;
import net.wanji.business.listener.ImageDelResultListener;
import net.wanji.business.mapper.CdjhsMirrorMgtMapper;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.common.common.Constants;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.exception.ServiceException;
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
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    @Autowired
    private TjDeviceDetailMapper tjDeviceDetailMapper;

    @Autowired
    private ImageDelResultListener imageDelResultListener;

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
            request.withProgressListener(new PutObjectProgressListener(requestId, multipartFile.getSize(), redisCache));

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

    @Async("fileDownloadHandlePool")
    public void deleteMirrors(List<CdjhsMirrorMgt> list){
        list = list.stream()
                .filter(item -> StringUtils.isNotEmpty(item.getMirrorPathCloud()))
                .collect(Collectors.toList());
        //删除云端镜像
        int batchSize = 1000;//阿里云批量删除每次最多1000个文件
        int totalSize = list.size();
        int splitCount = (totalSize + batchSize - 1) / batchSize;
        for(int i = 0; i < splitCount; i++){
            int fromIndex = i * batchSize;
            int toIndex = Math.min((i + 1) * batchSize, totalSize);
            List<CdjhsMirrorMgt> mirrors = list.subList(fromIndex, toIndex);
            //获取待删除镜像objectName
            List<String> objectNameList = mirrors.stream()
                    .map(item -> {
                        String mirrorPathCloud = item.getMirrorPathCloud();
                        String domainName = getDomainName();
                        return mirrorPathCloud.substring(domainName.length());
                    }).collect(Collectors.toList());
            delete(objectNameList);
        }
        //删除本地镜像和通知域控删除镜像
        list = list.stream()
                .filter(item -> StringUtils.isNotEmpty(item.getMirrorPathLocal()))
                .collect(Collectors.toList());
        //查询域控设备
        TjDeviceDetailDto query = new TjDeviceDetailDto();
        query.setDeviceType(Constants.YUKONG);
        List<DeviceDetailVo> ykList = tjDeviceDetailMapper.selectByCondition(query);
        List<String> ykUniques = ykList.stream()
                .map(TjDeviceDetail::getUniques)
                .collect(Collectors.toList());
        for(CdjhsMirrorMgt mirrorMgt: list){
            String localFilePath = mirrorMgt.getMirrorPathLocal();
            String imageId = mirrorMgt.getImageId();
            File file = new File(localFilePath);
            if(file.exists() && file.isFile()){
                boolean delete = file.delete();
                log.info("本地镜像{}删除成功: {}", localFilePath, delete);
            }
            if(!ykUniques.isEmpty()){
                for(String uniques: ykUniques){
                    ImageDeleteReqDto imageDelReq = ImageDeleteReqDto.builder()
                            .timestamp(System.currentTimeMillis())
                            .deviceId(uniques)
                            .imageId(imageId)
                            .build();

                    String imageDelMessage = JSONObject.toJSONString(imageDelReq);
                    JSONObject imageDel = JSONObject.parseObject(imageDelMessage);
                    String imageDelChannel = RedisKeyUtils.getImageDelChannel(uniques);
                    redisCache.publishMessage(imageDelChannel, imageDel);
                    log.info("给域控{}下发镜像清除指令: {}", uniques, imageDelMessage);
                    Integer status = imageDelResultListener.awaitingMessage(uniques, imageDelReq.getImageId(), 5, TimeUnit.SECONDS);
                    log.info("收到域控{}上报镜像清除结果: {}", uniques, status);
                }
            }

        }
    }

    //批量删除
    public void delete(List<String> objectNameList) {
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndPoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try {
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(ossConfig.getBucketName()).withKeys(objectNameList).withEncodingType("url");
            DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(deleteObjectsRequest);
            List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();
            for(String object: deletedObjects){
                String objectName = URLDecoder.decode(object, "UTF-8");
                log.info("云端镜像{}删除成功", objectName);
            }
        }catch (Exception e){
            log.error("阿里云批量删除文件失败: {}", JSONObject.toJSONString(objectNameList));
            e.printStackTrace();
        }finally {
            ossClient.shutdown();
        }
    }

    public void delete(String objectName){
        OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndPoint(), ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
        try {
            ossClient.deleteObject(ossConfig.getBucketName(), objectName);
            log.info("云端镜像{}删除成功", objectName);
        }catch (Exception e){
            log.error("云端镜像{}删除失败", objectName);
            e.printStackTrace();
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
            //更新本地存储路径
            FileInputStream inputStream = new FileInputStream(localFilePath);
            String md5 = DigestUtils.md5DigestAsHex(inputStream);
            //更新本地md5值
            CdjhsMirrorMgt mirrorMgt = new CdjhsMirrorMgt();
            mirrorMgt.setId(cdjhsMirrorMgt.getId());
            mirrorMgt.setMirrorPathLocal(localFilePath);
            mirrorMgt.setMd5(md5);
            mirrorMgt.setUploadStatus(0);
            mirrorMgt.setUpdateTime(DateUtils.getNowDate());
            cdjhsMirrorMgtMapper.updateCdjhsMirrorMgt(mirrorMgt);

            //删除云端镜像
            delete(objectName);
        } catch (Exception e){
            CdjhsMirrorMgt mirrorMgt = new CdjhsMirrorMgt();
            mirrorMgt.setId(cdjhsMirrorMgt.getId());
            mirrorMgt.setUploadStatus(1);
            mirrorMgt.setUpdateTime(DateUtils.getNowDate());
            cdjhsMirrorMgtMapper.updateCdjhsMirrorMgt(mirrorMgt);
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
        String downloadPath = WanjiConfig.getDownloadPath();
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
            throw new ServiceException("从阿里云下载文件失败");
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

    public double getUploadFileProgress(String requestId){
        String key = RedisKeyUtils.getOssProgressDetailKey(requestId);
        Map<String, Long> map = redisCache.getCacheMap(key);
        if(Objects.isNull(map) || map.isEmpty()){
            return 0.0;
        }
        log.info(String.valueOf(map.get(RedisKeyUtils.TOTAL_BYTES)));
        log.info(String.valueOf(map.get(RedisKeyUtils.UPLOADED)));
        long totalBytes = Long.parseLong(String.valueOf(map.get(RedisKeyUtils.TOTAL_BYTES)));
        long uploaded = Long.parseLong(String.valueOf(map.get(RedisKeyUtils.UPLOADED)));
        return uploaded * 100.0 / totalBytes;
    }

    public boolean check(String pathLocal, String pathCloud) {
        String domainName = getDomainName();
        String localDir = WanjiConfig.getDownloadPath() + ossConfig.getBucketName() + File.separator;
        String localObjectName = pathLocal.substring(localDir.length());
        String cloudObjectName = pathCloud.substring(domainName.length());
        return localObjectName.equals(cloudObjectName);
    }
}
