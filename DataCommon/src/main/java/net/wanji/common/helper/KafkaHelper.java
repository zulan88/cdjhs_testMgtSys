package net.wanji.common.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


//@Component
public class KafkaHelper {

    @Value("${kafka.topic.v2xCarData}")
    public String v2xCarData;

    @Value("${kafka.topic.qhV2xCarData}")
    public String qhV2xCarData;

    @Value("${kafka.topic.weatherData}")
    public String weatherData;

    @Value("${kafka.topic.metSensor}")
    public String metSensor;

    @Value("${kafka.topic.v2xSceneStatisticData}")
    public String v2xSceneStatisticData;

    @Value("${kafka.topic.v2xCarListData}")
    public String v2xCarListData;

    @Value("${kafka.topic.matchResultData}")
    public String matchResultData;

    @Value("${kafka.topic.matchResultDataForTraffic}")
    public String matchResultDataForTraffic ;

    @Value("${kafka.topic.b5event}")
    public String b5event;

    @Value("${kafka.topic.trafficControlData}")
    public String trafficControlData;

    @Value("${kafka.topic.v2xTZv2xSignalData}")
    public String v2xTZv2xSignalData;

    @Value("${kafka.topic.trafficData}")
    public String trafficData;

    public String getKafkaKey(String kafkaTopic){
        StringBuilder sb = new StringBuilder();
        sb.append(kafkaTopic);
        sb.append(":");
        LocalDateTime now = LocalDateTime.now();
        sb.append(now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH.mm.ss.SSS")));
        return sb.toString();
    }

}