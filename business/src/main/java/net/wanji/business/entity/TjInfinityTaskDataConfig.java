package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName(value = "tj_infinity_task_data_config")
@Data
public class TjInfinityTaskDataConfig implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 测试任务id
     */
    @TableField(value = "task_id")
    private Integer taskId;


    /**
     * 测试用例id
     */
    @TableField(value = "case_id")
    private Integer caseId;

    /**
     * 类型：av,mvReal,mvSimulation,sp
     */
    @TableField(value = "type")
    private String type;

    /**
     * 参与者ID
     */
    @TableField(value = "participator_id")
    private String participatorId;

    /**
     * 参与者名称
     */
    @TableField(value = "participator_name")
    private String participatorName;

    /**
     * 绑定设备id
     */
    @TableField(value = "device_id")
    private Integer deviceId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String deviceName;
}
