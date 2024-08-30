package net.wanji.business.domain.dto;

import ch.qos.logback.classic.Logger;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.wanji.business.exercise.dto.evaluation.StartPoint;
import net.wanji.business.exercise.dto.luansheng.CommonField;
import net.wanji.business.service.record.impl.FileWriteRunnable;
import net.wanji.business.util.LongitudeLatitudeUtils;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hcy
 * @version 1.0
 * @className ToLocalDto
 * @description TODO
 * @date 2024/4/2 11:03
 **/
@Data
@NoArgsConstructor
public class ToLocalDto extends CommonField {
    private Integer taskId;
    private Integer caseId;
    private String fileName;
    private Integer fileId;
    private FileWriteRunnable toLocalThread;
    private String kafkaTopic;
    private String mainVehicleId;
    private String username;
    private AtomicInteger count = new AtomicInteger(0);
    private List<StartPoint> startPoints;
    private List<Integer> triggeredScenes = new ArrayList<>();
    private double radius;
    private int sequence;//当前场景
    private String mainChannel;
    private boolean isCompetition;
    private Logger logger;

    public ToLocalDto(Integer taskId, Integer caseId) {
        this.taskId = taskId;
        this.caseId = caseId;
    }

    public ToLocalDto(Integer taskId, Integer caseId, Integer fileId) {
        this.taskId = taskId;
        this.caseId = caseId;
        this.fileId = fileId;
    }

    public ToLocalDto(Integer taskId, Integer caseId, String fileName,
                      Integer fileId) {
        this.taskId = taskId;
        this.caseId = caseId;
        this.fileName = fileName;
        this.fileId = fileId;
    }

    public ToLocalDto(Integer taskId, Integer caseId, String fileName,
                      Integer fileId, String kafkaTopic, String username,
                      List<StartPoint> startPoints, double radius,
                      String mainChannel, boolean isCompetition, Logger logger) {
        this.taskId = taskId;
        this.caseId = caseId;
        this.fileName = fileName;
        this.fileId = fileId;
        this.kafkaTopic = kafkaTopic;
        this.username = username;
        this.startPoints = startPoints;
        this.sequence = -1;//起点
        this.radius = radius;
        this.mainChannel = mainChannel;
        this.isCompetition = isCompetition;
        this.logger = logger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ToLocalDto that = (ToLocalDto) o;
        return Objects.equals(taskId, that.taskId) && Objects.equals(caseId,
                that.caseId) && Objects.equals(fileId, that.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, caseId, fileId);
    }

    public void switchScene(){
        sequence++;
    }

    public void calculate(Point2D.Double position){
        if(!startPoints.isEmpty() && sequence < startPoints.size()){
            for(int i = sequence + 1; i < Math.min(sequence + 5, startPoints.size()); i++){
                StartPoint startPoint = startPoints.get(i);
                Point2D.Double sceneStartPos = new Point2D.Double(startPoint.getLongitude(), startPoint.getLatitude());
                boolean arrivedSceneStartPos = LongitudeLatitudeUtils.isInCriticalDistance(sceneStartPos, position, radius);
                if(arrivedSceneStartPos){
                    triggeredScenes.add(startPoint.getSequence());
                    sequence = i;
                    break;
                }
            }
        }
    }
}
