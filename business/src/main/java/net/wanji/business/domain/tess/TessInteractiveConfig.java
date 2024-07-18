package net.wanji.business.domain.tess;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-07-18 5:47 下午
 */
@Data
public class TessInteractiveConfig {
    private RedisConfigure configure;

    private String tessngChannel;

    private String commandChannel;

    private String mainCarChannel;

    private String heartChannel;
}
