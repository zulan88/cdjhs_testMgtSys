package net.wanji.business.domain.tess;

import lombok.Data;

/**
 * @author: jenny
 * @create: 2024-07-18 6:33 下午
 */
@Data
public class TessStartReqParam {
    private String timestamp;

    private TessStartParam data;
}
