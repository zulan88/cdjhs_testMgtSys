package net.wanji.onsite.service;

import net.wanji.onsite.entity.TjOnsiteCase;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2024-06-04
 */
public interface TjOnsiteCaseService extends IService<TjOnsiteCase> {

    void uploadToOnsite(TjOnsiteCase tjOnsiteCase, String xodrPath, String xoscPath);

    TjOnsiteCase getOnsiteCaseByNumId(String onsiteNum);

}
