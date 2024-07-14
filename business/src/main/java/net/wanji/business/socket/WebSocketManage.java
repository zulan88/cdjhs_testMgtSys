package net.wanji.business.socket;

import net.wanji.business.common.Constants.ChannelBuilder;
import net.wanji.business.common.Constants.ContentTemplate;
import net.wanji.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/11 15:54
 * @Descriptoin:
 */

@Validated
public class WebSocketManage {

    private static final Logger log = LoggerFactory.getLogger("business");

    /**
     * concurrent包的线程安全Map，用来存放每个客户端对应的CumWebSocket对象。
     */
    private static final ConcurrentHashMap<String, WebSocketProperties> CLIENTS = new ConcurrentHashMap<>();

    public static String buildKey(String userName, String id, String clientType, String signId) {
        return StringUtils.isEmpty(signId)
                ? StringUtils.format(ContentTemplate.SIMULATION_KEY_TEMPLATE, userName, id, clientType)
                : StringUtils.format(ContentTemplate.REAL_KEY_TEMPLATE, userName, id, clientType, signId);
    }

    public static void join(@NotNull WebSocketProperties webSocketProperties) {
        if (!ChannelBuilder.validClientType(webSocketProperties.getClientType())) {
            log.error(StringUtils.format("客户端类型{}不合法", webSocketProperties.getClientType()));
            webSocketProperties.closeSession();
            return;
        }
        String key = webSocketProperties.getKey();
        CLIENTS.put(key, webSocketProperties);
        log.info(StringUtils.format("客户端{}加入，当前在线数量：{}", key, getOnlineCount()));
    }

    public static void remove(String key, boolean couldRemove) {
        if (!CLIENTS.containsKey(key)) {
            log.error(StringUtils.format("remove:客户端{}不存在", key));
            return;
        }
        if(!couldRemove){
            return;
        }
        log.info("WebSocketManage remove:[{}]", key);
        CLIENTS.remove(key);
    }

    public static void sendInfo(String key, String message) {
        if (!CLIENTS.containsKey(key)) {
            //log.error(StringUtils.format("sendInfo:客户端{}不存在", key));
            CLIENTS.remove(key);
            return;
        }
        CLIENTS.get(key).sendMessage(message);
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
