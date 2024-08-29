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
 * @since 2024-08-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cdjhs_static_element")
public class CdjhsStaticElement implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
    private Integer id;

    /**
     * 静态元素名称
     */
    @TableField("name")
    private String name;

    /**
     * 静态元素内容
     */
    @TableField("data")
    private String data;


}
