package net.wanji.business.trajectory;

import net.wanji.business.trajectory.TaskChain.TaskItem;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: guanyuduo
 * @date: 2024/1/19 16:02
 * @descriptoin:
 */

public class TaskChainManager {

    private static ConcurrentHashMap<String, TaskChain> taskChainMap = new ConcurrentHashMap<>(8);

    public static boolean containsKey(String taskChainNumber) {
        return taskChainMap.containsKey(taskChainNumber);
    }

    public static void put(String taskChainNumber, TaskChain taskChain) {
        taskChainMap.put(taskChainNumber, taskChain);
    }

    public static TaskChain get(String taskChainNumber) {
        return taskChainMap.get(taskChainNumber);
    }

    public static boolean remove(String taskChainNumber) {
        return taskChainMap.remove(taskChainNumber) != null;
    }


}
