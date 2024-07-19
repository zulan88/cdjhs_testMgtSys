package net.wanji.business.domain.tess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jenny
 * @create: 2024-07-18 6:33 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TessStartReq {
    private String timestamp;

    private TessStartParam data;
}
