package net.wanji.socket.realTest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/11 15:54
 * @Descriptoin:
 */

public class RealWebSocketManage {

    private static final Logger log = LoggerFactory.getLogger("business");

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的CumWebSocket对象。
     */
    private static final ConcurrentHashMap<String, RealWebSocketServer> CLIENTS = new ConcurrentHashMap<>();

    public static final String TEMPLETE_KEY = "{}_{}_{}";

    public static void join(String id, RealWebSocketServer socketServer) {
        if (CLIENTS.containsKey(id)) {
            log.info(String.format("客户端%s已加入", id));
            return;
        }
        CLIENTS.put(id, socketServer);
        log.info(String.format("客户端%s加入，当前在线数量：%d", id, getOnlineCount()));
    }

    public static void close(String id) {
        if (!CLIENTS.containsKey(id)) {
            log.error(String.format("close:客户端%s不存在", id));
            return;
        }
        CLIENTS.get(id).onClose();
    }

    public static void remove(String id) {
        if (!CLIENTS.containsKey(id)) {
            log.error(String.format("remove:客户端%s不存在", id));
            return;
        }
        CLIENTS.remove(id);
    }

    public static void sendInfo(String id, String message) {
        if (!CLIENTS.containsKey(id)) {
//            log.error(String.format("sendInfo:客户端%s不存在", id));
            return;
        }
        CLIENTS.get(id).getSession().getAsyncRemote().sendText(message);
    }

    /**
     * 群发
     *
     * @param message
     */
    private static void sendAll(String message) {
        for (Map.Entry<String, RealWebSocketServer> client : CLIENTS.entrySet()) {
            try {
                client.getValue().getSession().getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.error("发送websocket错误",e);
            }
        }
    }

    /**
     * 当前在线人数
     *
     * @return
     */
    public static synchronized int getOnlineCount() {
        return CLIENTS.size();
    }

}
