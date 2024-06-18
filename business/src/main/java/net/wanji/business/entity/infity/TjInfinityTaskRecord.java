package net.wanji.business.entity.infity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.wanji.business.entity.common.Lifecycle;

import java.io.Serializable;

/**
 * @author hcy
 * @version 1.0
 * @className TjInfinityTaskRecord
 * @description TODO
 * @date 2024/3/14 10:09
 **/
@EqualsAndHashCode(callSuper = true)
@TableName(value = "tj_infinity_task_record")
@Data
public class TjInfinityTaskRecord extends Lifecycle implements Serializable {

  private static final long serialVersionUID = 3894895063602370083L;

  /**
   * 主键
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  /**
   * 任务ID
   */
  @TableField("task_id")
  private Integer taskId;

  @TableField("case_id")
  private Integer caseId;

  /**
   * 持续时间(s)
   */
  @TableField("duration")
  private Long duration;

  /**
   * 记录文件id
   */
  @TableField("data_file_id")
  private Integer dataFileId;
}
