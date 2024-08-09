package net.wanji.onsite.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wj
 * @since 2024-08-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("Evaluation")
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评价记录ID
     */
    @TableId("id")
    private String id;

    /**
     * 关联路网
     */
    @TableField("netId")
    private String netId;

    /**
     * 需评价主车ID列表
     */
    @TableField("mainVehiId")
    private String mainVehiId;

    /**
     * 轨迹数据配置
     */
    @TableField("kafkaTopic")
    private String kafkaTopic;

    /**
     * 得分
     */
    @TableField("score")
    private String score;

    /**
     * 是否被移除
     */
    @TableField("isDeleted")
    private Boolean isDeleted;

    /**
     * 创建事件
     */
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private LocalDateTime updateTime;

    /**
     * 当前评价状态
     */
    @TableField("status")
    private String status;

    @TableField("filterType")
    private String filterType;

    /**
     * 文件轨迹配置
     */
    @TableField("jsonDataPath")
    private String jsonDataPath;


}
