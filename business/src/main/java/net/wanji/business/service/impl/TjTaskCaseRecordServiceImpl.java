package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.entity.TjTaskCaseRecord;
import net.wanji.business.service.TjTaskCaseRecordService;
import net.wanji.business.mapper.TjTaskCaseRecordMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author guanyuduo
 * @description 针对表【tj_task_case_record】的数据库操作Service实现
 * @createDate 2023-09-02 21:23:32
 */
@Service
public class TjTaskCaseRecordServiceImpl
    extends ServiceImpl<TjTaskCaseRecordMapper, TjTaskCaseRecord>
    implements TjTaskCaseRecordService {
  @Resource
  TjTaskCaseRecordMapper tjTaskCaseRecordMapper;

  @Override
  public List<Map<String, Object>> selectTaskRecordInfo(Integer taskId, Integer selectedRecordId) {
    return tjTaskCaseRecordMapper.selectTaskRecordInfo(taskId, selectedRecordId);
  }
}
