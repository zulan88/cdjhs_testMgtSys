package net.wanji.business.schedule;

import com.alibaba.fastjson.JSONObject;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.dto.TjDeviceDetailDto;
import net.wanji.business.domain.vo.DeviceDetailVo;
import net.wanji.business.entity.TjDeviceDetail;
import net.wanji.business.entity.TjTask;
import net.wanji.business.entity.infity.TjInfinityTask;
import net.wanji.business.exercise.ExerciseHandler;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.business.service.RestService;
import net.wanji.business.service.TjDeviceDetailService;
import net.wanji.business.service.TjInfinityTaskService;
import net.wanji.business.service.TjTaskService;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.redis.RedisUtil;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.common.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DeviceStatusReflash {
    private static final Logger log = LoggerFactory.getLogger("deviceStatusLog");

    @Autowired
    private TjInfinityTaskService tjInfinityTaskService;

    @Autowired
    private RestService restService;

    @Autowired
    TjDeviceDetailService tjDeviceDetailService;

    @Autowired
    private TjDeviceDetailMapper deviceDetailMapper;

    @Autowired
    private TjTaskService tjTaskService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisCache redisCache;

    //@Scheduled(cron = "0 */5 * * * ?")
    public void reflash() {
        List<TjDeviceDetail> list = tjDeviceDetailService.list();
        for (TjDeviceDetail tjDeviceDetail : list) {
            Integer status = tjDeviceDetailService.selectDeviceState(tjDeviceDetail.getDeviceId(), tjDeviceDetail.getCommandChannel(), false);
            tjDeviceDetail.setStatus(status);
        }
        tjDeviceDetailService.updateBatchById(list);
    }

    /**
     * 判断任务是否逾期
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void editstatus(){
        List<TjTask> list = tjTaskService.list();
        for (TjTask tjTask : list) {
            Date date = new Date();
            if(date.compareTo(tjTask.getEndTime())>0){
                tjTask.setOpStatus(4);
                tjTaskService.updateById(tjTask);
            }
        }
    }

    /**
     * 释放长时间处于准备中的任务
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void editTWstatus(){
        TjDeviceDetailDto deviceDetailDto = new TjDeviceDetailDto();
        deviceDetailDto.setSupportRoles(Constants.PartRole.MV_SIMULATION);
        List<DeviceDetailVo> deviceDetailVos = deviceDetailMapper.selectByCondition(deviceDetailDto);
        if (CollectionUtils.isEmpty(deviceDetailVos)) {
            return ;
        }
        DeviceDetailVo detailVo = deviceDetailVos.get(0);
        List<TjTask> list = tjTaskService.list();
        for (TjTask tjTask : list) {
            if(tjTask.getStatus().equals("prepping")){
                if(!redisUtil.exists("tw_"+tjTask.getId())){
                    tjTask.setStatus(tjTask.getLastStatus());
                    tjTaskService.updateById(tjTask);
                    restService.stopTessNg(detailVo.getIp(), detailVo.getServiceAddress(), Constants.ChannelBuilder.buildTaskDataChannel(tjTask.getCreatedBy(), tjTask.getId()),1);
                }
            }
        }
        List<TjInfinityTask> list1 = tjInfinityTaskService.list();
        for (TjInfinityTask tjInfinityTask : list1) {
            if(tjInfinityTask.getStatus().equals("prepping")){
                if(!redisUtil.exists("twin_"+tjInfinityTask.getId())){
                    tjInfinityTask.setStatus(tjInfinityTask.getLastStatus());
                    tjInfinityTaskService.updateById(tjInfinityTask);
                    restService.stopTessNg(detailVo.getIp(), detailVo.getServiceAddress(), Constants.ChannelBuilder.buildTestingDataChannel(tjInfinityTask.getCreatedBy(), tjInfinityTask.getId()), 0);
                }
            }
        }
    }


    @Scheduled(cron = "0/20 * * * * ?")
    public void redisDeviceStatusLog(){
        String prefix = RedisKeyUtils.DEVICE_STATUS_PRE + RedisKeyUtils.DEVICE_STATUS_PRE_LINK;
        Set<String> keys = redisCache.getKeys(prefix);
        if(StringUtils.isNotEmpty(keys)){
            List<String> onlineDevices = new ArrayList<>();
            List<String> idleDevices = new ArrayList<>();
            for(String key: keys){
                Integer state = redisCache.getCacheObject(key);
                if(Objects.nonNull(state)){
                    String[] split = key.split(RedisKeyUtils.DEVICE_STATUS_PRE_LINK);
                    String uniques = split[1];
                    onlineDevices.add(uniques);
                    if(state == 2 && !ExerciseHandler.occupationMap.containsKey(uniques)){
                        idleDevices.add(uniques);
                    }
                }
            }
            log.info("当前在线练习域控: {}", JSONObject.toJSONString(onlineDevices));
            log.info("当前空闲练习域控: {}", JSONObject.toJSONString(idleDevices));
        }else{
            log.info("当前无在线域控设备");
        }
    }

}
