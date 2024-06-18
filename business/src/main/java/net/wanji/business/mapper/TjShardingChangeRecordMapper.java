package net.wanji.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wanji.business.entity.infity.TjShardingChangeRecord;
import net.wanji.business.entity.infity.TjShardingResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hcy
 * @version 1.0
 * @interfaceName TjShardingChangeRecordMapper
 * @description TODO
 * @date 2024/3/12 18:21
 **/
public interface TjShardingChangeRecordMapper
    extends BaseMapper<TjShardingChangeRecord> {
  List<TjShardingResult> shardingResult(@Param("taskId") Integer taskId,
      @Param("caseId") Integer caseId, @Param("recordId") Integer recordId);
}
