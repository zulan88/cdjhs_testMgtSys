package net.wanji.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.domain.dto.TjAtlasTreeDto;
import net.wanji.business.entity.TjAtlasTree;
import net.wanji.business.exception.BusinessException;

import java.util.Map;

public interface ITjAtlasTreeService extends IService<TjAtlasTree> {


    Map<String,Object> init();

    /**
     * 保存、修改树信息
     * @param tjAtlasTreeDto
     * @return
     */
    boolean saveOrUpdateTree(TjAtlasTreeDto tjAtlasTreeDto);

    boolean deleteTree(Integer treeId) throws BusinessException;

}
