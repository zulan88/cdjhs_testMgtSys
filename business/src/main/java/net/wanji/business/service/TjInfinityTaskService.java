package net.wanji.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.domain.bo.SaveCustomIndexWeightBo;
import net.wanji.business.domain.bo.SaveCustomScenarioWeightBo;
import net.wanji.business.domain.dto.TaskDto;
import net.wanji.business.domain.vo.CasePageVo;
import net.wanji.business.domain.vo.PlatformSSDto;
import net.wanji.business.domain.vo.task.infinity.InfinityTaskInitVo;
import net.wanji.business.domain.vo.task.infinity.InfinityTaskPreparedVo;
import net.wanji.business.entity.infity.TjInfinityTask;
import net.wanji.business.exception.BusinessException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author hcy
 * @version 1.0
 * @interfaceName TjInfinityTaskService
 * @description TODO
 * @date 2024/3/11 13:11
 **/
@Service
public interface TjInfinityTaskService extends IService<TjInfinityTask> {


    /**
     * 任务数量统计
     *
     * @return
     */
    Map<String, Long> selectCount(TaskDto taskDto);

    /**
     * 页面列表
     *
     * @param in
     * @return
     */
    List<Map<String, Object>> pageList(TaskDto in);

    /**
     * 查询任务用例
     *
     * @param taskId
     * @return
     */
    List<CasePageVo> getTaskCaseList(Integer taskId);

    /**
     * 测试报告的跳转外链
     *
     * @param request
     * @return
     */
    String getTestReportOuterChain(HttpServletRequest request);

    int saveTask(Map<String, Object> task) throws BusinessException;


    void saveCustomScenarioWeight(SaveCustomScenarioWeightBo saveCustomScenarioWeightBo);

    void saveCustomIndexWeight(SaveCustomIndexWeightBo saveCustomIndexWeightBo);

    int updateTaskStatus(String status, int id);

    /**
     * 运行任务初始化
     * @param taskId
     */
    InfinityTaskInitVo init(Integer taskId) throws BusinessException;

    /**
     * 准备（状态检查）
     * @param taskId
     * @return
     */
    InfinityTaskPreparedVo prepare(Integer taskId) throws BusinessException;

    /**
     * 任务预开始
     * @param taskId
     * @return
     */
    boolean preStart(Integer taskId);

    /**
     *
     * @param platformSSDto
     * @return
     * @throws BusinessException
     */
    boolean startStop(PlatformSSDto platformSSDto) throws BusinessException;

    /**
     * 删除
     * @param id 任务id
     * @return
     */
    boolean delete(Integer id);
}
