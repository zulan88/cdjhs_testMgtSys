package net.wanji.business.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author glace
 * @version 1.0
 * @className CommunicationDelayVo
 * @description TODO
 * @date 2023/8/31 15:41
 **/
@Data
public class CommunicationDelayVo {
  private List<String> time;
  private List<List<Integer>> delay;
  private List<String> type;
}
