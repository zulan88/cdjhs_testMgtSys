package net.wanji.business.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.Tjshape;
import net.wanji.business.domain.vo.FragmentedScenesDetailVo;
import net.wanji.business.domain.vo.ScenelibVo;
import net.wanji.business.entity.TjCase;
import net.wanji.business.entity.TjCasePartConfig;
import net.wanji.business.entity.TjFragmentedSceneDetail;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjFragmentedSceneDetailMapper;
import net.wanji.business.schedule.PlaybackSchedule;
import net.wanji.business.schedule.SceneLabelMap;
import net.wanji.business.service.TjCasePartConfigService;
import net.wanji.business.service.TjCaseService;
import net.wanji.business.util.AnalyzeOpenX;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.utils.CounterUtil;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.onsite.entity.TjOnsiteCase;
import net.wanji.onsite.service.TjOnsiteCaseService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.wanji.business.mapper.TjScenelibMapper;
import net.wanji.business.entity.TjScenelib;
import net.wanji.business.service.ITjScenelibService;

/**
 * scenelibService业务层处理
 * 
 * @author wanji
 * @date 2023-10-31
 */
@Service
public class TjScenelibServiceImpl extends ServiceImpl<TjScenelibMapper,TjScenelib> implements ITjScenelibService
{
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


    /**
     * 查询scenelib
     * 
     * @param id scenelibID
     * @return scenelib
     */
    @Override
    public TjScenelib selectTjScenelibById(Long id)
    {
        return tjScenelibMapper.selectTjScenelibById(id);
    }

    /**
     * 查询scenelib列表
     * 
     * @param tjScenelib scenelib
     * @return scenelib
     */
    @Override
    public List<TjScenelib> selectTjScenelibList(TjScenelib tjScenelib)
    {
        return tjScenelibMapper.selectTjScenelibList(tjScenelib);
    }

    /**
     * 新增scenelib
     * 
     * @param tjScenelib scenelib
     * @return 结果
     */
    @Override
    public int insertTjScenelib(TjScenelib tjScenelib)
    {
        List<String> labellist = new ArrayList<>();
        if(tjScenelib.getLabels().split(",").length>0) {
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
    public boolean insertTjScenelibBatch(List<TjScenelib> tjScenelibs) {
        for (TjScenelib tjScenelib:tjScenelibs){
            List<String> labellist = new ArrayList<>();
            if(tjScenelib.getLabels().split(",").length>0) {
                for (String id : tjScenelib.getLabels().split(",")) {
                    labellist.addAll(sceneDetailMapper.getalllabel(id));
                }
            }
            tjScenelib.setAllStageLabels(CollectionUtils.isNotEmpty(labellist)
                    ? labellist.stream().distinct().collect(Collectors.joining(","))
                    : null);
            tjScenelib.setCreateBy("admin");
            tjScenelib.setCreateDatetime(LocalDateTime.now());
            tjScenelib.setNumber(StringUtils.format(Constants.ContentTemplate.SCENE_NUMBER_TEMPLATE, DateUtils.getNowDayString(),
                    CounterUtil.getRandomChar()));
        }
        return this.saveBatch(tjScenelibs);
    }

    /**
     * 修改scenelib
     * 
     * @param tjScenelib scenelib
     * @return 结果
     */
    @Override
    public int updateTjScenelib(TjScenelib tjScenelib)
    {
        List<String> labellist = new ArrayList<>();
        if(tjScenelib.getLabels()!=null&&tjScenelib.getLabels().split(",").length>0) {
            for (String id : tjScenelib.getLabels().split(",")) {
                labellist.addAll(sceneDetailMapper.getalllabel(id));
            }
        }
        tjScenelib.setAllStageLabels(CollectionUtils.isNotEmpty(labellist)
                ? labellist.stream().distinct().collect(Collectors.joining(","))
                : null);
        tjScenelib.setUpdateBy("admin");
        tjScenelib.setUpdateDatetime(LocalDateTime.now());
        return tjScenelibMapper.updateTjScenelib(tjScenelib);
    }

    /**
     * 批量删除scenelib
     * 
     * @param ids 需要删除的scenelibID
     * @return 结果
     */
    @Override
    public int deleteTjScenelibByIds(Long[] ids)
    {
        return tjScenelibMapper.deleteTjScenelibByIds(ids);
    }

    /**
     * 删除scenelib信息
     * 
     * @param id scenelibID
     * @return 结果
     */
    @Override
    public int deleteTjScenelibById(Long id)
    {
        return tjScenelibMapper.deleteTjScenelibById(id);
    }

    @Override
    public List<ScenelibVo> selectScenelibVoList(ScenelibVo scenelibVo) {
        return tjScenelibMapper.selectScenelibVoList(scenelibVo);
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
                if(labelshow!=null) {
                    if(labelshows.length()>0) {
                        labelshows.append(",").append(labelshow);
                    }else {
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
        tjOnsiteCaseService.uploadToOnsite(tjOnisteCase,scenelib.getXodrPath(),scenelib.getXoscPath());
    }

    @Override
    public void playback(Integer id, String participantId, int action) throws BusinessException, IOException {
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
    }

    private synchronized String buildOnsiteNumber() {
        return StringUtils.format(Constants.ContentTemplate.ONSITE_NUMBER_TEMPLATE, DateUtils.getNowDayString(),
                CounterUtil.getNextNumber(Constants.ContentTemplate.ONSITE_NUMBER_TEMPLATE));
    }
}