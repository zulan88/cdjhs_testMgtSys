package net.wanji.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.domain.BusinessTreeSelect;
import net.wanji.business.domain.dto.TjFragmentedSceneDetailDto;
import net.wanji.business.domain.dto.TjFragmentedScenesDto;
import net.wanji.business.domain.dto.TreeTypeDto;
import net.wanji.business.entity.TjScenelibTree;
import net.wanji.business.exception.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 片段式场景表 服务类
 * </p>
 *
 * @author wj
 * @since 2023-06-27
 */
public interface TjScenelibTreeService extends IService<TjScenelibTree> {

    /**
     * 初始化
     * @return
     */
    Map<String, Object> init();

    /**
     * 创建场景编号
     * @return
     */
    String buildSceneNumber();

    /**
     * 初始化编辑页
     * @param type
     * @return
     */
    Map<String, Object> initEditPage();

    /**
     * 添加场景树类型
     * @param treeTypeDto
     * @return
     */
    boolean saveSceneTreeType(TreeTypeDto treeTypeDto) throws BusinessException ;

    /**
     * 删除树类型
     * @param dictCode
     * @return
     */
    boolean deleteTreeType(Long dictCode) throws BusinessException;

    /**
     * 获取所有使用中的场景
     * @param type
     * @param name
     * @return
     */
    List<TjScenelibTree> selectUsingScenes(String type);

    /**
     * 构建树结构
     * @param scenes
     * @return
     */
    List<TjScenelibTree> buildSceneTree(List<TjScenelibTree> scenes);

    /**
     * 查找文件夹下所有场景
     * @param sceneId
     * @param scenes
     */
    void selectChildrenFromFolder(Integer sceneId, List<TjScenelibTree> scenes);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param scenes 场景列表
     * @return 下拉树结构列表
     */
    List<BusinessTreeSelect> buildSceneTreeSelect(List<TjScenelibTree> scenes, String name);

    /**
     * 根据id删除场景
     * @param id
     * @return
     */
    boolean deleteSceneById(Integer id) throws BusinessException;


    /**
     * 保存场景树
     * @param fragmentedScenesDto
     * @return
     */
    boolean saveSceneTree(TjFragmentedScenesDto fragmentedScenesDto);

}
