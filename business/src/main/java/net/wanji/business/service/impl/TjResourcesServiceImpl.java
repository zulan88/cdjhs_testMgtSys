package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.common.Constants.ColumnName;
import net.wanji.business.common.Constants.SysType;
import net.wanji.business.common.Constants.UsingStatus;
import net.wanji.business.domain.BusinessTreeSelect;
import net.wanji.business.domain.dto.TjResourcesDto;
import net.wanji.business.entity.TjCase;
import net.wanji.business.entity.TjResources;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjResourcesMapper;
import net.wanji.business.service.TjCaseService;
import net.wanji.business.service.TjResourcesDetailService;
import net.wanji.business.service.TjResourcesService;
import net.wanji.business.util.BusinessTreeUtils;
import net.wanji.common.core.domain.SimpleSelect;
import net.wanji.common.core.domain.entity.SysDictData;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.bean.BeanUtils;
import net.wanji.system.service.ISysDictTypeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源信息表 服务实现类
 * </p>
 *
 * @author wj
 * @since 2023-06-27
 */
@Service
public class TjResourcesServiceImpl extends ServiceImpl<TjResourcesMapper, TjResources> implements TjResourcesService {

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private TjCaseService tjCaseService;

    @Autowired
    private TjResourcesDetailService resourcesDetailService;

    @Override
    public Map<String, Object> init() {
        List<SysDictData> resourceType = dictTypeService.selectDictDataByType(SysType.RESOURCE_TYPE);
        List<SysDictData> sceneTreeType = dictTypeService.selectDictDataByType(SysType.SCENE_TREE_TYPE);
        List<SysDictData> roadWayType = dictTypeService.selectDictDataByType(SysType.ROAD_WAY_TYPE);
        Map<String, Object> result = new HashMap<>(3);
        result.put(SysType.RESOURCE_TYPE, CollectionUtils.emptyIfNull(resourceType).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        result.put(SysType.SCENE_TREE_TYPE, CollectionUtils.emptyIfNull(sceneTreeType).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        result.put(SysType.ROAD_WAY_TYPE, CollectionUtils.emptyIfNull(roadWayType).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        return result;
    }

    @Override
    public List<TjResources> selectUsingResources(String type) {
        QueryWrapper<TjResources> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ColumnName.STATUS_COLUMN, UsingStatus.ENABLE).eq(ColumnName.TYPE_COLUMN, type);
        List<TjResources> result = this.list(queryWrapper);
        return CollectionUtils.emptyIfNull(result).stream().sorted(Comparator.comparing(TjResources::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<TjResources> buildResourcesTree(List<TjResources> resources) {
        List<TjResources> returnList = new ArrayList<>();
        if (CollectionUtils.isEmpty(resources)) {
            return returnList;
        }
        List<Integer> tempList = new ArrayList<>();
        for (TjResources item : resources) {
            tempList.add(item.getId());
        }
        for (Iterator<TjResources> iterator = resources.iterator(); iterator.hasNext(); ) {
            TjResources resource = iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(resource.getParentId())) {
                recursionFn(resources, resource);
                returnList.add(resource);
            }
        }
        if (returnList.isEmpty()) {
            returnList = resources;
        }
        return returnList;
    }

    @Override
    public List<BusinessTreeSelect> buildResourcesTreeSelect(List<TjResources> resources, String name) {
        List<TjResources> resourcesTrees = buildResourcesTree(resources);
        return resourcesTrees.stream().map(BusinessTreeSelect::new).map(tree ->
                        BusinessTreeUtils.fuzzySearch(tree, name)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteTree(Integer id) throws BusinessException {
        TjResources resources = this.getById(id);
        if (ObjectUtils.isEmpty(resources)) {
            throw new BusinessException("未查询到对应节点");
        }
        boolean success = resourcesDetailService.deleteByResourceId(id);
        if (!success) {
            throw new BusinessException("删除资源失败");
        }
        return this.removeById(id);
    }

    @Override
    public boolean saveTree(TjResourcesDto resourcesDto) {
        if (ObjectUtils.isEmpty(resourcesDto.getId())) {
            TjResources tjResources = new TjResources();
            BeanUtils.copyBeanProp(tjResources, resourcesDto);
            tjResources.setStatus(UsingStatus.ENABLE);
            tjResources.setCreatedBy(SecurityUtils.getUsername());
            tjResources.setCreatedDate(LocalDateTime.now());
            return this.save(tjResources);
        } else {
            TjResources resources = this.getById(resourcesDto.getId());
            resources.setName(resourcesDto.getName());
            resources.setUpdatedBy(SecurityUtils.getUsername());
            resources.setUpdatedDate(LocalDateTime.now());
            return this.updateById(resources);
        }
    }

    @Override
    public boolean deleteResourcesById(Integer id) {
        TjResources resources = new TjResources();
        resources.setId(id);
        resources.setStatus(UsingStatus.DISABLE);
        resources.setUpdatedBy(SecurityUtils.getUsername());
        resources.setUpdatedDate(LocalDateTime.now());
        return this.updateById(resources);
    }

    @Override
    public boolean saveResource(TjResourcesDto resourcesDto) {
        TjResources tjResources = new TjResources();
        BeanUtils.copyBeanProp(tjResources, resourcesDto);
        if (ObjectUtils.isEmpty(tjResources.getId())) {
            tjResources.setCreatedBy(SecurityUtils.getUsername());
            tjResources.setCreatedDate(LocalDateTime.now());
        } else {
            tjResources.setUpdatedBy(SecurityUtils.getUsername());
            tjResources.setUpdatedDate(LocalDateTime.now());
        }
        return this.saveOrUpdate(tjResources);
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<TjResources> list, TjResources t) {
        // 得到子节点列表
        List<TjResources> childList = getChildList(list, t);
        t.setChildren(childList);
        for (TjResources tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<TjResources> getChildList(List<TjResources> list, TjResources t) {
        List<TjResources> tlist = new ArrayList<>();
        Iterator<TjResources> it = list.iterator();
        while (it.hasNext()) {
            TjResources n = it.next();
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<TjResources> list, TjResources t) {
        return getChildList(list, t).size() > 0;
    }
}
