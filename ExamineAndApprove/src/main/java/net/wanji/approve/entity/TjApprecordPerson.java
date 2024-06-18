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
 * 
 * </p>
 *
 * @author wj
 * @since 2023-12-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tj_apprecord_person")
public class TjApprecordPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("record_id")
    private Integer recordId;

    @TableField("person_id")
    private Integer personId;

    @TableField("device_id")
    private Integer deviceId;

    @TableField("device_name")
    private String deviceName;

    @TableField("other_task")
    private String otherTask;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass()!= that.getClass()) {
            return false;
        }
        TjApprecordPerson other = (TjApprecordPerson) that;
        return (this.getPersonId() == null? other.getPersonId() == null : this.getPersonId().equals(other.getPersonId()));
    }


}
