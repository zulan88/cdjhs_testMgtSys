package net.wanji.web.controller.business;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import net.wanji.business.domain.Label;
import net.wanji.business.domain.vo.FragmentedScenesDetailVo;
import net.wanji.business.domain.vo.SceneDetailVo;
import net.wanji.business.domain.vo.TreeVo;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.schedule.SceneLabelMap;
import net.wanji.business.service.ILabelsService;
import net.wanji.business.service.TjFragmentedSceneDetailService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Api(tags = "场景创建-标签管理")
@RestController
@RequestMapping("/labels")
public class LabelsController extends BaseController {

    @Autowired
    private ILabelsService labelsService;

    @Autowired
    private TjFragmentedSceneDetailService tjFragmentedSceneDetailService;

    @Autowired
    private SceneLabelMap sceneLabelMap;

    @ApiOperationSort(1)
    @ApiOperation(value = "1.列表页查询（输出为树形结构）")
    @GetMapping("/list")
    public AjaxResult list(Integer id) throws BusinessException {
        List<Label> labelList = labelsService.selectLabelsList(new Label());
        Map<Long, Label> labelToNodeMap = new HashMap<>();
        TreeVo treeVo = new TreeVo();
        treeVo.setTotal(labelList.size());
        List<Label> roots = new ArrayList<>();
        Set<Long> set = new HashSet<>();
        if (id != null) {
            FragmentedScenesDetailVo detailVo = tjFragmentedSceneDetailService.getDetailVo(id,null);
            List<String> labels = detailVo.getLabelList();
            for (String str : labels) {
                try {
                    long intValue = Long.parseLong(str);
                    set.add(intValue);
                } catch (NumberFormatException e) {
                    // 处理无效的整数字符串
                }
            }
        }
        for (Label label : labelList) {
            if (set.contains(label.getId())) {
                label.setStatus(true);
            }
            labelToNodeMap.put(label.getId(), label);

            if (label.getParentId() == null) {
                roots.add(label);
            }
        }
        for (Label label : labelList) {
            Label currentNode = labelToNodeMap.get(label.getId());
            Label parentNode = labelToNodeMap.get(label.getParentId());

            if (parentNode != null) {
                parentNode.getChildren().add(currentNode);
            }
        }
        treeVo.setTrees(roots);
        return AjaxResult.success(treeVo);
    }

    @ApiOperationSort(3)
    @ApiOperation(value = "3.获取标签树")
    @PostMapping("/tree")
    public AjaxResult getlabeltree(@RequestBody List<String> labels) throws BusinessException {
        List<Label> labelList = labelsService.selectLabelsList(new Label());
        Map<Long, Label> labelToNodeMap = new HashMap<>();
        TreeVo treeVo = new TreeVo();
        treeVo.setTotal(labelList.size());
        List<Label> roots = new ArrayList<>();
        Set<Long> set = new HashSet<>();

        for (String str : labels) {
            try {
                long intValue = Long.parseLong(str);
                set.add(intValue);
            } catch (NumberFormatException e) {
                // 处理无效的整数字符串
            }
        }

        for (Label label : labelList) {
            if (set.contains(label.getId())) {
                label.setStatus(true);
            }
            labelToNodeMap.put(label.getId(), label);

            if (label.getParentId() == null) {
                roots.add(label);
            }
        }
        for (Label label : labelList) {
            Label currentNode = labelToNodeMap.get(label.getId());
            Label parentNode = labelToNodeMap.get(label.getParentId());

            if (parentNode != null) {
                parentNode.getChildren().add(currentNode);
            }
        }
        treeVo.setTrees(roots);
        return AjaxResult.success(treeVo);
    }

