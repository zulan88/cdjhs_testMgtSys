package net.wanji.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wanji.business.entity.TjCasePartConfig;
import net.wanji.business.entity.TjCaseRealRecord;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author guanyuduo
 * @description 针对表【tj_case_real_record】的数据库操作Mapper
 * @createDate 2023-08-30 19:12:28
 * @Entity net.wanji.business.entity.TjCaseRealRecord
 */
public interface TjCaseRealRecordMapper extends BaseMapper<TjCaseRealRecord> {

  @Select(
      "SELECT R.START_TIME, R.END_TIME,C.PARTICIPANT_ROLE FROM tj_case_real_record R "
          + "LEFT JOIN tj_case_part_config C ON R.CASE_ID = C.CASE_ID WHERE R.ID = #{recordId} GROUP BY C.PARTICIPANT_ROLE")
  List<Map<String, Object>> recordPartInfo(Integer recordId);
}
