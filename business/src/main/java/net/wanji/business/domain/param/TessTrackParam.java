package net.wanji.business.domain.param;

import lombok.Data;

import java.util.List;

@Data
public class TessTrackParam {

    Integer caseId;

    Integer mapId;

    Integer deviceCount;

    List<DeviceConnInfo> deviceList;

    public TessTrackParam(Integer caseId, Integer mapId,Integer deviceCount, List<DeviceConnInfo> deviceList) {
        this.caseId = caseId;
        this.mapId = mapId;
        this.deviceCount = deviceCount;
        this.deviceList = deviceList;
    }

}
