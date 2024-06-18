package net.wanji.onsite.service;

import java.io.File;
import java.util.List;

public interface TjOnsiteRestService {

    /**
     * onsite上传文件
     * @param filesToUpload
     * @return
     */
    boolean upLodeFile(List<File> filesToUpload, String dictory);

    /**
     * onsite主车轨迹生成
     * @param task
     * @return
     */
    Boolean routePlanOnsite(String task);

    /**
     * onsite主车轨迹下载
     * @param task
     * @return
     */
    void getOnsiteTrace(String task, String savePath);

}
