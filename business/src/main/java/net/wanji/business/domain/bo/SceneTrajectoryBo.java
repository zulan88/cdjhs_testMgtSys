package net.wanji.business.domain.bo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/28 9:05
 * @Descriptoin:
 */
@Data
public class SceneTrajectoryBo implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ParticipantTrajectoryBo> participantTrajectories;

    public SceneTrajectoryBo buildId() {
//        if (CollectionUtils.isNotEmpty(participantTrajectories)) {
//            IntStream.range(0, participantTrajectories.size()).forEachOrdered(i ->
//                    participantTrajectories.get(i).setId(String.valueOf(i + 1)));
//        }
        return this;
    }

    public String toJsonString() {
        return JSONObject.toJSONString(this);
    }
}
