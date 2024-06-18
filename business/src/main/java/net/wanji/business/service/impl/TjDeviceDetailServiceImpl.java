package net.wanji.business.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.common.Constants.ChannelBuilder;
import net.wanji.business.common.Constants.DeviceStatusEnum;
import net.wanji.business.common.Constants.SysType;
import net.wanji.business.common.Constants.YN;
import net.wanji.business.component.DeviceStateToRedis;
import net.wanji.business.domain.dto.TjDeviceDetailDto;
import net.wanji.business.domain.dto.device.DeviceReadyStateParam;
import net.wanji.business.domain.dto.device.DeviceStateDto;
import net.wanji.business.domain.vo.DeviceDetailVo;
import net.wanji.business.entity.TjDeviceDetail;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.business.service.DeviceStateSendService;
import net.wanji.business.service.RestService;
import net.wanji.business.service.StatusManage;
import net.wanji.business.service.TjDeviceDetailService;
import net.wanji.business.trajectory.DeviceStateListener;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.system.service.ISysDictDataService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author guanyuduo
 * @description 针对表【tj_device】的数据库操作Service实现
 * @createDate 2023-08-17 10:56:39
 */
@Service
public class TjDeviceDetailServiceImpl extends ServiceImpl<TjDeviceDetailMapper, TjDeviceDetail> implements TjDeviceDetailService {

    private static final Logger log = LoggerFactory.getLogger("device");

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private TjDeviceDetailMapper deviceDetailMapper;

    @Autowired
    private DeviceStateToRedis deviceStateToRedis;

    @Autowired
    private DeviceStateSendService deviceStateSendService;

    @Autowired
    private RestService restService;

    @Autowired
    private DeviceStateListener deviceStateListener;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final static String DEVICE_BUSY_STATUS = "device_busy_status_%s";
    private final static String DEVICE_BUSY_STATUS_KET = "device_busy_status_key_%d_%d";

    @PostConstruct
    public void initDeviceState() {
        List<TjDeviceDetail> deviceDetails = this.list();
        for (TjDeviceDetail deviceDetail : deviceDetails) {
            selectDeviceState(deviceDetail.getDeviceId(), deviceDetail.getCommandChannel(), false);
        }
    }


    @Override
    public List<DeviceDetailVo> getAllDevices(TjDeviceDetailDto deviceDto) {
        List<DeviceDetailVo> devices = deviceDetailMapper.selectByCondition(deviceDto);
        return CollectionUtils.emptyIfNull(devices).stream().map(this::translate).collect(Collectors.toList());
    }

    @Override
    public DeviceDetailVo getDeviceDetail(TjDeviceDetailDto deviceDto) {
        List<DeviceDetailVo> devices = deviceDetailMapper.selectByCondition(deviceDto);
        DeviceDetailVo detailVo = CollectionUtils.isEmpty(devices) ? new DeviceDetailVo() : devices.get(0);
        return this.translate(detailVo);
    }

    private DeviceDetailVo translate(DeviceDetailVo detailVo) {
        detailVo.setStatusName(DeviceStatusEnum.getDescByCode(detailVo.getStatus()));
        detailVo.setSupportRolesName(dictDataService.selectDictLabel(SysType.PART_ROLE, detailVo.getSupportRoles()));
        return detailVo;
    }

    @Override
    public boolean saveDevice(TjDeviceDetailDto deviceDetailDto) {
        TjDeviceDetail deviceDetail = new TjDeviceDetail();
        if (ObjectUtils.isEmpty(deviceDetailDto.getDeviceId())) {
            // todo 设备状态刷新接口
            BeanUtils.copyProperties(deviceDetailDto, deviceDetail);
            deviceDetail.setStatus(YN.N_INT);
            deviceDetail.setLastOnlineDate(LocalDateTime.now());
            deviceDetail.setCreatedBy(SecurityUtils.getUsername());
            deviceDetail.setAttribute2(SecurityUtils.getUsername());
            deviceDetail.setCreatedDate(LocalDateTime.now());
            deviceDetail.setIsInner(0);
            if (!ObjectUtils.isEmpty(deviceDetailDto.getAttribute2())) {
                deviceDetail.setUpdatedBy(deviceDetailDto.getAttribute2());
            }
            return this.save(deviceDetail);
        }
        deviceDetail.setDeviceId(deviceDetailDto.getDeviceId());
        deviceDetail.setDeviceName(deviceDetailDto.getDeviceName());
        deviceDetail.setSupportRoles(deviceDetailDto.getSupportRoles());
        deviceDetail.setIp(deviceDetailDto.getIp());
        deviceDetail.setServiceAddress(deviceDetailDto.getServiceAddress());
        deviceDetail.setDataChannel(deviceDetailDto.getDataChannel());
        deviceDetail.setCommandChannel(deviceDetailDto.getCommandChannel());

        if (!ObjectUtils.isEmpty(deviceDetailDto.getAttribute2())) {
            deviceDetail.setUpdatedBy(deviceDetailDto.getAttribute2());
        }
        deviceDetail.setUpdatedDate(LocalDateTime.now());
        return this.updateById(deviceDetail);
    }

    @Override
    public boolean deleteDevice(Integer deviceId) {
        return this.removeById(deviceId);
    }

    @Override
    public boolean batchDeleteDevice(List<Integer> deviceIds) {
        return this.removeByIds(deviceIds);
    }

    @Override
    public Integer selectDeviceState(Integer deviceId, String commandChannel, boolean wait) {
        Integer state = deviceStateToRedis.query(deviceId, DeviceStateToRedis.DEVICE_STATE_PREFIX, ChannelBuilder.DEFAULT_STATUS_CHANNEL);
        if (!ObjectUtils.isEmpty(state) && state == 1) {
            log.info("缓存查询设备{}状态：{}", deviceId, state);
            return state;
        }
        return handDeviceState(deviceId, commandChannel, wait);
    }

