package net.wanji.business.service.evaluation.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.entity.evaluation.total.TjCaseSceneItemScore;
import net.wanji.business.mapper.evaluation.total.TjCaseSceneItemScoreMapper;
import net.wanji.business.service.evaluation.TjCaseSceneItemScoreService;
import org.springframework.stereotype.Service;

/**
 * @author hcy
 * @version 1.0
 * @className TjCaseSceneItemScoreServiceImpl
 * @description TODO
 * @date 2024/3/27 16:54
 **/
@Service
public class TjCaseSceneItemScoreServiceImpl
    extends ServiceImpl<TjCaseSceneItemScoreMapper, TjCaseSceneItemScore>
    implements TjCaseSceneItemScoreService {
}
