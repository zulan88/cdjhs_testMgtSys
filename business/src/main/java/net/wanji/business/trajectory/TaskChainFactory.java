package net.wanji.business.trajectory;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.wanji.business.entity.TjTaskCase;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.service.TjTaskCaseService;
import net.wanji.business.trajectory.TaskChain.TaskItem;
import net.wanji.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.NoSuchElementException;

/**
 * @author: guanyuduo
 * @date: 2024/1/19 15:59
 * @descriptoin:
 */
@Component
public class TaskChainFactory {

    private static final Logger log = LoggerFactory.getLogger("business");

    @Autowired
    @Lazy
    private TjTaskCaseService taskCaseService;

    /**
     * 创建任务链
     * @param taskChainNumber
     * @param taskId
     * @return
     */
    public TaskChainFactory createTaskChain(String taskChainNumber, String createdBy, Integer taskId) {
        TaskChain taskChain = new TaskChain(taskChainNumber, createdBy,
                taskCaseService.list(new LambdaQueryWrapper<TjTaskCase>().eq(TjTaskCase::getTaskId, taskId)
                        .orderByAsc(TjTaskCase::getSort)));
        TaskChainManager.put(taskChainNumber, taskChain);
        log.info("创建任务链{} -> 任务数{} : {}", taskChainNumber, taskChain.getChainSize(), JSONObject.toJSONString(taskChain));
        return this;
    }

    /**
     * 确认状态
     * @param taskChainNumber
     */
    public void confirmState(String taskChainNumber) throws BusinessException {
        if (Stage.WAITING.getCode() != getCurrentNodeStage(taskChainNumber)) {
            log.error("状态转换失败：{}", printStageWithDuration(taskChainNumber));
            throw new BusinessException("测试等待中 -> 状态确认中");
        }
        getTaskChain(taskChainNumber).confirm();
        printStage(taskChainNumber);
    }

    public void selectState(String taskChainNumber) throws BusinessException {
        TaskChain taskChain = getTaskChain(taskChainNumber);
        TaskItem currentNode = getCurrentNode(taskChainNumber);
        taskCaseService.getStatus(currentNode.taskCase, taskChain.getCreatedBy(), true);
        // 查询状态
        while (!this.isConfirmed(taskChainNumber)) {
            if (System.currentTimeMillis() - currentNode.stageStartTime > 1000 * 60 * 5) {
                log.error("任务链{}状态查询超时：{}", taskChainNumber, printStageWithDuration(taskChainNumber));
                throw new BusinessException("任务链状态查询超时");
            }
            try {
                Thread.sleep(500);
                taskCaseService.getStatus(currentNode.taskCase, taskChain.getCreatedBy(), false);
            } catch (InterruptedException v1) {
                printStage(taskChainNumber);
                log.error("任务链{}状态查询失败：{}", taskChainNumber, v1.getMessage());
                // todo 不可无限循环
            }
        }
        // 准备
        taskCaseService.prepare(currentNode.taskCase, taskChain.getCreatedBy());
        // 开始
        taskCaseService.controlTask(currentNode.taskCase.getTaskId(), currentNode.taskCase.getId(), 1,
                taskChain.getCreatedBy(), taskChainNumber);
    }

    /**
     * 确认完成
     * @param taskChainNumber
     */
    public void confirmComplete(String taskChainNumber) throws BusinessException {
        if (Stage.CONFIRMING.getCode() != getCurrentNodeStage(taskChainNumber)) {
            log.error("状态确认完成 转换失败：{}", printStageWithDuration(taskChainNumber));
            throw new BusinessException("状态确认完成 转换失败");
        }
        getTaskChain(taskChainNumber).stageComplete();
        printStage(taskChainNumber);
    }

    /**
     * 是否确认完成
     * @param taskChainNumber
     * @return
     */
    public boolean isConfirmed(String taskChainNumber) {
        return Stage.CONFIRMING.getCode() == getCurrentNodeStage(taskChainNumber) && isCompletedStage(taskChainNumber);
    }

    /**
     * 准备
     * @param taskChainNumber
     */
    public void prepare(String taskChainNumber) throws BusinessException {
        if (!isConfirmed(taskChainNumber)) {
            log.error("状态确认完成 -> 测试准备中 状态转换失败：{}", printStageWithDuration(taskChainNumber));
            throw new BusinessException("状态确认完成 -> 测试准备中 状态转换失败");
        }
        getTaskChain(taskChainNumber).prepare();
        printStage(taskChainNumber);
    }

    /**
     * 准备完成
     * @param taskChainNumber
     */
    public void prepareComplete(String taskChainNumber) throws BusinessException {
        if (Stage.PREPARING.getCode() != getCurrentNodeStage(taskChainNumber)) {
            log.error("准备完成 转换失败：{}", printStageWithDuration(taskChainNumber));
            throw new BusinessException("准备完成 转换失败");
        }
        getTaskChain(taskChainNumber).stageComplete();
        printStage(taskChainNumber);
    }

    /**
     * 是否准备完成
     * @param taskChainNumber
     * @return
     */
    public boolean isPrepared(String taskChainNumber) {
        return Stage.PREPARING.getCode() == getCurrentNodeStage(taskChainNumber) && isCompletedStage(taskChainNumber);
    }

