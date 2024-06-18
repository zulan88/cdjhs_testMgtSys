package net.wanji.approve.entity.vo;

import lombok.Data;
import net.wanji.approve.entity.TjDateSchedule;

import java.util.List;

@Data
public class ScheduleVo extends TjDateSchedule {

    List<String> recordList;

    public List<String> getRecordList() {
        if (recordList == null) {
            recordList = new java.util.ArrayList<>();
        }
        return recordList;
    }

    public String getMouth(){
        if(this.getDate().length() > 7){
            return this.getDate().substring(0,7);
        }else {
            return "error";
        }
    }
}
