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
 * @create: 2024-06-21 4:29 下午
 */
@Slf4j
public class PutObjectProgressListener implements ProgressListener {
    private long bytesWritten = 0;
    private long totalBytes = -1;
    private boolean succeed = false;
    private String requestId;
    private RedisCache redisCache;

    public PutObjectProgressListener(){

    }

    public PutObjectProgressListener(String requestId, long totalSize, RedisCache redisCache){
        this.requestId = requestId;
        this.redisCache = redisCache;
        this.totalBytes = totalSize;
        String key = RedisKeyUtils.getOssProgressDetailKey(requestId);
        Map<String, Long> map = new HashMap<>();
        map.put(RedisKeyUtils.TOTAL_BYTES, totalBytes);
        map.put(RedisKeyUtils.UPLOADED, bytesWritten);
        this.redisCache.setCacheMap(key, map);
        this.redisCache.expire(key, 1, TimeUnit.DAYS);
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        String key = RedisKeyUtils.getOssProgressDetailKey(this.requestId);
        switch (eventType){
            case TRANSFER_STARTED_EVENT:
                log.info("文件上传开始...");
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                this.redisCache.setCacheMapValue(key, RedisKeyUtils.TOTAL_BYTES, totalBytes);
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                this.redisCache.setCacheMapValue(key, RedisKeyUtils.UPLOADED, bytesWritten);
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                break;
            case TRANSFER_FAILED_EVENT:
                log.info("文件上传失败...");
                break;
            default:
                break;
        }
    }
}
