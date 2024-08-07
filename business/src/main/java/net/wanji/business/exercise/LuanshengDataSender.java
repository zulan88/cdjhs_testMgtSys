package net.wanji.business.exercise;

import com.alibaba.fastjson.JSONObject;
import net.wanji.business.common.Constants;
import net.wanji.business.service.KafkaProducer;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: jenny
 * @create: 2024-08-05 2:57 下午
 */
@Component
public class LuanshengDataSender {
    @Value("${trajectory.luansheng}")
    private String luanshengTopic;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Async("luanShengHandlePool")
    public void send(List<ClientSimulationTrajectoryDto> participants, Integer taskId, String sceneName){
        List<TrajectoryValueDto> value = new ArrayList<>();
        for(ClientSimulationTrajectoryDto participant: participants){
            boolean isMainCar = participant.getRole().equals(Constants.PartRole.AV);
            List<TrajectoryValueDto> participantValue = participant.getValue();
            List<TrajectoryValueDto> trajectories = participantValue.stream()
                    .peek(item -> {
                        if (isMainCar) {
                            item.setDataType(0);
                        } else {
                            int dataType = Objects.isNull(item.getCarSource()) || item.getCarSource() == 1 ? 1 : 2;
                            item.setDataType(dataType);
                        }
                    }).collect(Collectors.toList());

            value.addAll(trajectories);
        }

        ParticipantTrajectoryDto trajectoryDto = new ParticipantTrajectoryDto();
        trajectoryDto.setValue(value);
        trajectoryDto.setTimestamp(System.currentTimeMillis());
        trajectoryDto.setTimestampType("CREATE_TIME");
        trajectoryDto.setSceneName(sceneName);

        String message = JSONObject.toJSONString(trajectoryDto);
        String key = StringUtils.format(Constants.ChannelBuilder.CDJHS_LUANSHENG_TASK_KEY_TEMPLATE, taskId);
        kafkaProducer.sendMessage(luanshengTopic, key, message);
    }
}
