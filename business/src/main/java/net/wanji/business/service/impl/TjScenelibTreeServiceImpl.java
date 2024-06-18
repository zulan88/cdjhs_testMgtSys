package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.common.Constants.*;
import net.wanji.business.domain.BusinessTreeSelect;
import net.wanji.business.domain.dto.TjFragmentedSceneDetailDto;
import net.wanji.business.domain.dto.TjFragmentedScenesDto;
import net.wanji.business.domain.dto.TreeTypeDto;
import net.wanji.business.entity.*;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.*;
import net.wanji.business.service.TjFragmentedScenesService;
import net.wanji.business.service.TjResourcesDetailService;
import net.wanji.business.service.TjScenelibTreeService;
import net.wanji.business.util.BusinessTreeUtils;
import net.wanji.common.core.domain.SimpleSelect;
import net.wanji.common.core.domain.entity.SysDictData;
import net.wanji.common.utils.CounterUtil;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.bean.BeanUtils;
import net.wanji.system.service.ISysDictDataService;
import net.wanji.system.service.ISysDictTypeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 片段式场景表 服务实现类
 * </p>
 *
 * @author wj
 * @since 2023-06-27
 */
@Service
public class TjScenelibTreeServiceImpl extends ServiceImpl<TjScenelibTreeMapper, TjScenelibTree>
        implements TjScenelibTreeService {

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private TjResourcesDetailService resourcesDetailService;

    @Autowired
    private TjScenelibMapper sceneDetailMapper;

    @Autowired
    private TjCaseMapper caseMapper;

    @Autowired
    private TjScenelibTreeMapper scenelibTreeMapper;

    @Override
    public Map<String, Object> init() {
        List<SysDictData> sceneTreeType = dictTypeService.selectDictDataByType(SysType.SCENE_LIB_TREE);
        Map<String, Object> result = new HashMap<>(8);
        result.put(SysType.SCENE_LIB_TREE, CollectionUtils.emptyIfNull(sceneTreeType).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        result.put("number", buildSceneNumber());
        return result;
    }

    @Override
    public Map<String, Object> initEditPage() {
        Map<String, Object> result = new HashMap<>(2);
        result.put("lanePoint", LanePointEnum.buildSelect());
        result.put("label", dictTypeService.selectDictDataByType(SysType.LABEL_TYPE));
        return result;
    }

    @Override
    public boolean saveSceneTreeType(TreeTypeDto treeTypeDto) throws BusinessException {
        List<SysDictData> sysDictData = dictTypeService.selectDictDataByType(SysType.SCENE_LIB_TREE);
        SysDictData dictData = null;
        if (ObjectUtils.isEmpty(treeTypeDto.getDictCode())) {
            // 添加
            List<String> labelList = CollectionUtils.emptyIfNull(sysDictData).stream().map(SysDictData::getDictLabel)
                    .collect(Collectors.toList());
            if (labelList.contains(treeTypeDto.getDictLabel())) {
                throw new BusinessException("名称重复");
            }
            dictData = new SysDictData();
            BeanUtils.copyBeanProp(dictData, treeTypeDto);
            long value = CollectionUtils.emptyIfNull(sysDictData).stream().mapToLong(SysDictData::getDictSort).max()
                    .orElse(0) + 1;
            dictData.setDictSort(value);
            dictData.setDictValue(String.valueOf(value));
            dictData.setDictType(SysType.SCENE_LIB_TREE);
            dictData.setIsDefault(YN.N);
            dictData.setStatus(String.valueOf(UsingStatus.ENABLE));
            dictData.setCreateBy(SecurityUtils.getUsername());
            dictData.setCreateTime(new Date());
            if (dictDataService.insertDictData(dictData) < 0) {
                throw new BusinessException("添加失败");
            }
        } else {
            SysDictData originData = dictDataService.selectDictDataById(treeTypeDto.getDictCode());
            if (ObjectUtils.isEmpty(originData)) {
                throw new BusinessException("未查询到对应类型");
            }
            // 修改
            List<String> otherNames = CollectionUtils.emptyIfNull(sysDictData).stream().filter(item ->
                            !Objects.equals(item.getDictCode(), treeTypeDto.getDictCode())).map(SysDictData::getDictLabel)
                    .collect(Collectors.toList());
            if (otherNames.contains(treeTypeDto.getDictLabel())) {
                throw new BusinessException("名称重复");
            }
            originData.setCssClass(treeTypeDto.getCssClass());
            originData.setDictLabel(treeTypeDto.getDictLabel());
            originData.setRemark(treeTypeDto.getRemark());
            originData.setUpdateBy(SecurityUtils.getUsername());
            originData.setUpdateTime(new Date());
            if (dictDataService.updateDictData(originData) < 0) {
                throw new BusinessException("修改失败");
            }
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteTreeType(Long dictCode) throws BusinessException {
        SysDictData dictData = dictDataService.selectDictDataById(dictCode);
        if (ObjectUtils.isEmpty(dictData)) {
            return false;
        }
        // 1.检查该类型下的所有场景是否存在测试用例
        QueryWrapper<TjScenelibTree> scenesQueryWrapper = new QueryWrapper<>();
        scenesQueryWrapper.eq(ColumnName.TYPE_COLUMN, dictData.getDictValue()).eq(ColumnName.IS_FOLDER_COLUMN, YN.N_INT);
        List<TjScenelibTree> scenesList = this.list(scenesQueryWrapper);
        List<Integer> sceneIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(scenesList)) {
            sceneIds = scenesList.stream().map(TjScenelibTree::getId).collect(Collectors.toList());
        }
        // 2.删除场景详情
        if (CollectionUtils.isNotEmpty(sceneIds)) {
            QueryWrapper<TjScenelib> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.in("tree_id", sceneIds);
            int removeDetail = sceneDetailMapper.delete(detailQueryWrapper);
            if (removeDetail != sceneIds.size()) {
                throw new BusinessException("删除详情失败");
            }
        }
        // 3.删除场景
        QueryWrapper<TjScenelibTree> scenesDeleteWrapper = new QueryWrapper<>();
        scenesDeleteWrapper.eq(ColumnName.TYPE_COLUMN, dictData.getDictValue());
        this.remove(scenesDeleteWrapper);
        // 4.删除场景树类型
        dictDataService.deleteDictDataByIds(new Long[]{dictCode});
        return true;
    }

    @Override
    public List<TjScenelibTree> selectUsingScenes(String type) {
        List<TjScenelibTree> result = scenelibTreeMapper.selectByCondition(type);
        return CollectionUtils.emptyIfNull(result).stream().sorted(Comparator.comparing(TjScenelibTree::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<TjScenelibTree> buildSceneTree(List<TjScenelibTree> scenes) {
        List<TjScenelibTree> returnList = new ArrayList<>();
        if (CollectionUtils.isEmpty(scenes)) {
            return returnList;
        }
        List<Integer> tempList = new ArrayList<>();
        for (TjScenelibTree item : scenes) {
            tempList.add(item.getId());
        }
        for (Iterator<TjScenelibTree> iterator = scenes.iterator(); iterator.hasNext(); ) {
            TjScenelibTree scene = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(scene.getParentId())) {
                recursionFn(scenes, scene);
                returnList.add(scene);
            }
        }
        if (returnList.isEmpty()) {
            returnList = scenes;
        }
        return returnList;
    }

    @Override
    public List<BusinessTreeSelect> buildSceneTreeSelect(List<TjScenelibTree> scenes, String name) {
        List<TjScenelibTree> sceneTrees = buildSceneTree(scenes);
        return sceneTrees.stream().map(BusinessTreeSelect::new).map(tree -> BusinessTreeUtils.fuzzySearch(tree, name))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSceneById(Integer id) throws BusinessException {
        TjScenelibTree scenes = this.getById(id);
        if (ObjectUtils.isEmpty(scenes)) {
            throw new BusinessException("节点不存在");
        }
        // 1.删除场景
        if (YN.N_INT == scenes.getIsFolder()) {
            sceneDetailMapper.deleteTjScenelibByTreeId(Collections.singletonList(scenes.getId()));
            this.removeById(id);
            return true;
        }
        // 2.删除文件夹
        // 2.1.查询文件夹下所有场景和文件夹
        List<TjScenelibTree> deleteCollector = new ArrayList<>();
        this.selectChildrenFromFolder(scenes.getId(), deleteCollector);
        // 2.2.筛选掉文件夹，校验所有场景节点是否存在测试用例，没有测试用例则删除所有场景详情
        List<Integer> sceneIds = deleteCollector.stream().filter(item -> YN.N_INT == item.getIsFolder())
                .map(TjScenelibTree::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(sceneIds)) {
            sceneDetailMapper.deleteTjScenelibByTreeId(Collections.singletonList(scenes.getId()));
        }
        // 2.3.删掉文件夹及其所有子节点
        deleteCollector.add(scenes);
        QueryWrapper<TjScenelibTree> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.in(ColumnName.ID_COLUMN,
                deleteCollector.stream().map(TjScenelibTree::getId).collect(Collectors.toList()));
        return this.remove(deleteWrapper);
    }


    @Override
    public void selectChildrenFromFolder(Integer id, List<TjScenelibTree> collector) {
        QueryWrapper<TjScenelibTree> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ColumnName.PARENT_ID_COLUMN, id);
        List<TjScenelibTree> childrenList = this.list(queryWrapper);

        if (CollectionUtils.isNotEmpty(childrenList)) {
            collector.addAll(childrenList);
            for (TjScenelibTree scenes : childrenList) {
                selectChildrenFromFolder(scenes.getId(), collector);
            }
        }
    }

    @Override
    public boolean saveSceneTree(TjFragmentedScenesDto fragmentedScenesDto) {
        TjScenelibTree tjFragmentedScenes;
        boolean flag = false;
        if (ObjectUtils.isEmpty(fragmentedScenesDto.getId())) {
            tjFragmentedScenes = new TjScenelibTree();
            BeanUtils.copyBeanProp(tjFragmentedScenes, fragmentedScenesDto);
            tjFragmentedScenes.setStatus(UsingStatus.ENABLE);
            tjFragmentedScenes.setCreatedBy(SecurityUtils.getUsername());
            tjFragmentedScenes.setCreatedDate(LocalDateTime.now());
        } else {
            tjFragmentedScenes = this.getById(fragmentedScenesDto.getId());
            tjFragmentedScenes.setName(fragmentedScenesDto.getName());
            tjFragmentedScenes.setUpdatedBy(SecurityUtils.getUsername());
            tjFragmentedScenes.setUpdatedDate(LocalDateTime.now());
        }
        try {
            flag = saveOrUpdate(tjFragmentedScenes);
        }catch(Exception e) {

        }
        return flag;
    }


    /**
     *
     * @param scenes
     * @param resourcesName
     * @param type 配置完成：1；克隆：2；
     */
    private void buildSceneName(TjScenelibTree scenes, String resourcesName, int type) {
        switch (type) {
            case 1:
                String newName = StringUtils.format(ContentTemplate.SCENE_NAME_TEMPLATE,
                        StringUtils.split(scenes.getName(), "_")[0], resourcesName);
                QueryWrapper<TjScenelibTree> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(ColumnName.PARENT_ID_COLUMN, scenes.getParentId()).eq(ColumnName.COMPLETED_COLUMN, YN.Y_INT)
                        .ne(ColumnName.ID_COLUMN, scenes.getId()).like(ColumnName.NAME_COLUMN, newName);
                List<TjScenelibTree> list = this.list(queryWrapper);
                int number = 0;
                for (TjScenelibTree item : CollectionUtils.emptyIfNull(list)) {
                    if (StringUtils.equals(newName, item.getName())) {
                        number = 1;
                        continue;
                    }
                    String[] nameItem = StringUtils.split(item.getName(), "_");
                    if (nameItem.length > 1) {
                        String[] suffixItem = StringUtils.split(nameItem[1], "#");
                        if (suffixItem.length > 1
                                && StringUtils.equals(newName, StringUtils.join(new String[]{nameItem[0], suffixItem[0]}, "_"))
                                && Integer.parseInt(suffixItem[1]) >= number) {
                            number = Integer.parseInt(suffixItem[1]) + 1;
                        }
                    }
                }
                if (number > 0) {
                    newName = newName.concat("#").concat(String.valueOf(number));
                }
                scenes.setName(newName);
                break;
            case 2:
//                String namePrefix = YN.Y_INT == scenes.getCompleted()
//                        ? StringUtils.split(scenes.getName(), "_")[0]
//                        : scenes.getName();
//                scenes.setName(StringUtils.format(ContentTemplate.COPY_SCENE_NAME_TEMPLATE, namePrefix,
//                        System.currentTimeMillis()));
                break;
            default:
                break;
        }
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<TjScenelibTree> list, TjScenelibTree t) {
        // 得到子节点列表
        List<TjScenelibTree> childList = getChildList(list, t);
        t.setChildren(childList);
        for (TjScenelibTree tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<TjScenelibTree> getChildList(List<TjScenelibTree> list, TjScenelibTree t) {
        List<TjScenelibTree> tlist = new ArrayList<>();
        Iterator<TjScenelibTree> it = list.iterator();
        while (it.hasNext()) {
            TjScenelibTree n = it.next();
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<TjScenelibTree> list, TjScenelibTree t) {
        return getChildList(list, t).size() > 0;
    }

    @Override
    public synchronized String buildSceneNumber() {
        return StringUtils.format(ContentTemplate.SCENE_NUMBER_TEMPLATE, DateUtils.getNowDayString(),
                CounterUtil.getRandomChar());
    }
}
