package net.wanji.business.exercise.dto.luansheng;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import net.wanji.business.common.Constants;
import net.wanji.business.exercise.LuanshengDataSender;
import net.wanji.business.exercise.dto.evaluation.StartPoint;
import net.wanji.business.service.KafkaProducer;
import net.wanji.business.util.LongitudeLatitudeUtils;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.core.redis.RedisCache;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: jenny
 * @create: 2024-08-28 5:29 下午
 */
@Data
public class RealPlaybackDomainTW implements Runnable{
    private Long taskId;
    private String topic;
    private List<List<ClientSimulationTrajectoryDto>> trajectories;
    private int index;
    private int length;
    private int sequence; //当前场景
    private List<StartPoint> sceneStartPoints;
    private double radius;
    private KafkaProducer kafkaProducer;
    private Long lastTimestamp;
    private boolean isSpeedOverLimit;
    private Long startTimeOfSpeed;
    private boolean isLonAccOverLimit;
    private Long startTimeOfLonAcc;
    private boolean isLonAcc2OverLimit;
    private Long startTimeOfLonAcc2;
    private boolean isLatAccOverLimit;
    private Long startTimeOfLatAcc;
    private boolean isLatAcc2OverLimit;
    private Long startTimeOfLatAcc2;
    private boolean isAngularOverLimit;
    private Long  startTimeOfAngular;

    public RealPlaybackDomainTW(Long taskId, String topic, List<List<ClientSimulationTrajectoryDto>> trajectories,
                                List<StartPoint> sceneStartPoints, double radius,
                                KafkaProducer kafkaProducer){
        this.taskId = taskId;
        this.topic = topic;
        this.trajectories = trajectories;
        this.index = 0;
        this.length = trajectories.size();
        this.sequence = -1;
        this.sceneStartPoints = sceneStartPoints;
        this.radius = radius;
        this.kafkaProducer = kafkaProducer;
    }

    @Override
    public void run() {
        try {
            if(index >= length){
                TWPlaybackSchedule.stopSendingData(taskId);
                return;
            }
            List<ClientSimulationTrajectoryDto> data = trajectories.get(index);
            ClientSimulationTrajectoryDto mainCar = data.stream()
                    .filter(n -> n.getRole().equals(Constants.PartRole.AV))
                    .collect(Collectors.toList()).get(0);
            //主车位置
            Double latitude = mainCar.getValue().get(0).getLatitude();
            Double longitude = mainCar.getValue().get(0).getLongitude();
            Point2D.Double position = new Point2D.Double(longitude, latitude);
            //当前场景
            if(!sceneStartPoints.isEmpty() && sequence < sceneStartPoints.size()){
                for(int i = sequence + 1; i < Math.min(sequence + 5, sceneStartPoints.size()); i++){
                    StartPoint startPoint = sceneStartPoints.get(i);
                    Point2D.Double sceneStartPos = new Point2D.Double(startPoint.getLongitude(), startPoint.getLatitude());
                    boolean arrivedSceneStartPos = LongitudeLatitudeUtils.isInCriticalDistance(sceneStartPos, position, radius);
                    if(arrivedSceneStartPos){
                        sequence = i;
                        break;
                    }
                }
            }
            int currentSceneIndex = sequence > -1 ? sequence : 0;
            String name = sceneStartPoints.get(currentSceneIndex).getName();
            ParticipantTrajectoryDto participantTrajectoryDto = LuanshengDataSender.getParticipantTrajectoryDto(data, name);
            String message = JSONObject.toJSONString(participantTrajectoryDto);
            kafkaProducer.sendMessage(topic, message);

            //更新图表缓存
            StatCache.update(taskId.intValue(), mainCar.getValue().get(0), this);
            index++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
