package net.wanji.business.schedule;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.wanji.business.exercise.dto.luansheng.CAMatchProcess;
import net.wanji.business.service.KafkaProducer;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TwinsPlayback {

    @Autowired
    KafkaProducer kafkaProducer;

    @Async
    public void sendTwinsPlayback(String topic, List<List<ClientSimulationTrajectoryDto>> trajectories) throws InterruptedException {
        Thread.sleep(1000);
        Gson gson = new Gson();
        for(List<ClientSimulationTrajectoryDto> trajectory : trajectories) {
            JsonArray jsonArray = gson.toJsonTree(trajectory).getAsJsonArray();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("participantTrajectories", jsonArray);
            kafkaProducer.sendMessage(topic, jsonObject.toString());
            Thread.sleep(99);
        }
    }

    public void sendCAMatchProcess(String topic, CAMatchProcess camatchProcessDto){
        Gson gson = new Gson();
        kafkaProducer.sendMessage(topic, gson.toJson(camatchProcessDto));
        System.out.println("CAMatchProcess发送成功");
    }

}
