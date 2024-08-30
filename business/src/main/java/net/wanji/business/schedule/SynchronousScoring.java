package net.wanji.business.schedule;

import net.wanji.business.common.Constants;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.exercise.dto.luansheng.CAMatchProcess;
import net.wanji.business.exercise.dto.luansheng.TaskCacheDto;
import net.wanji.business.mapper.CdjhsExerciseRecordMapper;
import net.wanji.business.service.RestService;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.domain.entity.SysDictData;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.onsite.entity.Evaluation;
import net.wanji.onsite.service.EvaluationService;
import net.wanji.system.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

@Component
public class SynchronousScoring {

    @Autowired
    private CdjhsExerciseRecordMapper cdjhsExerciseRecordMapper;

    @Autowired
    private RestService restService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private TwinsPlayback twinsPlayback;

    @Autowired
    private RedisCache redisCache;

    @Scheduled(fixedDelayString = "${scheduled.interval:30000}")
    public void synchronousScoring() throws Exception {
        System.out.println("触发时间"+new java.util.Date());
        CdjhsExerciseRecord cdjhsExerciseRecord = new CdjhsExerciseRecord();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -24);
        cdjhsExerciseRecord.setStartTime(calendar.getTime());
        cdjhsExerciseRecord.setIsCompetition(1);
        cdjhsExerciseRecord.setStatus(3);
        List<CdjhsExerciseRecord> recordList = cdjhsExerciseRecordMapper.selectCdjhsExerciseRecordList(cdjhsExerciseRecord);
        for (CdjhsExerciseRecord record : recordList) {
            if (record.getScore()==null){
                if(record.getEvaluationUrl()!=null){
                    Map<String, String> params = extractParameters(record.getEvaluationUrl());
                    String taskID = params.get("taskID");
                    if(taskID!=null){
                        Double score = restService.getEvaluationResult(taskID);
                        if(score!=null){
                            record.setScore(score);
                            if(record.getSubScore() != null && record.getTotalScore() == null){
                                List<SysDictData> testType = dictTypeService.selectDictDataByType(Constants.SysType.SORCE_WEIGHT);
                                if(testType.size()>0){
                                    Integer value = Integer.parseInt(testType.get(0).getDictValue());
                                    Double totalScore = (score * value + record.getSubScore() * (100 - value)) / 100.0;
                                    record.setTotalScore(totalScore);
                                    TaskCacheDto cache = redisCache.getCacheObject(RedisKeyUtils.CDJHS_CURRENT_TASK_CACHE);
                                    if(Objects.nonNull(cache)){
                                        cache.setScoreStatus(1);
                                        redisCache.setCacheObject(RedisKeyUtils.CDJHS_CURRENT_TASK_CACHE, cache);
                                        CAMatchProcess caMatchProcess = CAMatchProcess.buildSorceFinished(record.getId(), record.getTeamId(), value, score, record.getSubScore(), totalScore);
                                        twinsPlayback.sendCAMatchProcess("CAMatchProcess", caMatchProcess);
                                    }
                                }
                            }
                            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
                        }
                    }
                }
            }
        }
    }

    public static Map<String, String> extractParameters(String url) throws Exception {
        Map<String, String> parameters = new HashMap<>();
        URL urlObj = new URL(url);
        String query = urlObj.getQuery();

        if (query != null) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
                parameters.put(key, value);
            }
        }

        return parameters;
    }

    @Async
    public void takeTotalScore(Integer recordId, Double subScore, Integer teamId){
        CdjhsExerciseRecord record = cdjhsExerciseRecordMapper.selectCdjhsExerciseRecordById(Long.valueOf(recordId));
        if(record!=null){
            record.setSubScore(subScore);
            if(record.getScore() != null && record.getTotalScore() == null){
                Double score = record.getScore();
                List<SysDictData> testType = dictTypeService.selectDictDataByType(Constants.SysType.SORCE_WEIGHT);
                if(testType.size()>0){
                    Integer value = Integer.parseInt(testType.get(0).getDictValue());
                    Double totalScore = (score * value + record.getSubScore() * (100 - value)) / 100.0;
                    record.setTotalScore(totalScore);
                    TaskCacheDto cache = redisCache.getCacheObject(RedisKeyUtils.CDJHS_CURRENT_TASK_CACHE);
                    if(Objects.nonNull(cache)){
                        cache.setScoreStatus(1);
                        redisCache.setCacheObject(RedisKeyUtils.CDJHS_CURRENT_TASK_CACHE, cache);
                        CAMatchProcess caMatchProcess = CAMatchProcess.buildSorceFinished(record.getId(), Long.valueOf(teamId), value, score, subScore, totalScore);
                        twinsPlayback.sendCAMatchProcess("CAMatchProcess", caMatchProcess);
                    }
                }
            }
            cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(record);
        }
    }

}
