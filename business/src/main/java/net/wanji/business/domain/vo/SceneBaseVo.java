package net.wanji.business.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/4 14:28
 * @Descriptoin:
 */
@Data
public class SceneBaseVo {

    /**
     * 图片
     */
    private String imgUrl;

    /**
     * 测试道路说明
     */
    private String testRoadDesc;

    /**
     * 测试场景说明
     */
    private String testSceneDesc;

    /**
     * 标签列表
     */
    private List<String> labelList;
}
