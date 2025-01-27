package net.wanji.business.schedule;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import net.wanji.business.common.Constants.RedisMessageType;
import net.wanji.business.domain.RealWebsocketMessage;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.exercise.dto.evaluation.EvaluationOutputResult;
import net.wanji.business.exercise.dto.evaluation.StartPoint;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.business.util.LongitudeLatitudeUtils;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.utils.DateUtils;
import net.wanji.framework.manager.AsyncManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/10 18:58
 * @Descriptoin:
 */
@Data
public class RealPlaybackDomain {

    private ScheduledFuture<?> future;
    private String key;
    private String mainChannel;
    private List<List<ClientSimulationTrajectoryDto>> trajectories;
    private boolean running;
    private int index;
    private int length;
    private EvaluationOutputResult evaluationOutput;
    private int sequence; //当前场景
    private List<StartPoint> sceneStartPoints;
    private double radius;
    private List<Integer> triggeredScenes;

    public RealPlaybackDomain(String key, String mainChannel, List<List<ClientSimulationTrajectoryDto>> trajectories, List<StartPoint> sceneStartPoints, double radius){
        this.key = key;
        this.trajectories = trajectories;
        this.mainChannel = mainChannel;
        this.running = true;
        this.index = 0;
        this.length = trajectories.size();
        this.sequence = -1;
        this.sceneStartPoints = sceneStartPoints;
        this.radius = radius;
        this.triggeredScenes = new ArrayList<>();

        this.future = AsyncManager.me().execute(() -> {
            try {
                if (!running) {
                    return;
                }
                if (index >= length) {
                    try {
                        RealWebsocketMessage endMsg = new RealWebsocketMessage(RedisMessageType.END, null,
                                null, "00:00");
                        WebSocketManage.sendInfo(key, JSONObject.toJSONString(endMsg));
                        return;
                    } finally {
                        RealPlaybackSchedule.stopSendingData(key);
                    }
                }
                if (CollectionUtils.isEmpty(trajectories)) {
                    return;
                }
                String duration = DateUtils.secondsToDuration((int) Math.floor((double) (length - index) / 10));
                List<ClientSimulationTrajectoryDto> data = trajectories.get(index);
                ClientSimulationTrajectoryDto mainCar = data.stream()
                        .filter(n -> mainChannel.equals(n.getSource()))
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
                            triggeredScenes.add(startPoint.getSequence());
                            sequence = i;
                            break;
                        }
                    }
                }
                RealWebsocketMessage msg = new RealWebsocketMessage(RedisMessageType.TRAJECTORY, sceneStartPoints, data, duration, triggeredScenes);
                WebSocketManage.sendInfo(key, JSONObject.toJSONString(msg));
                index++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 100);
    }

    public RealPlaybackDomain(String key, String mainChannel, List<List<ClientSimulationTrajectoryDto>> trajectories) {
        this.key = key;
        this.trajectories = trajectories;
        this.mainChannel = mainChannel;
        this.running = true;
        this.index = 0;
        this.length = trajectories.size();
        // 主车轨迹
//        List<List<SimulationTrajectoryDto>> mainTrajectories = trajectories.stream()
//                .map(m -> m.stream()
//                        .filter(n -> mainChannel.equals(n.getSource()) && CollectionUtils.isNotEmpty(n.getValue()))
//                        .collect(Collectors.toList()))
//                .filter(CollectionUtils::isNotEmpty)
//                .collect(Collectors.toList());
        // 所有点位
//        List<TrajectoryDetailBo> points = new ArrayList<>();
        // 主车全程、剩余时间、到达时间
//        ArrayList<Point2D.Double> doubles = new ArrayList<>();
//        for (TrajectoryDetailBo trajectoryDetailBo : points) {
//            String[] pos = trajectoryDetailBo.getPosition().split(",");
//            doubles.add(new Point2D.Double(Double.parseDouble(pos[0]), Double.parseDouble(pos[1])));
//        }
//        CountDown countDown = new CountDown(doubles);
        // 主车途径点提示
//        PathwayPoints pathwayPoints = new PathwayPoints(points);
        // 提示板
        Map<String, Object> realMap = new HashMap<>();

        this.future = AsyncManager.me().execute(() -> {
            // send data
            try {
                if (!running) {
                    return;
                }
                if (index >= length) {
                    try {
                        RealWebsocketMessage endMsg = new RealWebsocketMessage(RedisMessageType.END, null,
                                null, "00:00");
                        WebSocketManage.sendInfo(key, JSONObject.toJSONString(endMsg));
                        return;
                    } finally {
                        RealPlaybackSchedule.stopSendingData(key);
                    }
                }
                if (CollectionUtils.isEmpty(trajectories)) {
                    return;
                }
                String duration = DateUtils.secondsToDuration((int) Math.floor((double) (length - index) / 10));
                List<ClientSimulationTrajectoryDto> data = trajectories.get(index);
//                    CountDownDto countDownDto = countDown.countDown(data.get(0).getSpeed(),
//                            new Point2D.Double(data.get(0).getLongitude(), data.get(0).getLatitude()));
//                    if (!ObjectUtils.isEmpty(countDownDto)) {
//                        double mileage = countDownDto.getFullLength();
//                        double remainLength = countDownDto.getRemainLength();
//                        realMap.put("mileage", String.format("%.2f", mileage));
//                        realMap.put("duration", countDownDto.getTimeRemaining());
//                        realMap.put("arriveTime", DateUtils.dateToString(countDownDto.getArrivalTime(), DateUtils.HH_MM_SS));
//                        double percent = mileage > 0 ? 1 - (remainLength / mileage) : 1;
//                        realMap.put("percent", percent * 100);
//                    }
//                    PathwayPoints nearestPoint = pathwayPoints.findNearestPoint(data.get(0).getLongitude(),
//                            data.get(0).getLatitude());
                Map<String, Object> tipsMap = new HashMap<>();
//                    if (nearestPoint.hasTips()) {
//                        tipsMap.put("name", realTestTrajectory.getName());
//                        tipsMap.put("pointName", nearestPoint.getPointName());
//                        tipsMap.put("pointDistance", nearestPoint.getDistance());
//                        tipsMap.put("pointSpeed", nearestPoint.getPointSpeed());
//                    }
                realMap.put("tips", tipsMap);

                // 仿真车未来轨迹
                List<Map<String, Double>> futureList = new ArrayList<>();
//                    if (!CollectionUtils.isEmpty(mainSimuTrajectories)) {
//                        // 仿真验证中当前主车位置
//                        data.add(mainSimuTrajectories.remove(0));
//                        // 仿真验证中主车剩余轨迹
//                        futureList = mainSimuTrajectories.stream().map(item -> {
//                            Map<String, Double> posMap = new HashMap<>();
//                            posMap.put("longitude", item.getLongitude());
//                            posMap.put("latitude", item.getLatitude());
//                            return posMap;
//                        }).collect(Collectors.toList());
//                    }
                realMap.put("simuFuture", futureList);
                Optional<TrajectoryValueDto> main = data.stream()
                        .filter(n -> mainChannel.equals(n.getSource()) && CollectionUtils.isNotEmpty(n.getValue()))
                        .map(m -> m.getValue().get(0)).findFirst();
                realMap.put("speed", main.isPresent() ? main.get().getSpeed() : 0);

                RealWebsocketMessage msg = new RealWebsocketMessage(RedisMessageType.TRAJECTORY, realMap, data, duration);
                WebSocketManage.sendInfo(key, JSONObject.toJSONString(msg));
                index ++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 100);
    }


    public void suspend() throws BusinessException {
        this.validFuture();
        if (!this.running) {
            throw new BusinessException("当前任务未处于运行状态");
        }
        this.running = false;
    }

    public void goOn() throws BusinessException {
        this.validFuture();
        this.running = true;
    }

    public synchronized void stopSendingData() throws BusinessException, IOException {
        this.validFuture();
        this.running = false;
        this.future.cancel(true);
        WebSocketManage.remove(this.key, true);
    }

    private void validFuture() throws BusinessException {
        if (ObjectUtils.isEmpty(this.future) || this.future.isCancelled()) {
            throw new BusinessException("任务不存在");
        }
    }

}
