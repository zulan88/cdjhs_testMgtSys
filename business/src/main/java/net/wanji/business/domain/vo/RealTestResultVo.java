package net.wanji.business.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.wanji.business.domain.bo.CaseTrajectoryDetailBo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/31 22:57
 * @Descriptoin:
 */
@Data
public class RealTestResultVo extends CaseTrajectoryDetailBo {

    private Integer id;

    private Integer taskId;

    private Integer recordId;

    private String caseNumber;

    private String testTypeName;

    private String sceneName;

    private List<String> channels;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
