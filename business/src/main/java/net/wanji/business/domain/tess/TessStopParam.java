package net.wanji.business.domain.tess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-07-18 6:43 下午
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TessStopParam {
    //任务ID列表
    private List<String> taskIds;

    //仿真状态
    //根据此状态过滤
    private String status;
}
