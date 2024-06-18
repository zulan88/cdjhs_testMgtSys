package net.wanji.business.socket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;
import java.io.IOException;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/19 14:36
 * @Descriptoin:
 */
@Slf4j
public class WebSocketProperties {
    private final String userName;
    private final String token;
    private final String id;
    private final Integer clientType;
    private final String signId;
    private final String key;
    private final long createTime;
    private final WebSocketSession session;

    public WebSocketProperties(String userName, String token, String id, Integer clientType,
                               String signId, String key, long createTime, WebSocketSession session) {
        this.token = token;
        this.userName = userName;
        this.id = id;
        this.clientType = clientType;
        this.signId = signId;
        this.key = key;
        this.createTime = createTime;
        this.session = session;
    }
    public void closeSession() {
        try {
            if (this.session.isOpen()) {
                this.session.close();
            }
        } catch (IOException e) {
            log.error("closeSession失败：{}", e);
        }
    }

    public synchronized void sendMessage(String message) {
        try {
            this.session.sendMessage(new TextMessage(message));
        } catch (IOException e) {
            log.error("sendMessage失败：{}", e);
        }
    }

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }

    public Integer getClientType() {
        return clientType;
    }

    public String getsignId() {
        return signId;
    }

    public String getKey() {
        return key;
    }

    public long getCreateTime() {
        return createTime;
    }

    public WebSocketSession getSession() {
        return session;
    }
}
