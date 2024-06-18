package net.wanji.business.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liuzhiheng
 * @date
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SceneQueryDto {

    @NotNull(message = "请确认所属场景")
    private Integer fragmentedSceneId; // 片段式场景id
    private Integer hazardIndex; // 危险等级
    private String resourcesDetailId; // 地图(多选以,分隔)
    private String sceneType; // 场景类型(多选以,分隔)
    private String sceneComplexity; // 场景复杂度(多选以,分隔)
    private String trafficFlowStatus; // 交通流状态(多选以,分隔)
    private String roadType; // 道路类型(多选以,分隔)
    private String weather; // 天气(多选以,分隔)
    private String roadCondition; // 路面状况(多选以,分隔)
    private Boolean collectStatus; // 收藏状态（未收藏：0；已收藏：1）
    private Boolean finished; // 是否完成（未完成：0；已完成：1）
    private List<Integer> fragmentedSceneIds;
}
