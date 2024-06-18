package net.wanji.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.entity.TjTaskCaseRecord;

import java.util.List;
import java.util.Map;

/**
* @author guanyuduo
* @description 针对表【tj_task_case_record】的数据库操作Service
* @createDate 2023-09-02 21:23:32
*/
public interface TjTaskCaseRecordService extends IService<TjTaskCaseRecord> {

  List<Map<String, Object>> selectTaskRecordInfo(Integer taskId, Integer selectedId);
}
