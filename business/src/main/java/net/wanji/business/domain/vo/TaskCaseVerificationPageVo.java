package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.domain.bo.TaskCaseConfigBo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/24 17:01
 * @Descriptoin:
 */
@Data
public class TaskCaseVerificationPageVo {

    private Integer taskId;

    private Integer taskCaseId;

    private String filePath;

    private String geoJsonPath;

    private Map<String, List<TaskCaseConfigBo>> statusMap;

    private Map<String, List<TaskCaseConfigBo>> viewMap;

    private boolean canStart;

    private String message;
}
