package net.wanji.business.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.wanji.business.annotion.CustomMerge;

import java.time.LocalDateTime;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/5 13:57
 * @Descriptoin:
 */
@Data
public class TaskReportVo {

    /**
     * 任务名称
     */
    @ExcelProperty
    @CustomMerge(isPk = true, needMerge = true)
    private String taskName;

    /**
     * 参与者名称
     */
    @ExcelProperty
    @CustomMerge(needMerge = true)
    private String participatorName;

    /**
     * 测试开始时间
     */
    @ExcelProperty
    @CustomMerge(needMerge = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 测试结束时间
     */
    @ExcelProperty
    @CustomMerge(needMerge = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 测试总时长
     */
    @ExcelProperty
    @CustomMerge(needMerge = true)
    private String testTotalTime;

    /**
     * 总分数
     */
    @ExcelProperty
    @CustomMerge(needMerge = true)
    private String scoreTotal;

    /**
     * 用例名称
     */
    @ExcelProperty
    @CustomMerge(isPk = true, needMerge = true)
    private String caseName;

    /**
     * 通过率
     */
    @ExcelProperty
    @CustomMerge(needMerge = true)
    private String passingRate;

    /**
     * 用例测试开始时间
     */
    @ExcelProperty
    @CustomMerge(needMerge = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime caseStartTime;

    /**
     * 用例测试结束时间
     */
    @ExcelProperty
    @CustomMerge(needMerge = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime caseEndTime;

    /**
     * 用例测试时长
     */
    @ExcelProperty
    @CustomMerge(needMerge = true)
    private String caseTestTime;

    /**
     * 类型
     */
    @ExcelProperty
    @CustomMerge(isPk = true, needMerge = true)
    private String dcType;

    /**
     * 权重
     */
    @ExcelProperty
    @CustomMerge(needMerge = true)
    private String dcWeight;

    /**
     * 指标
     */
    @ExcelProperty
    private String diaName;

    /**
     * 分数
     */
    @ExcelProperty
    private String score;

    /**
     * 时长
     */
    @ExcelProperty
    private String time;

}
