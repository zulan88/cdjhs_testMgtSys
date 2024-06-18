package net.wanji.business.service.evaluation.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.RealWebsocketMessage;
import net.wanji.business.entity.TjTaskCaseRecord;
import net.wanji.business.entity.evaluation.single.TjTestSingleSceneScore;
import net.wanji.business.entity.infity.TjShardingChangeRecord;
import net.wanji.business.evaluation.EvalContext;
import net.wanji.business.mapper.evaluation.single.TjTestSingleSceneScoreMapper;
import net.wanji.business.service.TjShardingChangeRecordService;
import net.wanji.business.service.TjTaskCaseRecordService;
import net.wanji.business.service.evaluation.TjTestSingleSceneScoreService;
import net.wanji.business.socket.WebSocketManage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author hcy
 * @version 1.0
 * @className TjTestSingleSceneScoreServiceImpl
 * @description TODO
 * @date 2024/4/22 15:35
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class TjTestSingleSceneScoreServiceImpl
    extends ServiceImpl<TjTestSingleSceneScoreMapper, TjTestSingleSceneScore>
    implements TjTestSingleSceneScoreService {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Resource
  private TjTestSingleSceneScoreMapper tjTestSingleSceneScoreMapper;

  private final TjShardingChangeRecordService tjShardingChangeRecordService;
  private final TjTaskCaseRecordService tjTaskCaseRecordService;

  @Override
  @Transactional
  public void data(Object data, EvalContext contextDto) {
    try {
      TjTestSingleSceneScore tjTestSingleSceneScore = objectMapper.readValue(
          String.valueOf(data), TjTestSingleSceneScore.class);
      if (1 == tjTestSingleSceneScore.getSenceStatus()) {
        // 评分结束后发送总评分
        evaluationInfoSave(tjTestSingleSceneScore, contextDto);
      } else {
        // 发送 ws
        wsSend(tjTestSingleSceneScore, contextDto.getWsClientKey());
      }
    } catch (Exception e) {
      if (log.isErrorEnabled()) {
        log.error("tjSceneScore process data error!", e);
      }
    }
  }

  public boolean saveByUnique(TjTestSingleSceneScore entity) {
    QueryWrapper<TjTestSingleSceneScore> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("task_id", entity.getTaskId());
    queryWrapper.eq("case_id", entity.getCaseId());
    queryWrapper.eq("record_id", entity.getRecordId());
    queryWrapper.eq("slice_id", entity.getSliceId());

    queryWrapper.eq("test_type", entity.getTestType());
    queryWrapper.eq("evaluative_id", entity.getEvaluativeId());
    TjTestSingleSceneScore sceneScore = this.getOne(queryWrapper);
    if (null == sceneScore) {
      return tjTestSingleSceneScoreMapper.insert(entity) > 0;
    } else {
      if (log.isErrorEnabled()) {
        log.error("Repeat data TjTestSingleSceneScore[{}]!", entity);
      }
      return false;
    }
  }

  @Override
  public void data(String channel, Object data, EvalContext contextDto) {

  }

  @Override
  public String dataId(Object data) {
    return null;
  }

  private void evaluationInfoSave(TjTestSingleSceneScore tjTestSingleSceneScore,
      EvalContext evalContext) {
    tjTestSingleSceneScore.setRecordId(evalContext.getRecordId());
    tjTestSingleSceneScore.setTestType(evalContext.getTestType());
    tjTestSingleSceneScore.setTaskId(evalContext.getTaskId());
    tjTestSingleSceneScore.setCaseId(evalContext.getCaseId());
    // 无限里程
    if (3 == evalContext.getTestType()) {
      Integer shardingRecordingId = shardingRecordingId(evalContext,
          tjTestSingleSceneScore.getSliceId());
      if (null != shardingRecordingId) {
        tjTestSingleSceneScore.setEvaluativeId(shardingRecordingId);
      } else {
        return;
      }
    } else {
      Integer recordId = taskCaseRecord(evalContext);
      if(null != recordId){
        tjTestSingleSceneScore.setEvaluativeId(recordId);
      }
    }
    saveByUnique(tjTestSingleSceneScore);
  }

  private void wsSend(TjTestSingleSceneScore tjTestSingleSceneScore,
      String wsClientKey) throws JsonProcessingException {
    RealWebsocketMessage msg = new RealWebsocketMessage(
        Constants.RedisMessageType.SCORE, Maps.newHashMap(),
        tjTestSingleSceneScore, "");
    WebSocketManage.sendInfo(wsClientKey,
        new ObjectMapper().writeValueAsString(msg));
  }

  private Integer shardingRecordingId(EvalContext evalContext,
      Integer sliceId) {
    QueryWrapper<TjShardingChangeRecord> recordQueryWrapper = new QueryWrapper<>();
    recordQueryWrapper.eq("task_id", evalContext.getTaskId());
    recordQueryWrapper.eq("case_id", evalContext.getCaseId());
    recordQueryWrapper.eq("record_id", evalContext.getRecordId());
    recordQueryWrapper.eq("sharding_id", sliceId);
    recordQueryWrapper.eq("state", 0);

    recordQueryWrapper.orderByDesc("create_timestamp");
    List<TjShardingChangeRecord> list = tjShardingChangeRecordService.list(
        recordQueryWrapper);
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0).getId();
    } else {
      if (log.isErrorEnabled()) {
        log.error("[{}] has no tj_sharding_change_record!", evalContext);
      }
      return null;
    }
  }

  private Integer taskCaseRecord(EvalContext evalContext) {
    LambdaQueryWrapper<TjTaskCaseRecord> rlqw = new LambdaQueryWrapper<>();
    rlqw.eq(TjTaskCaseRecord::getTaskId, evalContext.getTaskId());
    rlqw.eq(TjTaskCaseRecord::getCaseId, evalContext.getCaseId());
    rlqw.eq(TjTaskCaseRecord::getRecordId, evalContext.getRecordId());
    rlqw.orderByDesc(TjTaskCaseRecord::getId);
    Page<TjTaskCaseRecord> page = tjTaskCaseRecordService.page(new Page<>(0, 1),
        rlqw);
    if (CollectionUtils.isNotEmpty(page.getRecords())) {
      return page.getRecords().get(0).getId();
    }
    return null;
  }

}
