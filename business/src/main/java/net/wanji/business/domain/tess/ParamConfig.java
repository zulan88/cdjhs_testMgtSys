package net.wanji.business.domain.tess;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: jenny
 * @create: 2024-07-18 5:53 下午
 */
@Component
public class ParamConfig {
    @Value("${spring.redis.host}")
    public String host;

    @Value("${spring.redis.port}")
    public Integer port;

    @Value("${spring.redis.database}")
    public Integer db;

    @Value("${spring.redis.password}")
    public String pwd;

    @Value("${tess.networkId}")
    public String networkId;

    @Value("${redis.channel.simulator}")
    public String simulatorChannel;

    @Value("${image.length.thresold}")
    public Integer imageLengthThresold;

    @Value("${tess.ip}")
    public String tessIp;

    @Value("${tess.port}")
    public Integer tessPort;

    @Value("${trajectory.radius}")
    public Double radius;

    @Value("${trajectory.topic}")
    public String kafkaTopic;

    @Value("${spring.kafka.bootstrap-servers}")
    public String kafkaHost;

    @Value("${trajectory.process}")
    public String luanshengProcess;

    @Value("${trajectory.objectivePercent}")
    public Integer objectivePercent;

    @Value("${trajectory.subjectivePercent}")
    public Integer subjectivePercent;
}
