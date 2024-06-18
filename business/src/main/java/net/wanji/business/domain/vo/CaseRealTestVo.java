package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.domain.bo.TrajectoryDetailBo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/30 19:22
 * @Descriptoin:
 */
@Data
public class CaseRealTestVo {

    private Integer id;

    private Integer taskId;

    private Integer taskCaseId;

    private String testTypeName;

    private String startTime;

    private String endTime;

    Map<String, List<TrajectoryDetailBo>> mainTrajectories;
}
