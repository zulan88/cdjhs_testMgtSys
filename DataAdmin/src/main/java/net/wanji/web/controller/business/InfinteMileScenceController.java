package net.wanji.web.controller.business;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.wanji.business.domain.*;
import net.wanji.business.entity.infity.TjInfinityTask;
import net.wanji.business.entity.infity.TjInfinityTaskRecord;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.service.ITjAtlasVenueService;
import net.wanji.business.service.InfinteMileScenceService;
import net.wanji.business.service.TjInfinityTaskRecordService;
import net.wanji.business.service.TjInfinityTaskService;
import net.wanji.business.service.record.DataCopyService;
import net.wanji.business.service.record.DataFileService;
import net.wanji.business.util.SceneLibMap;
import net.wanji.business.util.ToBuildOpenX;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.exception.ServiceException;
import net.wanji.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static net.wanji.framework.datasource.DynamicDataSourceContextHolder.log;

@RestController
@RequestMapping("/infintemile")
public class InfinteMileScenceController extends BaseController {

    @Autowired
    private InfinteMileScenceService infinteMileScenceService;

    @Autowired
    private ITjAtlasVenueService tjAtlasVenueService;

    @Autowired
    private TjInfinityTaskService tjInfinityTaskService;

    @Autowired
    private TjInfinityTaskRecordService tjInfinityTaskRecordService;

    @Autowired
    private DataFileService dataFileService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    ToBuildOpenX toBuildOpenX;

    @GetMapping("/list")
    public TableDataInfo list(Integer status) {
        startPage();
        List<InfinteMileScenceExo> list = infinteMileScenceService.selectInfinteMileScenceList(status);
        if (!SecurityUtils.getUsername().equals("admin")) {
            list = list.stream().filter(infinteMileScenceExo -> infinteMileScenceExo.getCreatedBy().equals(SecurityUtils.getUsername())).collect(Collectors.toList());
        }
        List<InfinteMileScenceExo> infinteMileScenceExoList = list.stream().peek(infinteMileScenceExo -> {
            infinteMileScenceService.dualInfiniteSimulation(infinteMileScenceExo);
            if (infinteMileScenceExo.getInElements() != null) {
                long testNum = infinteMileScenceExo.getInElements().stream().filter(inElement -> inElement.getType().equals(0)).count();
                infinteMileScenceExo.setTestNum(testNum);
                infinteMileScenceExo.setOtherNum(infinteMileScenceExo.getInElements().size() - testNum);
            }
        }).collect(Collectors.toList());
        return getDataTable(infinteMileScenceExoList, list);
    }

    @GetMapping("/init")
    public AjaxResult init() {
        return AjaxResult.success(infinteMileScenceService.buildSceneNumber());
    }

    @PostMapping("/save")
    public AjaxResult save(@RequestBody InfinteMileScenceExo infinteMileScence) throws BusinessException, ServiceException {
        Integer id = infinteMileScenceService.saveInfinteMileScence(infinteMileScence);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        return AjaxResult.success(jsonObject);
    }

    @DeleteMapping("/delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id) {
        QueryWrapper<TjInfinityTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("case_id", id);
        if (tjInfinityTaskService.count(queryWrapper) > 0) {
            return AjaxResult.error("该场景下存在仿真任务，请先删除仿真任务");
        }
        return toAjax(infinteMileScenceService.removeById(id));
    }

    @PostMapping("/debugging")
    public AjaxResult debugging(@RequestBody InfinteMileScenceExo infinteMileScence) throws BusinessException {
        if (infinteMileScence.getId() == null) {
            return AjaxResult.error("请先保存场景");
        }
        String repeatKey = "DEBUGGING_INSCENE_" + infinteMileScence.getViewId();
        if (redisCache.hasKey(repeatKey) && !redisCache.getCacheObject(repeatKey).equals(SecurityUtils.getUsername())) {
            return AjaxResult.error("有其他用户正在调试该场景，请稍后再试");
        } else if (redisCache.hasKey(repeatKey)) {
            infinteMileScenceService.stopInfinteSimulation(infinteMileScence.getId());
        }
        redisCache.setCacheObject(repeatKey, SecurityUtils.getUsername(), 3, TimeUnit.MINUTES);
        String key = "DEBUGGING_SUBMIT_" + infinteMileScence.getViewId();
        if (!redisCache.lock(key, key, 10)) {
            return AjaxResult.error("正在连接仿真软件，请稍后再试");
        }
        infinteMileScenceService.debugging(infinteMileScence);
        redisCache.unlock2(key, key);
        return AjaxResult.success();
    }

