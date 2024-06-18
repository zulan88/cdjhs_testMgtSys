package net.wanji.business.service.record.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.RealWebsocketMessage;
import net.wanji.business.domain.dto.RecordSimulationTrajectoryDto;
import net.wanji.business.entity.DataFile;
import net.wanji.business.service.record.DataCopyService;
import net.wanji.business.socket.WebSocketManage;
import net.wanji.business.util.LongitudeLatitudeUtils;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.StringUtils;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author glace
 * @version 1.0
 * @className HistoryFileReadRunnable
 * @description TODO
 * @date 2023/8/17 15:47
 **/
@Slf4j
@RequiredArgsConstructor
public class FileReadThread extends Thread {

  @Getter
  private final RateLimiter rateLimiter;
  private final DataFile dataFile;
  private final File localFile;
  private final Long startOffset;
  private final Long endOffset;
  private final List<Long> offsets;
  private final String wsClientKey;
  private final DataCopyService dataCopyService;
  private final Map<String, List<FileReadThread>> taskThreadMap;
  private final List<Point2D.Double> pts;

  private Map<String, List<? extends ExtendedDataWrapper>> extendedDataMap;
  private int progressChange = 0;
  private long totalRecords = 0;
  private ObjectMapper objectMapper = new ObjectMapper();
  private Exception exception;

  private final AtomicBoolean pause = new AtomicBoolean(false);
  private final AtomicBoolean stop = new AtomicBoolean(false);

  @Override
  public void run() {

    long startPoint = startOffset;
    if (startOffset < 0 || endOffset > offsets.size()) {
      throw new IllegalArgumentException(
          String.format("lineNum must between %s and %s", 0, offsets.size()));
    }
    totalRecords = endOffset - startOffset;
    try (RandomAccessFile raf = new RandomAccessFile(localFile, "r")) {
      String encode = dataFile.getEncode();
      if (null == encode) {
        encode = "GB2312";
      }
      raf.seek(offsets.get(startOffset.intValue()));
      while (startPoint <= endOffset && !stop.get()) {
        if (null != rateLimiter) {
          if (pause.get()) {
            synchronized (rateLimiter) {
              rateLimiter.wait();
            }
          }
          rateLimiter.acquire();
        }
        byte[] bytes = raf.readLine().getBytes(StandardCharsets.ISO_8859_1);
        List<RecordSimulationTrajectoryDto> trajectories;
        try {
          trajectories = objectMapper.readValue(new String(bytes, encode),
              new TypeReference<List<RecordSimulationTrajectoryDto>>() {
              });
        } catch (Exception e) {
          if (log.isErrorEnabled()) {
            log.error("data parse error!", e);
          }
          continue;
        }
        if (dataSend(trajectories, startPoint - startOffset))
          continue;
        startPoint++;
      }
    } catch (Exception e) {
      exception = e;
      if (log.isErrorEnabled()) {
        log.error("History file read error!", e);
      }
    } finally {
      removeThreadRecord();
      dataSendEnd(startPoint - startOffset);
    }
  }

  public void extendedDataPut(
      Map<String, List<? extends ExtendedDataWrapper>> extendedDataWrappers) {
    if (null != extendedDataWrappers) {
      if (null == extendedDataMap) {
        extendedDataMap = new HashMap<>();
      }
      extendedDataMap.putAll(extendedDataWrappers);
    }
  }

  private void removeThreadRecord() {
    if (StringUtils.isNotEmpty(wsClientKey)) {
      List<FileReadThread> fileReadThreads = taskThreadMap.get(wsClientKey);
      if (null != fileReadThreads) {
        fileReadThreads.remove(this);
      } else {
        taskThreadMap.remove(wsClientKey);
      }
    }
  }

  private boolean dataSend(List<RecordSimulationTrajectoryDto> trajectories,
      long count) throws JsonProcessingException {
    // 数据拷贝
    if (dataCopyService != null) {
      dataCopy(trajectories, count);
    }
    if (StringUtils.isNotEmpty(wsClientKey)) {
      wsMessageSend(trajectories, (double) count);
    }
    return false;
  }

