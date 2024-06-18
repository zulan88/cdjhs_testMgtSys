package net.wanji.business.service.evaluation.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.entity.evaluation.total.TjCaseScore;
import net.wanji.business.mapper.evaluation.total.TjCaseScoreMapper;
import net.wanji.business.service.evaluation.TjCaseScoreService;
import org.springframework.stereotype.Service;

/**
 * @author hcy
 * @version 1.0
 * @className TjCaseScoreServiceImpl
 * @description TODO
 * @date 2024/3/27 16:49
 **/
@Slf4j
@Service
public class TjCaseScoreServiceImpl
    extends ServiceImpl<TjCaseScoreMapper, TjCaseScore>
    implements TjCaseScoreService {

}
