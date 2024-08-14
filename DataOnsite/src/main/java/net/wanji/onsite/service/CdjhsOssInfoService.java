package net.wanji.onsite.service;

import net.wanji.onsite.entity.CdjhsOssInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2024-08-14
 */
public interface CdjhsOssInfoService extends IService<CdjhsOssInfo> {

    List<CdjhsOssInfo> getnewest();

}
