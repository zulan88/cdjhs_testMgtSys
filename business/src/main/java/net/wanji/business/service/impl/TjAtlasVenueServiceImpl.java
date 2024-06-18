package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.domain.dto.TjAtlasVenueDto;
import net.wanji.business.entity.TjAtlasVenue;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjAtlasVenueMapper;
import net.wanji.business.service.ITjAtlasVenueService;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.bean.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 地图库测试场地明细表 服务实现类
 * </p>
 *
 * @author zyl
 * @since 2024-02-27
 */
@Service
public class TjAtlasVenueServiceImpl extends ServiceImpl<TjAtlasVenueMapper, TjAtlasVenue> implements ITjAtlasVenueService {


    @Override
    public boolean saveOrUpdateVenue(TjAtlasVenueDto tjAtlasVenueDto) {

        if(ObjectUtils.isEmpty(tjAtlasVenueDto.getId())){//新增
            TjAtlasVenue tjAtlasVenue = new TjAtlasVenue();
            BeanUtils.copyBeanProp(tjAtlasVenue,tjAtlasVenueDto);
            tjAtlasVenue.setCreatedBy(SecurityUtils.getUsername());
            tjAtlasVenue.setCreatedDate(LocalDateTime.now());
            return this.save(tjAtlasVenue);
        }else{

            TjAtlasVenue tjAtlasVenue = this.getById(tjAtlasVenueDto.getId());
            tjAtlasVenue.setName(tjAtlasVenueDto.getName());
            tjAtlasVenue.setIsField(tjAtlasVenueDto.getIsField());
            tjAtlasVenue.setGeoJsonPath(tjAtlasVenueDto.getGeoJsonPath());
            tjAtlasVenue.setOpenDrivePath(tjAtlasVenueDto.getOpenDrivePath());
            tjAtlasVenue.setFieldImgPath(tjAtlasVenueDto.getFieldImgPath());
            tjAtlasVenue.setUpdatedBy(SecurityUtils.getUsername());
            tjAtlasVenue.setUpdatedDate(LocalDateTime.now());
            tjAtlasVenue.setAttribute1(tjAtlasVenueDto.getAttribute1());
            return this.updateById(tjAtlasVenue);
        }
    }

    /**
     * 根据地图树的id以及关键字查询
     *
     * @param tjAtlasVenue
     * @return
     */
    @Override
    public List<TjAtlasVenue> getAtlasVenueData(TjAtlasVenueDto tjAtlasVenue) throws BusinessException {

        if(ObjectUtils.isEmpty(tjAtlasVenue.getTreeId())){
            throw new BusinessException("请选择地图树信息");
        }

        LambdaQueryWrapper<TjAtlasVenue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ObjectUtils.isNotEmpty(tjAtlasVenue.getTreeId()),
                TjAtlasVenue::getTreeId,tjAtlasVenue.getTreeId());

        queryWrapper.eq(ObjectUtils.isNotEmpty(tjAtlasVenue.getIsField()),
                TjAtlasVenue::getIsField,tjAtlasVenue.getIsField());

        queryWrapper.eq(ObjectUtils.isNotEmpty(tjAtlasVenue.getSceneType()),
                TjAtlasVenue::getSceneType,tjAtlasVenue.getSceneType());

        queryWrapper.like(ObjectUtils.isNotEmpty(tjAtlasVenue.getName()),
                TjAtlasVenue::getName,tjAtlasVenue.getName());

        queryWrapper.orderByDesc(TjAtlasVenue::getId);
        return this.list(queryWrapper);
    }

    @Override
    public boolean deleteVenueById(Integer id) throws BusinessException{
        TjAtlasVenue tjAtlasVenue = this.getById(id);
        if(ObjectUtils.isEmpty(tjAtlasVenue)){
            throw new BusinessException("未找到节点信息");
        }
        return this.removeById(id);
    }

    @Override
    public boolean deleteVenueByTreeId(Integer treeId) {
        LambdaQueryWrapper<TjAtlasVenue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TjAtlasVenue::getTreeId,treeId);
        try {
            baseMapper.delete(queryWrapper);
            return true;
        }catch (Exception e){

            return false;
        }
    }
}
