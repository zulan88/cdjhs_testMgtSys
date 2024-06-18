package net.wanji.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.domain.PartConfigSelect;
import net.wanji.business.domain.bo.CaseInfoBo;
import net.wanji.business.domain.bo.CaseTrajectoryDetailBo;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.business.domain.dto.CaseQueryDto;
import net.wanji.business.domain.dto.TjCaseDto;
import net.wanji.business.domain.vo.CaseDetailVo;
import net.wanji.business.domain.vo.CasePageVo;
import net.wanji.business.domain.vo.CaseVerificationVo;
import net.wanji.business.domain.vo.TjCasePartRoleVo;
import net.wanji.business.entity.TjCase;
import net.wanji.business.entity.TjCaseOp;
import net.wanji.business.exception.BusinessException;
import net.wanji.common.core.domain.SimpleSelect;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wj
 * @since 2023-06-29
 */
public interface TjCaseService extends IService<TjCase> {

    /**
     * 测试用例列表页初始化
     *
     * @return
     */
    Map<String, List<SimpleSelect>> init();

    /**
     * 测试用例编辑页初始化
     *
     * @return
     */
    Map<String, List<PartConfigSelect>> initEdit(Integer caseId);

    /**
     * 测试用例编辑页新UI初始化
     *
     * @return
     */
    List<TjCasePartRoleVo> initEditNew(Integer caseId) throws BusinessException;

    /**
     * 分页列表
     *
     * @param caseQueryDto
     * @return
     * @throws BusinessException
     */
    List<CasePageVo> pageList(CaseQueryDto caseQueryDto, String selectType);

    List<CasePageVo> pageListByIds(List<Integer> ids, Integer treeId);

    Long takeExpense(List<Integer> ids);

    /**
     * 查询用例详情
     *
     * @param caseId
     * @return
     */
    CaseDetailVo selectCaseDetail(Integer caseId);

    /**
     * 获取用例详情
     *
     * @param caseId
     * @return
     */
    CaseInfoBo getCaseDetail(Integer caseId) throws BusinessException;


    /**
     * 获取配置信息
     *
     * @param caseId
     * @param caseTrajectoryDetailBo
     * @param deviceConfig
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    List<PartConfigSelect> getConfigSelect(Integer caseId, CaseTrajectoryDetailBo caseTrajectoryDetailBo, boolean deviceConfig);

    /**
     * 获取配置信息
     *
     * @param caseId
     * @param sceneTrajectoryBo
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    List<PartConfigSelect> getConfigSelect(Integer caseId, SceneTrajectoryBo sceneTrajectoryBo);

    /**
     * 保存测试用例
     *
     * @param tjCaseDto
     * @return
     */
    boolean saveCase(TjCaseDto tjCaseDto) throws BusinessException;


    /**
     * 修改状态
     *
     * @param caseId
     * @return
     */
    boolean updateStatus(Integer caseId) throws BusinessException;

    /**
     * 修改状态
     *
     * @param caseIds
     * @param action  1:启用 2:禁用
     * @return
     */
    boolean batchUpdateStatus(List<Integer> caseIds, Integer action) throws BusinessException;


    /**
     * 轨迹回放
     *
     * @param id            用例ID
     * @param participantId 参与者ID
     * @param action        操作 PlaybackAction
     * @throws BusinessException
     * @throws IOException
     */
    void playback(Integer id, String participantId, int action) throws BusinessException, IOException;


    /**
     * 删除用例
     *
     * @param
     * @return
     */
    boolean batchDelete(List<Integer> caseIds) throws BusinessException;


    /**
     * 查询用例仿真详情
     *
     * @param caseId
     * @return
     */
    CaseVerificationVo getSimulationDetail(Integer caseId) throws BusinessException;

    /**
     * 获取配置详情
     *
     * @param caseId
     * @return
     * @throws BusinessException
     */
    List<PartConfigSelect> getConfigDetail(Integer caseId) throws BusinessException, InterruptedException, ExecutionException;

    List<TjCasePartRoleVo> getConfigDetailNew(Integer caseId) throws BusinessException;

    /**
     * 删除实车测试记录
     *
     * @param recordId
     * @return
     * @throws BusinessException
     */
    boolean deleteRecord(Integer recordId) throws BusinessException;

    List<TjCaseOp> selectCaseOp(Integer taskId);
}
