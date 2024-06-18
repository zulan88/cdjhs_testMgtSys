package net.wanji.business.service;

import net.wanji.business.domain.BusinessTreeSelect;
import net.wanji.business.domain.dto.TjFragmentedScenesDto;
import net.wanji.business.domain.dto.TjResourcesDto;
import net.wanji.business.entity.TjResources;
import net.wanji.business.entity.TjResources;
import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.exception.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资源信息表 服务类
 * </p>
 *
 * @author wj
 * @since 2023-06-27
 */
public interface TjResourcesService extends IService<TjResources> {
    /**
     * 初始化
     * @return
     */
    Map<String, Object> init();

    /**
     * 获取所有使用中的资源
     * @return
     */
    List<TjResources> selectUsingResources(String type);

    /**
     * 构建树结构
     * @param resources
     * @return
     */
    List<TjResources> buildResourcesTree(List<TjResources> resources);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param resources 资源列表
     * @return 下拉树结构列表
     */
    List<BusinessTreeSelect> buildResourcesTreeSelect(List<TjResources> resources, String name);

    /**
     * 删除资源树节点
     * @param id
     * @return
     */
    boolean deleteTree(Integer id) throws BusinessException;

    /**
     * 保存树节点
     * @param resourcesDto
     * @return
     */
    boolean saveTree(TjResourcesDto resourcesDto);

    /**
     * 根据id删除资源
     * @param id
     * @return
     */
    boolean deleteResourcesById(Integer id);

    /**
     * 保存资源
     * @param resourcesDto
     * @return
     */
    boolean saveResource(TjResourcesDto resourcesDto);
}
