package net.wanji.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.domain.dto.TjAtlasVenueDto;
import net.wanji.business.entity.TjAtlasVenue;
import net.wanji.business.exception.BusinessException;

import java.util.List;

/**
 * <p>
 * 地图库测试场地明细表 服务类
 * </p>
 *
 * @author zyl
 * @since 2024-02-27
 */
public interface ITjAtlasVenueService extends IService<TjAtlasVenue> {


    boolean saveOrUpdateVenue(TjAtlasVenueDto tjAtlasVenueDto);

    /**
     * 根据地图树的id以及关键字查询
     * @param tjAtlasVenueDto
     * @return
     */
    List<TjAtlasVenue> getAtlasVenueData(TjAtlasVenueDto tjAtlasVenueDto) throws BusinessException;

    boolean deleteVenueById(Integer id)throws BusinessException;

    boolean deleteVenueByTreeId(Integer treeId);
}
