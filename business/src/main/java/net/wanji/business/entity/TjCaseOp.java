package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class TjCaseOp extends TjCase{

    @TableField("op_status")
    String opStatus;

    @TableField(exist = false)
    String score;

}
