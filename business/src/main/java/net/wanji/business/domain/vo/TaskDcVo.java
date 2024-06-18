package net.wanji.business.domain.vo;

import lombok.Data;
import net.wanji.business.entity.TjTaskDc;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/11 22:04
 * @Descriptoin:
 */
@Data
public class TaskDcVo extends TjTaskDc {
    private String name;
    private String type;
}
