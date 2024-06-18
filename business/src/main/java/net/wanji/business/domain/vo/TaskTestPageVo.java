package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.domain.bo.CaseConfigBo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/24 17:01
 * @Descriptoin:
 */
@Data
public class TaskTestPageVo {

    private Integer id;

    private Integer taskId;

    private Integer caseId;

    private String filePath;

    private String geoJsonPath;

    private Map<String, List<CaseConfigBo>> statusMap;

    private Set<String> channels;

    private boolean canStart;

    private String message;
}