    @Override
    public Integer handDeviceState(Integer deviceId, String commandChannel, boolean wait) {
        String lock = "STATE_" + commandChannel;
        if (redisCache.hasKey(lock)) {
            Integer state = deviceStateToRedis.query(deviceId, ChannelBuilder.DEFAULT_STATUS_CHANNEL, DeviceStateToRedis.DEVICE_STATE_PREFIX);
            return ObjectUtils.isEmpty(state) ? 0 : state;
        }
        redisCache.setCacheObject(lock, lock, 5, TimeUnit.SECONDS);

        DeviceStateDto deviceStateDto = new DeviceStateDto();
        deviceStateDto.setDeviceId(deviceId);
        deviceStateDto.setType(0);
        deviceStateDto.setTimestamp(System.currentTimeMillis());
        log.info("手动查询设备{}状态：{}", deviceId, JSONObject.toJSONString(deviceStateDto));
        deviceStateSendService.sendData(commandChannel, deviceStateDto);
        if (!wait) {
            Integer state = deviceStateToRedis.query(deviceId, ChannelBuilder.DEFAULT_STATUS_CHANNEL, DeviceStateToRedis.DEVICE_STATE_PREFIX);
            return ObjectUtils.isEmpty(state) ? 0 : state;
        }
        String key = DeviceStateToRedis.DEVICE_STATE_PREFIX + "_" + deviceId + "_" + ChannelBuilder.DEFAULT_STATUS_CHANNEL;
        try {
            StatusManage.addCountDownLatch(key, 50);
        } catch (InterruptedException e) {
            log.error("手动查询设备{}状态异常：{}", deviceId, e);
        }
        return (Integer) StatusManage.getValue(key);
    }

    @Override
    public Integer selectDeviceReadyState(Integer deviceId, String statusChannel, DeviceReadyStateParam stateParam, boolean wait) {
        Integer state = deviceStateToRedis.query(deviceId, DeviceStateToRedis.DEVICE_READY_STATE_PREFIX, statusChannel);
        if (!ObjectUtils.isEmpty(state) && state == 1) {
            log.info("缓存查询设备{}准备状态：{}", deviceId, state);
            return state;
        }
        return handDeviceReadyState(deviceId, statusChannel, stateParam, wait);
    }

    @Override
    public Integer handDeviceReadyState(Integer deviceId, String statusChannel, DeviceReadyStateParam stateParam, boolean wait) {
        String lock = "READY_STATE_" + statusChannel + deviceId;
        if (redisCache.hasKey(lock)) {
            Integer readyState = deviceStateToRedis.query(deviceId, statusChannel, DeviceStateToRedis.DEVICE_READY_STATE_PREFIX);
            return ObjectUtils.isEmpty(readyState) ? 0 : readyState;
        }
        redisCache.setCacheObject(lock, lock, 5, TimeUnit.SECONDS);
        if (!ChannelBuilder.DEFAULT_STATUS_CHANNEL.equals(statusChannel)) {
            deviceStateListener.addDeviceStateListener(statusChannel);
        }
        log.info("手动查询设备{}准备状态", deviceId);
        restService.selectDeviceReadyState(stateParam);
        if (!wait) {
            Integer readyState = deviceStateToRedis.query(deviceId, statusChannel, DeviceStateToRedis.DEVICE_READY_STATE_PREFIX);
            return ObjectUtils.isEmpty(readyState) ? 0 : readyState;
        }
        String key = DeviceStateToRedis.DEVICE_READY_STATE_PREFIX + "_" + deviceId + "_" + statusChannel;
        try {
            StatusManage.addCountDownLatch(key, 50);
        } catch (InterruptedException e) {
            log.error("手动查询设备{}准备状态异常", deviceId, e);
        }
        return (Integer) StatusManage.getValue(key);
    }

    @Override
    public Integer selectDeviceBusyStatus(String deviceId) {
        String redisKey = String.format(DEVICE_BUSY_STATUS, deviceId);
        int status = 0;
        if (redisCache.hasKey(redisKey)) {
            Map<String, Integer> cacheMap = redisCache.getCacheMap(redisKey);
            if (!cacheMap.isEmpty()) {
                for (Integer value : cacheMap.values()) {
                    if (1 == value) {
                        status = 1;
                        break;
                    }
                }
            }
        }
        return status;
    }

    @Override
    public synchronized boolean allDevicesIdle(List<String> deviceIds) {
        if (null != deviceIds) {
            for (String deviceId : deviceIds) {
                if (1 == this.selectDeviceBusyStatus(deviceId)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public synchronized Boolean setDeviceBusyStatus(String deviceId, Integer taskId,
                                                    Integer caseId, Integer busyStatus, boolean occupy) {
        String redisKey = String.format(DEVICE_BUSY_STATUS, deviceId);
        String valueKey = String.format(DEVICE_BUSY_STATUS_KET, taskId, caseId);
        Object valueResult = redisTemplate.opsForHash().get(redisKey, valueKey);
        if (!occupy) {
            if (null != valueResult) {
                redisTemplate.opsForHash().put(redisKey, valueKey, busyStatus);
                return true;
            }
        } else if (0 == busyStatus) {
            redisTemplate.opsForHash().delete(redisKey, valueKey);
            return true;
        } else {
            Integer status = this.selectDeviceBusyStatus(deviceId);
            if (0 == status) {
                Boolean result = redisTemplate.opsForHash()
                        .putIfAbsent(redisKey, valueKey, busyStatus);
                return Boolean.TRUE.equals(result);
            }
        }
        return false;
    }

}
