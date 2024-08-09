package net.wanji.business.exercise.dto.jidaevaluation.trajectory;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-07-09 22:54
 */
@Data
public class RealTimeParticipant {
    private Integer id;

    private Double x;

    private Double y;

    private Double length;

    private Double width;

    private Double height;

    private Double speed;

    private Double angle;

    private Double acce;

    private Boolean isMain;

    //场景id
    private Integer regionalId;

    //0表示没有安全员介入，1表示存在安全员介入
    private Integer isSecurityInvolved;
}
