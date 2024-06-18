package net.wanji.approve.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 工作人员表
 * </p>
 *
 * @author wj
 * @since 2023-12-05
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tj_workers")
public class TjWorkers implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 人员编号
     */
    @TableField("worker_id")
    private String workerId;

    /**
     * 人员类型
     */
    @TableField("type")
    private String type;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 电话
     */
    @TableField("phone_number")
    private String phoneNumber;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;


}
