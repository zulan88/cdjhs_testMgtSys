package net.wanji.business.domain.tess;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-07-18 5:42 下午
 */
@Data
public class TessStartParam {
    private String taskId;

    private TessInteractiveConfig interactiveConfig;

    private String networkId;

}