    @GetMapping("/getflowsite")
    public AjaxResult getflowsite(@RequestParam("mapId") Integer mapId) throws BusinessException {
//        TjAtlasVenue tjAtlasVenue = tjAtlasVenueService.getById(mapId);
//        List<TrafficFlow> jsonArray = JSON.parseArray(tjAtlasVenue.getAttribute3(), TrafficFlow.class);
        List<TrafficFlow> list = infinteMileScenceService.getTrafficFlow(mapId);
        return AjaxResult.success(list);
    }

    @GetMapping("/getinfo")
    public AjaxResult test(@RequestParam("id") Integer id) {
        InfinteMileScenceExo infinteMileScenceExo = infinteMileScenceService.selectInfinteMileScenceById(id);
        return AjaxResult.success(infinteMileScenceExo);
    }

    @GetMapping("/simustop")
    public AjaxResult simustop(@RequestParam("id") Integer id) throws BusinessException {
        infinteMileScenceService.stopInfinteSimulation(id);
        return AjaxResult.success();
    }

    @GetMapping("/getsliceimg")
    public AjaxResult simustart(@RequestParam("id") Integer id) throws BusinessException {
        TjInfinityTask tjInfinityTask = tjInfinityTaskService.getById(id);
        Integer scenceId = tjInfinityTask.getCaseId();
        return AjaxResult.success(infinteMileScenceService.getSiteSlices(scenceId));
    }

    @GetMapping("/sliceToOpenX")
    public AjaxResult testPlaybackByTime(Integer recordId, Long startTime,
                                         Long endTime, Integer shardingId) throws Exception {
        TjInfinityTaskRecord tjInfinityTaskRecord = tjInfinityTaskRecordService.getById(recordId);
        TjInfinityTask tjInfinityTask = tjInfinityTaskService.getById(tjInfinityTaskRecord.getCaseId());
        Integer scenceId = tjInfinityTask.getCaseId();
        dataFileService.playback(tjInfinityTaskRecord.getDataFileId(), startTime, endTime, scenceId, shardingId,
                new DataCopyService() {

                    Map<String, List<ClientSimulationTrajectoryDto>> map = new HashMap<>();

                    InfinteMileScenceExo scenceExo = infinteMileScenceService.selectInfinteMileScenceById(scenceId);

                    @Override
                    public void data(String data) {
                        List<ClientSimulationTrajectoryDto> list = JSON.parseArray(data, ClientSimulationTrajectoryDto.class);
                        AtomicBoolean flag = new AtomicBoolean(true);
                        list.forEach(clientSimulationTrajectoryDto -> {
                            if (clientSimulationTrajectoryDto.getRole().equals("av") && flag.get()) {
                                List<ClientSimulationTrajectoryDto> res = map.getOrDefault("av", new ArrayList<>());
                                res.add(clientSimulationTrajectoryDto);
                                map.put("av", res);
                                flag.set(false);
                            } else {
                                for (TrajectoryValueDto trajectoryValueDto : clientSimulationTrajectoryDto.getValue()) {
                                    ClientSimulationTrajectoryDto client = new ClientSimulationTrajectoryDto();
                                    client.setRole(clientSimulationTrajectoryDto.getRole());
                                    List<TrajectoryValueDto> trajectoryValueDtos = new ArrayList<>();
                                    trajectoryValueDtos.add(trajectoryValueDto);
                                    client.setValue(trajectoryValueDtos);
                                    List<ClientSimulationTrajectoryDto> res = map.getOrDefault(trajectoryValueDto.getId(), new ArrayList<>());
                                    res.add(client);
                                    map.put(trajectoryValueDto.getId(), res);
                                }
                            }
                        });
                    }

                    @Override
                    public void progress(int progress) {
                        log.info("progress=[{}]", progress);
                    }

                    @Override
                    public void stop(Exception e) {
                        try {
                            log.info("stop:", e);
                            SceneLibMap.put(startTime, toBuildOpenX.sclicetoOpenX(scenceExo, map));
                        } catch (BusinessException | IOException | JAXBException | TransformerException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
        while (!SceneLibMap.isExist(startTime)) {
            Thread.sleep(500);
        }
        return AjaxResult.success(SceneLibMap.get(startTime));
    }

}
