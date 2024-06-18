package net.wanji.business.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/12 13:32
 * @Descriptoin:
 */
@AllArgsConstructor
@Data
public class RealWebsocketMessage {

    private String type;

    private Object info;

    private Object data;

    private String countDown;
}
