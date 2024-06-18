package net.wanji.common.common;

import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/31 19:25
 * @Descriptoin:
 */
public class RealTestTrajectoryDto {

    private String channel;

    private boolean main;

    private String id;

    private String name;

    /**
     * 点位 List<TrajectoryDetailBo>
     */
    private String points;

    private List<TrajectoryValueDto> mainSimuTrajectories;

    private List<SimulationTrajectoryDto> data;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public List<TrajectoryValueDto> getMainSimuTrajectories() {
        return mainSimuTrajectories;
    }

    public void setMainSimuTrajectories(List<TrajectoryValueDto> mainSimuTrajectories) {
        this.mainSimuTrajectories = mainSimuTrajectories;
    }

    public List<SimulationTrajectoryDto> getData() {
        return data;
    }

    public void setData(List<SimulationTrajectoryDto> data) {
        this.data = data;
    }
}
