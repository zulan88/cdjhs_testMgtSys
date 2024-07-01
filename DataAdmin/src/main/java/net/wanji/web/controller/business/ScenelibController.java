package net.wanji.web.controller.business;

import com.alibaba.fastjson2.JSON;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import io.swagger.models.auth.In;
import net.wanji.business.domain.BusinessTreeSelect;
import net.wanji.business.domain.Label;
import net.wanji.business.domain.dto.TjFragmentedSceneDetailDto;
import net.wanji.business.domain.dto.TjFragmentedScenesDto;
import net.wanji.business.domain.dto.TreeTypeDto;
import net.wanji.business.domain.vo.FragmentedScenesDetailVo;
import net.wanji.business.domain.vo.ScenelibVo;
import net.wanji.business.domain.vo.TagtoSceneVo;
import net.wanji.business.entity.TjFragmentedSceneDetail;
import net.wanji.business.entity.TjGeneralizeScene;
import net.wanji.business.entity.TjScenelib;
import net.wanji.business.entity.TjScenelibTree;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.schedule.SceneLabelMap;
import net.wanji.business.service.*;
import net.wanji.business.util.AnalyzeOpenX;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import net.wanji.business.util.ToBuildOpenX;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scenelib")
public class ScenelibController extends BaseController {

    @Autowired
    private SceneLabelMap sceneLabelMap;

    @Autowired
    ITjScenelibService scenelibService;

    @Autowired
    TjScenelibTreeService scenelibTreeService;

    @Autowired
    ToBuildOpenX toBuildOpenX;

    @Autowired
    private TjFragmentedSceneDetailService tjFragmentedSceneDetailService;

    @Autowired
    private ILabelsService labelsService;

    @Autowired
    private TjGeneralizeSceneService generalizeSceneService;

    @PostMapping("/list")
    public TableDataInfo scenelist(@RequestBody ScenelibVo scenelibVo) throws BusinessException {
        startPage();
        List<ScenelibVo> list = scenelibService.selectScenelibVoList(scenelibVo);
        for(ScenelibVo scenelibVo1 : list){
            String labels = scenelibVo1.getLabels();
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
            scenelibVo1.setSceneSort(labelshows.toString());
        }
        return getDataTable(list);
    }

    @PostMapping("/libadd")
    public AjaxResult add(@RequestBody TjScenelib tjScenelib) throws BusinessException{
        int res = scenelibService.insertTjScenelib(tjScenelib);
        if(tjScenelib.getIsGen()==null || tjScenelib.getIsGen().equals(0)) {
            FragmentedScenesDetailVo detailVo = tjFragmentedSceneDetailService.getDetailVo(tjScenelib.getSceneDetailId(), null);
            if (StringUtils.isEmpty(detailVo.getRouteFile())) {
                throw new BusinessException("创建失败：场景未进行仿真验证");
            }
            toBuildOpenX.scenetoOpenX(detailVo, tjScenelib.getId(), tjScenelib.getIsGen());
            TjFragmentedSceneDetail fragmentedSceneDetail = new FragmentedScenesDetailVo();
            fragmentedSceneDetail.setId(tjScenelib.getSceneDetailId());
            fragmentedSceneDetail.setSceneStatus(1);
            tjFragmentedSceneDetailService.updateOne(fragmentedSceneDetail);
        }else if(tjScenelib.getIsGen().equals(1)) {
            TjGeneralizeScene generalizeScene = generalizeSceneService.getById(tjScenelib.getSceneDetailId());
            if (StringUtils.isEmpty(generalizeScene.getRouteFile())) {
                throw new BusinessException("创建失败：场景未进行仿真验证");
            }
            FragmentedScenesDetailVo detailVo = tjFragmentedSceneDetailService.getDetailVo(generalizeScene.getSceneId(), null);
            detailVo.setId(generalizeScene.getId());
            toBuildOpenX.scenetoOpenX(detailVo, tjScenelib.getId(), tjScenelib.getIsGen());
        }
        return toAjax(res);
    }


    @PostMapping("/libaddBatch")
    public AjaxResult addBatch(@RequestBody List<TjScenelib> tjScenelibs) throws BusinessException{

        return toAjax(scenelibService.insertTjScenelibBatch(tjScenelibs));
    }

    @PutMapping("/libedit")
    public AjaxResult edit(@RequestBody TjScenelib tjScenelib) throws BusinessException{
        return toAjax(scenelibService.updateTjScenelib(tjScenelib));
    }


    @PutMapping("/libstatus")
    public AjaxResult updatestatus(@RequestBody List<TjScenelib> scenelibs){
        return toAjax(scenelibService.updateBatchandCase(scenelibs));
    }

    @DeleteMapping("/lib/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) throws BusinessException {
        return toAjax(scenelibService.deleteTjScenelibByIds(ids));
    }

    @GetMapping("/init")
    public AjaxResult init() {
        return AjaxResult.success(JSON.toJSON(scenelibTreeService.init()));
    }

    @GetMapping("/initEditPage")
    public AjaxResult initEditPage() {
        return AjaxResult.success(scenelibTreeService.initEditPage());
    }

