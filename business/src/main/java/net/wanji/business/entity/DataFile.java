package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.wanji.business.entity.common.Lifecycle;

import java.time.LocalDateTime;

/**
 * @author hcy
 * @version 1.0
 * @className DataFile
 * @description 文件信息
 * @date 2024/4/1 15:59
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("tj_data_file")
public class DataFile extends Lifecycle {
  /**
   * 主键id
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  /**
   * 文件名称
   */
  @TableField("file_name")
  private String fileName;

  /**
   * 开始时间戳
   */
  @TableField("data_start_timestamp")
  private Long dataStartTimestamp;
  /**
   * 结束时间戳
   */
  @TableField("data_end_timestamp")
  private Long dataStopTimestamp;

  /**
   * 每行的开始offset
   */
  @TableField("line_offset")
  private byte[] lineOffset;
  /**
   * 分析进度
   */
  @TableField("progress")
  private String progress;

  @TableField("encode")
  private String encode;

  /**
   * 备注
   */
  @TableField("remark")
  private String remark;
}
