package net.wanji.approve.entity.dto;

import lombok.Data;
import net.wanji.approve.entity.TjApprecordPerson;

import java.util.List;

@Data
public class ScheduleDto {

    List<String> dates;

    Integer recordId;

    String deviceIds;

    String personIds;

    List<TjApprecordPerson> personList;

}
