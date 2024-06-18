package net.wanji.business.schedule;

import net.wanji.business.common.Constants;
import net.wanji.business.domain.dto.TjDeviceDetailDto;
import net.wanji.business.domain.vo.DeviceDetailVo;
import net.wanji.business.entity.TjDeviceDetail;
import net.wanji.business.entity.TjTask;
import net.wanji.business.entity.infity.TjInfinityTask;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.business.service.RestService;
import net.wanji.business.service.TjDeviceDetailService;
import net.wanji.business.service.TjInfinityTaskService;
import net.wanji.business.service.TjTaskService;
import net.wanji.common.redis.RedisUtil;
import net.wanji.common.utils.SecurityUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class DeviceStatusReflash {

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

    @Scheduled(cron = "0 */5 * * * ?")
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

}
