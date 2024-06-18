package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.entity.TjResourcesDetail;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/28 13:20
 * @Descriptoin:
 */
@Data
public class ResourcesDetailVo extends TjResourcesDetail {

    /**
     * 场景树类型名称（attribute1）
     */
    private String sceneTreeTypeName;
    /**
     * 道路朝向类型名称（attribute2）
     */
    private String roadWayTypeName;



}
