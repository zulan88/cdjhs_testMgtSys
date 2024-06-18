package net.wanji.business.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.wanji.business.common.Constants.RedisMessageType;
import net.wanji.business.domain.WebsocketMessage;
import net.wanji.common.utils.DateUtils;
import net.wanji.openScenario.properties.OpenScenarioProject;
import net.wanji.business.socket.WebSocketManage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/11 17:18
 * @Descriptoin:
 */
@Service
public class ScenarioService {

    public static final Map<String, String> nameMap = new HashMap<>();

    public static final Map<String, OpenScenarioProject> openScenarioProjectMap = new HashMap<>();

    int index = 0;

    public void start(String zipName) {

        index = 0;

        String xoscFile = nameMap.get(zipName);

        OpenScenarioProject openScenarioProject = new OpenScenarioProject(xoscFile);
        System.out.println(JSONObject.toJSONString(openScenarioProject));



        List<JSONArray> list = new ArrayList<>();
        JSONArray actArray = (JSONArray) openScenarioProject.getStoryboard().getStory().get("acts");
        for (int i = 0; i < actArray.size(); i++) {
            JSONObject jsonObject = actArray.getJSONObject(i);
            JSONArray polyline = jsonObject.getJSONArray("polyline");
            list.add(polyline);
        }
        final ScheduledFuture<?>[] futures = new ScheduledFuture<?>[1];
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        Runnable task = () -> {
            int size = 0;
            List<JSONObject> tra = new ArrayList<>();
            for (JSONArray jsonArray : list) {
                size = jsonArray.size();
                if (index == size) {
                    futures[0].cancel(false);
                }
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                tra.add(jsonObject);
            }
            String countDown = DateUtils.secondsToDuration(
                    (int) Math.floor((double) (size - index) / 10));
            WebsocketMessage message = new WebsocketMessage(RedisMessageType.TRAJECTORY, countDown, tra);
            WebSocketManage.sendInfo(zipName, JSONObject.toJSONString(message));
            index ++;
        };
        futures[0] = scheduledExecutorService.scheduleAtFixedRate(task, 0, 100, TimeUnit.MILLISECONDS);
    }
}