  private void dataCopy(List<RecordSimulationTrajectoryDto> trajectories,
      double count) throws JsonProcessingException {
    // 过滤信息
    if (null != pts) {
      for (int i = trajectories.size() - 1; i >= 0; i--) {
        List<Map<String, Object>> values = (List<Map<String, Object>>) trajectories.get(
            i).getValue();
        for (int j = values.size() - 1; j >= 0; j--) {
          Map<String, Object> tt = values.get(j);
          if (!LongitudeLatitudeUtils.isInPolygon(new Point2D.Double(
              Double.parseDouble(tt.get("longitude").toString()),
              Double.parseDouble(tt.get("latitude").toString())), pts)) {
            values.remove(tt);
          }
        }
        if (values.size() == 0) {
          trajectories.remove(trajectories.get(i));
        }
      }
    }
    if (trajectories.size() > 0) {
      dataCopyService.data(objectMapper.writeValueAsString(trajectories));
      if (progressChange != (int) count / 500) {
        dataCopyService.progress((int) (count / totalRecords * 100));
        progressChange++;
      }
    }
  }

  private void wsMessageSend(List<RecordSimulationTrajectoryDto> trajectories,
      double count) throws JsonProcessingException {
    String duration = DateUtils.secondsToDuration((int) Math.floor(count / 10));
    RealWebsocketMessage msg = new RealWebsocketMessage(
        Constants.RedisMessageType.TRAJECTORY, Maps.newHashMap(), trajectories,
        duration);
    WebSocketManage.sendInfo(wsClientKey,
        new ObjectMapper().writeValueAsString(msg));
    // 非轨迹数据发送
    extendDataChose(getDataCurrentTimestamp(trajectories));
  }

  private static Long getDataCurrentTimestamp(
      List<RecordSimulationTrajectoryDto> trajectories) {
    if (null != trajectories && !trajectories.isEmpty()) {
      return trajectories.get(0).getTimestamp();
    }
    return 0L;
  }

  public void setPauseState(boolean state) {
    pause.set(state);
  }

  public void sendStop() {
    stop.set(true);
  }

  private void dataSendEnd(long count) {
    if (null != dataCopyService) {
      dataCopyService.progress(100);
      dataCopyService.stop(exception);
    }
    if (StringUtils.isNotEmpty(wsClientKey)) {
      wsMessageSendStop((double) count);
    }
  }

  private void wsMessageSendStop(double count) {
    String duration = DateUtils.secondsToDuration((int) Math.floor(count / 10));
    RealWebsocketMessage endMsg = new RealWebsocketMessage(
        Constants.RedisMessageType.END, null, null, duration);
    try {
      WebSocketManage.sendInfo(wsClientKey,
          new ObjectMapper().writeValueAsString(endMsg));
    } catch (JsonProcessingException e) {
      if (log.isErrorEnabled()) {
        log.error("History file read end send error!", e);
      }
    }
  }

  private void extendDataChose(Long currentTimestamp)
      throws JsonProcessingException {
    if (null != extendedDataMap) {
      Set<Map.Entry<String, List<? extends ExtendedDataWrapper>>> entries = extendedDataMap.entrySet();
      for (Map.Entry<String, List<? extends ExtendedDataWrapper>> entry : entries) {
        String type = entry.getKey();
        List<? extends ExtendedDataWrapper> wrappers = entry.getValue();
        if (null != wrappers && wrappers.size() > 0) {
          ExtendedDataWrapper extendedDataWrapper = wrappers.get(0);
          Long dataTimestamp = extendedDataWrapper.getTimestamp();
          if (dataTimestamp <= currentTimestamp) {
            ExtendedDataWrapper remove = wrappers.remove(0);
            RealWebsocketMessage msg = new RealWebsocketMessage(type,
                Maps.newHashMap(), remove.getData(), "");
            WebSocketManage.sendInfo(wsClientKey,
                new ObjectMapper().writeValueAsString(msg));
          }
        }
      }
    }
  }
}
