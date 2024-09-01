package net.wanji.business.service;

import net.wanji.business.entity.CdjhsRefereeScoring;
import com.baomidou.mybatisplus.extension.service.IService;

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

    void notice(Integer recordId, String teamName);

    Integer buildScoreData(Integer recordId, Integer teamId, String teamName, Integer entryOrder);

    Map<String, Object> getScoreData(Integer userId);

    boolean submitScore(CdjhsRefereeScoring refereeScoring);
}
