package net.wanji.business.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.wanji.business.domain.vo.ParticipantTrajectoryVo;

import java.util.List;
import java.util.Map;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/12 13:32
 * @Descriptoin:
 */
//@AllArgsConstructor
@Data
public class WebsocketMessage {

    public WebsocketMessage(String type, String countDown, Object data){
        this.type = type;
        this.countDown = countDown;
        this.data = data;
    }

    private String type;

    private String countDown;

    private Object data;

    private List<ParticipantTrajectoryVo> objlist;

    private Boolean canStop;
}
