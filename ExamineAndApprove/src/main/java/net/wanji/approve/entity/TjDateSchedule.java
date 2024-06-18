package net.wanji.approve.entity;

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
 * @since 2023-12-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tj_date_schedule")
public class TjDateSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("date")
    private String date;

    @TableField("year")
    private String year;

    @TableField("quarter")
    private String quarter;

    @TableField("appointment_ids")
    private String appointmentIds;


}