    /**
     * 开始
     * @param taskChainNumber
     * @throws BusinessException
     */
    public void start(String taskChainNumber) throws BusinessException {
        if (!this.isPrepared(taskChainNumber)) {
            log.error("开始测试 转换失败：{}", printStageWithDuration(taskChainNumber));
            throw new BusinessException("开始测试 转换失败");
        }
        getTaskChain(taskChainNumber).start();
        printStage(taskChainNumber);
    }

    /**
     * 是否存在下一个任务用例
     * @param taskChainNumber
     * @return
     */
    public boolean hasNext(String taskChainNumber) {
        return getTaskChain(taskChainNumber).hasNext();
    }

    /**
     * 下一步
     * @param taskChainNumber
     * @return
     * @throws BusinessException
     */
    public TaskItem next(String taskChainNumber) throws BusinessException {
        if (!hasNext(taskChainNumber)) {
            log.info("任务链{} -> 测试完成", taskChainNumber);
            destroy(taskChainNumber);
            return null;
        }
        return getNextNode(taskChainNumber);
    }


    /**
     * 销毁任务链
     * @param taskChainNumber
     * @return
     */
    public boolean destroy(String taskChainNumber) {
        log.info("销毁任务链{}", taskChainNumber);
        return TaskChainManager.remove(taskChainNumber);
    }

    /**
     * 获取当前节点所处阶段是否完成
     * @param taskChainNumber
     * @return
     */
    public boolean isCompletedStage(String taskChainNumber) {
        return hasChain(taskChainNumber) && getCurrentNode(taskChainNumber).stageState;
    }


    public void printStage(String taskChainNumber) {
        log.info("任务链{} 第{}节点 当前阶段：{} 是否完成：{}", taskChainNumber, getCurrentNodeSort(taskChainNumber),
                Stage.getName(getCurrentNodeStage(taskChainNumber)), getCurrentNodeStageState(taskChainNumber));
    }

    public String printStageWithDuration(String taskChainNumber) {
        return StringUtils.format("任务链{} 第{}节点 当前阶段：{} 是否完成：{} 持续时长：{}ms", taskChainNumber, getCurrentNodeSort(taskChainNumber),
                Stage.getName(getCurrentNodeStage(taskChainNumber)), getCurrentNodeStageState(taskChainNumber),
                getStageDuration(taskChainNumber));
    }

    /**
     * 计算当前阶段持续时长
     * @param taskChainNumber
     * @return
     */
    public long getStageDuration(String taskChainNumber) {
        return System.currentTimeMillis() - getCurrentNode(taskChainNumber).stageStartTime;
    }

    /**
     * 任务链是否存在
     * @param taskChainNumber
     * @return
     */
    public boolean hasChain(String taskChainNumber) {
        return TaskChainManager.containsKey(taskChainNumber);
    }

    /**
     * 获取任务链
     * @param taskChainNumber
     * @return
     */
    public TaskChain getTaskChain(String taskChainNumber) {
        return TaskChainManager.get(taskChainNumber);
    }

    /**
     * 获取当前节点
     * @param taskChainNumber
     * @return
     */
    public TaskItem getCurrentNode(String taskChainNumber) {
        return hasChain(taskChainNumber)
                ? getTaskChain(taskChainNumber).currentNode()
                : null;
    }

    /**
     * 获取当前节点排序
     * @param taskChainNumber
     * @return
     */
    public Integer getCurrentNodeSort(String taskChainNumber) {
        return hasChain(taskChainNumber)
                ? getCurrentNode(taskChainNumber).sort
                : null;
    }

    /**
     * 获取当前节点用例信息
     * @param taskChainNumber
     * @return
     */
    public TjTaskCase getCurrentNodeCase(String taskChainNumber) {
        return hasChain(taskChainNumber)
                ? getCurrentNode(taskChainNumber).taskCase
                : null;
    }

    /**
     * 获取当前节点ID
     * @param taskChainNumber
     * @return
     */
    public Integer getCurrentNodeId(String taskChainNumber) {
        return hasChain(taskChainNumber)
                ? getCurrentNode(taskChainNumber).taskCase.getId()
                : null;
    }

    /**
     * 获取当前节点所处阶段
     * @param taskChainNumber
     * @return
     */
    public Integer getCurrentNodeStage(String taskChainNumber) {
        return hasChain(taskChainNumber)
                ? getCurrentNode(taskChainNumber).stage
                : -1;
    }

    /**
     * 获取当前节点所处阶段是否完成
     * @param taskChainNumber
     * @return
     */
    public boolean getCurrentNodeStageState(String taskChainNumber) {
        return hasChain(taskChainNumber) && getCurrentNode(taskChainNumber).stageState;
    }

    public TaskItem getNextNode(String taskChainNumber) {
        return hasChain(taskChainNumber)
                ? getTaskChain(taskChainNumber).next()
                : null;
    }



    public enum Stage {
        WAITING(0, "测试等待中"),
        CONFIRMING(1, "状态确认中"),
        PREPARING(2, "测试准备中"),
        RUNNING(3, "测试执行中"),
        END(4, "结束");

        private int code;
        private String name;

        Stage(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public static String getName(int code) {
            for (Stage stage : Stage.values()) {
                if (stage.code == code) {
                    return stage.getName();
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }
}
