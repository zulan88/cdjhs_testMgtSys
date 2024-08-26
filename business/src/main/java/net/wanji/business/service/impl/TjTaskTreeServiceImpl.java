package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.wanji.business.common.Constants;
import net.wanji.business.entity.TjTask;
import net.wanji.business.entity.TjTaskTree;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjTaskTreeMapper;
import net.wanji.business.service.TjTaskService;
import net.wanji.business.service.TjTaskTreeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.common.core.domain.entity.SysDictData;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.system.service.ISysDictDataService;
import net.wanji.system.service.ISysDictTypeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 用例树表 服务实现类
 * </p>
 *
 * @author wj
 * @since 2024-08-26
 */
@Service
public class TjTaskTreeServiceImpl extends ServiceImpl<TjTaskTreeMapper, TjTaskTree> implements TjTaskTreeService {

    @Autowired
    TjTaskService tjTaskService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Override
    public List<TjTaskTree> selectByParent(Integer parentId) {
        QueryWrapper<TjTaskTree> queryWrapper = new QueryWrapper<>();
        if (parentId != null) {
            queryWrapper.eq("parent_id", parentId);
        }
        queryWrapper.eq("status", 0);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean saveTree(TjTaskTree tjTaskTree) throws BusinessException {
        if (tjTaskTree.getId()==null){
            tjTaskTree.setCreatedBy(SecurityUtils.getUsername());
            tjTaskTree.setCreatedDate(LocalDateTime.now());
        }else {
            tjTaskTree.setUpdatedBy(SecurityUtils.getUsername());
            tjTaskTree.setUpdatedDate(LocalDateTime.now());
        }
        try {
            return saveOrUpdate(tjTaskTree);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("目录设置异常");
        }
    }

    @Override
    public boolean deleteTree(Integer id) throws BusinessException {
        if (id == null) {
            throw new BusinessException("目录ID不能为空");
        }
        List<TjTask> list = tjTaskService.selectByTreeId(id);
        if (CollectionUtils.isNotEmpty(list)) {
            throw new BusinessException("该目录下存在用例，不可删除！");
        }
        return this.removeById(id);
    }

    @Override
    public boolean saveParentTree(SysDictData dictData) throws BusinessException {
        List<SysDictData> sysDictData = dictTypeService.selectDictDataByType(Constants.SysType.TASK_TYPE);
        if (dictData.getDictCode() == null){
            long value = CollectionUtils.emptyIfNull(sysDictData).stream().mapToLong(SysDictData::getDictSort).max()
                    .orElse(0) + 1;
            dictData.setDictSort(value);
            dictData.setDictValue(String.valueOf(value));
            dictData.setDictType(Constants.SysType.TASK_TYPE);
            dictData.setIsDefault(Constants.YN.N);
            dictData.setStatus(String.valueOf(Constants.UsingStatus.ENABLE));
            dictData.setCreateBy(SecurityUtils.getUsername());
            dictData.setCreateTime(new Date());
            if (dictDataService.insertDictData(dictData) < 0) {
                throw new BusinessException("添加失败");
            }
        }else {
            SysDictData originData = dictDataService.selectDictDataById(dictData.getDictCode());
            if (ObjectUtils.isEmpty(originData)) {
                throw new BusinessException("未查询到对应类型");
            }
            // 修改
            List<String> otherNames = CollectionUtils.emptyIfNull(sysDictData).stream().filter(item ->
                            !Objects.equals(item.getDictCode(), dictData.getDictCode())).map(SysDictData::getDictLabel)
                    .collect(Collectors.toList());
            if (otherNames.contains(dictData.getDictLabel())) {
                throw new BusinessException("名称重复");
            }
            originData.setCssClass(dictData.getCssClass());
            originData.setDictLabel(dictData.getDictLabel());
            originData.setRemark(dictData.getRemark());
            originData.setUpdateBy(SecurityUtils.getUsername());
            originData.setUpdateTime(new Date());
            if (dictDataService.updateDictData(originData) < 0) {
                throw new BusinessException("修改失败");
            }
        }
        return true;
    }

    @Override
    public boolean deleteParentTree(Long dictCode) throws BusinessException {
        SysDictData dictData = dictDataService.selectDictDataById(dictCode);
        if (ObjectUtils.isEmpty(dictData)) {
            throw new BusinessException("未查询到对应一级目录");
        }
        List<TjTaskTree> list = selectByParent(dictData.getDictCode().intValue());
        if (CollectionUtils.isEmpty(list)) {
            dictDataService.deleteDictDataByIds(new Long[]{dictCode});
            return true;
        }else {
            throw new BusinessException("该目录下存在子目录，不可删除！");
        }
    }
}
