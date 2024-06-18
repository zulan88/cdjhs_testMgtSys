package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.common.Constants.ColumnName;
import net.wanji.business.common.Constants.SysType;
import net.wanji.business.common.Constants.UsingStatus;
import net.wanji.business.common.Constants.YN;
import net.wanji.business.domain.BusinessTreeSelect;
import net.wanji.business.domain.dto.TjDeviceDto;
import net.wanji.business.domain.dto.TreeTypeDto;
import net.wanji.business.entity.TjDevice;
import net.wanji.business.entity.TjDeviceDetail;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjDeviceDetailMapper;
import net.wanji.business.mapper.TjDeviceMapper;
import net.wanji.business.service.TjDeviceService;
import net.wanji.business.util.BusinessTreeUtils;
import net.wanji.common.core.domain.SimpleSelect;
import net.wanji.common.core.domain.entity.SysDictData;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.bean.BeanUtils;
import net.wanji.system.service.ISysDictDataService;
import net.wanji.system.service.ISysDictTypeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author guanyuduo
 * @description 针对表【tj_device】的数据库操作Service实现
 * @createDate 2023-08-17 10:56:39
 */
@Service
public class TjDeviceServiceImpl extends ServiceImpl<TjDeviceMapper, TjDevice> implements TjDeviceService {

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private TjDeviceDetailMapper deviceDetailMapper;

    @Override
    public Map<String, List<SimpleSelect>> init() {
        List<SysDictData> deviceType = dictTypeService.selectDictDataByType(SysType.DEVICE_TYPE);
        List<SysDictData> partRole = dictTypeService.selectDictDataByType(SysType.PART_ROLE);
        Map<String, List<SimpleSelect>> result = new HashMap<>(1);
        result.put(SysType.DEVICE_TYPE, CollectionUtils.emptyIfNull(deviceType).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        result.put(SysType.PART_ROLE, CollectionUtils.emptyIfNull(partRole).stream()
                .map(SimpleSelect::new).collect(Collectors.toList()));
        return result;
    }

    @Override
    public boolean saveTreeType(TreeTypeDto treeTypeDto) throws BusinessException {
        List<SysDictData> sysDictData = dictTypeService.selectDictDataByType(SysType.DEVICE_TYPE);
        SysDictData dictData = null;
        if (ObjectUtils.isEmpty(treeTypeDto.getDictCode())) {
            // 添加
            List<String> labelList = CollectionUtils.emptyIfNull(sysDictData).stream().map(SysDictData::getDictLabel)
                    .collect(Collectors.toList());
            if (labelList.contains(treeTypeDto.getDictLabel())) {
                throw new BusinessException("名称重复");
            }
            dictData = new SysDictData();
            net.wanji.common.utils.bean.BeanUtils.copyBeanProp(dictData, treeTypeDto);
            long value = CollectionUtils.emptyIfNull(sysDictData).stream().mapToLong(SysDictData::getDictSort).max()
                    .orElse(0) + 1;
            dictData.setDictSort(value);
            dictData.setDictValue(String.valueOf(value));
            dictData.setDictType(SysType.DEVICE_TYPE);
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
        QueryWrapper<TjDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ColumnName.TYPE_COLUMN, dictData.getDictValue());
        List<TjDevice> devices = this.list(queryWrapper);
        // 2.删除场景详情
        if (CollectionUtils.isNotEmpty(devices)) {
            QueryWrapper<TjDeviceDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.in(ColumnName.DEVICE_TYPE_COLUMN,
                    devices.stream().map(TjDevice::getId).collect(Collectors.toList()));
            int removeDetail = deviceDetailMapper.delete(detailQueryWrapper);
            if (removeDetail != devices.size()) {
                throw new BusinessException("删除详情失败");
            }
        }
        // 3.删除场景
        QueryWrapper<TjDevice> scenesDeleteWrapper = new QueryWrapper<>();
        scenesDeleteWrapper.eq(ColumnName.TYPE_COLUMN, dictData.getDictValue());
        this.remove(scenesDeleteWrapper);
        // 4.删除场景树类型
        dictDataService.deleteDictDataByIds(new Long[]{dictCode});
        return true;
    }

    @Override
    public List<TjDevice> selectUsingDeviceTree(String type) {
        QueryWrapper<TjDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ColumnName.TYPE_COLUMN, type).eq(ColumnName.STATUS_COLUMN, YN.N_INT)
                .orderByDesc(ColumnName.CREATED_DATE_COLUMN);
        return this.list(queryWrapper);
    }

