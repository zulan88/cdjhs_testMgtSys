package net.wanji.business.service;

import com.alibaba.fastjson.JSONObject;
import net.wanji.business.domain.TrafficFlow;
import net.wanji.business.domain.bo.SaveCustomIndexWeightBo;
import net.wanji.business.domain.bo.SaveCustomScenarioWeightBo;
import net.wanji.business.domain.bo.SaveTaskSchemeBo;
import net.wanji.business.domain.dto.device.DeviceReadyStateParam;
import net.wanji.business.domain.dto.device.TaskSaveDto;
import net.wanji.business.domain.param.CaseRuleControl;
import net.wanji.business.domain.param.CaseTrajectoryParam;
import net.wanji.business.domain.param.TessParam;
import net.wanji.business.domain.param.TessTrackParam;
import net.wanji.business.domain.tess.TessStartReq;
import net.wanji.business.domain.tess.TessStopReq;
import net.wanji.business.domain.vo.IndexCustomWeightVo;
import net.wanji.business.domain.vo.IndexWeightDetailsVo;
import net.wanji.business.domain.vo.SceneIndexSchemeVo;
import net.wanji.business.domain.vo.SceneWeightDetailsVo;
import net.wanji.business.exercise.dto.evaluation.EvaluationOutputReq;
import net.wanji.business.exercise.dto.jidaevaluation.evaluation.EvaluationCreateDto;
import net.wanji.business.exercise.dto.jidaevaluation.network.NetworkCreateDto;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/9 15:25
 * @Descriptoin:
 */

public interface RestService {

    // ========================= TESSNG =========================

    /**
     * 启动tess服务
     * @param ip
     * @param port
     * @param tessParam
     * @return
     */
    int startServer(String ip, Integer port, TessParam tessParam);

    int startTessng(String ip, Integer port, TessStartReq tessStartReq);

    boolean stopTessng(String ip, Integer port, TessStopReq tessStopReq);

    String queryTessStatus(String ip, Integer port, String taskId, String status, Integer count);

    /**
     * 创建路网记录
     */
    String createNetwork(NetworkCreateDto networkCreateDto);

    /**
     * 创建评价记录
     */
    String createEvaluation(EvaluationCreateDto evaluationCreateDto);

    /**
     * 启动SV云控车服务
     */
    int takeSvServer(String ip, Integer port, TessTrackParam tessTrackParam, int type);


    /**
     * 获取发车点信息
     * @param ip
     * @param port
     * @param mapId
     * @return
     */
    List<TrafficFlow> getTrafficFlow(String ip, String port, Integer mapId);

    /**
     * 多场景路径规划
     * @param ip
     * @param port
     * @param params
     * @return
     */
    boolean startRoutingPlan(String ip, Integer port, Map<String, Object> params);

    /**
     * 停止无限里程tess服务
     * @param ip
     * @param port
     * @return
     */
    boolean stopTessNg(String ip, String port, String dataChannel, int type);



    /**
     * 查询设备准备状态
     * @param deviceReadyStateParam
     * @return
     */
    boolean selectDeviceReadyState(DeviceReadyStateParam deviceReadyStateParam);

    /**
     * 请求算法输出场景评分
     */
    String getEvaluationOutput(EvaluationOutputReq param);


    /**
     * 获取济达场景&指标方案列表
     * @param taskSaveDto
     * @return {@link java.util.List<net.wanji.business.domain.vo.SceneIndexSchemeVo>}
     * @author liruitao
     * @date 2023-11-14
     */
    List<SceneIndexSchemeVo> getSceneIndexSchemeList(TaskSaveDto taskSaveDto);

    /**
     * 获取场景权重详情
     * @param id
     * @return {@link java.lang.String}
     * @author liruitao
     * @date 2023-11-14
     */
    List<SceneWeightDetailsVo> getSceneWeightDetailsById(String id);

    /**
     * 获取指标权重详情
     * @param id
     * @return {@link String}
     * @author liruitao
     * @date 2023-11-14
     */
    List<IndexWeightDetailsVo> getIndexWeightDetailsById(String id);

    /**
     * 获取自定义指标权重详情
     * @param
     * @return {@link List<IndexCustomWeightVo>}
     * @author liruitao
     * @date 2023-11-14
     */
    List<IndexCustomWeightVo> getValuationIndexCustomWeight();

    /**
     * 创建任务和方案关联
     * @param saveTaskSchemeBo
     * @return {@link Map}
     * @author liruitao
     * @date 2023-11-15
     */
    Map<String, String> saveTaskScheme(SaveTaskSchemeBo saveTaskSchemeBo);

    /**
     * 自定义-场景权重创建
     * @param saveCustomScenarioWeightBo
     * @return {@link Map}
     * @author liruitao
     * @date 2023-11-15
     */
    Map<String, String> saveCustomScenarioWeight(SaveCustomScenarioWeightBo saveCustomScenarioWeightBo);

    /**
     * 自定义-指标权重创建
     * @param saveCustomIndexWeightBo
     * @return {@link String}
     * @author liruitao
     * @date 2023-11-16
     */
    Map<String, String> saveCustomIndexWeight(SaveCustomIndexWeightBo saveCustomIndexWeightBo);

    /**
     * 下载测试报告
     * @return {@link String}
     * @author liruitao
     * @date 2023-11-22
     */
    void downloadTestReport(HttpServletResponse response, int taskId);


    // ========================= 主控 =========================
    /**
     * 向主控发送规则
     * @param caseRuleControl
     * @return
     */
    boolean sendRuleUrl(CaseRuleControl caseRuleControl);

    /**
     * 向主控发送用例轨迹信息
     * @param param
     * @return
     */
    boolean sendCaseTrajectoryInfo(CaseTrajectoryParam param);

    /**
     * 手动终止
     * @param taskId
     * @param caseId
     * @return
     */
    boolean sendManualTermination(Integer taskId, Integer caseId, Integer testMode);

    // ========================= 设备 =========================
    /**
     * 查询设备信息
     * @param ip
     * @param method
     * @return
     */
    Map<String, Object> searchDeviceInfo(String ip, HttpMethod method);

    /**
     * 获取评价结果
     * @param taskId
     * @return
     */
    JSONObject getCarTestResult(Integer taskId);

}
