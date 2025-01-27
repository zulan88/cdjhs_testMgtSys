package net.wanji.business.service.impl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.CdShape;
import net.wanji.business.domain.PartConfigSelect;
import net.wanji.business.domain.Tjshape;
import net.wanji.business.domain.WoPostion;
import net.wanji.business.domain.bo.CaseTrajectoryDetailBo;
import net.wanji.business.domain.bo.ParticipantTrajectoryBo;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.business.domain.bo.TrajectoryDetailBo;
import net.wanji.business.domain.dto.CaseQueryDto;
import net.wanji.business.domain.dto.RoutingPlanDto;
import net.wanji.business.domain.dto.TjCaseDto;
import net.wanji.business.domain.dto.TjFragmentedSceneDetailDto;
import net.wanji.business.domain.vo.*;
import net.wanji.business.entity.TjCase;
import net.wanji.business.entity.TjCasePartConfig;
import net.wanji.business.entity.TjFragmentedSceneDetail;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjCaseMapper;
import net.wanji.business.mapper.TjFragmentedSceneDetailMapper;
import net.wanji.business.schedule.PlaybackSchedule;
import net.wanji.business.schedule.SceneLabelMap;
import net.wanji.business.service.*;
import net.wanji.business.util.AnalyzeOpenX;
import net.wanji.business.util.SceneLibMap;
import net.wanji.business.util.ToBuildOpenX;
import net.wanji.business.util.ToBuildOpenXUtil;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.file.FileUtils;
import net.wanji.common.utils.CounterUtil;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.onsite.entity.TjOnsiteCase;
import net.wanji.onsite.service.TjOnsiteCaseService;
import net.wanji.openx.generated.RoutePosition;
import net.wanji.openx.generated.Trajectory;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.ProjCoordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.wanji.business.mapper.TjScenelibMapper;
import net.wanji.business.entity.TjScenelib;

import javax.annotation.Resource;

/**
 * scenelibService业务层处理
 *
 * @author wanji
 * @date 2023-10-31
 */
@Service
public class TjScenelibServiceImpl extends ServiceImpl<TjScenelibMapper, TjScenelib> implements ITjScenelibService {
    @Autowired
    private TjScenelibMapper tjScenelibMapper;
    @Autowired
    private TjFragmentedSceneDetailMapper sceneDetailMapper;
    @Autowired
    private SceneLabelMap sceneLabelMap;
    @Autowired
    private TjCaseService caseService;
    @Autowired
    private TjCasePartConfigService casePartConfigService;
    @Autowired
    private TjOnsiteCaseService tjOnsiteCaseService;
    @Autowired
    private AnalyzeOpenX analyzeOpenX;
    @Autowired
    private TjFragmentedSceneDetailService tjFragmentedSceneDetailService;


    /**
     * 查询scenelib
     *
     * @param id scenelibID
     * @return scenelib
     */
    @Override
    public TjScenelib selectTjScenelibById(Long id) {
        return tjScenelibMapper.selectTjScenelibById(id);
    }

    /**
     * 查询scenelib列表
     *
     * @param tjScenelib scenelib
     * @return scenelib
     */
    @Override
    public List<TjScenelib> selectTjScenelibList(TjScenelib tjScenelib) {
        return tjScenelibMapper.selectTjScenelibList(tjScenelib);
    }

    /**
     * 新增scenelib
     *
     * @param tjScenelib scenelib
     * @return 结果
     */
    @Override
    public int insertTjScenelib(TjScenelib tjScenelib) {
        List<String> labellist = new ArrayList<>();
        if (tjScenelib.getLabels().split(",").length > 0) {
            for (String id : tjScenelib.getLabels().split(",")) {
                labellist.addAll(sceneDetailMapper.getalllabel(id));
            }
        }
        tjScenelib.setAllStageLabels(CollectionUtils.isNotEmpty(labellist)
                ? labellist.stream().distinct().collect(Collectors.joining(","))
                : null);
        tjScenelib.setCreateBy("admin");
        tjScenelib.setCreateDatetime(LocalDateTime.now());
        tjScenelib.setSceneSource(0);
        tjScenelib.setSceneStatus(1);
        tjScenelib.setNumber(StringUtils.format(Constants.ContentTemplate.SCENE_NUMBER_TEMPLATE, DateUtils.getNowDayString(),
                CounterUtil.getRandomChar()));
        return tjScenelibMapper.insertTjScenelib(tjScenelib);
    }


