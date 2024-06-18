package net.wanji.approve.service;

import net.wanji.approve.entity.TjTesteeObjectInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 测试预约申请-被测对象列表 服务类
 * </p>
 *
 * @author wj
 * @since 2023-12-06
 */
public interface TjTesteeObjectInfoService extends IService<TjTesteeObjectInfo> {

    void adddevice(Integer id, String dataChannel, String commandChannel);

    boolean deletedevice(Integer id);

}
