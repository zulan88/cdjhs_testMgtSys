package net.wanji.business.schedule;

import com.alibaba.fastjson.JSONObject;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.Tjshape;
import net.wanji.business.domain.WebsocketMessage;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.common.utils.DateUtils;
import net.wanji.framework.manager.AsyncManager;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class PlaybackOnsite {

    private String key;
    private List<Tjshape> data;
    private int index;
    private boolean running;
    private ScheduledFuture<?> future;

    public PlaybackOnsite(String key, List<Tjshape> data) {
        this.key = key;
        this.data = data;
        this.index = 0;
        this.running = true;
        long period = 100;
        if (ObjectUtils.isEmpty(data)) {
            return;
        } else if (data.size() > 2) {
            period = data.get(1).getDuration() - data.get(0).getDuration();
        }else {
            return;
        }
        long finalPeriod = period;
        this.future = AsyncManager.me().execute(() -> {
            // send data
            try {
                if (!running) {
                    return;
                }
                if (index >= data.size()) {
                    WebsocketMessage message = new WebsocketMessage(Constants.RedisMessageType.END, null, null);
                    WebSocketManage.sendInfo(key, JSONObject.toJSONString(message));
                    PlaybackSchedule.stopSendingDataOnsite(key);
                    return;
                }
                WebsocketMessage message = new WebsocketMessage(
                        Constants.RedisMessageType.TRAJECTORY,
                        DateUtils.secondsToDuration((int) Math.floor((double) (data.size() - index) * finalPeriod / 1000)),
                        data.get(index));
                WebSocketManage.sendInfo(key, JSONObject.toJSONString(message));
                index++;
            } catch (BusinessException | IOException e) {
                e.printStackTrace();
            }
        }, 0, period);
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