    @Override
    public boolean insertTjScenelibBatch(List<TjScenelib> tjScenelibs) throws BusinessException {
        //场景库入库
        for (TjScenelib tjScenelib : tjScenelibs) {
            tjScenelib.setNumber(StringUtils.format(Constants.ContentTemplate.SCENE_NUMBER_TEMPLATE, DateUtils.getNowDayString(),
                    CounterUtil.getRandomChar()));
            //入库场景编辑器对应的库表中
            Integer SceneDetailId = insertframeSeanDetailNew(tjScenelib.getXoscPath(), tjScenelib);

            List<String> labellist = new ArrayList<>();
            if (tjScenelib.getLabels().split(",").length > 0) {
                for (String id : tjScenelib.getLabels().split(",")) {
                    labellist.addAll(sceneDetailMapper.getalllabel(id));
                }
            }
            tjScenelib.setAllStageLabels(CollectionUtils.isNotEmpty(labellist)
                    ? labellist.stream().distinct().collect(Collectors.joining(","))
                    : null);
            tjScenelib.setCreateBy("admin");
            tjScenelib.setCreateDatetime(LocalDateTime.now());
            tjScenelib.setSceneDetailId(SceneDetailId);

        }
        return this.saveBatch(tjScenelibs);
    }

    String projca = "+proj=tmerc +lon_0=108.90577060170472 +lat_0=34.37650478465651 +ellps=WGS84";

    String projtj = "+proj=tmerc +lon_0=121.20585769414902 +lat_0=31.290823210868965 +ellps=WGS84";

    @Autowired
    public ToBuildOpenXUtil toBuildOpenXUtil;
    private static final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

