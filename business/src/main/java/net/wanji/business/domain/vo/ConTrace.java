package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.common.common.SimulationTrajectoryDto;

import java.util.List;

@Data
public class ConTrace {

    private List<SimulationTrajectoryDto> mainPoints;

    private List<SceneTrajectoryBo> sceneTrajectoryBoList;

}
