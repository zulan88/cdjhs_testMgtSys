package net.wanji.business.service.evaluation;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.entity.evaluation.single.TjTestSingleSceneScore;
import net.wanji.business.evaluation.RedisChannelDataProcessor;

/**
 * @author hcy
 * @version 1.0
 * @className TjTestSingleSceneScoreService
 * @description TODO
 * @date 2024/4/22 15:35
 **/
public interface TjTestSingleSceneScoreService
    extends IService<TjTestSingleSceneScore>, RedisChannelDataProcessor {
}
