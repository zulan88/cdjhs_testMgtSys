package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用例树表
 * </p>
 *
 * @author wj
 * @since 2024-08-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tj_task_tree")
public class TjTaskTree implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 测试类型（虚实融合测试：virtualRealFusion；虚实对比测试：virtualRealContrast；人在环路测试：mainInLoop；平行推演测试：parallelDeduction；三项映射测试：threeTermMapping）
     */
    @TableField("type")
    private String type;

    /**
     * 状态 0：使用中；1：已删除
     */
    @TableField("status")
    private Integer status;

    /**
     * 父级id
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 级别
     */
    @TableField("level")
    private Integer level;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 创建日期
     */
    @TableField("created_date")
    private LocalDateTime createdDate;

    /**
     * 修改人
     */
    @TableField("updated_by")
    private String updatedBy;

    /**
     * 修改日期
     */
    @TableField("updated_date")
    private LocalDateTime updatedDate;


}
