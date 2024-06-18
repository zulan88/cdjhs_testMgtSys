package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.wanji.common.annotation.Excel;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author wj
 * @since 2023-06-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tj_case_part_config")
public class TjCasePartConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用例ID
     */
    @TableField("case_id")
    private Integer caseId;

    /**
     * 参与者角色（AV：av；MV-实车：mvReal；MV-仿真车：mvSimulation；SP：sp）
     */
    @TableField("participant_role")
    @Excel(name = "参与者角色")
    private String participantRole;

    /**
     * 参与者ID（场景配置关联使用）
     */
    @TableField("business_id")
    @Excel(name = "危险指数")
    private String businessId;

    /**
     * 参与者类型（主车：main；从车：slave；行人：pedestrian）
     */
    @TableField("business_type")
    @Excel(name = "参与者类型")
    private String businessType;

    /**
     * 参与者名称
     */
    @TableField("name")
    @Excel(name = "参与者名称")
    private String name;

    /**
     * 模型（1-小客车；2-大货车；3-大巴车；4-行人；5-自行车；）
     */
    @TableField("model")
    @Excel(name = "模型")
    private Integer model;

    /**
     * 关联实际客户端ID
     */
    @TableField("device_id")
    private Integer deviceId;

    @TableField("frist_site")
    private String fristSite;
}
