package net.wanji.business.oss;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import lombok.extern.slf4j.Slf4j;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.RedisKeyUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: jenny
 * @create: 2024-06-22 2:02 下午
 */
@Slf4j
public class UploadMultipartProgressListener implements ProgressListener {
    //当前分片已上传字节数
    private long bytesWritten = 0;
    //当前分片文件总大小
    private long totalBytes = -1;
    private boolean succeed = false;
    private String uploadId;
    private RedisCache redisCache;
    private long totalSize;
    private int totalChunks;
    private long chunkSize;
    private int chunkIndex;

    public UploadMultipartProgressListener(){

    }

    public UploadMultipartProgressListener(String uploadId, RedisCache redisCache, long totalSize, int totalChunks, long chunkSize, int chunkIndex){
        this.uploadId = uploadId;
        this.redisCache = redisCache;
        this.totalSize = totalSize;
        this.totalChunks = totalChunks;
        this.chunkSize = chunkSize;
        this.chunkIndex = chunkIndex;
        String key = RedisKeyUtils.getOssProgressDetailKey(uploadId);
        if(!redisCache.hasKey(key)){
            Map<String, Long> map = new HashMap<>();
            map.put(RedisKeyUtils.TOTAL_BYTES, totalSize);
            map.put(RedisKeyUtils.UPLOADED, 0L);
            this.redisCache.setCacheMap(key, map);
            this.redisCache.expire(key, 1, TimeUnit.DAYS);
        }
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        String key = RedisKeyUtils.getOssProgressDetailKey(uploadId);
        switch (eventType){
            case TRANSFER_STARTED_EVENT:
                log.info("分片上传事件-{}-切片{}开始上传", uploadId, chunkIndex);
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                //计算该分片所属上传事件已上传字节数
                long totalBytesWritten;
                if(chunkIndex == totalChunks - 1){
                    totalBytesWritten = totalSize - chunkSize + bytesWritten;
                }else{
                    totalBytesWritten = chunkIndex * chunkSize + bytesWritten;
                }
                this.redisCache.setCacheMapValue(key, RedisKeyUtils.UPLOADED, totalBytesWritten);
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                this.redisCache.setCacheMapValue(key, RedisKeyUtils.UPLOADED, totalSize);
                break;
            case TRANSFER_FAILED_EVENT:
                log.info("分片上传事件{}-切片{}上传失败", uploadId, chunkIndex);
                break;
            default:
                break;
        }
    }
}
