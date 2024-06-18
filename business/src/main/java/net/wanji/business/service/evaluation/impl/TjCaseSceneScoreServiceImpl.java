package net.wanji.business.service.evaluation.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.entity.evaluation.total.TjCaseSceneScore;
import net.wanji.business.mapper.evaluation.total.TjCaseSceneScoreMapper;
import net.wanji.business.service.evaluation.TjCaseSceneScoreService;
import org.springframework.stereotype.Service;

/**
 * @author hcy
 * @version 1.0
 * @className TjCaseSceneScoreServiceImpl
 * @description TODO
 * @date 2024/3/27 16:54
 **/
@Service
public class TjCaseSceneScoreServiceImpl extends
    ServiceImpl<TjCaseSceneScoreMapper, TjCaseSceneScore> implements
    TjCaseSceneScoreService {
}
