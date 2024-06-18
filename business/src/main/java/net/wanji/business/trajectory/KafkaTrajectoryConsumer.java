package net.wanji.business.trajectory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import net.wanji.business.common.Constants.ChannelBuilder;
import net.wanji.business.common.Constants.RedisMessageType;
import net.wanji.business.common.Constants.TestingStatusEnum;
import net.wanji.business.domain.RealWebsocketMessage;
import net.wanji.business.domain.dto.ToLocalDto;
import net.wanji.business.entity.TjCaseRealRecord;
import net.wanji.business.entity.TjTaskCaseRecord;
import net.wanji.business.listener.KafkaCollector;
import net.wanji.business.mapper.TjCaseRealRecordMapper;
import net.wanji.business.mapper.TjTaskCaseRecordMapper;
import net.wanji.business.service.record.DataFileService;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.business.util.RedisLock;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.constant.CacheConstants;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/5 14:00
 * @Descriptoin:
 */
@Component
@RequiredArgsConstructor
public class KafkaTrajectoryConsumer {
  private static final Logger log = LoggerFactory.getLogger("kafka");
  /**
   * fileId:ToLocalDto
   */
  private static final Set<ToLocalDto> toLocalSet = new HashSet<>();
  @Resource
  private TjTaskCaseRecordMapper taskCaseRecordMapper;
  @Resource
  private TjCaseRealRecordMapper caseRealRecordMapper;

  private final KafkaCollector kafkaCollector;
  private final RedisLock redisLock;
  private final RedisCache redisCache;
  private final DataFileService dataFileService;

  @KafkaListener(id = "singleTrajectory",
      topics = { "tj_master_fusion_data" },
      groupId = "#{T(java.lang.String).valueOf(new java.util.Random().nextInt(1000))}")
  public void listen(ConsumerRecord<String, String> record) {
    JSONObject jsonObject = JSONObject.parseObject(record.value());
    Integer taskId = jsonObject.getInteger("taskId");
    Integer caseId = jsonObject.getInteger("caseId");
    String userName = selectUserOfTask(taskId, caseId);

    String key = taskId > 0 ?
        ChannelBuilder.buildTaskDataChannel(userName, taskId) :
        ChannelBuilder.buildTestingDataChannel(userName, caseId);
    JSONArray participantTrajectories = jsonObject.getJSONArray(
        "participantTrajectories");
    // 轨迹数据
    writeLocal(taskId, caseId, participantTrajectories);
    // 收集数据
    List<ClientSimulationTrajectoryDto> data = participantTrajectories.stream()
        .map(t -> JSONObject.parseObject(t.toString(),
            ClientSimulationTrajectoryDto.class)).collect(Collectors.toList());
    outLog(data);
    if (taskId > 0) {
      data.forEach(t -> redisLock.renewLock("task_" + t.getSource()));
    } else {
      redisLock.renewLock("case_" + caseId);
    }
    kafkaCollector.collector(key, caseId, data);
    // 发送ws数据
    String duration = DateUtils.secondsToDuration(
        (int) Math.floor((double) (kafkaCollector.getSize(key)) / 10));

    RealWebsocketMessage msg = new RealWebsocketMessage(
            RedisMessageType.TRAJECTORY, Maps.newHashMap(), simplifyWebsocketMessage(data),
            duration);
      WebSocketManage.sendInfo(key, JSONObject.toJSONString(msg));
  }

  private Object simplifyWebsocketMessage(List<ClientSimulationTrajectoryDto> data){
    List<Map<String, Object>> res = new ArrayList<>();
    for (ClientSimulationTrajectoryDto datum : data) {
      if(datum.getValue() == null || datum.getValue().isEmpty()) continue;
      Map<String, Object> item = new HashMap<>();
      item.put("role", datum.getRole());
      item.put("source", datum.getSource());
      item.put("timestamp", datum.getTimestamp());
      List<Map<String, Object>> value = new ArrayList<>();
      for (TrajectoryValueDto trajectoryValueDto : datum.getValue()) {
        Map<String, Object> trajectoryValue = new HashMap<>();
        trajectoryValue.put("latitude", trajectoryValueDto.getLatitude());
        trajectoryValue.put("speed", trajectoryValueDto.getSpeed());
        trajectoryValue.put("id", trajectoryValueDto.getId());
        trajectoryValue.put("courseAngle", trajectoryValueDto.getCourseAngle());
        trajectoryValue.put("driveType", trajectoryValueDto.getDriveType());
        trajectoryValue.put("vehicleType", trajectoryValueDto.getVehicleType());
        trajectoryValue.put("longitude", trajectoryValueDto.getLongitude());
        trajectoryValue.put("name", trajectoryValueDto.getName());
        value.add(trajectoryValue);
      }
      item.put("value", value);
      res.add(item);
    }
    return res;
  }

