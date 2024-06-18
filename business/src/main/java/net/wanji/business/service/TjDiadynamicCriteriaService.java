package net.wanji.business.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

import net.wanji.business.domain.bo.DiadynamicCriteriaBo;
import net.wanji.business.entity.TjDiadynamicCriteria;

/**
* @author guowenhao
* @description 针对表【tj_diadynamic_criteria(诊断指标表)】的数据库操作Service
* @createDate 2023-08-30 16:39:32
*/
public interface TjDiadynamicCriteriaService extends IService<TjDiadynamicCriteria> {

    /**
     * 查询诊断指标页面列表
     *
     * @param dcBo 诊断指标信息
     * @return 诊断指标集合
     */
    public List<TjDiadynamicCriteria> selectPageList(DiadynamicCriteriaBo dcBo);

    /**
     * 计算公式修改
     * @param dcBo
     * @return
     */
    public int editDesignConditions(DiadynamicCriteriaBo dcBo);

}
