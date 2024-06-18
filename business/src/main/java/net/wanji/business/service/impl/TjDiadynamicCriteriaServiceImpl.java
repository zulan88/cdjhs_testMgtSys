package net.wanji.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.wanji.business.common.Constants;
import net.wanji.business.domain.bo.DiadynamicCriteriaBo;
import net.wanji.business.entity.TjDiadynamicCriteria;
import net.wanji.business.mapper.TjDiadynamicCriteriaMapper;
import net.wanji.business.service.TjDiadynamicCriteriaService;
import net.wanji.common.utils.StringUtils;

/**
 * @author guowenhao
 * @description 针对表【tj_diadynamic_criteria(诊断指标表)】的数据库操作Service实现
 * @createDate 2023-08-30 16:39:32
 */
@Service
public class TjDiadynamicCriteriaServiceImpl extends ServiceImpl<TjDiadynamicCriteriaMapper, TjDiadynamicCriteria>
    implements TjDiadynamicCriteriaService {

    @Autowired
    private TjDiadynamicCriteriaMapper tjDiadynamicCriteriaMapper;

    @Override
    public List<TjDiadynamicCriteria> selectPageList(DiadynamicCriteriaBo dcBo) {
        QueryWrapper<TjDiadynamicCriteria> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(dcBo.getName()))
            queryWrapper.like(Constants.ColumnName.NAME_COLUMN, dcBo.getName());
        if (StringUtils.isNotEmpty(dcBo.getIndexDescribe()))
            queryWrapper.like("index_describe", dcBo.getIndexDescribe());
        if (StringUtils.isNotEmpty(dcBo.getType()))
            queryWrapper.eq("type", dcBo.getType());
        List<TjDiadynamicCriteria> tjDcList = tjDiadynamicCriteriaMapper.selectList(queryWrapper);
        // return tjDcList.stream().map(m -> {
        // DiadynamicCriteriaVo diadynamicCriteriaVo = new DiadynamicCriteriaVo();
        // BeanUtils.copyBeanProp(diadynamicCriteriaVo, m);
        // return diadynamicCriteriaVo;
        // }).collect(Collectors.toList());
        return tjDcList;
    }

    @Override
    public int editDesignConditions(DiadynamicCriteriaBo dcBo) {
        QueryWrapper<TjDiadynamicCriteria> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Constants.ColumnName.ID_COLUMN, dcBo.getId());
        TjDiadynamicCriteria tjDiadynamicCriteria = this.getOne(queryWrapper);
        tjDiadynamicCriteria.setDesignConditions(dcBo.getDesignConditions());
        return this.updateById(tjDiadynamicCriteria) ? 1 : 0;
    }
}
