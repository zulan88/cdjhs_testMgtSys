package net.wanji.business.domain;

import lombok.Data;
import java.util.List;

@Data
public class SiteSlice {

    Integer sliceId;

    String sliceName;

    List<SitePoint> route;

    String imgData;

}
