package net.wanji.business.util;

import net.wanji.business.common.Constants;
import net.wanji.business.domain.bo.CaseConfigBo;
import net.wanji.business.domain.bo.CaseInfoBo;
import net.wanji.business.domain.dto.TessngEvaluateDto;
import net.wanji.business.domain.param.DeviceConnInfo;
import net.wanji.business.domain.param.DeviceConnRule;
import net.wanji.business.entity.TjDeviceDetail;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hcy
 * @version 1.0
 * @className DeviceUtils
 * @description TODO
 * @date 2024/3/8 10:18
 **/
public class DeviceUtils {

  public static String getVisualDeviceId(Integer caseId, Integer deviceId,
      String supportRoles) {
    return getVisualDeviceId(caseId, deviceId, supportRoles, null);
  }

  /**
   * 获取设备虚拟id（用于区分tessng）
   *
   * @param caseId
   * @param deviceId
   * @param supportRoles
   * @return
   */
  public static String getVisualDeviceId(Integer caseId, Integer deviceId,
      String supportRoles, String username) {
    if (Constants.PartRole.MV_SIMULATION.equals(supportRoles)) {
      return TessngUtils.virtualDeviceId(0, caseId, username);
    }
    return deviceId.toString();
  }

  /**
   * 根据设备ID去重
   *
   * @param caseConfigs
   * @return
   */
  public static List<CaseConfigBo> deWeightConfigsByDeviceId(
      List<CaseConfigBo> caseConfigs) {
    return caseConfigs.stream()
        .filter(info -> !ObjectUtils.isEmpty(info.getDeviceId())).collect(
            Collectors.collectingAndThen(Collectors.toCollection(
                    () -> new TreeSet<>(
                        Comparator.comparing(CaseConfigBo::getDeviceId))),
                ArrayList::new));
  }

  public static List<DeviceConnRule> generateDeviceConnRules(
      List<TjDeviceDetail> deviceDetails, String commandChannel,
      String dataChannel,
      Map<Integer, List<TessngEvaluateDto>> tessngEvaluateAVs,
      Integer recordId) {
    List<DeviceConnRule> rules = new ArrayList<>();
    for (int i = 0; i < deviceDetails.size(); i++) {
      TjDeviceDetail deviceDetail = deviceDetails.get(i);
      for (int j = 0; j < deviceDetails.size(); j++) {
        if (j == i) {
          continue;
        }
        Map<String, Object> sourceParams = new HashMap<>();
        Map<String, Object> targetParams = new HashMap<>();

        TjDeviceDetail targetDevice = deviceDetails.get(j);

        DeviceConnRule rule = new DeviceConnRule();
        rule.setSource(createConnInfo(deviceDetail, commandChannel, dataChannel,
            sourceParams));
        extendParams(tessngEvaluateAVs, recordId, deviceDetail, sourceParams,
            targetDevice, targetParams);
        rule.setTarget(createConnInfo(targetDevice, commandChannel, dataChannel,
            targetParams));
        // 主车接收tessng过滤后数据通道
        avDevcieDataChannelChange(deviceDetail, targetDevice, rule);
        rules.add(rule);
      }
    }
    return rules;
  }

  private static void avDevcieDataChannelChange(TjDeviceDetail deviceDetail,
      TjDeviceDetail targetDevice, DeviceConnRule rule) {
    if (Constants.PartRole.AV.equals(deviceDetail.getSupportRoles())
        && Constants.PartRole.MV_SIMULATION.equals(
        targetDevice.getSupportRoles())) {
      rule.getTarget().setChannel(deviceDetail.getDataChannel()
          + Constants.TessngInteraction.NEARBY_DATA_CHANNEL_SUFFIX);
    }
  }

  private static void extendParams(
      Map<Integer, List<TessngEvaluateDto>> tessngEvaluateAVs, Integer recordId,
      TjDeviceDetail deviceDetail, Map<String, Object> sourceParams,
      TjDeviceDetail targetDevice, Map<String, Object> targetParams) {
    // 发送数据设备参数
    if (Constants.PartRole.MV_SIMULATION.equals(
        deviceDetail.getSupportRoles())) {
      sourceParams.put(Constants.TessngInteraction.RECORD_ID, recordId);
    }

    // 接收数据设备参数
    if (null != tessngEvaluateAVs
        && tessngEvaluateAVs.get(targetDevice.getDeviceId()) != null) {
      targetParams.put(Constants.TessngInteraction.EVALUATION_INFOS,
          tessngEvaluateAVs.get(targetDevice.getDeviceId()));
      // tessng额外上传主车相邻的背景车数据通道
      if (Constants.PartRole.MV_SIMULATION.equals(
          deviceDetail.getSupportRoles())) {
        targetParams.put(Constants.TessngInteraction.NEARBY_DATA_CHANNEL,
            targetDevice.getDataChannel()
                + Constants.TessngInteraction.NEARBY_DATA_CHANNEL_SUFFIX);
      }
    }
  }

  public static DeviceConnInfo createConnInfo(TjDeviceDetail deviceDetail,
      String commandChannel, String dataChannel, Map<String, Object> params) {
    return Constants.PartRole.MV_SIMULATION.equals(
        deviceDetail.getSupportRoles()) ?
        createSimulationConnInfo(String.valueOf(deviceDetail.getDeviceId()),
            commandChannel, dataChannel, params) :
        new DeviceConnInfo(String.valueOf(deviceDetail.getDeviceId()),
            deviceDetail.getCommandChannel(), deviceDetail.getDataChannel(),
            deviceDetail.getSupportRoles(), params);
  }

  public static DeviceConnInfo createSimulationConnInfo(String deviceId,
      String commandChannel, String dataChannel, Map<String, Object> params) {
    return new DeviceConnInfo(deviceId, commandChannel, dataChannel,
        Constants.PartRole.MV_SIMULATION, params);
  }
}
