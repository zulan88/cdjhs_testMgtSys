package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 地图库测试场地明细表
 * </p>
 *
 * @author zyl
 * @since 2024-02-27
 */
@Getter
@Setter
@TableName("tj_atlas_venue")
public class TjAtlasVenue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 地图树id
     */
    private Integer treeId;

    /**
     * 测试场地名称
     */
    private String name;

    /**
     * 是否支持实地测试（0：不支持；1支持）
     */
    private Integer isField;

    /**
     * geoJson文件地址
     */
    private String geoJsonPath;

    /**
     * openDrive文件地址
     */
    private String openDrivePath;

    /**
     * 场地实景图片地址
     */
    private String fieldImgPath;

    /**
     * 备用字段1
     */
    private String attribute1;

    /**
     * 备用字段2
     */
    private String attribute2;

    /**
     * 备用字段3
     */
    private String attribute3;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建日期
     */
    private LocalDateTime createdDate;

    /**
     * 修改人
     */
    private String updatedBy;

    /**
     * 修改日期
     */
    private LocalDateTime updatedDate;

    /**
     * 场地类型  0-分段式场景，1-无限里程
     */
    private Integer sceneType;
}
