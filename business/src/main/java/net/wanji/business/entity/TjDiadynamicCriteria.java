package net.wanji.business.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 诊断指标表
 * @TableName tj_diadynamic_criteria
 */
@Accessors(chain = true)
@TableName(value ="tj_diadynamic_criteria")
@Data
public class TjDiadynamicCriteria implements Serializable {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 指标名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 指标描述
     */
    @TableField(value = "index_describe")
    private String indexDescribe;

    /**
     * 计算公式
     */
    @TableField(value = "computational_formula")
    private String computationalFormula;

    /**
     * 计算参数
     */
    @TableField(value = "design_conditions")
    private String designConditions;

    /**
     * 权重
     */
    @TableField(value = "weight")
    private String weight;

    /**
     * 建议
     */
    @TableField(value = "suggest")
    private String suggest;

    /**
     * 类型
     */
    @TableField(value = "type")
    private String type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}