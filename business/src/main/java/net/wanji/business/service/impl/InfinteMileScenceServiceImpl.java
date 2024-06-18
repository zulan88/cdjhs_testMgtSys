package net.wanji.business.service.impl;

import com.google.gson.Gson;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.*;
import net.wanji.business.domain.dto.TjDeviceDetailDto;
import net.wanji.business.domain.param.TessParam;
import net.wanji.business.domain.vo.DeviceDetailVo;
import net.wanji.business.entity.InfinteMileScence;
import net.wanji.business.entity.TjAtlasVenue;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.InfinteMileScenceMapper;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.business.service.ITjAtlasVenueService;
import net.wanji.business.service.InfinteMileScenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.service.RestService;
import net.wanji.business.trajectory.RedisTrajectory2Consumer;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.exception.ServiceException;
import net.wanji.common.utils.CounterUtil;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.bean.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wj
 * @since 2024-02-23
 */
@Service
public class InfinteMileScenceServiceImpl extends ServiceImpl<InfinteMileScenceMapper, InfinteMileScence> implements InfinteMileScenceService {

    @Autowired
    private TjDeviceDetailMapper deviceDetailMapper;

    @Autowired
    private RedisTrajectory2Consumer redisTrajectoryConsumer;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RestService restService;

    @Autowired
    private ITjAtlasVenueService tjAtlasVenueService;


    @Override
    public List<InfinteMileScenceExo> selectInfinteMileScenceList(Integer status) {
        List<InfinteMileScenceExo> list = baseMapper.selectInfinteMileScenceExo(status);
        return list;
    }

    @Override
    public synchronized String buildSceneNumber() {
        return StringUtils.format(Constants.ContentTemplate.INFINTE_NUMBER_TEMPLATE, DateUtils.getNowDayString(),
                CounterUtil.getRandomChar());
    }

    @Override
    public Integer saveInfinteMileScence(InfinteMileScenceExo infinteMileScence) throws BusinessException, ServiceException {
        if (infinteMileScence.getStatus()!= null && infinteMileScence.getStatus().equals(1)){
            checkInfiniteSimulation(infinteMileScence);
        }
        if (infinteMileScence.getId() != null){
            InfinteMileScence res = baseMapper.selectById(infinteMileScence.getId());
            if (res.getStatus() != null && res.getStatus().equals(1)){
                throw new ServiceException("场景已提交，无法修改", 303);
            }
            if (infinteMileScence.getStatus()!= null && infinteMileScence.getStatus().equals(1)){
                if (res.getRouteFile()== null){
                    throw new ServiceException("场景中主车轨迹不合规，请重新绘制轨迹并仿真", 304);
                }
            }
        }
        Gson gson = new Gson();
        boolean flag = false;
        if (infinteMileScence.getInElements()!= null && infinteMileScence.getInElements().size() > 0){
            infinteMileScence.setElement(gson.toJson(infinteMileScence.getInElements()));
        }
        if (infinteMileScence.getTrafficFlows()!= null && infinteMileScence.getTrafficFlows().size() > 0){
            infinteMileScence.setTrafficFlow(gson.toJson(infinteMileScence.getTrafficFlows()));
        }
        if (infinteMileScence.getSiteSlices()!= null && infinteMileScence.getSiteSlices().size() > 0){
            Set<Integer> buttom = new HashSet<>();
            List<SiteSlice> siteSlices = new ArrayList<>();
            for (SiteSlice slice : infinteMileScence.getSiteSlices()) {
                if (slice.getSliceId() == null){
                    throw new BusinessException("切片ID不能为空");
                }
                if (buttom.contains(slice.getSliceId())){
                    throw new BusinessException("切片ID重复");
                }
                buttom.add(slice.getSliceId());
                SiteSlice exo = new SiteSlice();
                exo.setSliceId(slice.getSliceId());
                exo.setRoute(slice.getRoute());
                exo.setSliceName(slice.getSliceName());
                siteSlices.add(exo);
                if (slice.getImgData() != null && slice.getImgData().length() > 0) {
                    String data = slice.getImgData().replace("/[\r\n]/g","");
                    slice.setImgData(data);
                }else {
                    flag = true;
                }
            }
            infinteMileScence.setSiteSlice(gson.toJson(siteSlices));
            infinteMileScence.setSliceImg(gson.toJson(infinteMileScence.getSiteSlices()));
        }
        if(infinteMileScence.getTrafficFlowConfigs()!= null && infinteMileScence.getTrafficFlowConfigs().size() > 0){
            infinteMileScence.setTrafficFlowConfig(gson.toJson(infinteMileScence.getTrafficFlowConfigs()));
        }
        if (null == infinteMileScence.getId()){
            infinteMileScence.setCreateDate(LocalDateTime.now());
            infinteMileScence.setCreatedBy(SecurityUtils.getUsername());
            this.save(infinteMileScence);
        }else{
            if(flag) {
                Map<Integer, SiteSlice> sliceMap = this.getSiteSlices(infinteMileScence.getId()).stream().collect(Collectors.toMap(SiteSlice::getSliceId, siteSlice -> siteSlice));
                List<SiteSlice> siteSlices = infinteMileScence.getSiteSlices();
                for (SiteSlice slice : siteSlices) {
                    if (sliceMap.containsKey(slice.getSliceId())) {
                        if (slice.getImgData() == null) {
                            String data = sliceMap.getOrDefault(slice.getSliceId(),new SiteSlice()).getImgData();
                            slice.setImgData(data);
                        }
                    }
                }
                infinteMileScence.setSliceImg(gson.toJson(siteSlices));
            }
            infinteMileScence.setUpdateDate(LocalDateTime.now());
            this.updateById(infinteMileScence);
        }
        return infinteMileScence.getId();
    }

