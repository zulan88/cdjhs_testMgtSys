package net.wanji.business.domain.tess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jenny
 * @create: 2024-07-18 5:42 下午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TessStartParam {
    private String taskId;

    private TessInteractiveConfig interactiveConfig;

    private String networkId;

}
