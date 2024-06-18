package net.wanji.business.domain.bo;

import lombok.Data;
import net.wanji.business.entity.TjTaskCase;
import net.wanji.business.entity.TjTaskCaseRecord;

import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/24 13:36
 * @Descriptoin:
 */
@Data
public class TaskCaseInfoBo extends TjTaskCase {

    private String caseNumber;

    private String sceneName;

    private String allStageLabel;

    private String filePath;

    private String geoJsonPath;

    private List<TjTaskCaseRecord> records;

    private List<TaskCaseConfigBo> dataConfigs;
}
