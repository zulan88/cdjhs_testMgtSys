package net.wanji.business.util;

import com.alibaba.fastjson.JSONObject;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.InfiniteTessParm;
import net.wanji.business.domain.param.TessParam;
import net.wanji.common.utils.SecurityUtils;

import java.util.List;

/**
 * @author hcy
 * @version 1.0
 * @className TessngUtils
 * @description TODO
 * @date 2024/3/8 10:01
 **/
public class TessngUtils {

  public static String virtualDeviceId(Integer taskId, Integer caseId) {
    return virtualDeviceId(taskId, caseId, null);
  }

  /**
   * tessng交互通信虚拟设备ID（控制通道）
   *
   * @param taskId
   * @param caseId
   * @return
   */
  public static String virtualDeviceId(Integer taskId, Integer caseId,
      String username) {
    if (null == username) {
      username = SecurityUtils.getUsername();
    }
    if (null == taskId || 0 == taskId) {
      return Constants.ChannelBuilder.buildTestingControlChannel(username,
          caseId);
    }
    return Constants.ChannelBuilder.buildTaskControlChannel(username, taskId);
  }

  /**
   * 构建唤醒tess服务的参数
   *
   * @param roadNum
   * @param caseId
   * @return
   */
  public static TessParam buildTessServerParam(Integer roadNum, String username,
      Integer caseId, List<String> mapList) {
    return new TessParam().buildRealTestParam(roadNum,
        Constants.ChannelBuilder.buildTestingDataChannel(username, caseId),
        Constants.ChannelBuilder.buildTestingControlChannel(username, caseId),
        Constants.ChannelBuilder.buildTestingEvaluateChannel(username, caseId),
        Constants.ChannelBuilder.buildTestingStatusChannel(username, caseId),
        mapList, Constants.ChannelBuilder.REAL,
        JSONObject.parseObject("{\"params\":{\"params\": []}}"));
  }

  public static TessParam buildInfinityTaskRunParam(Integer taskId,
      String userName, List<String> mapList, InfiniteTessParm infiniteTessParm) {
    infiniteTessParm.setTaskId(taskId);

    return new TessParam().buildRealTestParam(0,
        Constants.ChannelBuilder.buildTestingDataChannel(userName, taskId),
        Constants.ChannelBuilder.buildTestingControlChannel(userName, taskId),
        Constants.ChannelBuilder.buildTestingEvaluateChannel(userName, taskId),
        Constants.ChannelBuilder.buildTestingStatusChannel(userName, taskId),
        mapList, 7, infiniteTessParm);
  }
}
