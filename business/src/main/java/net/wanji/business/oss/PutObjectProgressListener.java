package net.wanji.business.oss;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import lombok.extern.slf4j.Slf4j;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.RedisKeyUtils;

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

    public PutObjectProgressListener(String requestId, RedisCache redisCache){
        this.requestId = requestId;
        this.redisCache = redisCache;
        String key = RedisKeyUtils.getOssProgressDetailKey(requestId);
        this.redisCache.setCacheMapValue(key, RedisKeyUtils.TOTAL_BYTES, totalBytes);
        this.redisCache.setCacheMapValue(key, RedisKeyUtils.UPLOADED, bytesWritten);
        this.redisCache.expire(key, 1, TimeUnit.DAYS);
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        String key = RedisKeyUtils.getOssProgressDetailKey(this.requestId);
        switch (eventType){
            case TRANSFER_STARTED_EVENT:
                log.info("Start to upload......");
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                this.redisCache.setCacheMapValue(key, RedisKeyUtils.TOTAL_BYTES, totalBytes);
                log.info(this.totalBytes + " bytes in total will be uploaded to OSS");
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                this.redisCache.setCacheMapValue(key, RedisKeyUtils.UPLOADED, bytesWritten);
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                log.info("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                break;
            case TRANSFER_FAILED_EVENT:
                log.info("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                break;
            default:
                break;
        }
    }
}