    public Integer insertframeSeanDetailNew(String xoscPath, TjScenelib tjScenelib) throws BusinessException {
        //往场景编辑器对应的表中
        TjFragmentedSceneDetailDto sceneDetailDto = new TjFragmentedSceneDetailDto();
        sceneDetailDto.setFragmentedSceneId(18);
        sceneDetailDto.setSceneSource("仿真");
        sceneDetailDto.setSimuType(0);
        sceneDetailDto.setTestSceneDesc(tjScenelib.getSceneDesc());
        List<String> labellist = new ArrayList<>();
        if (tjScenelib.getLabels().split(",").length > 0) {
            for (String id : tjScenelib.getLabels().split(",")) {
                labellist.addAll(sceneDetailMapper.getalllabel(id));
            }
        }
        sceneDetailDto.setLabelList(labellist);

        //读文件， 拼trajectoryDetail routingFile
        Map<String, List<TrajectoryDetailBo>> map = new HashMap<>();
        try {
            List<CdShape> analyze = analyzeOpenX.cdParseXML(xoscPath);
            String proj = projca;
            if (tjScenelib.getGeojsonPath().equals("10")) {
                proj = projtj;
            }

            String finalProj = proj;
            analyze.forEach(item -> {
                List<TrajectoryDetailBo> trajectoryDetailBos = new ArrayList<>();
                long index = 1;
                for (WoPostion wo : item.getWoPostionList()) {
                    JSONObject retotrans = toBuildOpenXUtil.retotrans(Double.parseDouble(wo.getX()), Double.parseDouble(wo.getY()), finalProj, Double.parseDouble(wo.getH()));
                    TrajectoryDetailBo trajectoryDetailBo = new TrajectoryDetailBo();
                    trajectoryDetailBo.setLane("0");
                    trajectoryDetailBo.setLongitude(retotrans.getString("longitude"));
                    trajectoryDetailBo.setLatitude(retotrans.getString("latitude"));
                    trajectoryDetailBo.setSpeed(0.0);
                    trajectoryDetailBo.setHeading(retotrans.getString("degree"));
                    Double time = Double.valueOf(wo.getTime());
                    if (time < 0) {
//                    System.out.println("时间"+item.getDuration());
                        continue;
                    }
                    trajectoryDetailBo.setTime(wo.getTime());
                    trajectoryDetailBo.setType("pathway");
                    trajectoryDetailBo.setModel(wo.getType());
                    trajectoryDetailBo.setFrameId(index);
                    if (index == 1) {
                        trajectoryDetailBo.setType("start");
                        trajectoryDetailBos.add(trajectoryDetailBo);
                    } else {
                        trajectoryDetailBos.add(trajectoryDetailBo);
                    }
                    index++;

                }
                map.put(item.getId(), trajectoryDetailBos);

            });
        } catch (Exception e) {
            throw new BusinessException("解析openx文件失败");
        }

        final int[] i = {2};
        CaseTrajectoryDetailBo caseTrajectoryDetailBo = new CaseTrajectoryDetailBo();
        List<ParticipantTrajectoryBo> participantTrajectoryBos = new ArrayList<>();
        map.forEach((name, trajectoryDetailBo) -> {
            trajectoryDetailBo.get(trajectoryDetailBo.size() - 1).setType("end");
//            trajectoryDetailBo.get(0).setTime("0");
            ParticipantTrajectoryBo participantTrajectoryBo = new ParticipantTrajectoryBo();
            participantTrajectoryBo.setId(String.valueOf(i[0]));
            participantTrajectoryBo.setName(name);
            participantTrajectoryBo.setRole("mvSimulation");
            participantTrajectoryBo.setType("slave");
            if (name.equals("A0")) {
                //participantTrajectoryBo.setName("主车");
                participantTrajectoryBo.setType("main");
                participantTrajectoryBo.setRole("av");
                participantTrajectoryBo.setId("1");
                trajectoryDetailBo.get(0).setType("start");
            }
            participantTrajectoryBo.setModel(trajectoryDetailBo.get(0).getModel());
            participantTrajectoryBo.setTrajectory(trajectoryDetailBo);
            participantTrajectoryBos.add(participantTrajectoryBo);
            i[0]++;
        });
        caseTrajectoryDetailBo.setParticipantTrajectories(participantTrajectoryBos);

        String forder = WanjiConfig.getScenePath() + File.separator + DateUtils.datePath();
        File folder = new File(forder);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String filePath = forder + File.separator + "Allpoint" + System.currentTimeMillis() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(filePath)), StandardCharsets.UTF_8))) {
            writer.write(caseTrajectoryDetailBo.toJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (ParticipantTrajectoryBo participantTrajectoryBo : caseTrajectoryDetailBo.getParticipantTrajectories()) {
            List<TrajectoryDetailBo> trajectoryDetailBos = filterEveryNth(participantTrajectoryBo.getTrajectory(), 10);
            participantTrajectoryBo.setTrajectory(trajectoryDetailBos);
        }

        sceneDetailDto.setTrajectoryJson(caseTrajectoryDetailBo);
        sceneDetailDto.setImgUrl(tjScenelib.getImgPath());


        sceneDetailDto.setRouteFile(filePath);
        sceneDetailDto.setNumber(tjScenelib.getNumber());
        if (tjScenelib.getGeojsonPath() != null) {
            sceneDetailDto.setMapId(Integer.valueOf(tjScenelib.getGeojsonPath()));
        } else {
            sceneDetailDto.setMapId(21);
        }
        sceneDetailDto.setRoadCondition(tjScenelib.getXoscPath());

        return tjFragmentedSceneDetailService.saveSceneDetailInfo(sceneDetailDto);
    }

    private static List<TrajectoryDetailBo> filterEveryNth(List<TrajectoryDetailBo> list, int n) {
        if (list == null || list.isEmpty()) {
            return list;
        }

//        TrajectoryDetailBo first = list.get(0);

        List<TrajectoryDetailBo> list1 = IntStream.rangeClosed(0, list.size() - 1)
                .filter(i -> i == 0 || i == list.size() - 1 || i % n == 0)
                .mapToObj(list::get)
                .collect(Collectors.toList());
        return list1;
    }


    public Integer insertframeSeanDetail(String xoscPath, TjScenelib tjScenelib) throws BusinessException {
        //往场景编辑器对应的表中
        TjFragmentedSceneDetailDto sceneDetailDto = new TjFragmentedSceneDetailDto();
        sceneDetailDto.setFragmentedSceneId(18);
        sceneDetailDto.setSceneSource("仿真");
        sceneDetailDto.setSimuType(0);
        sceneDetailDto.setTestSceneDesc(tjScenelib.getSceneDesc());
        List<String> labellist = new ArrayList<>();
        if (tjScenelib.getLabels().split(",").length > 0) {
            for (String id : tjScenelib.getLabels().split(",")) {
                labellist.addAll(sceneDetailMapper.getalllabel(id));
            }
        }
        sceneDetailDto.setLabelList(labellist);

        //读文件， 拼trajectoryDetail routingFile
        Map<String, List<TrajectoryDetailBo>> map = new HashMap<>();
        List<Tjshape> analyze = analyzeOpenX.analyze(xoscPath);
        List<SimulationTrajectoryDto> simulationTrajectoryDtos = new ArrayList<>();
        final int[] frameId = {1};
        final int[] count = {0};
        analyze.forEach(item -> {
            SimulationTrajectoryDto simulationTrajectoryDto = new SimulationTrajectoryDto();
            simulationTrajectoryDto.setTimestamp(StringUtils.getTimeStamp());
            simulationTrajectoryDto.setTimestampType("CREATE_TIME");
            List<TrajectoryValueDto> trajectoryValueDtos = new ArrayList<>();
            for (WoPostion wo : item.getWoPostionList()) {
                if (count[0] % 20 != 0) {
                    count[0]++;
                    continue;
                }
                count[0]++;
                JSONObject retotrans = toBuildOpenXUtil.retotrans(Double.parseDouble(wo.getX()), Double.parseDouble(wo.getY()), projca, Double.parseDouble(wo.getH()));
                TrajectoryDetailBo trajectoryDetailBo = new TrajectoryDetailBo();
                List<TrajectoryDetailBo> trajectoryDetailBos = map.get(wo.getId());
                int index = 0;
                if (trajectoryDetailBos != null) {
                    index = trajectoryDetailBos.size();
                } else {
                    trajectoryDetailBos = new ArrayList<>();
                }
                trajectoryDetailBo.setFrameId((long) index);
                trajectoryDetailBo.setLane("0");
                if (wo.getId().equals("A0")) {
                    trajectoryDetailBo.setLongitude(wo.getX());
                    trajectoryDetailBo.setLatitude(wo.getY());
                }
                trajectoryDetailBo.setLongitude(retotrans.getString("longitude"));
                trajectoryDetailBo.setLatitude(retotrans.getString("latitude"));
                trajectoryDetailBo.setSpeed(0.0);
                Double time = item.getDuration() / 1000D;
                if (time < 0) {
//                    System.out.println("时间"+item.getDuration());
                    continue;
                }
                trajectoryDetailBo.setTime(String.valueOf(time));
                trajectoryDetailBo.setType("pathway");
                trajectoryDetailBo.setModel(wo.getType());
                TrajectoryValueDto trajectoryValueDto = new TrajectoryValueDto();
                if (index == 0) {
                    trajectoryDetailBo.setType("start");
                    trajectoryDetailBos.add(trajectoryDetailBo);
                    map.put(wo.getId(), trajectoryDetailBos);
                    trajectoryValueDto.setName("主车");
                    trajectoryValueDto.setDriveType(1);
                } else {
                    trajectoryDetailBos.add(trajectoryDetailBo);
                    trajectoryValueDto.setName(wo.getId());
                    trajectoryValueDto.setDriveType(2);
                }


                trajectoryValueDto.setCourseAngle(Double.parseDouble(wo.getH()));
                trajectoryValueDto.setDriveType(wo.getType());
                trajectoryValueDto.setFrameId(frameId[0]);
                trajectoryValueDto.setGlobalTimeStamp(String.valueOf(System.currentTimeMillis() + item.getDuration()));
                trajectoryValueDto.setId(wo.getId());
                trajectoryValueDto.setHeight(131);
                trajectoryValueDto.setLatitude(retotrans.getDoubleValue("latitude"));
                trajectoryValueDto.setLongitude(retotrans.getDoubleValue("longitude"));
                trajectoryValueDto.setLength(449);
                trajectoryValueDto.setOriginalColor(3);
                trajectoryValueDto.setPicLicense("china");
                trajectoryValueDto.setSpeed(0);
                trajectoryValueDto.setTimestamp(sf.format(new Date(Long.parseLong(trajectoryValueDto.getGlobalTimeStamp()))));
                trajectoryValueDto.setVehicleColor(0);
                trajectoryValueDto.setVehicleType(1);
                trajectoryValueDto.setWidth(195);
                trajectoryValueDtos.add(trajectoryValueDto);
                simulationTrajectoryDto.setValue(trajectoryValueDtos);

            }
            frameId[0]++;
            simulationTrajectoryDtos.add(simulationTrajectoryDto);
        });

//        Map<String, List<TrajectoryDetailBo>> sortedMap = map.entrySet().stream()
//                .sorted(Map.Entry.comparingByKey(Comparator.comparingInt(Integer::parseInt)))
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        final int[] i = {1};
        CaseTrajectoryDetailBo caseTrajectoryDetailBo = new CaseTrajectoryDetailBo();
        List<ParticipantTrajectoryBo> participantTrajectoryBos = new ArrayList<>();
        map.forEach((name, trajectoryDetailBo) -> {
            trajectoryDetailBo.get(trajectoryDetailBo.size() - 1).setType("end");
            ParticipantTrajectoryBo participantTrajectoryBo = new ParticipantTrajectoryBo();
            participantTrajectoryBo.setId(String.valueOf(i[0]));
            participantTrajectoryBo.setName(name);
            participantTrajectoryBo.setRole("mvSimulation");
            participantTrajectoryBo.setType("slave");
            if (name.equals("A0")) {
                //participantTrajectoryBo.setName("主车");
                participantTrajectoryBo.setType("main");
                participantTrajectoryBo.setRole("av");
                trajectoryDetailBo.get(0).setType("start");
            }
            participantTrajectoryBo.setModel(trajectoryDetailBo.get(0).getModel());
            participantTrajectoryBo.setTrajectory(trajectoryDetailBo);
            participantTrajectoryBos.add(participantTrajectoryBo);
            i[0]++;
        });
        caseTrajectoryDetailBo.setParticipantTrajectories(participantTrajectoryBos);
        sceneDetailDto.setTrajectoryJson(caseTrajectoryDetailBo);
        sceneDetailDto.setImgUrl(tjScenelib.getImgPath());

        //routingfile入库
        String filePath = WanjiConfig.getUploadPath() + "Routing" + System.currentTimeMillis() + ".txt";
        // 使用try-with-resources语句自动管理BufferedWriter资源
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (SimulationTrajectoryDto dto : simulationTrajectoryDtos) {
                // 假设toString()方法返回了想要写入的字符串
                writer.write(JSONObject.toJSONString(dto));
                writer.newLine(); // 添加一个新行
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        sceneDetailDto.setRouteFile(filePath);
        sceneDetailDto.setNumber(tjScenelib.getNumber());
        if (tjScenelib.getGeojsonPath() != null) {
            sceneDetailDto.setMapId(Integer.valueOf(tjScenelib.getGeojsonPath()));
        } else {
            sceneDetailDto.setMapId(21);
        }
        sceneDetailDto.setRoadCondition(tjScenelib.getXoscPath());

        return tjFragmentedSceneDetailService.saveSceneDetailInfo(sceneDetailDto);
    }

    /**
     * 修改scenelib
     *
     * @param tjScenelib scenelib
     * @return 结果
     */
    @Override
    public int updateTjScenelib(TjScenelib tjScenelib) {
        List<String> labellist = new ArrayList<>();
        if (tjScenelib.getLabels() != null && tjScenelib.getLabels().split(",").length > 0) {
            for (String id : tjScenelib.getLabels().split(",")) {
                labellist.addAll(sceneDetailMapper.getalllabel(id));
            }
        }
        tjScenelib.setAllStageLabels(CollectionUtils.isNotEmpty(labellist)
                ? labellist.stream().distinct().collect(Collectors.joining(","))
                : null);
        tjScenelib.setUpdateBy("admin");
        tjScenelib.setUpdateDatetime(LocalDateTime.now());
        if (tjScenelib.getImgPath() != null || tjScenelib.getSceneDesc() != null) {
            Integer secenceId = this.getById(tjScenelib.getId()).getSceneDetailId();
            TjFragmentedSceneDetail tjFragmentedSceneDetail = new TjFragmentedSceneDetail();
            tjFragmentedSceneDetail.setImgUrl(tjScenelib.getImgPath());
            tjFragmentedSceneDetail.setTestSceneDesc(tjScenelib.getSceneDesc());
            tjFragmentedSceneDetail.setId(secenceId);
            if (tjScenelib.getLabels() != null) {
                tjFragmentedSceneDetail.setLabel(tjScenelib.getLabels());
                tjFragmentedSceneDetail.setAllStageLabel(tjScenelib.getAllStageLabels());
            }
            tjFragmentedSceneDetailService.updateById(tjFragmentedSceneDetail);
        }

        return tjScenelibMapper.updateById(tjScenelib);
    }

    /**
     * 批量删除scenelib
     *
     * @param ids 需要删除的scenelibID
     * @return 结果
     */
    @Override
    public int deleteTjScenelibByIds(Long[] ids) {
        for (Long id : ids) {
            TjScenelib tjScenelib = this.getById(id);
            if (tjScenelib.getSceneStatus() != null && tjScenelib.getSceneStatus() == 1) {
                return 0;
            }
        }
        return tjScenelibMapper.deleteTjScenelibByIds(ids);
    }

    /**
     * 删除scenelib信息
     *
     * @param id scenelibID
     * @return 结果
     */
    @Override
    public int deleteTjScenelibById(Long id) {
        return tjScenelibMapper.deleteTjScenelibById(id);
    }

    @Override
    public List<ScenelibVo> selectScenelibVoList(ScenelibVo scenelibVo) {
        return tjScenelibMapper.selectScenelibVoList(scenelibVo);
    }

    @Autowired
    private TjCaseMapper caseMapper;

    @Override
    public boolean updateBatchandCase(List<TjScenelib> scenelibs) {
        //当置场景库场景状态为有效式，同步case表中的数据
        //有效时，判断case表中有没有数据，有的话状态变成有效，没有的话插入一条
        //无效时，状态置为其他
        //case配置表中数据同步
        scenelibs.forEach(item -> {   //没有数据先插入数据
            List<CaseDetailVo> caseVos = caseMapper.selectCasesByScean(item.getId());
            if (caseVos.isEmpty()) {
                TjScenelib scenelib = this.getById(item.getId());
                TjCaseDto tjCaseDto = new TjCaseDto();
                tjCaseDto.setRemark("1");
                tjCaseDto.setTreeId(Math.toIntExact(scenelib.getTreeId()));
                tjCaseDto.setSceneDetailId(scenelib.getSceneDetailId());
                tjCaseDto.setSceneLibId(item.getId());
                //PartConfigSelect partConfigSelect = new PartConfigSelect();
                //List<CasePartConfigVo> parts = new ArrayList<>();
                //CasePartConfigVo casePartConfigVo1 = new CasePartConfigVo();
                //casePartConfigVo1.setParticipantRole("av");
                //CasePartConfigVo casePartConfigVo2 = new CasePartConfigVo();
                //casePartConfigVo2.setParticipantRole("svTracking");
                //parts.add(casePartConfigVo2);
                //partConfigSelect.setParts(parts);
                //List<PartConfigSelect> partConfigSelects = new ArrayList<>();
                //partConfigSelects.add(partConfigSelect);
                tjCaseDto.setLabels(scenelib.getLabels());
                if (scenelib.getGeojsonPath() != null) {
                    tjCaseDto.setMapId(Integer.valueOf(scenelib.getGeojsonPath()));
                } else {
                    tjCaseDto.setMapId(21);
                }
                tjCaseDto.setEvoNum(scenelib.getEvoNum());
                WoPostion end = SceneLibMap.getEnd(scenelib.getXoscPath());
                String proj = projca;
                if (scenelib.getGeojsonPath().equals("10")) {
                    proj = projtj;
                }
                if (end != null) {
                    double x1 = Double.parseDouble(end.getXTarget().split(",")[0]);
                    double x2 = Double.parseDouble(end.getXTarget().split(",")[1]);
                    double y1 = Double.parseDouble(end.getYTarget().split(",")[0]);
                    double y2 = Double.parseDouble(end.getYTarget().split(",")[1]);
                    JSONObject end1 = toBuildOpenXUtil.retotrans(x1, y1, proj, 0D);
                    JSONObject end2 = toBuildOpenXUtil.retotrans(x2, y2, proj, 0D);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add(end1);
                    jsonArray.add(end2);
                    tjCaseDto.setTestTarget(jsonArray.toJSONString());
                }
                //tjCaseDto.setPartConfigSelects(partConfigSelects);
                try {
                    caseService.saveCase(tjCaseDto);
                    SceneLibMap.clear(scenelib.getXoscPath());
                } catch (BusinessException e) {
                    throw new RuntimeException(e);
                }

            }
            String status = "invalid";
            if (item.getSceneStatus() == 1) {  //有效
                status = "effective";
            }
            //根据场景id更新case的状态
            caseMapper.updateCaseStatusBySceanId(item.getId(), status);
        });
        return this.updateBatchById(scenelibs);

    }

    @Override
    public boolean updateBatch(List<TjScenelib> scenelibs) {
        return this.updateBatchById(scenelibs);
    }

    @Override
    public List<ScenelibVo> selectTjSceneDetailListAnd(List<Integer> labellist, Integer treeId) {
        return tjScenelibMapper.selectTjSceneDetailListAnd(labellist, treeId);
    }

    @Override
    public List<ScenelibVo> selectTjSceneDetailListOr(List<Integer> labellist, Integer treeId) {
        return tjScenelibMapper.selectTjSceneDetailListOr(labellist, treeId);
    }

    @Override
    public void takeOnsiteCase(Long id) {
        TjScenelib scenelib = tjScenelibMapper.selectTjScenelibById(id);
        String labels = scenelib.getLabels();
        StringBuilder labelshows = new StringBuilder();
        for (String str : labels.split(",")) {
            try {
                long intValue = Long.parseLong(str);
                String labelshow = sceneLabelMap.getSceneLabel(intValue);
                if (labelshow != null) {
                    if (labelshows.length() > 0) {
                        labelshows.append(",").append(labelshow);
                    } else {
                        labelshows.append(labelshow);
                    }
                }
            } catch (NumberFormatException e) {
                // 处理无效的整数字符串
            }
        }
        String testSence = labelshows.toString();
        TjCase tjCase = new TjCase();
        tjCase.setTreeId(-1);
        tjCase.setCaseNumber("justonsite");
        tjCase.setMapId(10);
        tjCase.setMapFile("onsite");
        tjCase.setTestScene(testSence);
        tjCase.setDetailInfo("{\"duration\":\"00:00\",\"participantTrajectories\":[{\"id\":\"1\",\"model\":1,\"name\":\"主车\",\"trajectory\":[{\"date\":\"2024-06-03 14:40:11.595\",\"frameId\":0,\"lane\":\"0\",\"latitude\":\"31.291504448207817\",\"longitude\":\"121.20197261213676\",\"pass\":true,\"position\":\"121.20197261213676,31.291504448207817\",\"reason\":\"已校验完成\",\"speed\":0.0,\"time\":\"0\",\"type\":\"start\"},{\"date\":\"2024-06-03 14:40:14.460\",\"frameId\":1,\"lane\":\"0\",\"latitude\":\"31.291601842814558\",\"longitude\":\"121.20210286785296\",\"pass\":true,\"position\":\"121.20210286785296,31.291601842814558\",\"reason\":\"已校验完成\",\"speed\":0.0,\"time\":\"3\",\"type\":\"pathway\"},{\"date\":\"2024-06-03 14:40:26.300\",\"frameId\":2,\"lane\":\"0\",\"latitude\":\"31.291922332731342\",\"longitude\":\"121.20250037236617\",\"pass\":true,\"position\":\"121.20250037236617,31.291922332731342\",\"reason\":\"已校验完成\",\"speed\":0.0,\"time\":\"11\",\"type\":\"end\"}],\"type\":\"main\"},{\"id\":\"2\",\"model\":1,\"name\":\"从车1\",\"trajectory\":[{\"date\":\"2024-06-03 14:40:11.595\",\"frameId\":0,\"lane\":\"0\",\"latitude\":\"31.291445435663146\",\"longitude\":\"121.20257336048302\",\"pass\":true,\"position\":\"121.20257336048302,31.291445435663146\",\"reason\":\"已校验完成\",\"speed\":0.0,\"time\":\"0\",\"type\":\"start\"},{\"date\":\"2024-06-03 14:40:14.399\",\"frameId\":1,\"lane\":\"0\",\"latitude\":\"31.29154235055443\",\"longitude\":\"121.20248072171934\",\"pass\":true,\"position\":\"121.20248072171934,31.29154235055443\",\"reason\":\"已校验完成\",\"speed\":0.0,\"time\":\"3\",\"type\":\"pathway\"},{\"date\":\"2024-06-03 14:40:25.954\",\"frameId\":2,\"lane\":\"0\",\"latitude\":\"31.29153707301341\",\"longitude\":\"121.20239145163798\",\"pass\":true,\"position\":\"121.20239145163798,31.29153707301341\",\"reason\":\"已校验完成\",\"speed\":0.0,\"time\":\"12\",\"type\":\"end\"}],\"type\":\"slave\"}]}");
        tjCase.setStatus("wait_test");
        tjCase.setRemark("onsite");
        tjCase.setCreatedBy("admin");
        tjCase.setCreatedDate(LocalDateTime.now());
        tjCase.setSceneDetailId(8);
        caseService.save(tjCase);
        TjCasePartConfig main = new TjCasePartConfig();
        main.setCaseId(tjCase.getId());
        main.setParticipantRole("av");
        main.setBusinessId("1");
        main.setBusinessType("main");
        main.setName("主车");
        main.setModel(1);
        TjCasePartConfig slave = new TjCasePartConfig();
        slave.setCaseId(tjCase.getId());
        slave.setParticipantRole("mvSimulation");
        slave.setBusinessId("2");
        slave.setBusinessType("slave");
        slave.setName("从车1");
        slave.setModel(1);
        casePartConfigService.save(main);
        casePartConfigService.save(slave);
        String onsitenum = buildOnsiteNumber();
        TjOnsiteCase tjOnisteCase = new TjOnsiteCase();
        tjOnisteCase.setName("onsite实测测试场景");
        tjOnisteCase.setXodrfile(scenelib.getXodrPath());
        tjOnisteCase.setOnsiteNumber(onsitenum);
        tjOnisteCase.setCaseId(tjCase.getId());
        tjOnisteCase.setScenelibId(scenelib.getId());
        tjOnisteCase.setSceneLabel(testSence);
        tjOnisteCase.setStatus(0);
        tjOnisteCase.setCreatedBy("admin");
        tjOnisteCase.setCreatedDate(LocalDateTime.now());
        tjOnsiteCaseService.save(tjOnisteCase);
        tjOnsiteCaseService.uploadToOnsite(tjOnisteCase, scenelib.getXodrPath(), scenelib.getXoscPath());
    }

    @Override
    public List<WoPostion> playback(Integer id, String participantId, int action) throws BusinessException, IOException {
        List<WoPostion> mainPostion = null;
        TjScenelib scenelib = this.getById(id);
        String key = Constants.ChannelBuilder.buildScenePreviewChannel(SecurityUtils.getUsername(), id);
        switch (action) {
            case Constants.PlaybackAction.START:
                if (StringUtils.isEmpty(scenelib.getXoscPath())) {
                    throw new BusinessException("OpenSCENARIO文件不存在");
                }
                List<Tjshape> routeList = analyzeOpenX.analyze(scenelib.getXoscPath());

                if (CollectionUtils.isEmpty(routeList)) {
                    throw new BusinessException("轨迹文件读取异常");
                }
                mainPostion = analyzeOpenX.parseCommentsFromXML(scenelib.getXoscPath());
                for (WoPostion woPostion : mainPostion) {
                    if (woPostion.getX() != null) {
                        woPostion.setId("start");
                    } else {
                        woPostion.setId("end");
                    }
                }
                PlaybackSchedule.startSendingOnsiteData(key, routeList);
                break;
            case Constants.PlaybackAction.SUSPEND:
                PlaybackSchedule.suspendOniste(key);
                break;
            case Constants.PlaybackAction.CONTINUE:
                PlaybackSchedule.goOnOnsite(key);
                break;
            case Constants.PlaybackAction.STOP:
                PlaybackSchedule.stopSendingDataOnsite(key);
                break;
            default:
                break;
        }
        return mainPostion;
    }

    private synchronized String buildOnsiteNumber() {
        return StringUtils.format(Constants.ContentTemplate.ONSITE_NUMBER_TEMPLATE, DateUtils.getNowDayString(),
                CounterUtil.getNextNumber(Constants.ContentTemplate.ONSITE_NUMBER_TEMPLATE));
    }
}