    //@PreAuthorize("@ss.hasPermi('sceneBase:saveTreeType')")
    @PostMapping("/saveTreeType")
    public AjaxResult saveTreeType(@Validated @RequestBody TreeTypeDto treeTypeDto) throws BusinessException {
        return scenelibTreeService.saveSceneTreeType(treeTypeDto)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

    //@PreAuthorize("@ss.hasPermi('sceneBase:deleteTreeType')")
    @GetMapping("/deleteTreeType/{dictCode}")
    public AjaxResult deleteTreeType(@PathVariable("dictCode") Long dictCode) throws BusinessException {
        return scenelibTreeService.deleteTreeType(dictCode)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

    //@PreAuthorize("@ss.hasPermi('sceneBase:selectTree')")
    @GetMapping("/selectTree")
    public AjaxResult selectTree(@RequestParam(value = "type") String type,
                                 @RequestParam(value = "name", required = false) String name) {
        List<TjScenelibTree> usingScenes = scenelibTreeService.selectUsingScenes(type);
        List<BusinessTreeSelect> tree = scenelibTreeService.buildSceneTreeSelect(usingScenes, name);
        return AjaxResult.success(tree);
    }

    //@PreAuthorize("@ss.hasPermi('sceneBase:saveSceneTree')")
    @PostMapping("/saveSceneTree")
    public AjaxResult saveSceneTree(@Validated @RequestBody TjFragmentedScenesDto fragmentedScenesDto) {
        return scenelibTreeService.saveSceneTree(fragmentedScenesDto)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

    //@PreAuthorize("@ss.hasPermi('sceneBase:deleteScene')")
    @GetMapping("/deleteScene/{sceneId}")
    public AjaxResult deleteScene(@PathVariable("sceneId") Integer sceneId) throws BusinessException {
        return scenelibTreeService.deleteSceneById(sceneId)
                ? AjaxResult.success("删除成功")
                : AjaxResult.error("删除失败");
    }

    @GetMapping("/getlabel")
    public AjaxResult getlabel(Long id) throws BusinessException {
        List<Label> labelList = labelsService.selectLabelsList(new Label());
        Map<Long,String> sceneMap = new HashMap<>();
        for(Label tlabel : labelList){
            Long parentId = tlabel.getParentId();
            String prelabel = null;
            if(parentId!=null) {
                prelabel = sceneMap.getOrDefault(parentId, null);
            }else {
                continue;
            }
            if(tlabel.getId().equals(2L)){
                continue;
            }
            if(prelabel==null){
                sceneMap.put(tlabel.getId(),tlabel.getName());
            }else {
                sceneMap.put(tlabel.getId(),prelabel+"-"+tlabel.getName());
            }
        }
        List<String> data = new ArrayList<>();
        if(id!=null) {
            TjScenelib tjScenelib = scenelibService.selectTjScenelibById(id);
            String[] labels = tjScenelib.getLabels().split(",");
            for (String str : labels) {
                try {
                    long intValue = Long.parseLong(str);
                    data.add(sceneMap.get(intValue));
                } catch (NumberFormatException e) {
                    // 处理无效的整数字符串
                }
            }
        }
        return AjaxResult.success(data);
    }

    @PostMapping("/tagtoscene")
    public TableDataInfo test(@RequestBody TagtoSceneVo tagtoSceneVo){
        startPage();
        if(tagtoSceneVo.getChoice().equals(0)) {
            List<ScenelibVo> res = scenelibService.selectTjSceneDetailListOr(tagtoSceneVo.getLabellist(), tagtoSceneVo.getTreeId());
            for(ScenelibVo scenelibVo1 : res){
                String labels = scenelibVo1.getLabels();
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
                scenelibVo1.setSceneSort(labelshows.toString());
            }
            return getDataTable(res);
        }else {
            List<ScenelibVo> res = scenelibService.selectTjSceneDetailListAnd(tagtoSceneVo.getLabellist(), tagtoSceneVo.getTreeId());
            for(ScenelibVo scenelibVo1 : res){
                String labels = scenelibVo1.getLabels();
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
                scenelibVo1.setSceneSort(labelshows.toString());
            }
            return getDataTable(res);
        }
    }



    @GetMapping("/preview")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用例ID", required = true, dataType = "Integer", paramType = "query", example = "280"),
            @ApiImplicitParam(name = "action", value = "动作（1:开始; 2:暂停; 3:继续; 4:结束）", required = true, dataType = "Integer", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "vehicleId", value = "参与者ID", required = false, dataType = "Integer", paramType = "query", example = "1")
    })
    public AjaxResult preview(@RequestParam(value = "id") Integer id,
                              @RequestParam(value = "action") int action,
                              @RequestParam(value = "vehicleId", required = false) String vehicleId)
            throws BusinessException, IOException {
        scenelibService.playback(id, vehicleId, action);
        return AjaxResult.success();
    }

    @GetMapping("/test")
    public AjaxResult test() throws BusinessException {
        List<Integer> mid = new ArrayList<>();
        mid.add(25);
        mid.add(26);
        mid.add(1);
        List<TjFragmentedSceneDetail> sceneDetails = tjFragmentedSceneDetailService.listByIds(mid);
        return AjaxResult.success(sceneDetails);
    }

}
