package net.wanji.business.domain.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import net.wanji.business.domain.bo.CaseTrajectoryDetailBo;
import net.wanji.business.entity.TjCase;
import net.wanji.common.utils.StringUtils;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/3 17:56
 * @Descriptoin:
 */
@Data
public class CaseVerificationVo {

    private Integer id;

    private String filePath;

    private String geoJsonPath;

    private CaseTrajectoryDetailBo detailInfo;

    private boolean finished;

    public CaseVerificationVo(TjCase tjCase, String filePath, String geoJsonPath) {
        this.id = tjCase.getId();
        this.filePath = filePath;
        this.geoJsonPath = geoJsonPath;
        this.detailInfo = StringUtils.isNotEmpty(tjCase.getDetailInfo())
                ? JSONObject.parseObject(tjCase.getDetailInfo(), CaseTrajectoryDetailBo.class)
                : null;
    }

}
