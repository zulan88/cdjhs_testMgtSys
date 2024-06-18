package net.wanji.business.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wanji.business.domain.vo.ScenelibVo;
import net.wanji.business.entity.TjScenelib;
import org.apache.ibatis.annotations.Param;

/**
 * scenelibMapper接口
 * 
 * @author wanji
 * @date 2023-10-31
 */
public interface TjScenelibMapper extends BaseMapper<TjScenelib>
{
    /**
     * 查询scenelib
     * 
     * @param id scenelibID
     * @return scenelib
     */
    public TjScenelib selectTjScenelibById(Long id);

    /**
     * 查询scenelib列表
     * 
     * @param tjScenelib scenelib
     * @return scenelib集合
     */
    public List<TjScenelib> selectTjScenelibList(TjScenelib tjScenelib);

    /**
     * 新增scenelib
     * 
     * @param tjScenelib scenelib
     * @return 结果
     */
    public int insertTjScenelib(TjScenelib tjScenelib);

    /**
     * 修改scenelib
     * 
     * @param tjScenelib scenelib
     * @return 结果
     */
    public int updateTjScenelib(TjScenelib tjScenelib);

    /**
     * 删除scenelib
     * 
     * @param id scenelibID
     * @return 结果
     */
    public int deleteTjScenelibById(Long id);

    /**
     * 批量删除scenelib
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTjScenelibByIds(Long[] ids);

    public List<ScenelibVo>selectScenelibVoList(ScenelibVo scenelibVo);

    public int deleteTjScenelibByTreeId(@Param("sceneIds") List<Integer> sceneIds);

    List<ScenelibVo> selectTjSceneDetailListAnd(@Param("labellist") List<Integer> labellist, @Param("treeId")Integer treeId);
    List<ScenelibVo> selectTjSceneDetailListOr(@Param("labellist") List<Integer> labellist, @Param("treeId")Integer treeId);
}
