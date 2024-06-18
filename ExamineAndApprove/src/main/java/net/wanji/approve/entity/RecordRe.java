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
@TableName("record_re")
public class RecordRe implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    @TableField("device_ids")
    private String deviceIds;

    @TableField("person_ids")
    private String personIds;

}
