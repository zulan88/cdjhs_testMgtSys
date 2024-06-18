package net.wanji.business.entity.common;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.wanji.common.annotation.Excel;

import java.time.LocalDateTime;

/**
 * @author hcy
 * @version 1.0
 * @className Lifecycle
 * @description TODO
 * @date 2024/3/14 10:15
 **/
@Data
public class Lifecycle {
  /**
   * 创建人
   */
  @TableField("created_by")
  @Excel(name = "创建人")
  private String createdBy;
  /**
   * 创建日期
   */
  @TableField("created_date")
  @Excel(name = "创建日期")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdDate;
  /**
   * 修改人
   */
  @TableField("updated_by")
  @Excel(name = "修改人")
  private String updatedBy;
  /**
   * 修改日期
   */
  @TableField("updated_date")
  @Excel(name = "修改日期")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedDate;
}
