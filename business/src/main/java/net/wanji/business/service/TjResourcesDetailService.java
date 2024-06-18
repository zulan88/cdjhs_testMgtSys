package net.wanji.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.domain.dto.TjResourcesDetailDto;
import net.wanji.business.domain.vo.ResourcesDetailVo;
import net.wanji.business.entity.TjResourcesDetail;
import net.wanji.business.exception.BusinessException;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 资源详情表 服务类
 * </p>
 *
 * @author wj
 * @since 2023-06-27
 */
public interface TjResourcesDetailService extends IService<TjResourcesDetail> {

    /**
     * 根据资源Id查询资源列表
     * @param resourceId
     * @return
     */
    List<ResourcesDetailVo> getDetailList(Integer resourceId, String name);

    /**
     * 预览
     * @param resourcesDetailDto
     * @return
     */
    ResourcesDetailVo preview(TjResourcesDetailDto resourcesDetailDto) throws BusinessException, IOException;

    /**
     * 保存资源详情
     * @param resourcesDetailDto
     * @return
     */
    boolean saveResourcesDetail(TjResourcesDetailDto resourcesDetailDto) throws BusinessException;

    /**
     * 收藏资源
     * @param resourceDetailId
     * @return
     */
    boolean collectByDetailId(Integer resourceDetailId);

    /**
     * 删除资源
     * @param resourceDetailId
     * @return
     */
    boolean deleteByDetailId(Integer resourceDetailId) throws BusinessException;

    /**
     * 删除资源节点下所有资源
     * @param resourceId
     * @return
     */
    boolean deleteByResourceId(Integer resourceId) throws BusinessException;

    /**
     * 根据条件查询地图下拉列表
     * @param type 资源类型
     * @param sceneTreeType 道路类型
     * @param roadWayType 道路单双向
     * @param laneNum 车道数
     * @return
     */
    List<TjResourcesDetail> getMapSelect(String type, String sceneTreeType, String roadWayType, Integer laneNum);

    List<TjResourcesDetail> selectAllResource();
}