  private String selectUserOfTask(Integer taskId, Integer caseId) {
    // todo 可以使用缓存：taskId_caseId -> userName
    String userName = null;
    String key = CacheConstants.USER_OF_CONTINUOUS_TASK_PREFIX + taskId;
    if (redisCache.hasKey(key)) {
      return String.valueOf(redisCache.redisTemplate.opsForHash()
          .get(key, String.valueOf(caseId)));
    }
    if (0 < taskId) {
      // todo 场景中间会传上一个已结束的caseId，导致中间轨迹丢失
      TjTaskCaseRecord taskCaseRecord = taskCaseRecordMapper.selectOne(
          new LambdaQueryWrapper<TjTaskCaseRecord>().eq(
                  TjTaskCaseRecord::getTaskId, taskId)
              .eq(TjTaskCaseRecord::getCaseId, caseId)
              .eq(TjTaskCaseRecord::getStatus,
                  TestingStatusEnum.NO_PASS.getCode())
              .isNull(TjTaskCaseRecord::getEndTime));
      if (!ObjectUtils.isEmpty(taskCaseRecord)) {
        userName = taskCaseRecord.getCreatedBy();
      }
    } else {
      TjCaseRealRecord caseRealRecord = caseRealRecordMapper.selectOne(
          new LambdaQueryWrapper<TjCaseRealRecord>().eq(
                  TjCaseRealRecord::getCaseId, caseId)
              .eq(TjCaseRealRecord::getStatus,
                  TestingStatusEnum.NO_PASS.getCode())
              .isNull(TjCaseRealRecord::getEndTime));
      if (!ObjectUtils.isEmpty(caseRealRecord)) {
        userName = caseRealRecord.getCreatedBy();
      }
    }
    // redisCache.setCacheObject(key, userName, 5, TimeUnit.SECONDS);
    return userName;
  }

  private void outLog(List<ClientSimulationTrajectoryDto> data) {
    long now = System.currentTimeMillis();
    if (now / 1000 % 20 == 0) {
      StringBuilder sb = new StringBuilder();
      for (ClientSimulationTrajectoryDto trajectoryDto : data) {
        sb.append(StringUtils.format("{}：{}ms；", trajectoryDto.getSource(),
            now - Long.parseLong(trajectoryDto.getTimestamp())));
      }
      log.info(sb.toString());
    }
  }

  public boolean subscribe(ToLocalDto toLocalDto) {
    toLocalDto.setToLocalThread(
        dataFileService.createToLocalThread(toLocalDto));
    toLocalSet.add(toLocalDto);
    return true;
  }

  public boolean unSubscribe(ToLocalDto toLocalDto) {
    try {
      Optional<ToLocalDto> localOp = toLocalSet.stream()
          .filter(e -> e.equals(toLocalDto)).findFirst();
      if (localOp.isPresent()) {
        ToLocalDto oldToLocal = localOp.get();
        dataFileService.writeStop(oldToLocal);
        return toLocalSet.remove(oldToLocal);
      }
      return false;
    } catch (Exception e) {
      if (log.isErrorEnabled()) {
        log.error("unSubscribe [{}] error!", toLocalDto, e);
      }
      return false;
    }
  }

  private void writeLocal(Integer taskId, Integer caseId,
      JSONArray participantTrajectories) {
    for (ToLocalDto toLocalDto : toLocalSet) {
      if (toLocalDto.getTaskId().equals(taskId) && toLocalDto.getCaseId()
          .equals(caseId)) {
        toLocalDto.getToLocalThread()
            .write(participantTrajectories.toJSONString());
      }
    }
  }
}
