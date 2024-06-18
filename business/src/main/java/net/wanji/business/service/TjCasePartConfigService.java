package net.wanji.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.domain.PartConfigSelect;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.business.domain.vo.CasePartConfigVo;
import net.wanji.business.entity.TjCasePartConfig;
import net.wanji.business.exception.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wj
 * @since 2023-06-29
 */
public interface TjCasePartConfigService extends IService<TjCasePartConfig> {
    /**
     * 查询用例角色配置
     *
     * @param caseId
     * @return
     */
    Map<String, List<CasePartConfigVo>> getConfigInfo(Integer caseId) throws BusinessException;

    List<CasePartConfigVo> getConfigInfoByCaseId(Integer caseId) throws BusinessException;

    /**
     * 场景参与者信息转换为配置信息
     *
     * @param sceneTrajectoryBo
     * @return key：参与者类型（part_type） value：配置
     */
    Map<String, List<CasePartConfigVo>> trajectory2Config(SceneTrajectoryBo sceneTrajectoryBo);

    /**
     * 实车验证设备配置
     *
     * @param partConfigSelects
     * @return
     */
    boolean saveFromSelected(List<PartConfigSelect> partConfigSelects);

    /**
     * 保存用例配置信息
     *
     * @param caseId    用例ID
     * @param configs 配置信息
     * @return
     * @throws BusinessException
     */
    boolean removeThenSave(Integer caseId,  List<TjCasePartConfig> configs) throws BusinessException;

    boolean saveAndupdate(Integer caseId,  List<TjCasePartConfig> configs) throws BusinessException;

}