    @ApiOperationSort(2)
    @ApiOperation(value = "2.列表页查询（新）")
    @GetMapping("/list2")
    public AjaxResult list2(Integer id) throws BusinessException {
        List<Label> labelList = labelsService.selectLabelsList(new Label());
        Map<Long, Label> labelToNodeMap = new HashMap<>();
        TreeVo treeVo = new TreeVo();
        treeVo.setTotal(labelList.size());
        List<Label> roots = new ArrayList<>();
        Set<Long> set = new HashSet<>();

        if (id != null) {
            FragmentedScenesDetailVo detailVo = tjFragmentedSceneDetailService.getDetailVo(id,null);
            List<String> labels = detailVo.getLabelList();
            for (String str : labels) {
                try {
                    long intValue = Long.parseLong(str);
                    set.add(intValue);
                } catch (NumberFormatException e) {
                    // 处理无效的整数字符串
                }
            }
        }

        for (Label label : labelList) {
            if (set.contains(label.getId())) {
                label.setStatus(true);
            }
            labelToNodeMap.put(label.getId(), label);

            if (label.getParentId() == null) {
                roots.add(label);
            }
        }

        for (Label label : labelList) {
            Label currentNode = labelToNodeMap.get(label.getId());
            Label parentNode = labelToNodeMap.get(label.getParentId());

            if (parentNode != null) {
                parentNode.getChildren().add(currentNode);
            }
        }

        for (Label parentNode : labelToNodeMap.values()) {
            List<Label> leafNodes = new ArrayList<>();
            for (Label child : parentNode.getChildren()) {
                if (child.getChildren().isEmpty()) {
                    leafNodes.add(child);
                }
            }
            parentNode.getChildren().removeIf(obj -> obj.getChildren().isEmpty());

            if (leafNodes.size() > 1) {
                StringBuilder sb = new StringBuilder();
                StringBuilder ids = new StringBuilder();
                for (Label leafNode : leafNodes) {
                    sb.append(leafNode.getName()).append(",");
                    ids.append(leafNode.getId()).append(",");
                }
                Label label = new Label();
                label.setName(sb.substring(0, sb.length() - 1));
                label.setDirection(ids.substring(0, ids.length() - 1));
                parentNode.getChildren().add(label);
            }
        }

        treeVo.setTrees(roots);
        return AjaxResult.success(treeVo);
    }

    @ApiOperationSort(4)
    @ApiOperation(value = "4.新增标签")
    @PostMapping
    public AjaxResult add(@RequestBody Label label) {
        labelsService.insertLabels(label);
        sceneLabelMap.reset(2l);
        return AjaxResult.success(label.getId());
    }

    @ApiOperationSort(5)
    @ApiOperation(value = "5.修改标签")
    @PutMapping
    public AjaxResult edit(@RequestBody Label label) {
        labelsService.updateLabels(label);
        sceneLabelMap.reset(2l);
        return AjaxResult.success();
    }

    @ApiOperationSort(6)
    @ApiOperation(value = "6.批量删除标签")
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) throws BusinessException {
        for (Long id : ids) {
            SceneDetailVo sceneDetailVo = new SceneDetailVo();
            sceneDetailVo.setLabel(String.valueOf(id));
            List<SceneDetailVo> list = tjFragmentedSceneDetailService.selectTjFragmentedSceneDetailList(sceneDetailVo);
            if (list.size() > 0) {
                AjaxResult.error("该标签被场景或用例选中，请删除场景和用例后再试");
            }
        }
        return toAjax(labelsService.deleteLabelsByIds(ids));
    }

    @ApiOperationSort(7)
    @ApiOperation(value = "7.获取标签简述列表")
    @GetMapping("/getlabel")
    public AjaxResult getlabel(Integer id) throws BusinessException {
        List<Label> labelList = labelsService.selectLabelsList(new Label());
        Map<Long, String> sceneMap = new HashMap<>();
        for (Label tlabel : labelList) {
            Long parentId = tlabel.getParentId();
            String prelabel = null;
            if (parentId != null) {
                prelabel = sceneMap.getOrDefault(parentId, null);
            } else {
                continue;
            }
            if (tlabel.getId().equals(2L)) {
                continue;
            }
            if (prelabel == null) {
                sceneMap.put(tlabel.getId(), tlabel.getName());
            } else {
                sceneMap.put(tlabel.getId(), prelabel + "-" + tlabel.getName());
            }
        }
        List<String> data = new ArrayList<>();
        if (id != null) {
            FragmentedScenesDetailVo detailVo = tjFragmentedSceneDetailService.getDetailVo(id,null);
            List<String> labels = detailVo.getLabelList();
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

    @ApiOperationSort(8)
    @ApiOperation(value = "8.场景类型标签树")
    @GetMapping("/singlelist")
    public AjaxResult singlelist() {
        List<Label> labelList = labelsService.selectLabelsList(new Label());
        Map<Long, Label> labelToNodeMap = new HashMap<>();
        TreeVo treeVo = new TreeVo();
        treeVo.setTotal(labelList.size());
        List<Label> roots = new ArrayList<>();
        for (Label label : labelList) {

            labelToNodeMap.put(label.getId(), label);

            if (label.getParentId() != null && label.getParentId().equals(2l)) {
                roots.add(label);
            }
        }
        for (Label label : labelList) {
            Label currentNode = labelToNodeMap.get(label.getId());
            Label parentNode = labelToNodeMap.get(label.getParentId());

            if (parentNode != null) {
                parentNode.getChildren().add(currentNode);
            }
        }
        treeVo.setTrees(roots);
        return AjaxResult.success(treeVo);
    }

}
