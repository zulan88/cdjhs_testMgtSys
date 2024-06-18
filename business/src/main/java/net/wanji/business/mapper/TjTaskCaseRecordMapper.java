package net.wanji.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wanji.business.entity.TjTaskCaseRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
* @author guanyuduo
* @description 针对表【tj_task_case_record】的数据库操作Mapper
* @createDate 2023-09-02 21:23:32
* @Entity net.wanji.business.entity.TjTaskCaseRecord
*/
public interface TjTaskCaseRecordMapper extends BaseMapper<TjTaskCaseRecord> {

    @Select(
            "SELECT R.START_TIME, R.END_TIME,C.TYPE FROM tj_task_case_record R "
                    + "LEFT JOIN tj_task_data_config C ON R.TASK_ID = C.TASK_ID WHERE R.ID = #{recordId}")
    List<Map<String, Object>> recordPartInfo(Integer recordId);

    List<Map<String, Object>> selectTaskRecordInfo(
        @Param("taskId") Integer taskId,
        @Param("selectedRecordId") Integer selectedRecordId);
}
