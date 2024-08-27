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

    List<CdjhsRefereeScoring> list(Integer taskId, Integer teamId);

    Map<String, Object> getScoreData(Integer taskId, Integer teamId, List<SysRole> roles);
}
