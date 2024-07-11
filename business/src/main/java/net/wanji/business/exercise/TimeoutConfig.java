package net.wanji.business.exercise;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: jenny
 * @create: 2024-07-10 5:41 下午
 */
@Component
public class TimeoutConfig {
    @Value("${timeout.config.device.status}")
    public Integer deviceStatus;

    @Value("${timeout.config.image.report}")
    public Integer imageReport;

    @Value("${timeout.config.image.delete}")
    public Integer imageDelete;

    @Value("${timeout.config.image.issue}")
    public Integer imageIssue;

    @Value("${timeout.config.task.issue}")
    public Integer taskIssue;

    @Value("${timeout.config.simulation.readyStatus}")
    public Integer simulationReadyStatus;

    @Value("${timeout.config.task.duration}")
    public Integer taskDuration;

    @Value("${timeout.config.mainCar.trajectory}")
    public Integer mainCarTrajectory;
}
