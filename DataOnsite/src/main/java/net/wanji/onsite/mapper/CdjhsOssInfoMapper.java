package net.wanji.onsite.mapper;

import net.wanji.onsite.entity.CdjhsOssInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2024-08-14
 */
public interface CdjhsOssInfoMapper extends BaseMapper<CdjhsOssInfo> {

    List<CdjhsOssInfo> getnewest();

}
