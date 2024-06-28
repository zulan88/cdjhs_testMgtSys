package net.wanji.business.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author: jenny
 * @create: 2024-06-28 4:28 下午
 */
@Data
public class ChunkMergeReq {
    @NotNull(message = "uploadId不能为空")
    private String uploadId;

    @NotNull(message = "objectName")
    private String objectName;
}
