package net.wanji.socket.realTest;

import net.wanji.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;

/**
 * @Auther: guanyuduo
 * @Date: 2022/6/16 10:30
 * @Descriptoin:
 */
@ServerEndpoint(value = "/realWs/{channel}")
@Component
public class RealWebSocketServer {

    private static final Logger log = LoggerFactory.getLogger("business");

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        String id = getChannelFromSession(session);
        if (StringUtils.isEmpty(id)) {
            onClose();
            return;
        }
        this.session = session;
        RealWebSocketManage.join(id, this);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (!ObjectUtils.isEmpty(this.session)) {
            RealWebSocketManage.remove(getChannelFromSession(this.session));
        }
    }

    /**
     * 收到客户端消息后调用
     *
     * @param message
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        log.info("客户端发送的消息：" + message);
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("----websocket-------有异常啦");
        onClose();
    }

    public Session getSession() {
        return session;
    }

    private String getChannelFromSession(Session session) {
        Map<String, String> pathParameters = session.getPathParameters();
        return pathParameters.get("channel");
    }

}
