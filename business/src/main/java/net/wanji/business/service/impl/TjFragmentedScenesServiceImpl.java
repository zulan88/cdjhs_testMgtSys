package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.common.Constants.ColumnName;
import net.wanji.business.common.Constants.ContentTemplate;
import net.wanji.business.common.Constants.LanePointEnum;
import net.wanji.business.common.Constants.SysType;
import net.wanji.business.common.Constants.UsingStatus;
import net.wanji.business.common.Constants.YN;
import net.wanji.business.domain.BusinessTreeSelect;
import net.wanji.business.domain.dto.TreeTypeDto;
import net.wanji.business.domain.dto.TjFragmentedSceneDetailDto;
import net.wanji.business.domain.dto.TjFragmentedScenesDto;
import net.wanji.business.entity.TjCase;
import net.wanji.business.entity.TjFragmentedSceneDetail;
import net.wanji.business.entity.TjFragmentedScenes;
import net.wanji.business.entity.TjResourcesDetail;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjCaseMapper;
import net.wanji.business.mapper.TjFragmentedSceneDetailMapper;
import net.wanji.business.mapper.TjFragmentedScenesMapper;
import net.wanji.business.service.TjFragmentedScenesService;
import net.wanji.business.service.TjResourcesDetailService;
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
public class TjFragmentedScenesServiceImpl extends ServiceImpl<TjFragmentedScenesMapper, TjFragmentedScenes>
        implements TjFragmentedScenesService {

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private TjResourcesDetailService resourcesDetailService;

    @Autowired
    private TjFragmentedSceneDetailMapper sceneDetailMapper;

    @Autowired
    private TjCaseMapper caseMapper;

    @Autowired
    private TjFragmentedScenesMapper fragmentedScenesMapper;

    @Override
    public Map<String, Object> init() {
//        List<SysDictData> sceneTreeType = dictTypeService.selectDictDataByType(SysType.SCENE_TREE_TYPE);
        List<SysDictData> sceneType = dictTypeService.selectDictDataByType(SysType.SCENE_TYPE);
        List<SysDictData> sceneComplexity = dictTypeService.selectDictDataByType(SysType.SCENE_COMPLEXITY);
        List<SysDictData> trafficFlowStatus = dictTypeService.selectDictDataByType(SysType.TRAFFIC_FLOW_STATUS);
        List<SysDictData> roadType = dictTypeService.selectDictDataByType(SysType.ROAD_TYPE);
        List<SysDictData> weather = dictTypeService.selectDictDataByType(SysType.WEATHER);
        List<SysDictData> roadCondition = dictTypeService.selectDictDataByType(SysType.ROAD_CONDITION);
        Map<String, Object> result = new HashMap<>(8);
//        result.put(SysType.SCENE_TREE_TYPE, CollectionUtils.emptyIfNull(sceneTreeType).stream()
//                .map(SimpleSelect::new).collect(Collectors.toList()));
        result.put(SysType.SCENE_TYPE, CollectionUtils.emptyIfNull(sceneType).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        result.put(SysType.SCENE_COMPLEXITY, CollectionUtils.emptyIfNull(sceneComplexity).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        result.put(SysType.TRAFFIC_FLOW_STATUS, CollectionUtils.emptyIfNull(trafficFlowStatus).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        result.put(SysType.ROAD_TYPE, CollectionUtils.emptyIfNull(roadType).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        result.put(SysType.WEATHER, CollectionUtils.emptyIfNull(weather).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        result.put(SysType.ROAD_CONDITION, CollectionUtils.emptyIfNull(roadCondition).stream()
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
        List<SysDictData> sysDictData = dictTypeService.selectDictDataByType(SysType.SCENE_TREE_TYPE);
        SysDictData dictData = null;
        if (ObjectUtils.isEmpty(treeTypeDto.getDictCode())) {
            // 添加
            List<String> labelList = CollectionUtils.emptyIfNull(sysDictData).stream().map(SysDictData::getDictLabel)
                    .collect(Collectors.toList());
            if (!SecurityUtils.getUsername().equals("admin")){
                treeTypeDto.setDictLabel(treeTypeDto.getDictLabel() + "(" + SecurityUtils.getUserId() + ")");
            }
            if (labelList.contains(treeTypeDto.getDictLabel())) {
                throw new BusinessException("名称重复");
            }
            dictData = new SysDictData();
            BeanUtils.copyBeanProp(dictData, treeTypeDto);
            long value = CollectionUtils.emptyIfNull(sysDictData).stream().mapToLong(SysDictData::getDictSort).max()
                    .orElse(0) + 1;
            dictData.setDictSort(value);
            dictData.setDictValue(String.valueOf(value));
            dictData.setDictType(SysType.SCENE_TREE_TYPE);
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
        QueryWrapper<TjFragmentedScenes> scenesQueryWrapper = new QueryWrapper<>();
        scenesQueryWrapper.eq(ColumnName.TYPE_COLUMN, dictData.getDictValue()).eq(ColumnName.IS_FOLDER_COLUMN, YN.N_INT);
        List<TjFragmentedScenes> scenesList = this.list(scenesQueryWrapper);
        List<Integer> sceneIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(scenesList)) {
            sceneIds = scenesList.stream().map(TjFragmentedScenes::getId).collect(Collectors.toList());
            QueryWrapper<TjFragmentedSceneDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.in(ColumnName.SCENE_ID_COLUMN, sceneIds);
            List<TjFragmentedSceneDetail> details = sceneDetailMapper.selectList(detailQueryWrapper);
            List<Integer> detailIds = CollectionUtils.emptyIfNull(details).stream().map(TjFragmentedSceneDetail::getId)
                    .collect(Collectors.toList());

            QueryWrapper<TjCase> caseQueryWrapper = new QueryWrapper<>();
            if(detailIds.size()>0){
                caseQueryWrapper.in(ColumnName.SCENE_DETAIL_ID_COLUMN, detailIds);
                List<TjCase> caseList = caseMapper.selectList(caseQueryWrapper);
                if (CollectionUtils.isNotEmpty(caseList)) {
                    throw new BusinessException("此类型下存在测试用例，无法删除");
                }
            }
        }
        // 2.删除场景详情
        if (CollectionUtils.isNotEmpty(sceneIds)) {
            QueryWrapper<TjFragmentedSceneDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.in(ColumnName.SCENE_ID_COLUMN, sceneIds);
            List<TjFragmentedSceneDetail> list = sceneDetailMapper.selectList(detailQueryWrapper);
            if(list.size()>0) {
                int removeDetail = sceneDetailMapper.delete(detailQueryWrapper);
                if (removeDetail != sceneIds.size()) {
                    throw new BusinessException("删除详情失败");
                }
            }
        }
        // 3.删除场景
        QueryWrapper<TjFragmentedScenes> scenesDeleteWrapper = new QueryWrapper<>();
        scenesDeleteWrapper.eq(ColumnName.TYPE_COLUMN, dictData.getDictValue());
        this.remove(scenesDeleteWrapper);
        // 4.删除场景树类型
        dictDataService.deleteDictDataByIds(new Long[]{dictCode});
        return true;
    }

    @Override
    public List<TjFragmentedScenes> selectUsingScenes(String type) {
        List<TjFragmentedScenes> result = fragmentedScenesMapper.selectByCondition(type);
        return CollectionUtils.emptyIfNull(result).stream().sorted(Comparator.comparing(TjFragmentedScenes::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<TjFragmentedScenes> buildSceneTree(List<TjFragmentedScenes> scenes) {
        List<TjFragmentedScenes> returnList = new ArrayList<>();
        if (CollectionUtils.isEmpty(scenes)) {
            return returnList;
        }
        List<Integer> tempList = new ArrayList<>();
        for (TjFragmentedScenes item : scenes) {
            tempList.add(item.getId());
        }
        for (Iterator<TjFragmentedScenes> iterator = scenes.iterator(); iterator.hasNext(); ) {
            TjFragmentedScenes scene = iterator.next();
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
    public List<BusinessTreeSelect> buildSceneTreeSelect(List<TjFragmentedScenes> scenes, String name) {
        List<TjFragmentedScenes> sceneTrees = buildSceneTree(scenes);
        String user = SecurityUtils.getUsername();
        if (!user.equals("admin")) {
            sceneTrees = sceneTrees.stream().filter(item -> {
                return item.getCreatedBy().equals(user);
            }).collect(Collectors.toList());
        }
        return sceneTrees.stream().map(BusinessTreeSelect::new).map(tree -> BusinessTreeUtils.fuzzySearch(tree, name))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteSceneById(Integer id) throws BusinessException {
        TjFragmentedScenes scenes = this.getById(id);
        if (ObjectUtils.isEmpty(scenes)) {
            throw new BusinessException("节点不存在");
        }
        // 1.删除场景
        if (YN.N_INT == scenes.getIsFolder()) {
            int count = caseMapper.selectCountBySceneIds(Collections.singletonList((scenes.getId())));
            if (count > 0) {
                throw new BusinessException("当前节点下存在测试用例，无法删除");
            }
            sceneDetailMapper.deleteBySceneIds(Collections.singletonList(scenes.getId()));
            this.removeById(id);
            return true;
        }
        // 2.删除文件夹
        // 2.1.查询文件夹下所有场景和文件夹
        List<TjFragmentedScenes> deleteCollector = new ArrayList<>();
        this.selectChildrenFromFolder(scenes.getId(), deleteCollector);
        // 2.2.筛选掉文件夹，校验所有场景节点是否存在测试用例，没有测试用例则删除所有场景详情
        List<Integer> sceneIds = deleteCollector.stream().filter(item -> YN.N_INT == item.getIsFolder())
                .map(TjFragmentedScenes::getId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(sceneIds)) {
            int count = caseMapper.selectCountBySceneIds(sceneIds);
            if (count > 0) {
                throw new BusinessException("当前文件夹下的场景存在测试用例，无法删除");
            }
            sceneDetailMapper.deleteBySceneIds(sceneIds);
        }
        // 2.3.删掉文件夹及其所有子节点
        deleteCollector.add(scenes);
        QueryWrapper<TjFragmentedScenes> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.in(ColumnName.ID_COLUMN,
                deleteCollector.stream().map(TjFragmentedScenes::getId).collect(Collectors.toList()));
        return this.remove(deleteWrapper);
    }


    @Override
    public void selectChildrenFromFolder(Integer id, List<TjFragmentedScenes> collector) {
        QueryWrapper<TjFragmentedScenes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ColumnName.PARENT_ID_COLUMN, id);
        List<TjFragmentedScenes> childrenList = this.list(queryWrapper);

        if (CollectionUtils.isNotEmpty(childrenList)) {
            collector.addAll(childrenList);
            for (TjFragmentedScenes scenes : childrenList) {
                selectChildrenFromFolder(scenes.getId(), collector);
            }
        }
    }

    @Override
    public boolean saveSceneTree(TjFragmentedScenesDto fragmentedScenesDto) {
        TjFragmentedScenes tjFragmentedScenes;
        fragmentedScenesDto.setIsFolder(0);
        if (ObjectUtils.isEmpty(fragmentedScenesDto.getId())) {
            tjFragmentedScenes = new TjFragmentedScenes();
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
        return saveOrUpdate(tjFragmentedScenes);
    }


    /**
     *
     * @param scenes
     * @param resourcesName
     * @param type 配置完成：1；克隆：2；
     */
    private void buildSceneName(TjFragmentedScenes scenes, String resourcesName, int type) {
        switch (type) {
            case 1:
                String newName = StringUtils.format(ContentTemplate.SCENE_NAME_TEMPLATE,
                        StringUtils.split(scenes.getName(), "_")[0], resourcesName);
                QueryWrapper<TjFragmentedScenes> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq(ColumnName.PARENT_ID_COLUMN, scenes.getParentId()).eq(ColumnName.COMPLETED_COLUMN, YN.Y_INT)
                        .ne(ColumnName.ID_COLUMN, scenes.getId()).like(ColumnName.NAME_COLUMN, newName);
                List<TjFragmentedScenes> list = this.list(queryWrapper);
                int number = 0;
                for (TjFragmentedScenes item : CollectionUtils.emptyIfNull(list)) {
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
    private void recursionFn(List<TjFragmentedScenes> list, TjFragmentedScenes t) {
        // 得到子节点列表
        List<TjFragmentedScenes> childList = getChildList(list, t);
        t.setChildren(childList);
        for (TjFragmentedScenes tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<TjFragmentedScenes> getChildList(List<TjFragmentedScenes> list, TjFragmentedScenes t) {
        List<TjFragmentedScenes> tlist = new ArrayList<>();
        Iterator<TjFragmentedScenes> it = list.iterator();
        while (it.hasNext()) {
            TjFragmentedScenes n = it.next();
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<TjFragmentedScenes> list, TjFragmentedScenes t) {
        return getChildList(list, t).size() > 0;
    }

    @Override
    public synchronized String buildSceneNumber() {
        return StringUtils.format(ContentTemplate.SCENE_NUMBER_TEMPLATE, DateUtils.getNowDayString(),
                CounterUtil.getRandomChar());
    }
}
