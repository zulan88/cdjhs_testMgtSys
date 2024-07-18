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
}
