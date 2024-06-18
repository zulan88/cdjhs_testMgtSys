package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.entity.TjInfinityTaskDataConfig;
import net.wanji.business.service.TjInfinityTaskDataConfigService;

import net.wanji.business.mapper.TjInfinityTaskDataConfigMapper;
import org.springframework.stereotype.Service;

@Service
public class TjInfinityTaskDataConfigServiceImpl extends ServiceImpl<TjInfinityTaskDataConfigMapper, TjInfinityTaskDataConfig>
        implements TjInfinityTaskDataConfigService {
}