    @Override
    public void debugging(InfinteMileScenceExo infinteMileScence) throws BusinessException {
        validDebugParam(infinteMileScence);
        InfinteMileScence scenes = this.getById(infinteMileScence.getId());
        if (ObjectUtils.isEmpty(scenes)) {
            throw new BusinessException("模板信息不存在，请先保存");
        }
        TjDeviceDetailDto deviceDetailDto = new TjDeviceDetailDto();
        deviceDetailDto.setSupportRoles(Constants.PartRole.MV_SIMULATION);
        List<DeviceDetailVo> deviceDetailVos = deviceDetailMapper.selectByCondition(deviceDetailDto);
        if (CollectionUtils.isEmpty(deviceDetailVos)) {
            throw new BusinessException("当前无可用仿真程序");
        }
        BeanUtils.copyBeanProp(infinteMileScence, scenes);
        infinteMileScence.getInElements().forEach(element -> {
            if (element.getType().equals(0) && element.getId()>10000){
                element.setId(1L);
            }
        });

        redisTrajectoryConsumer.addRunningChannelInfinite(infinteMileScence);

        List<String> mapList = new ArrayList<>();
        if (ObjectUtils.isEmpty(infinteMileScence.getMapId())) {
            mapList.add("10");
        }else {
            mapList.add(String.valueOf(infinteMileScence.getMapId()));
        }

        List<TrafficFlow> trafficFlows = new ArrayList<>();
        for(TrafficFlow trafficFlow : infinteMileScence.getTrafficFlows()){
            if (trafficFlow.getDeparturePoints()!= null && trafficFlow.getDeparturePoints().size() > 0){
                trafficFlows.add(trafficFlow);
            }
        }

        InfiniteTessParm testStartParam = new InfiniteTessParm();
        testStartParam.setInElements(infinteMileScence.getInElements());
        testStartParam.setTrafficFlows(trafficFlows);
        testStartParam.setTrafficFlowConfigs(infinteMileScence.getTrafficFlowConfigs());
        for(InElement element : testStartParam.getInElements()){
            SitePoint firstpoint = element.getRoute().get(0);
            SitePoint point = new SitePoint();
            point.setLatitude(firstpoint.getLatitude());
            point.setLongitude(firstpoint.getLongitude());
            element.getRoute().add(point);
        }

        String channel = Constants.ChannelBuilder.buildInfiniteSimulationChannel(SecurityUtils.getUsername(), infinteMileScence.getViewId());
        DeviceDetailVo detailVo = deviceDetailVos.get(0);
        int start = restService.startServer(detailVo.getIp(), Integer.valueOf(detailVo.getServiceAddress()),
                new TessParam().buildnfiniteSimulationParam("1", channel, testStartParam, mapList));
        if (start==0) {
            String repeatKey = "DEBUGGING_INSCENE_" + infinteMileScence.getViewId();
            redisCache.deleteObject(repeatKey);
            throw new BusinessException("仿真程序连接失败");
        }else if (start==2){
            String repeatKey = "DEBUGGING_INSCENE_" + infinteMileScence.getViewId();
            redisCache.deleteObject(repeatKey);
            throw new BusinessException("仿真程序忙，请稍后再试");
        }

    }

