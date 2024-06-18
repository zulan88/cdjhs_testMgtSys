package net.wanji.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.domain.param.TessTrackParam;
import net.wanji.business.domain.vo.CaseRealTestVo;
import net.wanji.business.domain.vo.CaseTreeVo;
import net.wanji.business.domain.vo.CommunicationDelayVo;
import net.wanji.business.domain.vo.RealTestResultVo;
import net.wanji.business.domain.vo.TaskCaseVerificationPageVo;
import net.wanji.business.domain.vo.TaskReportVo;
import net.wanji.business.entity.TjScenelib;
import net.wanji.business.entity.TjTaskCase;
import net.wanji.business.exception.BusinessException;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author guowenhao
 * @description 针对表【tj_task_case(测试任务-用例详情表)】的数据库操作Service
 * @createDate 2023-08-31 17:39:16
 */
public interface TjTaskCaseService extends IService<TjTaskCase> {

    /**
     * 获取任务用例中参与者状态
     *
     * @param param
     * @param user 触发人
     * @param hand 是否手动
     * @return
     */
    TaskCaseVerificationPageVo getStatus(TjTaskCase param, String user, boolean hand) throws BusinessException;

    /**
     * 准备（开始倒计时）
     *
     * @param taskId
     * @return
     */
    CaseRealTestVo prepare(TjTaskCase param, String user) throws BusinessException;


    /**
     * 开始/结束/回放
     *
     * @param taskId
     * @param taskCaseId
     * @param action
     * @param taskChainNumber 任务链编号
     * @return
     */
    CaseRealTestVo controlTask(Integer taskId, Integer taskCaseId, Integer action, String user, String taskChainNumber) throws BusinessException;

    CaseRealTestVo getTaskInfo(Integer taskId) throws BusinessException;

    /**
     * 开始/结束
     *
     * @param taskId
     * @param caseId
     * @param action
     * @param taskEnd
     * @param context
     * @return
     * @throws BusinessException
     * @throws IOException
     */
    CaseRealTestVo caseStartEnd(Integer taskId, Integer caseId, Integer action,
                                boolean taskEnd, Map<String, Object> context, Integer testModel) throws BusinessException;

    /**
     * 回放
     *
     * @param taskId   任务ID
     * @param caseId   任务用例ID
     * @param action   1：开始回放 2：暂停；3：继续；4：结束回放
     * @param recordId 测试记录ID
     * @return
     * @throws BusinessException
     * @throws IOException
     */
    void playback(Integer taskId, Integer caseId, Integer recordId,
        Integer action) throws BusinessException, IOException;

    void playbackTW(Integer taskId, Integer caseId, String topic) throws BusinessException, IOException;

    /**
     * 获取评估结果
     * @param taskId
     * @param id
     * @return
     * @throws BusinessException
     */
    Object getEvaluation(Integer taskId, Integer id) throws BusinessException;

    /**
     * 获取实时测试结果
     * @param taskId 任务ID
     * @param id 任务用例ID
     * @return
     * @throws BusinessException
     */
    List<RealTestResultVo> getResult(Integer taskId, Integer id) throws BusinessException;

    /**
     * 通信时延
     *
     * @param taskId 任务ID
     * @param id 任务用例ID
     * @return
     */
    CommunicationDelayVo communicationDelayVo(Integer taskId, Integer id) throws BusinessException;

    List<TaskReportVo> getReport(Integer taskId, Integer taskCaseId);

    /**
     * 停止任务
     * @param taskId
     * @param taskCaseId
     * @throws BusinessException
     */
    void stop(Integer taskId, Integer taskCaseId, String user) throws BusinessException;

    /**
     * 手动终止任务
     * @param taskId
     * @param taskCaseId
     * @throws BusinessException
     */
    void manualTermination(Integer taskId, Integer taskCaseId, Integer testModel) throws BusinessException;

    void stopSvTrack(Integer caseId, TessTrackParam tessTrackParam);

    List<CaseTreeVo> selectTree(String type, Integer taskId);

    boolean addTaskCase(@NotNull Integer taskId, @NotNull List<Integer> caseIds) throws BusinessException ;

    boolean deleteTaskCase(@NotNull Integer taskId, @NotNull List<Integer> caseIds);

    TaskCaseVerificationPageVo getStatustw(TjTaskCase param) throws BusinessException;

    void twStop(Integer taskId, Integer caseId, String status);

    TjScenelib tasktolib(Integer taskId, Integer caseId);

}
