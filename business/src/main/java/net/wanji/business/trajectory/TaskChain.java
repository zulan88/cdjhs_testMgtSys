package net.wanji.business.trajectory;

import net.wanji.business.entity.TjTaskCase;
import net.wanji.business.trajectory.TaskChain.TaskItem;
import net.wanji.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * @author: guanyuduo
 * @date: 2024/1/19 15:38
 * @descriptoin:
 */

public class TaskChain {

    private String taskChainNumber;

    private String createdBy;

    private LinkedList<TaskItem> taskNodes;

    public TaskChain(String taskChainNumber, String createdBy, List<TjTaskCase> taskCases) {
        this.taskChainNumber = taskChainNumber;
        this.createdBy = createdBy;
        this.taskNodes = taskCases.stream().map(TaskItem::new).collect(Collectors.toCollection(LinkedList::new));
    }

    public void confirm() {
        this.taskNodes.getFirst().stage = 1;
        this.taskNodes.getFirst().stageStartTime = System.currentTimeMillis();
        this.taskNodes.getFirst().stageState = false;
    }

    public void prepare() {
        this.taskNodes.getFirst().stage = 2;
        this.taskNodes.getFirst().stageStartTime = System.currentTimeMillis();
        this.taskNodes.getFirst().stageState = false;
    }

    public void start() {
        this.taskNodes.getFirst().stage = 3;
        this.taskNodes.getFirst().stageStartTime = System.currentTimeMillis();
        this.taskNodes.getFirst().stageState = false;
    }

    public boolean hasNext() {
        return this.taskNodes.size() > 1;
    }

    public TaskItem next() {
        return this.taskNodes.pollFirst();
    }

    public void stageComplete() {
        this.taskNodes.getFirst().stageState = true;
    }

    public TaskItem currentNode() {
        return this.taskNodes.getFirst();
    }

    public String getTaskChainNumber() {
        return taskChainNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public int getChainSize() {
        return this.taskNodes.size();
    }

    public static class TaskItem {
        /**
         * 阶段 0：等待中 1：状态确认中 2：准备中 3：执行中
         */
        int stage;

        /**
         * 阶段状态
         */
        boolean stageState;

        /**
         * 任务排序
         */
        int sort;

        /**
         * 阶段开始时间
         */
        long stageStartTime;

        /**
         * 任务内容
         */
        TjTaskCase taskCase;

        TaskItem(TjTaskCase taskCase) {
            this.taskCase = taskCase;
            this.sort = taskCase.getSort();
        }

        @Override
        public String toString() {
            return "{" +
                    "stage: " + stage +
                    ", stageState: " + stageState +
                    ", sort: " + sort +
                    ", stageStartTime: " + stageStartTime +
                    ", taskCase: " + taskCase +
                    '}';
        }

    }
}
