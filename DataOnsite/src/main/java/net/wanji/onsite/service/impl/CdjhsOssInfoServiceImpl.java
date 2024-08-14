package net.wanji.onsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.wanji.onsite.entity.CdjhsOssInfo;
import net.wanji.onsite.mapper.CdjhsOssInfoMapper;
import net.wanji.onsite.service.CdjhsOssInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wj
 * @since 2024-08-14
 */
@Service
public class CdjhsOssInfoServiceImpl extends ServiceImpl<CdjhsOssInfoMapper, CdjhsOssInfo> implements CdjhsOssInfoService {

    @Override
    public List<CdjhsOssInfo> getnewest() {
        return this.baseMapper.getnewest();
    }
}
