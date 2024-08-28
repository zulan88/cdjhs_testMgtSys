package net.wanji.onsite.entity;

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
 * @since 2024-08-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cdjhs_referee_members")
public class CdjhsRefereeMembers implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 裁判员id
     */
    @TableId("user_id")
    private Integer userId;

    /**
     * 裁判员名称
     */
    @TableField("user_name")
    private String userName;


}
