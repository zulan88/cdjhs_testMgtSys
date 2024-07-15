package net.wanji.business.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

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

    private List<Integer> triggeredScenes;

    public RealWebsocketMessage(String type, Object info, Object data, String countDown){
        this.type = type;
        this.info = info;
        this.data = data;
        this.countDown = countDown;
    }
}
