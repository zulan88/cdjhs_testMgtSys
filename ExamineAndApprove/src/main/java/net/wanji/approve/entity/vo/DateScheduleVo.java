package net.wanji.approve.entity.vo;

import lombok.Data;

import java.util.List;

@Data
public class DateScheduleVo {

    String date;

    List<ScheduleVo> scheduleList;

}
