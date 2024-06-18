package net.wanji.business.socket;

import net.wanji.framework.interceptor.PortalHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/18 17:47
 * @Descriptoin:
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private MyWebSocketHandle myMessageHandler;
    @Autowired
    private PortalHandshakeInterceptor portalHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myMessageHandler, "/ws")
                .addInterceptors(portalHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}
