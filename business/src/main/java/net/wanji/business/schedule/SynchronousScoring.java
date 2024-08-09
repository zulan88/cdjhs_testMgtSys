package net.wanji.business.schedule;

import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.mapper.CdjhsExerciseRecordMapper;
import net.wanji.business.service.RestService;
import net.wanji.onsite.entity.Evaluation;
import net.wanji.onsite.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

}
