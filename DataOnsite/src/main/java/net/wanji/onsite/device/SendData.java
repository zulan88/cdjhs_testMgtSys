package net.wanji.onsite.device;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import net.wanji.common.common.SimulationMessage;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.constant.Constants;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static net.wanji.framework.datasource.DynamicDataSourceContextHolder.log;

@Component
public class SendData {

    @Autowired
    private RedisCache redisCache;

    @Value("${masterControl.manualTermination}")
    private String sendManualTerminationUrl;

    @Autowired
    private RestTemplate restTemplate;

    private Map<String, List<SimulationTrajectoryDto>> map = new ConcurrentHashMap<>();

    private Map<String, Boolean> flag = new ConcurrentHashMap<>();

    private List<SimulationTrajectoryDto> readOriTrajectory(String filePath) {
//        filePath = WanjiConfig.getProfile() + StringUtils.substringAfter(filePath, Constants.RESOURCE_PREFIX);
        List<SimulationTrajectoryDto> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(JSONObject.parseObject(line, SimulationTrajectoryDto.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SimulationTrajectoryDto> mainTrajectory(String fileName, String id) {
        List<SimulationTrajectoryDto> participantTrajectories = null;
        participantTrajectories = readOriTrajectory(fileName);
        participantTrajectories = participantTrajectories.stream()
                .filter(item -> !ObjectUtils.isEmpty(item.getValue())
                        && item.getValue().stream().anyMatch(p -> id.equals(p.getId())))
                .peek(s -> s.setValue(s.getValue().stream().filter(p -> !id.equals(p.getId()))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());

        return participantTrajectories;
    }

    public void prepareData(String channel, String filePath) {
        System.out.println("onsite开始准备轨迹");
        List<SimulationTrajectoryDto> list = mainTrajectory(filePath, "1");
        if (list.size() > 0) {
            map.put(channel, list);
            flag.put(channel, true);
        }
    }

    @Async
    public void startSendData(String channel, String mainChannel, Integer caseId) {
        List<SimulationTrajectoryDto> list = map.getOrDefault(channel, null);
        Gson gson = new Gson();
        if (list != null) {
            for (SimulationTrajectoryDto dto : list) {
                SimulationMessage message = new SimulationMessage();
                message.setType("trajectory");
                Long timestamp = System.currentTimeMillis();
                dto.setTimestamp(String.valueOf(timestamp/1000D));
                dto.getValue().forEach(item -> {
                    item.setGlobalTimeStamp(String.valueOf(timestamp));
                });
                message.setValue(dto);
                String data = gson.toJson(message);
                redisCache.publishMessage(channel, JSONObject.parseObject(data));
                redisCache.publishMessage(mainChannel, JSONObject.parseObject(data));
                if (!flag.containsKey(channel)) {
                    break;
                }
                try {
                    Thread.sleep(98);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (flag.containsKey(channel)) {
            flag.remove(channel);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String user = channel.substring(0, channel.indexOf("_"));
            stopSignal(caseId, user);
        }
    }

    public void status(String channel, int deviceId) {
        String online = "deviceState_"+deviceId+"_STATUSResult";
        String key = "deviceReadyState_" + deviceId + "_" + channel;
        redisCache.setCacheObject(online, 1, 5, TimeUnit.MINUTES);
        redisCache.setCacheObject(key, 1, 5, TimeUnit.MINUTES);
    }


    public void stopSendData(String channel) {
        flag.remove(channel);
    }

    private void stopSignal(Integer caseId, String user){
        try {
            String resultUrl = sendManualTerminationUrl;
            log.info("============================== sendManualTerminationUrl：{}", resultUrl);
            Map<String, Object> param = new HashMap<>();
            param.put("taskId", 0);
            param.put("caseId", caseId);
            param.put("testMode", 0);
            HashMap<String, Object> context = new HashMap<>();
            context.put("user", user);
            param.put("context", context);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> resultHttpEntity = new HttpEntity<>(param, httpHeaders);
            log.info("============================== sendManualTermination param：{}", JSONObject.toJSONString(param));
            ResponseEntity<String> response =
                    restTemplate.exchange(resultUrl, HttpMethod.POST, resultHttpEntity, String.class);
            if (response.getStatusCodeValue() == 200) {
                if (!"true".equals(response.getBody())) {
                    log.error("远程服务调用失败");
                }
            }
        } catch (Exception e) {
            log.error("远程服务调用失败:{}", e);
        }
    }


}