    @Override
    public InfinteMileScenceExo selectInfinteMileScenceById(Integer id) {
        InfinteMileScence infinteMileScence = this.getById(id);
        InfinteMileScenceExo infinteMileScenceExo = new InfinteMileScenceExo();
        BeanUtils.copyBeanProp(infinteMileScenceExo, infinteMileScence);
        dualInfiniteSimulation(infinteMileScenceExo);
        TjAtlasVenue atlasVenue = tjAtlasVenueService.getById(infinteMileScence.getMapId());
        infinteMileScenceExo.setMapName(atlasVenue.getName());
        return infinteMileScenceExo;
    }

    @Override
    public InfinteMileScenceExo selectInfinteMileScenceById2(Integer id) {
        InfinteMileScence infinteMileScence = this.getById(id);
        InfinteMileScenceExo infinteMileScenceExo = new InfinteMileScenceExo();
        infinteMileScenceExo.setRouteFile(infinteMileScence.getRouteFile());
        infinteMileScenceExo.setMapFile(infinteMileScence.getMapFile());
        infinteMileScenceExo.setMapId(infinteMileScence.getMapId());
        return infinteMileScenceExo;
    }

    @Override
    public SiteSlice getSiteSlice(Integer caseId, Integer siteId) {
        InfinteMileScence infinteMileScence = this.getById(caseId);
        InfinteMileScenceExo infinteMileScenceExo = new InfinteMileScenceExo();
        BeanUtils.copyBeanProp(infinteMileScenceExo, infinteMileScence);
        dualInfiniteSimulation(infinteMileScenceExo);
        if (infinteMileScenceExo.getSiteSlices() != null && infinteMileScenceExo.getSiteSlices().size() > 0){
            for (SiteSlice siteSlice : infinteMileScenceExo.getSiteSlices()){
                if (Objects.equals(siteSlice.getSliceId(), siteId)){
                    return siteSlice;
                }
            }
        }
        return null;
    }

    private void validDebugParam(InfinteMileScenceExo infinteMileScenceExo) throws BusinessException{
        if (infinteMileScenceExo.getInElements()== null || infinteMileScenceExo.getInElements().size() == 0){
            throw new BusinessException("参与者轨迹不能为空");
        }else if (infinteMileScenceExo.getTrafficFlows()== null || infinteMileScenceExo.getTrafficFlows().size() == 0){
            throw new BusinessException("车流量信息不能为空");
        } else if (infinteMileScenceExo.getTrafficFlowConfigs()== null || infinteMileScenceExo.getTrafficFlowConfigs().size() == 0){
            throw new BusinessException("车流量配置信息不能为空");
        }
        AtomicBoolean flag = new AtomicBoolean(false);
        infinteMileScenceExo.getInElements().forEach(element -> {
            if (element.getType() == 0){
                flag.set(true);
            }
        });
        if (!flag.get()){
            throw new BusinessException("未配置AV参与者");
        }
    }

