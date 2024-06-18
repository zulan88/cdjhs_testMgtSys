package net.wanji.business.util;

/**
 * @author hcy
 * @version 1.0
 * @className RedisCacheUtils
 * @description TODO
 * @date 2024/3/26 16:25
 **/
public class RedisCacheUtils {
  private final static String RECORD_ID_PREFIX = "RECORD_ID_";

  public static String createRecordCacheId(Integer taskId, Integer caseId) {
    return RECORD_ID_PREFIX + taskId + "_" + caseId;
  }
}
