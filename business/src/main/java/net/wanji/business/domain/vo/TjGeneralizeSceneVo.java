package net.wanji.business.domain.vo;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.business.entity.TjGeneralizeScene;
import net.wanji.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Data
public class TjGeneralizeSceneVo extends TjGeneralizeScene {

    private SceneTrajectoryBo trajectoryJson;

    private List<String> labelList;

    private List<ConflictInfo> conflictInfos;

    public SceneTrajectoryBo getTrajectoryJson() {
        try {
            if (StringUtils.isNotEmpty(this.getTrajectoryInfo())) {
                return JSONObject.parseObject(this.getTrajectoryInfo(), SceneTrajectoryBo.class);
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("parse error!", e);
            }
        }
        return trajectoryJson;
    }

    public List<String> getLabelList() {
        if (StringUtils.isNotEmpty(this.getLabel())) {
            return Arrays.stream(this.getLabel().split(",")).collect(Collectors.toList());
        }
        return labelList;
    }

    public List<ConflictInfo> getConflictInfos() {
        if (conflictInfos == null){
            conflictInfos = new ArrayList<>();
        }
        return conflictInfos;
    }
}
