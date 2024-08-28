package net.wanji.business.service;

import net.wanji.business.entity.CdjhsRefereeScoring;
import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.common.core.domain.entity.SysRole;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wj
 * @since 2024-08-26
 */
public interface CdjhsRefereeScoringService extends IService<CdjhsRefereeScoring> {

    Integer buildScoreData(Integer taskId, Integer teamId, Integer entryOrder);

    Map<String, Object> getScoreData(Integer userId);
}