    @Override
    public List<TjDevice> buildSceneTree(List<TjDevice> scenes) {
        List<TjDevice> returnList = new ArrayList<>();
        if (CollectionUtils.isEmpty(scenes)) {
            return returnList;
        }
        List<Integer> tempList = new ArrayList<>();
        for (TjDevice item : scenes) {
            tempList.add(item.getId());
        }
        for (Iterator<TjDevice> iterator = scenes.iterator(); iterator.hasNext(); ) {
            TjDevice scene = iterator.next();
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
    public List<BusinessTreeSelect> buildDevicesTreeSelect(List<TjDevice> scenes, String name) {
        List<TjDevice> devicesTrees = buildSceneTree(scenes);
        return devicesTrees.stream().map(BusinessTreeSelect::new).map(tree -> BusinessTreeUtils.fuzzySearch(tree, name))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteDeviceTree(Integer id) throws BusinessException {
        TjDevice devices = this.getById(id);
        if (ObjectUtils.isEmpty(devices)) {
            throw new BusinessException("节点不存在");
        }
        // 1.删除文件夹
        List<TjDevice> deleteCollector = new ArrayList<>();
        this.selectChildrenFromFolder(devices.getId(), deleteCollector);
        deleteCollector.add(devices);
        QueryWrapper<TjDevice> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.in(ColumnName.ID_COLUMN,
                deleteCollector.stream().map(TjDevice::getId).collect(Collectors.toList()));
        return this.remove(deleteWrapper);
    }

    @Override
    public void selectChildrenFromFolder(Integer id, List<TjDevice> collector) {
        QueryWrapper<TjDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ColumnName.PARENT_ID_COLUMN, id);
        List<TjDevice> childrenList = this.list(queryWrapper);
        if (CollectionUtils.isNotEmpty(childrenList)) {
            collector.addAll(childrenList);
            for (TjDevice scenes : childrenList) {
                selectChildrenFromFolder(scenes.getId(), collector);
            }
        }
    }

    @Override
    public boolean saveDevicesTree(TjDeviceDto deviceDto) {
        TjDevice tDevice;
        if (ObjectUtils.isEmpty(deviceDto.getId())) {
            tDevice = new TjDevice();
            BeanUtils.copyBeanProp(tDevice, deviceDto);
            tDevice.setParentId(0);
            tDevice.setLevel(1);
            tDevice.setStatus(UsingStatus.ENABLE);
            tDevice.setCreatedBy(SecurityUtils.getUsername());
            tDevice.setCreatedDate(LocalDateTime.now());
        } else {
            tDevice = this.getById(deviceDto.getId());
            tDevice.setName(deviceDto.getName());
            tDevice.setUpdatedBy(SecurityUtils.getUsername());
            tDevice.setUpdatedDate(LocalDateTime.now());
        }
        return saveOrUpdate(tDevice);
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<TjDevice> list, TjDevice t) {
        // 得到子节点列表
        List<TjDevice> childList = getChildList(list, t);
        t.setChildren(childList);
        for (TjDevice tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<TjDevice> getChildList(List<TjDevice> list, TjDevice t) {
        List<TjDevice> tlist = new ArrayList<>();
        Iterator<TjDevice> it = list.iterator();
        while (it.hasNext()) {
            TjDevice n = it.next();
            if (n.getParentId().longValue() == t.getId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<TjDevice> list, TjDevice t) {
        return getChildList(list, t).size() > 0;
    }
}
