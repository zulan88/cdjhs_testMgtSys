package net.wanji.business.domain.vo;

import lombok.Data;

@Data
public class ConflictInfo {

    /**
     * 参与者ID
     */
    private String id;
    /**
     * 参与者类型(主车:main; 从车:slave; 行人：pedestrian； 障碍物：obstacle)
     */
    private String type;
    /**
     * 参与者名称
     */
    private String name;

    private Double conflictSpeed;

}
