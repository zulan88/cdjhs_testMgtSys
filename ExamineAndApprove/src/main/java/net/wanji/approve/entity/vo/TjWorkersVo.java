package net.wanji.approve.entity.vo;

import lombok.Data;
import net.wanji.approve.entity.TjWorkers;

@Data
public class TjWorkersVo extends TjWorkers {

    Integer isSelect;

    private Integer deviceId;

    private String deviceName;

    private String otherTask;

}
