package net.wanji.business.schedule;

import com.alibaba.fastjson.JSONObject;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.WebsocketMessage;
import net.wanji.business.domain.bo.TrajectoryDetailBo;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.common.utils.DateUtils;
import net.wanji.framework.manager.AsyncManager;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class PreviewTask {

    private String key;
    private List<List<TrajectoryDetailBo>> sencedata;
    private int index;
    private boolean running;
    private ScheduledFuture<?> future;

    public PreviewTask(String key, List<List<TrajectoryDetailBo>> sencedata) {
        this.key = key;
        this.sencedata = sencedata;
        this.index = 0;
        this.running = true;
        this.future = AsyncManager.me().execute(() -> {
            try {
                if (!running) {
                    return;
                }
                if (index >= sencedata.size()) {
                    WebsocketMessage message = new WebsocketMessage(Constants.RedisMessageType.END, null, null);
                    WebSocketManage.sendInfo(key, JSONObject.toJSONString(message));
                    PlaybackSchedule.stopPreview(key);
                    return;
                }
                WebsocketMessage message = new WebsocketMessage(
                        Constants.RedisMessageType.TRAJECTORY,
                        DateUtils.secondsToDuration((int) Math.floor((double) (sencedata.size() - index) / 10)),
                        sencedata.get(index));
                WebSocketManage.sendInfo(key, JSONObject.toJSONString(message));
                index++;
            } catch (BusinessException | IOException e) {
                e.printStackTrace();
            }
        },0, 100);
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
