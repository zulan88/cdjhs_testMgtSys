package net.wanji.business.exercise;

import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.service.ICdjhsExerciseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author: jenny
 * @create: 2024-07-05 9:06 上午
 */
@Service
public class ReloadService {
    @Autowired
    private ICdjhsExerciseRecordService recordService;

    //@PostConstruct
    public void init(){
        //查询未被执行的练习任务
        List<CdjhsExerciseRecord> records = recordService.selectUnexecutedExercises();
        for(CdjhsExerciseRecord record: records){
            recordService.putIntoTaskQueue(record);
        }
    }
}
