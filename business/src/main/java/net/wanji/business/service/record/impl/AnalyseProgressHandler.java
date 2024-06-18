package net.wanji.business.service.record.impl;

import javax.annotation.concurrent.ThreadSafe;

/**
 * @author glace
 * @version 1.0
 * @className AnalyseProgressHandler
 * @description TODO
 * @date 2023/8/17 14:03
 **/
@ThreadSafe
public class AnalyseProgressHandler {
  private static final ThreadLocal<Long> changeTag = new ThreadLocal<>();

  {
    changeTag.set(0L);
  }

  public void record(Integer id, AnalyseProgress analyseProgress,
      long fileLength, long currentSize) {
    double fileD = (double) fileLength;
    // 20MB
    if (currentSize / (20 * 1024 * 1024) != changeTag.get()) {
      analyseProgress.progress(id,
          String.format("%2.0f", currentSize / fileD * 100));
      changeTag.set(changeTag.get() + 1);
      if (currentSize / fileD == 1) {
        changeTag.set(0L);
      }
    }
  }

  public interface AnalyseProgress {
    void progress(Integer id, String progress);
  }
}
