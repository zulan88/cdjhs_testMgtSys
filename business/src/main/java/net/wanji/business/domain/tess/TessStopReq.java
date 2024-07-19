package net.wanji.business.domain.tess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: jenny
 * @create: 2024-07-18 21:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TessStopReq {
    private String timestamp;

    private TessStopParam data;
}
