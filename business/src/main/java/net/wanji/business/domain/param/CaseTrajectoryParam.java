package net.wanji.business.domain.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author: guanyuduo
 * @date: 2023/11/16 10:59
 * @descriptoin: 用例轨迹参数
 */
@Data
public class CaseTrajectoryParam {

    private Integer taskId;
    private Integer caseId;
    private boolean isContinuous;
    /**
     * 测试类型：0：单用例测试（实车试验）；1：连续性场景测试（多场景任务一次启停）；2：批量测试（n场景任务n次启停；3：无限里程）
     */
    private int testMode;

    /**
     * 车辆类型与车辆ID的对应关系<类型：ID>
     */
    private Map<String, String> vehicleIdTypeMap;

    /**
     * 上下文环境参数
     */
    private Map<String, Object> context;

    /**
     * 主车数据频道
     */
    private String dataChannel;
    /**
     * 主车控制频道
     */
    private String controlChannel;

    /**
     * 主车场景轨迹点位信息/切片位置信息
     */
    private List<CaseSSInfo> caseTrajectorySSVoList;

    /**
     * 任务持续时间(S)
     */
    private Long taskDuration;

    public List<CaseSSInfo> getCaseTrajectorySSVoList() {
        if (caseTrajectorySSVoList == null) {
            caseTrajectorySSVoList = new java.util.ArrayList<>();
        }
        return caseTrajectorySSVoList;
    }
}
