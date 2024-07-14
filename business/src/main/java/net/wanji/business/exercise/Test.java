package net.wanji.business.exercise;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpiringMap;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.exercise.utils.ToBuildOpenXTransUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: jenny
 * @create: 2024-06-23 7:17 下午
 */
@Slf4j
public class Test {

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        LinkedBlockingQueue<CdjhsExerciseRecord> queue = new LinkedBlockingQueue<>(20);
        CdjhsExerciseRecord record1 = new CdjhsExerciseRecord();
        record1.setWaitingNum(0);
        queue.put(record1);

        CdjhsExerciseRecord record2 = new CdjhsExerciseRecord();
        record2.setWaitingNum(1);
        queue.put(record2);

        CdjhsExerciseRecord record3 = new CdjhsExerciseRecord();
        record3.setWaitingNum(2);
        queue.add(record3);

        CdjhsExerciseRecord[] records = queue.toArray(new CdjhsExerciseRecord[0]);
        List<CdjhsExerciseRecord> list = Stream.of(records)
                .peek(data -> data.setWaitingNum(data.getWaitingNum() + 1))
                .collect(Collectors.toList());
        for(CdjhsExerciseRecord record: list){
            System.out.println(record.getWaitingNum());
        }

        System.out.println("***********queue");
        for(CdjhsExerciseRecord record: queue){
            System.out.println(record.getWaitingNum());
        }

        Iterator<CdjhsExerciseRecord> iterator = queue.iterator();
        while (iterator.hasNext()){
            CdjhsExerciseRecord next = iterator.next();
            if(next.getWaitingNum() == 1){
                iterator.remove();
            }
        }
        System.out.println("***********queue");
        for(CdjhsExerciseRecord record: queue){
            System.out.println(record.getWaitingNum());
        }

        System.out.println("***********array");
        for(CdjhsExerciseRecord record: list){
            System.out.println(record.getWaitingNum());
        }


    }
}
