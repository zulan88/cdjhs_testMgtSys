package net.wanji.business.domain.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class HistogramVo {

    private List<String> type;

    private List<Object> data;

    private List<String> xAxis;
}
