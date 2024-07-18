package net.wanji.business.domain.tess;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-07-18 5:49 下午
 */
@Data
public class RedisConfigure {
    private String host;

    private Integer port;

    private Integer db;

    private String pwd;
}
