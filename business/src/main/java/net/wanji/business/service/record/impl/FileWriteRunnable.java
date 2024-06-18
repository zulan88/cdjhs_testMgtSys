package net.wanji.business.service.record.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.domain.dto.ToLocalDto;
import net.wanji.business.entity.DataFile;
import net.wanji.business.service.record.DataFileService;
import net.wanji.common.file.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author hcy
 * @version 1.0
 * @className FileToLocalThread
 * @description TODO
 * @date 2024/4/2 9:14
 **/
@Slf4j
@RequiredArgsConstructor
public class FileWriteRunnable implements Runnable {
  private final String name;
  private final String path;
  private final ToLocalDto toLocalDto;
  private final DataFileService dataFileService;

  private final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(
      5000);
  private final AtomicBoolean stop = new AtomicBoolean(false);
  private boolean init = true;

  @Override
  public void run() {
    FileWriter writer = null;
    try {
      writer = new FileWriter(new File(path, name));
      while (!stop.get()) {
        String data = queue.poll(5, TimeUnit.SECONDS);
        if (!init) {
          writer.write("\n");
        } else {
          init = false;
        }
        if (null != data) {
          writer.write(data);
        }
      }
    } catch (Exception e) {
      if (log.isErrorEnabled()) {
        log.error("file [{}] [{}] write error!", path, name, e);
      }
      try {
        FileUtils.deleteFile(new File(path, name));
      } catch (Exception de) {
        if (log.isErrorEnabled()) {
          log.error("File [{}] [{}] delete error!", path, name, de);
        }
      }
    } finally {
      try {
        if (null != writer) {
          writer.flush();
          writer.close();
        }
      } catch (Exception e) {
        log.error("File [{}] [{}] writer close error!", path, name, e);
      } finally {
        try {
          DataFile dataFile = dataFileService.getById(toLocalDto.getFileId());
          dataFile.setEncode("utf-8");
          FileAnalysis.lineOffset(path, dataFile, (id, progress) -> {
            DataFile dataFileQ = new DataFile();
            dataFileQ.setId(toLocalDto.getFileId());
            dataFileQ.setProgress(progress);
            dataFileService.updateById(dataFileQ);
          });
          dataFileService.updateById(dataFile);
        } catch (IOException e) {
          if (log.isErrorEnabled()) {
            log.error("fileAnalysis [{}] error!", toLocalDto.getFileId(), e);
          }
        }
      }
    }
  }

  public boolean write(String data) {
    try {
      queue.add(data);
      return true;
    } catch (Exception e) {
      if (log.isErrorEnabled()) {
        log.error("file [{}] [{}] write error!", path, name, e);
      }
      return false;
    }
  }

  public boolean stop() {
    stop.set(true);
    return true;
  }
}
