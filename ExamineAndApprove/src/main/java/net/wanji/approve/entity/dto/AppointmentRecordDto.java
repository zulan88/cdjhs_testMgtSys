package net.wanji.approve.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.wanji.approve.entity.AppointmentRecord;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class AppointmentRecordDto extends AppointmentRecord {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime endDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime appstartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime appendDate;

    public boolean dateExists() {
        return startDate!= null || endDate!= null;
    }

    public boolean appDateExists() {
        return appstartDate!= null || appendDate!= null;
    }

}
