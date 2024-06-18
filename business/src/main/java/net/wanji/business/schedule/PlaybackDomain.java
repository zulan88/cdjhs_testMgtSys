package net.wanji.business.schedule;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import net.wanji.business.common.Constants.RedisMessageType;
import net.wanji.business.domain.WebsocketMessage;
import net.wanji.business.exception.BusinessException;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.utils.DateUtils;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.framework.manager.AsyncManager;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/10 18:58
 * @Descriptoin:
 */
@Data
public class PlaybackDomain {
    private String key;
    private List<List<TrajectoryValueDto>> data;
    private int index;
    private boolean running;
    private ScheduledFuture<?> future;

    public PlaybackDomain(String key, List<List<TrajectoryValueDto>> data) {
        this.key = key;
        this.data = data;
        this.index = 0;
        this.running = true;
        this.future = AsyncManager.me().execute(() -> {
            // send data
            try {
                if (!running) {
                    return;
                }
                if (index >= data.size()) {
                    WebsocketMessage message = new WebsocketMessage(RedisMessageType.END, null, null);
                    WebSocketManage.sendInfo(key, JSONObject.toJSONString(message));
                    PlaybackSchedule.stopSendingData(key);
                    return;
                }
                WebsocketMessage message = new WebsocketMessage(
                        RedisMessageType.TRAJECTORY,
                        DateUtils.secondsToDuration((int) Math.floor((double) (data.size() - index) / 10)),
                        data.get(index));
                WebSocketManage.sendInfo(key, JSONObject.toJSONString(message));
                index++;
            } catch (BusinessException | IOException e) {
                e.printStackTrace();
            }
        }, 0, 100);
    }

    public void suspend() throws BusinessException {
        this.validFuture();
        if (!this.running) {
            throw new BusinessException("当前任务未处于运行状态");
        }
        this.running = false;
    }

    public void goOn() throws BusinessException {
        this.validFuture();
        this.running = true;
    }

    public synchronized void stopSendingData() throws BusinessException, IOException {
        this.validFuture();
        this.running = false;
        this.future.cancel(true);
    }

    private void validFuture() throws BusinessException {
        if (ObjectUtils.isEmpty(this.future)) {
            throw new BusinessException("任务不存在");
        }
    }
}
