package net.wanji.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.domain.bo.SaveCustomIndexWeightBo;
import net.wanji.business.domain.bo.SaveCustomScenarioWeightBo;
import net.wanji.business.domain.bo.TaskBo;
import net.wanji.business.domain.dto.RoutingPlanDto;
import net.wanji.business.domain.dto.TaskDto;
import net.wanji.business.domain.dto.device.TaskSaveDto;
import net.wanji.business.domain.vo.CasePageVo;
import net.wanji.business.domain.vo.TaskListVo;
import net.wanji.business.entity.TjTask;
import net.wanji.business.exception.BusinessException;
import net.wanji.common.core.domain.SimpleSelect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author guowenhao
* @description 针对表【tj_task(测试任务表)】的数据库操作Service
* @createDate 2023-08-31 17:39:16
*/
public interface TjTaskService extends IService<TjTask> {


    /**
     * 列表页初始化
     * @return
     */
    Map<String, List<SimpleSelect>> initPage();

    Map<String, List<SimpleSelect>> initPageOp();

    /**
     * 任务数量统计
     * @return
     */
    Map<String, Long> selectCount(TaskDto taskDto);
    /**
     * 页面列表
     * @param in
     * @return
     */
    List<TaskListVo> pageList(TaskDto in);

    /**
     * 查询任务用例
     * @param taskId
     * @return
     */
    List<CasePageVo> getTaskCaseList(Integer taskId);

    /**
     * 节点数据初始化
     * @return
     */
    Object initProcessed(Integer processNode);

    /**
     * 任务节点信息
     * @param taskSaveDto
     * @return
     */
    Object processedInfo(TaskSaveDto taskSaveDto) throws BusinessException;

    /**
     * 保存创建任务
     * @param in
     * @return
     */
    int saveTask(TaskBo in) throws BusinessException;

    /**
     * 路径规划
     * @param routingPlanDto
     * @return
     * @throws BusinessException
     */
    boolean routingPlan(RoutingPlanDto routingPlanDto) throws BusinessException;

    TjTask hasUnSubmitTask();

    /**
     * 导出
     * @param response
     * @param taskId
     */
    void export(HttpServletResponse response, Integer taskId) throws IOException;

    /**
     * 新增自定义场景权重信息
     * @param saveCustomScenarioWeightBo
     * @return
     * @author liruitao
     * @date 2023-11-15
     */
    void saveCustomScenarioWeight(SaveCustomScenarioWeightBo saveCustomScenarioWeightBo);

    /**
     * 新增自定义指标权重信息
     * @param saveCustomIndexWeightBo
     * @return
     * @author liruitao
     * @date 2023-11-16
     */
    void saveCustomIndexWeight(SaveCustomIndexWeightBo saveCustomIndexWeightBo);


    String getTestReportOuterChain(HttpServletRequest request);

    TjTask selectOneById(Integer id);

    boolean taskDelete(Integer id);
}
