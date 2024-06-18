package net.wanji.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.wanji.common.annotation.Excel;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/5 13:57
 * @Descriptoin:
 */
@Data
public class TaskCaseReportVo {

    /**
     * 用例ID
     */
    private Integer caseId;
    /**
     * 用例名称
     */
    private String caseName;

    /**
     * 通过率
     */
    private String passingRate;

    /**
     * 用例测试开始时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime caseStartTime;

    /**
     * 用例测试结束时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime caseEndTime;

    /**
     * 用例测试时长
     */
    private String caseTestTime;

    private List<DiadynamicVo> diadynamicVos;


}
