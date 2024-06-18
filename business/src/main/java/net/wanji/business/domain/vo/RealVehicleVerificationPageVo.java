package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.domain.bo.CaseConfigBo;
import net.wanji.common.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/24 17:01
 * @Descriptoin:
 */
@Data
public class RealVehicleVerificationPageVo {

    private Integer caseId;

    private String filePath;

    private String geoJsonPath;

    private Map<String, List<CaseConfigBo>> statusMap;

    private Map<String, List<CaseConfigBo>> viewMap;

    private boolean canStart;

    /**
     * 测试用例运行状态：运行中：true，闲置之中：false
     */
    private boolean running;

    private String message;

    public void setMessage(String message) {
        this.message = message;
        if (StringUtils.isEmpty(message)) {
            this.canStart = true;
        }
    }
}
