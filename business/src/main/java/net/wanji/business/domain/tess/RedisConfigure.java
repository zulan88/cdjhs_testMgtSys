package net.wanji.business.domain.tess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jenny
 * @create: 2024-07-18 5:49 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RedisConfigure {
    private String host;

    private Integer port;

    private Integer db;

    private String pwd;
}
