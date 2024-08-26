package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wj
 * @since 2024-08-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cdjhs_referee_scoring")
public class CdjhsRefereeScoring implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    /**
     * 任务id
     */
    @TableField("task_id")
    private Integer taskId;

    /**
     * 队伍编号
     */
    @TableField("team_id")
    private Integer teamId;

    /**
     * 出场顺序
     */
    @TableField("entry_order")
    private String entryOrder;

    /**
     * 裁判员id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 裁判员名称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 舒适性得分
     */
    @TableField("score_point1")
    private Integer scorePoint1;

    /**
     * 交规符合性得分
     */
    @TableField("score_point2")
    private Integer scorePoint2;


}
