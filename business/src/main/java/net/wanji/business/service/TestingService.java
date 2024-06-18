package net.wanji.business.service;

import net.wanji.business.domain.param.TessTrackParam;
import net.wanji.business.domain.vo.CaseTestPrepareVo;
import net.wanji.business.domain.vo.CaseTestStartVo;
import net.wanji.business.domain.vo.CommunicationDelayVo;
import net.wanji.business.domain.vo.RealTestResultVo;
import net.wanji.business.domain.vo.RealVehicleVerificationPageVo;
import net.wanji.business.exception.BusinessException;

import java.io.IOException;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/24 9:57
 * @Descriptoin:
 */

public interface TestingService {

    /**
     * 获取用例中参与者状态
     *
     * @param caseId
     * @return
     */
    RealVehicleVerificationPageVo getStatus(Integer caseId, boolean hand) throws BusinessException;

    /**
     * 准备（开始倒计时）
     *
     * @param caseId
     * @return
     */
    CaseTestPrepareVo prepare(Integer caseId) throws BusinessException;

    /**
     * 开始（实车试验倒计时结束）
     * @param caseId
     * @return
     * @throws BusinessException
     */
    CaseTestStartVo controlTask(Integer caseId) throws BusinessException;

    /**
     * 开始（与主控交互）
     *
     * @param caseId
     * @param action
     * @return
     */
    CaseTestStartVo start(Integer caseId, Integer action, String username) throws BusinessException, IOException;

    /**
     * 结束（与主控交互）
     * @param caseId
     * @param action
     * @throws BusinessException
     */
    void end(Integer caseId, int action, String username) throws BusinessException;



    CaseTestStartVo hjktest(Integer caseId) throws BusinessException;

    void stop(Integer caseId) throws BusinessException;

    void playback(Integer recordId, Integer action) throws BusinessException, IOException;

    /**
     * 获取实车验证结果
     *
     * @param recordId
     * @return
     */
    RealTestResultVo getResult(Integer recordId) throws BusinessException;

    /**
     * 通信时延
     *
     * @param recordId
     * @return
     */
    CommunicationDelayVo communicationDelayVo(Integer recordId);

    void stopSvTrack(Integer caseId, TessTrackParam tessTrackParam) throws BusinessException;


    /**
     * 手动终止任务
     * @param caseId
     * @throws BusinessException
     */
    void manualTermination(Integer caseId, Integer testModel) throws BusinessException;
}
