package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.entity.infity.TjInfinityTaskRecord;
import net.wanji.business.mapper.TjInfinityTaskRecordMapper;
import net.wanji.business.service.TjInfinityTaskRecordService;
import org.springframework.stereotype.Service;

/**
 * @author hcy
 * @version 1.0
 * @className TjInfinityTaskRecordServiceImpl
 * @description TODO
 * @date 2024/4/1 13:38
 **/
@Service
public class TjInfinityTaskRecordServiceImpl
    extends ServiceImpl<TjInfinityTaskRecordMapper, TjInfinityTaskRecord>
    implements TjInfinityTaskRecordService {
}
