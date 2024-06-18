package net.wanji.business.service;

import net.wanji.business.domain.param.GeneralizeScene;
import net.wanji.business.domain.vo.TjGeneralizeSceneVo;
import net.wanji.business.entity.TjGeneralizeScene;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2024-01-17
 */
public interface TjGeneralizeSceneService extends IService<TjGeneralizeScene> {

    List<TjGeneralizeSceneVo> selectList(TjGeneralizeScene tjGeneralizeScene);

}
