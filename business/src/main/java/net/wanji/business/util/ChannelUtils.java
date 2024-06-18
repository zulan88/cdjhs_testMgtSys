package net.wanji.business.util;

import net.wanji.business.common.Constants;
import net.wanji.common.utils.SecurityUtils;

/**
 * @author hcy
 * @version 1.0
 * @className ChannelUtils
 * @description TODO
 * @date 2024/3/8 9:43
 **/
public class ChannelUtils {

  /**
   * 根据角色获取指令通道
   *
   * @param supportRoles
   * @param caseId
   * @param commandChanel
   * @return
   */
  public static String getCommandChannelByRole(String supportRoles, Integer caseId,
      String commandChanel) {
    return Constants.PartRole.MV_SIMULATION.equals(supportRoles) ?
        Constants.ChannelBuilder.buildTestingControlChannel(
            SecurityUtils.getUsername(), caseId) :
        commandChanel;
  }
}
