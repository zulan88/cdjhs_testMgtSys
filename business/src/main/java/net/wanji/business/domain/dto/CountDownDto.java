package net.wanji.business.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author glace
 * @version 1.0
 * @className CountDownDto
 * @description TODO
 * @date 2023/8/31 13:29
 **/
@Getter
public class CountDownDto {
  /**
   * 全程长度
   */
  @Setter
  private Double fullLength;
  /**
   * 剩余长度
   */
  @Setter
  private Double remainLength;
  /**
   * 到达时间
   */
  private Date arrivalTime;
  /**
   * 剩余时间(s)
   */
  private Long timeRemaining;

  public void setTimeRemaining(Long timeRemaining) {
    this.timeRemaining = timeRemaining;

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime localDateTime = now.plusSeconds(timeRemaining);
    arrivalTime = Date.from(
        localDateTime.atZone(ZoneId.systemDefault()).toInstant());
  }
}
