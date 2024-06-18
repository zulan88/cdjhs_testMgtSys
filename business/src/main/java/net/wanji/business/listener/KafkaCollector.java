package net.wanji.business.listener;

import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: guanyuduo
 * @date: 2023/12/8 15:08
 * @descriptoin:
 */
@Component
public class KafkaCollector {

    private static final Logger log = LoggerFactory.getLogger("kafka");

    public static Map<String, Map<Integer, List<List<ClientSimulationTrajectoryDto>>>> collectorMap = new ConcurrentHashMap<>();


    public boolean collector(String key, Integer caseId, List<ClientSimulationTrajectoryDto> data) {
        // 没有key
        if (!collectorMap.containsKey(key)) {
            Map<Integer, List<List<ClientSimulationTrajectoryDto>>> caseTrajectoryMap = new HashMap<>();
            List<List<ClientSimulationTrajectoryDto>> trajectoriesList = new ArrayList<>();
            trajectoriesList.add(data);
            caseTrajectoryMap.put(caseId, trajectoriesList);
            collectorMap.put(key, caseTrajectoryMap);
            return true;
        }
        Map<Integer, List<List<ClientSimulationTrajectoryDto>>> caseTrajectoryMap = collectorMap.get(key);
        // 有key，没有用例
        if (!caseTrajectoryMap.containsKey(caseId)) {
            List<List<ClientSimulationTrajectoryDto>> trajectoriesList = new ArrayList<>();
            trajectoriesList.add(data);
            caseTrajectoryMap.put(caseId, trajectoriesList);
            collectorMap.put(key, caseTrajectoryMap);
            return true;
        }
        // 有key，有用例
        List<List<ClientSimulationTrajectoryDto>> trajectories = caseTrajectoryMap.get(caseId);
        trajectories.add(data);
        if (System.currentTimeMillis() / 1000 % 20 == 0) {
            log.info("{} - {} ： {}", key, caseId, trajectories.size());
        }
        collectorMap.put(key, caseTrajectoryMap);
        return true;
    }

    public int getSize(String key) {
        if (collectorMap.containsKey(key)) {
            int size = 0;
            for (Map.Entry<Integer, List<List<ClientSimulationTrajectoryDto>>> entry : collectorMap.get(key).entrySet()) {
                size += entry.getValue().size();
            }
            return size;
        }
        return 0;
    }

    public int getSize(String key, Integer caseId) {
        return collectorMap.containsKey(key)
                ? (collectorMap.get(key).containsKey(caseId) ? collectorMap.get(key).get(caseId).size() : 0)
                : 0;
    }

    public List<List<ClientSimulationTrajectoryDto>> take(String key, Integer caseId) {
        return collectorMap.containsKey(key)
                ? new ArrayList<>((collectorMap.get(key).getOrDefault(caseId, null)))
                : new ArrayList<>();
    }

    public void remove(String key, Integer caseId) {
        if (!collectorMap.containsKey(key)) {
            return;
        }
        if (ObjectUtils.isEmpty(caseId)) {
            collectorMap.remove(key);
            return;
        }
        Map<Integer, List<List<ClientSimulationTrajectoryDto>>> caseTrajectoryMap = collectorMap.get(key);
        if (!ObjectUtils.isEmpty(caseId) && caseTrajectoryMap.containsKey(caseId)) {
            collectorMap.remove(key);
        }
    }

}
