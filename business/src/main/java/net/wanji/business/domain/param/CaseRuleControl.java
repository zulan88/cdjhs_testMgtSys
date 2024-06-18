package net.wanji.business.domain.param;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/23 17:49
 * @Descriptoin:
 */
@AllArgsConstructor
@Data
public class CaseRuleControl {
    /**
     * 时间戳ms
     */
    private Long timestamp;
    /**
     * 任务id
     */
    private int taskId;
    /**
     * 用例id
     */
    private Integer caseId;
    /**
     * 任务类型，1：开始，0：结束,2：无限里程开始
     */
    private Integer taskType;
    /**
     * 规则
     */
    private List<DeviceConnRule> rules;
    /**
     * 主车控制频道
     */
    private String mainControlChannel;
    /**
     * 测试任务结束
     */
    private boolean taskEnd;
}
