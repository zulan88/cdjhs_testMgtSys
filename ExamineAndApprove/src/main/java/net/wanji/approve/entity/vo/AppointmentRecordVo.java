package net.wanji.approve.entity.vo;

import lombok.Data;
import net.wanji.approve.entity.AppointmentRecord;
import net.wanji.approve.entity.TjTesteeObjectInfo;
import net.wanji.business.domain.vo.CasePageVo;

import java.util.List;

@Data
public class AppointmentRecordVo extends AppointmentRecord {

    TjTesteeObjectInfo tjTesteeObjectInfo;

}
