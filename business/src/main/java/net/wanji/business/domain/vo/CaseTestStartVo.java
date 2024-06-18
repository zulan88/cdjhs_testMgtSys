package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.domain.bo.TrajectoryDetailBo;

import java.util.List;
import java.util.Map;

/**
 * @author: guanyuduo
 * @date: 2023/10/20 18:01
 * @descriptoin:
 */
@Data
public class CaseTestStartVo extends CaseTestPrepareVo {

    private String startTime;

    private String endTime;

    Map<String, List<TrajectoryDetailBo>> mainTrajectories;
}
