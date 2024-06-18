package net.wanji.business.service;

import net.wanji.business.domain.InfinteMileScenceExo;
import net.wanji.business.domain.SiteSlice;
import net.wanji.business.domain.TrafficFlow;
import net.wanji.business.entity.InfinteMileScence;
import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.exception.BusinessException;
import net.wanji.common.exception.ServiceException;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2024-02-23
 */
public interface InfinteMileScenceService extends IService<InfinteMileScence> {

    List<InfinteMileScenceExo> selectInfinteMileScenceList(Integer status);

    String buildSceneNumber();

    Integer saveInfinteMileScence(InfinteMileScenceExo infinteMileScence) throws BusinessException, ServiceException;

    void debugging(InfinteMileScenceExo infinteMileScence) throws BusinessException;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    InfinteMileScenceExo selectInfinteMileScenceById(Integer id);

    InfinteMileScenceExo selectInfinteMileScenceById2(Integer id);

    /**
     * 获取站点切片
     * @param caseId
     * @param siteId
     * @return
     */
    SiteSlice getSiteSlice(Integer caseId, Integer siteId);

    /**
     * 处理实体格式
     * @param infinteMileScenceExo
     */
    void dualInfiniteSimulation(InfinteMileScenceExo infinteMileScenceExo);

    /**
     * 停止仿真
     * @param id
     * @return
     * @throws BusinessException
     */
    boolean stopInfinteSimulation(Integer id);

    List<TrafficFlow> getTrafficFlow(Integer mapid) throws BusinessException;

    List<SiteSlice> getSiteSlices(Integer id);

}
