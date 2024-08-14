package net.wanji.onsite.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2024-08-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("cdjhs_oss_info")
public class CdjhsOssInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("type")
    private String type;

    @TableField("path")
    private String path;

    @TableField("version")
    private String version;

    @TableField("upload_date")
    private String uploadDate;


}
