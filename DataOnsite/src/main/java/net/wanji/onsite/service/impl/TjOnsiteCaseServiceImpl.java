package net.wanji.onsite.service.impl;

import net.wanji.onsite.entity.TjOnsiteCase;
import net.wanji.onsite.mapper.TjOnsiteCaseMapper;
import net.wanji.onsite.service.TjOnsiteRestService;
import net.wanji.onsite.service.TjOnsiteCaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wj
 * @since 2024-06-04
 */
@Service
public class TjOnsiteCaseServiceImpl extends ServiceImpl<TjOnsiteCaseMapper, TjOnsiteCase> implements TjOnsiteCaseService {

    @Autowired
    private TjOnsiteRestService tjOnsiteRestService;

    @Override
    @Async
    public void uploadToOnsite(TjOnsiteCase tjOnsiteCase, String xodrPath, String xoscPath) {
        List<File> files = new java.util.ArrayList<>();
        files.add(new File(xodrPath));
        files.add(new File(xoscPath));
        if (tjOnsiteRestService.upLodeFile(files,tjOnsiteCase.getOnsiteNumber())){
            tjOnsiteRestService.routePlanOnsite(tjOnsiteCase.getOnsiteNumber());
        }

    }

    @Override
    public TjOnsiteCase getOnsiteCaseByNumId(String onsiteNum) {
        return baseMapper.getOnsiteCaseByNum(onsiteNum);
    }
}
