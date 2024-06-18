package net.wanji.business.service.record;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.domain.dto.ToLocalDto;
import net.wanji.business.entity.DataFile;
import net.wanji.business.service.record.impl.ExtendedDataWrapper;
import net.wanji.business.service.record.impl.FileWriteRunnable;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;

/**
 * @author hcy
 * @version 1.0
 * @className DataFileService
 * @description TODO
 * @date 2024/4/1 16:45
 **/
public interface DataFileService extends IService<DataFile> {

  FileWriteRunnable createToLocalThread(ToLocalDto toLocalDto);

  boolean writeStop(ToLocalDto toLocalDto) throws Exception;

  /**
   * 回放
   *
   * @param playbackId     回放ID，记录回放线程
   * @param fileId         文件记录ID
   * @param startTimestamp
   * @param endTimestamp
   */
  void playback(String playbackId, Integer fileId, Long startTimestamp,
      Long endTimestamp,
      Map<String, List<? extends ExtendedDataWrapper>> extendedDataWrappers)
      throws Exception;

  /**
   * 回放数据拷贝
   *
   * @param fileId
   * @param startTimestamp
   * @param endTimestamp
   * @param caseId          场景Id
   * @param shardingId      分片Id
   * @param dataCopyService
   * @throws Exception
   */
  void playback(Integer fileId, Long startTimestamp, Long endTimestamp,
      Integer caseId, Integer shardingId, DataCopyService dataCopyService)
      throws Exception;

  /**
   * 回放
   *
   * @param playbackId           回放ID，记录回放线程，可为null，为null时不会发送数据至ws
   * @param fileId               文件记录ID
   * @param startTimestamp       开始回放时间戳
   * @param endTimestamp         结束回放时间戳
   * @param playbackInterval     回放时间间隔（ms），转存可以设置为0
   * @param pts                  取车范围
   * @param dataCopyService      数据拷贝，可为null
   * @param extendedDataWrappers 回放扩展数据
   * @throws Exception
   */
  void playback(String playbackId, Integer fileId, Long startTimestamp,
      Long endTimestamp, Integer playbackInterval, List<Point2D.Double> pts,
      DataCopyService dataCopyService,
      Map<String, List<? extends ExtendedDataWrapper>> extendedDataWrappers)
      throws Exception;

  boolean playbackStop(String playbackId);

  /**
   * 暂停
   *
   * @param state      true：暂停，false：开始
   * @param playbackId
   * @return
   */
  boolean playbackPause(Boolean state, String playbackId);

  /**
   * 删除 文件及记录
   * @param id
   * @return
   */
  boolean delete(Integer id);
}
