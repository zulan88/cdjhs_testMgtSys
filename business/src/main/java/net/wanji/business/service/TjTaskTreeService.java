package net.wanji.business.service;

import net.wanji.business.domain.dto.CaseTreeDto;
import net.wanji.business.entity.TjTaskTree;
import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.exception.BusinessException;
import net.wanji.common.core.domain.entity.SysDictData;

import java.util.List;

/**
 * <p>
 * 用例树表 服务类
 * </p>
 *
 * @author wj
 * @since 2024-08-26
 */
public interface TjTaskTreeService extends IService<TjTaskTree> {

    public List<TjTaskTree> selectByParent(Integer parentId);

    boolean saveTree(TjTaskTree tjTaskTree) throws BusinessException;

    boolean deleteTree(Integer id) throws BusinessException;

    boolean saveParentTree(SysDictData sysDictData) throws BusinessException;

    boolean deleteParentTree(Long dictCode) throws BusinessException;
}
