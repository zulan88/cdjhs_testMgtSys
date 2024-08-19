package net.wanji.business.exercise;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.CdjhsCarDetail;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.entity.TjDeviceDetail;
import net.wanji.business.exercise.dto.TestStartReqDto;
import net.wanji.business.exercise.enums.CarStatusEnum;
import net.wanji.business.exercise.enums.TaskStatusEnum;
import net.wanji.business.mapper.CdjhsExerciseRecordMapper;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.business.service.ICdjhsCarDetailService;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author: jenny
 * @create: 2024-08-13 10:06 上午
 */
@Component
@Slf4j
public class ScheduleSendEndCommand2YK {
    @Autowired
    private ICdjhsCarDetailService cdjhsCarDetailService;

    @Autowired
    private TjDeviceDetailMapper tjDeviceDetailMapper;

    @Autowired
    private CdjhsExerciseRecordMapper cdjhsExerciseRecordMapper;

    @Autowired
    private RedisCache redisCache;

    @Scheduled(cron = "0/10 * * * * ?")
    public void sendInCompetition(){
        try {
            //查询状态处于运行中的实车域控
            CdjhsCarDetail carDetail = new CdjhsCarDetail();
            carDetail.setStatus(CarStatusEnum.RUNNING.getStatus());
            List<CdjhsCarDetail> cdjhsCarDetails = cdjhsCarDetailService.queryByCondition(carDetail);
            if(StringUtils.isNotEmpty(cdjhsCarDetails)){
                //找出任务已结束但是仍然上报状态是运行中的域控
                List<CdjhsCarDetail> results = cdjhsCarDetails.stream()
                        .filter(car -> !ExerciseHandler.occupationMap.containsKey(car.getDeviceCode()))
                        .collect(Collectors.toList());

                for(CdjhsCarDetail cdjhsCarDetail: results){
                    String deviceCode = cdjhsCarDetail.getDeviceCode();
                    TjDeviceDetail tjDeviceDetail = tjDeviceDetailMapper.selectByUniques(deviceCode);
                    String dataChannel = tjDeviceDetail.getDataChannel();
                    String commandChannel = tjDeviceDetail.getCommandChannel();

                    //查找已结束的比赛任务
                    CdjhsExerciseRecord record = new CdjhsExerciseRecord();
                    record.setUserName(cdjhsCarDetail.getUserName());
                    record.setTeamName(cdjhsCarDetail.getTeamName());
                    record.setMirrorName(cdjhsCarDetail.getImageName());
                    record.setMirrorId(cdjhsCarDetail.getImageId());
                    record.setMd5(cdjhsCarDetail.getMd5());
                    record.setCarCode(cdjhsCarDetail.getCarCode());
                    record.setDeviceId(cdjhsCarDetail.getDeviceCode());
                    record.setStatus(TaskStatusEnum.FINISHED.getStatus());
                    record.setIsCompetition(1);
                    CdjhsExerciseRecord result = cdjhsExerciseRecordMapper.selectMatchedRecordByCondition(record);
                    if(Objects.nonNull(result)){
                        issue(deviceCode, dataChannel, commandChannel, result);
                    }
                }
            }
        }catch (Exception e){
            log.error("定时给实车域控下发结束指令报错", e);
        }
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void sendInExercise(){
        try {
            List<String> list = new ArrayList<>();
            String prefix = RedisKeyUtils.DEVICE_STATUS_PRE + RedisKeyUtils.DEVICE_STATUS_PRE_LINK;
            Set<String> keys = redisCache.getKeys(prefix);
            if(StringUtils.isNotEmpty(keys)){
                for(String key: keys){
                    Integer state = redisCache.getCacheObject(key);
                    if(Objects.nonNull(state)){
                        String[] split = key.split(RedisKeyUtils.DEVICE_STATUS_PRE_LINK);
                        String uniques = split[1];
                        if(state == 1){
                            list.add(uniques);
                        }
                    }
                }
            }
            //找出练习任务已结束但是域控仍然上报状态是占用的设备
            List<String> results = list.stream()
                    .filter(device -> !ExerciseHandler.occupationMap.containsKey(device))
                    .collect(Collectors.toList());
            if(!results.isEmpty()){
                for(String uniques: results){
                    TjDeviceDetail tjDeviceDetail = tjDeviceDetailMapper.selectByUniques(uniques);
                    String dataChannel = tjDeviceDetail.getDataChannel();
                    String commandChannel = tjDeviceDetail.getCommandChannel();

                    CdjhsExerciseRecord record = new CdjhsExerciseRecord();
                    record.setDeviceId(uniques);
                    record.setStatus(TaskStatusEnum.FINISHED.getStatus());
                    record.setIsCompetition(0);
                    CdjhsExerciseRecord result = cdjhsExerciseRecordMapper.selectMatchedRecordByCondition(record);
                    if(Objects.nonNull(result)){
                        issue(uniques, dataChannel, commandChannel, result);
                    }
                }
            }

        }catch (Exception e){
            log.error("定时给练习域控下发结束指令报错", e);
        }
    }

    private void issue(String deviceCode, String dataChannel, String commandChannel, CdjhsExerciseRecord result) {
        String tessDataChannel = Constants.ChannelBuilder.buildTaskDataChannel(result.getUserName(), result.getId().intValue());
        TestStartReqDto ykEnd = TaskExercise.buildYKTestStartEnd(dataChannel, tessDataChannel, 0);
        String endCommand = JSONObject.toJSONString(ykEnd);
        JSONObject ykEndMessage = JSONObject.parseObject(endCommand);
        redisCache.publishMessage(commandChannel, ykEndMessage);
        log.info("给域控【{}】指令通道【{}】下发任务结束:{}", deviceCode, commandChannel, endCommand);
    }
}
