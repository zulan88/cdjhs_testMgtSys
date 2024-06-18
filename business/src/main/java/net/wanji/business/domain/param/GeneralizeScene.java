package net.wanji.business.domain.param;

import lombok.Data;

@Data
public class GeneralizeScene {

    private Integer id;

    private Integer minSpeed;

    private Integer maxSpeed;

    private Integer step;

    // 0:简单 1:复杂
    private Integer type;

    public Integer getType() {
        if (type == null) {
            return 0;
        }
        return type;
    }
}