    public void dualInfiniteSimulation(InfinteMileScenceExo infinteMileScenceExo) {
        Gson gson = new Gson();
        if(infinteMileScenceExo.getElement()!= null&&infinteMileScenceExo.getElement().length() > 0){
            List<InElement> inElements = Arrays.asList(gson.fromJson(infinteMileScenceExo.getElement(), InElement[].class));
            infinteMileScenceExo.setInElements(inElements);
        }
        if(infinteMileScenceExo.getTrafficFlow()!= null&&infinteMileScenceExo.getTrafficFlow().length() > 0){
            List<TrafficFlow> trafficFlows = Arrays.asList(gson.fromJson(infinteMileScenceExo.getTrafficFlow(), TrafficFlow[].class));
            infinteMileScenceExo.setTrafficFlows(trafficFlows);
        }
        if(infinteMileScenceExo.getSiteSlice()!= null&&infinteMileScenceExo.getSiteSlice().length() > 0){
            List<SiteSlice> siteSlices = Arrays.asList(gson.fromJson(infinteMileScenceExo.getSiteSlice(), SiteSlice[].class));
            infinteMileScenceExo.setSiteSlices(siteSlices);
        }
        if(infinteMileScenceExo.getTrafficFlowConfig()!= null&&infinteMileScenceExo.getTrafficFlowConfig().length() > 0){
            List<TrafficFlowConfig> trafficFlowConfigs = Arrays.asList(gson.fromJson(infinteMileScenceExo.getTrafficFlowConfig(), TrafficFlowConfig[].class));
            infinteMileScenceExo.setTrafficFlowConfigs(trafficFlowConfigs);
        }
    }

    @Override
    public boolean stopInfinteSimulation(Integer id){
        InfinteMileScence infinteMileScence = this.getById(id);
        String channel = Constants.ChannelBuilder.buildInfiniteSimulationChannel(SecurityUtils.getUsername(), infinteMileScence.getViewId());
        TjDeviceDetailDto deviceDetailDto = new TjDeviceDetailDto();
        deviceDetailDto.setSupportRoles(Constants.PartRole.MV_SIMULATION);
        List<DeviceDetailVo> deviceDetailVos = deviceDetailMapper.selectByCondition(deviceDetailDto);
        if (CollectionUtils.isEmpty(deviceDetailVos)) {
            return false;
        }
        DeviceDetailVo detailVo = deviceDetailVos.get(0);
        return restService.stopTessNg(detailVo.getIp(), detailVo.getServiceAddress(),channel,0);
    }

    @Override
    public List<TrafficFlow> getTrafficFlow(Integer maoid) throws BusinessException {
        TjDeviceDetailDto deviceDetailDto = new TjDeviceDetailDto();
        deviceDetailDto.setSupportRoles(Constants.PartRole.MV_SIMULATION);
        List<DeviceDetailVo> deviceDetailVos = deviceDetailMapper.selectByCondition(deviceDetailDto);
        if (CollectionUtils.isEmpty(deviceDetailVos)) {
            throw new BusinessException("当前无可用仿真程序");
        }
        DeviceDetailVo detailVo = deviceDetailVos.get(0);
        List<TrafficFlow> list =  restService.getTrafficFlow(detailVo.getIp(), detailVo.getServiceAddress(),maoid);
        return list;
    }

    @Override
    public List<SiteSlice> getSiteSlices(Integer id) {
        Gson gson = new Gson();
        String data = baseMapper.getSliceImage(id);
        if (data == null || data.length() == 0) {
            return new ArrayList<>();
        }
        List<SiteSlice> siteSlices = Arrays.asList(gson.fromJson(data, SiteSlice[].class));
        return siteSlices;
    }

    private void checkInfiniteSimulation(InfinteMileScenceExo infinteMileScence) throws BusinessException {
        validDebugParam(infinteMileScence);
        if (infinteMileScence.getSiteSlices()== null || infinteMileScence.getSiteSlices().size() == 0){
            throw new BusinessException("场景切片不能为空");
        }
    }

}
