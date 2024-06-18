package net.wanji.business.domain.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import net.wanji.common.utils.StringUtils;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/5 18:05
 * @Descriptoin:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TrajectoryDetailBo implements Serializable {

    private static final long serialVersionUID = 1L;

    //（开始：start；途径点：pathway；冲突点：conflict；结束点：end；途经点有参：pathwayar）
    private String type;
    private String time;
    private Long frameId;
    private String position;
    private String longitude;
    private String latitude;
    private String lane;
    private Double speed;
    private String speedUnit;
    private Boolean pass;
    private String reason = "等待校验";

    private String date;

    public void setPosition(String position) {
        if (StringUtils.isNotEmpty(position)) {
            this.position = position;
            String[] pos = position.split(",");
            if (!ObjectUtils.isEmpty(pos)) {
                this.longitude = pos[0];
                this.latitude = pos[1];
            }
        }
    }

    /**
     * 发送至其他端时，清空不必要的属性
     */
    public void clearProperties() {
        this.reason = null;
        this.pass = null;
        this.date = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TrajectoryDetailBo that = (TrajectoryDetailBo) o;
        return Objects.equals(type, that.type) && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, position);
    }
}
