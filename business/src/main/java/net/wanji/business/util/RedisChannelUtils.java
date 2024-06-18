package net.wanji.business.util;

import net.wanji.business.common.Constants;
import net.wanji.common.utils.SecurityUtils;

/**
 * @author hcy
 * @version 1.0
 * @className RedisChannelUtils
 * @description TODO
 * @date 2024/3/14 15:57
 **/
public class RedisChannelUtils {

  /**
   * 根据角色获取指令通道
   *
   * @param taskId
   * @param caseId
   * @param supportRole
   * @param commandChannel
   * @return
   */
  public static String getCommandChannelByRole(Integer taskId, Integer caseId,
      String supportRole, String commandChannel, String userName) {
    if (userName == null){
      userName = SecurityUtils.getUsername();
    }
    return Constants.PartRole.MV_SIMULATION.equals(supportRole) ?
        Constants.ChannelBuilder.buildTestingControlChannel(
            userName, caseId) :
        commandChannel;
  }

  /**
   * 根据角色获取准备状态通道
   *
   * @param caseId
   * @param supportRoles
   * @return
   */
  public static String getReadyStatusChannelByRole(Integer caseId,
      String supportRoles, String userName) {
    if (userName == null){
      userName = SecurityUtils.getUsername();
    }
    return Constants.PartRole.MV_SIMULATION.equals(supportRoles) ?
        Constants.ChannelBuilder.buildTestingStatusChannel(
            userName, caseId) :
        Constants.ChannelBuilder.DEFAULT_STATUS_CHANNEL;
  }
}
