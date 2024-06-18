package net.wanji.framework.interceptor;

import com.alibaba.fastjson.JSONObject;
import net.wanji.common.constant.Constants;
import net.wanji.common.core.domain.model.LoginUser;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.spring.SpringUtils;
import net.wanji.framework.web.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/18 14:03
 * @Descriptoin:
 */
@Component
public class PortalHandshakeInterceptor implements HandshakeInterceptor {

    private final Logger logger = LoggerFactory.getLogger(PortalHandshakeInterceptor.class);

    @Autowired
    private TokenService tokenService;

    /**
     * 初次握手访问前
     *
     * @param request
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param map
     * @return
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) {
        logger.info("HandshakeInterceptor beforeHandshake start...");
        URI uri = request.getURI();

        System.out.println(uri.getPath());
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
            System.out.println(JSONObject.toJSONString(req.getParameterMap()));
            String authorization = req.getHeader("Sec-WebSocket-Protocol");
            if (Objects.isNull(authorization)) {
                serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
                logger.info("【beforeHandshake】 authorization Parse failure. authorization = {}", authorization);
                return false;
            }
            authorization = authorization.replace(Constants.TOKEN_PREFIX, "");
            logger.info("authorization = {}", authorization);
            if (Objects.isNull(tokenService)) {
                tokenService = SpringUtils.getBean(TokenService.class);
            }
            LoginUser loginUser = tokenService.getLoginUser(authorization);
            if (ObjectUtils.isEmpty(loginUser)) {
                return false;
            }
            logger.info("userName = {}, token = {}", loginUser.getUsername(), authorization);

            //存入数据，方便在hander中获取，这里只是在方便在webSocket中存储了数据，并不是在正常的httpSession中存储，想要在平时使用的session中获得这里的数据，需要使用session 来存储一下
            map.put("userName", nullToEmpty(loginUser.getUsername(), ""));
            map.put("createTime", System.currentTimeMillis());
            map.put("token", nullToEmpty(authorization, ""));
            map.put("id", nullToEmpty(req.getParameter("id"), -1));
            map.put("clientType",
                nullToEmpty(req.getParameter("clientType"), ""));
            map.put("signId", nullToEmpty(req.getParameter("signId"), ""));
            map.put(HttpSessionHandshakeInterceptor.HTTP_SESSION_ID_ATTR_NAME,
                req.getSession().getId());
            logger.info("【beforeHandshake】 WEBSOCKET_INFO_MAP: {}", map);
        }
        logger.info("HandshakeInterceptor beforeHandshake end...");
        return true;
    }

    /**
     * 初次握手访问后，将前端自定义协议头Sec-WebSocket-Protocol原封不动返回回去，否则会报错
     *
     * @param request
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param e
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {
        logger.info("HandshakeInterceptor afterHandshake start...");
        HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse httpResponse = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();
        if (StringUtils.isNotEmpty(httpRequest.getHeader("Sec-WebSocket-Protocol"))) {
            httpResponse.addHeader("Sec-WebSocket-Protocol", httpRequest.getHeader("Sec-WebSocket-Protocol"));
        }
        logger.info("HandshakeInterceptor afterHandshake end...");
    }

    private Object nullToEmpty(Object obj, Object defaultVal){
        if(null == obj){
            return defaultVal;
        }
        return obj;
    }

}
