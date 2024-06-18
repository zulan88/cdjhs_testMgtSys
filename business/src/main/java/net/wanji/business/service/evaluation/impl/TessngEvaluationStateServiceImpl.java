package net.wanji.business.service.evaluation.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.entity.evaluation.TessngEvaluationState;
import net.wanji.business.mapper.evaluation.TessngEvaluationStateMapper;
import net.wanji.business.service.evaluation.TessngEvaluationStateService;
import net.wanji.common.annotation.DataSource;
import net.wanji.common.enums.DataSourceType;
import org.springframework.stereotype.Service;

/**
 * @author hcy
 * @version 1.0
 * @className TessngEvaluationStateServiceImpl
 * @description TODO
 * @date 2024/6/5 13:25
 **/
@Service
public class TessngEvaluationStateServiceImpl
    extends ServiceImpl<TessngEvaluationStateMapper, TessngEvaluationState>
    implements TessngEvaluationStateService {

  @Override
  @DataSource(DataSourceType.TESSNG)
  public boolean save(TessngEvaluationState entity) {
    return super.save(entity);
  }
}
