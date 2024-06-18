package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.common.Constants.ColumnName;
import net.wanji.business.common.Constants.FileExtension;
import net.wanji.business.common.Constants.FileTypeEnum;
import net.wanji.business.common.Constants.GeoJsonType;
import net.wanji.business.common.Constants.ResourceType;
import net.wanji.business.common.Constants.SysType;
import net.wanji.business.common.Constants.YN;
import net.wanji.business.domain.dto.TjResourcesDetailDto;
import net.wanji.business.domain.vo.ResourcesDetailVo;
import net.wanji.business.entity.TjResources;
import net.wanji.business.entity.TjResourcesDetail;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjFragmentedSceneDetailMapper;
import net.wanji.business.mapper.TjResourcesDetailMapper;
import net.wanji.business.mapper.TjResourcesMapper;
import net.wanji.business.service.TjResourcesDetailService;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.bean.BeanUtils;
import net.wanji.common.utils.file.FileUploadUtils;
import net.wanji.common.utils.file.FileUtils;
import net.wanji.system.service.ISysDictDataService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源详情表 服务实现类
 * </p>
 *
 * @author wj
 * @since 2023-06-27
 */
@Service
public class TjResourcesDetailServiceImpl extends ServiceImpl<TjResourcesDetailMapper, TjResourcesDetail>
        implements TjResourcesDetailService {

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private TjResourcesMapper resourcesMapper;

    @Autowired
    private TjFragmentedSceneDetailMapper sceneDetailMapper;


    @Override
    public List<ResourcesDetailVo> getDetailList(Integer resourceId, String name) {
        QueryWrapper<TjResourcesDetail> queryWrapper = new QueryWrapper();
        queryWrapper.eq(ColumnName.RESOURCES_ID_COLUMN, resourceId);
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like(ColumnName.NAME_COLUMN, name);
        }
        List<TjResourcesDetail> details = this.list(queryWrapper);
        return CollectionUtils.emptyIfNull(details).stream().map(detail -> {
                    ResourcesDetailVo detailVo = new ResourcesDetailVo();
                    BeanUtils.copyBeanProp(detailVo, detail);
                    detailVo.setSceneTreeTypeName(dictDataService.selectDictLabel(SysType.SCENE_TREE_TYPE
                            , detailVo.getAttribute1()));
                    detailVo.setRoadWayTypeName(dictDataService.selectDictLabel(SysType.ROAD_WAY_TYPE
                            , detailVo.getAttribute2()));
                    return detailVo;
                }).sorted(Comparator.comparingInt(TjResourcesDetail::getCollectStatus)
                        .reversed()
                        .thenComparing(TjResourcesDetail::getCreatedDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public ResourcesDetailVo preview(TjResourcesDetailDto resourcesDetailDto) throws BusinessException, IOException {
        ResourcesDetailVo resourcesDetail = new ResourcesDetailVo();
        BeanUtils.copyBeanProp(resourcesDetail, resourcesDetailDto);
        if (ObjectUtils.isEmpty(resourcesDetailDto.getId())) {
            resourcesDetail.setCode(StringUtils.generateRandomString());
        } else {
            TjResourcesDetail oldDetail = this.getById(resourcesDetailDto.getId());
            if (ObjectUtils.isEmpty(oldDetail)) {
                throw new BusinessException("未查询到对应资源详情");
            }
            resourcesDetail.setId(oldDetail.getId());
            resourcesDetail.setCode(oldDetail.getCode());
        }
        validPreviewParam(resourcesDetailDto);

        String file = FileUploadUtils.getAbsolutePathFileName(resourcesDetailDto.getFilePath());
        List<String> itemNames = FileUtils.readZipFile(file);
        if (CollectionUtils.isEmpty(itemNames) || itemNames.size() < 3) {
            throw new BusinessException("zip文件中需要包含opendrive路网文件，背景及边线geojson文件");
        }
        String format = null;
        String compressed = null;
        String lines = null;
        for (String fileName : itemNames) {
            switch (FilenameUtils.getExtension(fileName)) {
                case FileExtension.XODR:
                    format = FileTypeEnum.OpenDrive.getType();
                    break;
                case FileExtension.GEO_JSON:
                    if (fileName.contains(GeoJsonType.COMPRESSED)) {
                        compressed = fileName;
                        break;
                    }
                    if (fileName.contains(GeoJsonType.LINES)) {
                        lines = fileName;
                        break;
                    }
                    break;
                default:
                    break;
            }
        }
        if (StringUtils.isEmpty(compressed) || StringUtils.isEmpty(lines)) {
            throw new BusinessException("zip文件中需要包含compressed及lines的geoJson文件");
        }
        resourcesDetail.setFormat(format);
        resourcesDetail.setAttribute4(StringUtils.format("{},{}", compressed, lines));
        return resourcesDetail;
    }

    private void validPreviewParam(TjResourcesDetailDto resourcesDetailDto) throws BusinessException {
        String extension = FilenameUtils.getExtension(resourcesDetailDto.getFilePath());
        if (!StringUtils.equals(extension, FileExtension.ZIP)) {
            throw new BusinessException("请上传zip文件");
        }
    }

    @Override
    public boolean saveResourcesDetail(TjResourcesDetailDto resourcesDetailDto) throws BusinessException {
        TjResources tjResources = resourcesMapper.selectById(resourcesDetailDto.getResourcesId());
        validSaveParam(tjResources.getType(), resourcesDetailDto);
        TjResourcesDetail detail = new TjResourcesDetail();
        BeanUtils.copyBeanProp(detail, resourcesDetailDto);
        if (ObjectUtils.isEmpty(resourcesDetailDto.getId())) {
            detail.setCollectStatus(YN.N_INT);
            detail.setCreatedBy(SecurityUtils.getUsername());
            detail.setCreatedDate(LocalDateTime.now());
            return this.save(detail);
        } else {
            detail.setUpdatedBy(SecurityUtils.getUsername());
            detail.setUpdatedDate(LocalDateTime.now());
            return this.updateById(detail);
        }
    }

    @Override
    public boolean collectByDetailId(Integer resourceDetailId) {
        TjResourcesDetail resourcesDetail = this.getById(resourceDetailId);
        resourcesDetail.setCollectStatus(resourcesDetail.getCollectStatus() ^ 1);
        resourcesDetail.setUpdatedBy(SecurityUtils.getUsername());
        resourcesDetail.setUpdatedDate(LocalDateTime.now());
        return this.updateById(resourcesDetail);
    }

    public void validSaveParam(String type, TjResourcesDetailDto resourcesDetailDto) throws BusinessException {
        switch (type) {
            case ResourceType.INTEGRATION_MAP:
                break;
            case ResourceType.ATOM_MAP:
                if (StringUtils.isEmpty(resourcesDetailDto.getAttribute1())) {
                    throw new BusinessException("请选择道路类型");
                }
                if (StringUtils.isEmpty(resourcesDetailDto.getAttribute2())) {
                    throw new BusinessException("请选择道路构成");
                }
                if (StringUtils.isEmpty(resourcesDetailDto.getAttribute3())) {
                    throw new BusinessException("请选择车道数");
                }
                break;
            case ResourceType.BG_TRAFFIC_FLOW:
                break;
            case ResourceType.MAIN:
                break;
            case ResourceType.FACILITIES:
                break;
            default:
                break;
        }

    }

    @Override
    public boolean deleteByDetailId(Integer resourceDetailId) throws BusinessException {
        List<Integer> usingDetailIds = sceneDetailMapper.selectUsingResources();
        if (CollectionUtils.emptyIfNull(usingDetailIds).contains(resourceDetailId)) {
            throw new BusinessException("存在使用此资源的场景");
        }
        return this.removeById(resourceDetailId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByResourceId(Integer resourceId) throws BusinessException {
        QueryWrapper<TjResourcesDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ColumnName.RESOURCES_ID_COLUMN, resourceId);
        List<TjResourcesDetail> resourceDetails = this.list(queryWrapper);
        List<Integer> detailIds = CollectionUtils.emptyIfNull(resourceDetails).stream().map(TjResourcesDetail::getId)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(detailIds)) {
            return true;
        }
        List<Integer> usingDetailIds = sceneDetailMapper.selectUsingResources();
        if (CollectionUtils.isNotEmpty(usingDetailIds)) {
            for (Integer usingId : usingDetailIds) {
                if (detailIds.contains(usingId)) {
                    throw new BusinessException("此资源节点中存在使用中的资源");
                }
            }
        }
        Map<String, Object> param = new HashMap<>(1);
        param.put(ColumnName.RESOURCES_ID_COLUMN, resourceId);
        return this.removeByMap(param);
    }

    @Override
    public List<TjResourcesDetail> getMapSelect(String type, String sceneTreeType, String roadWayType,
                                                Integer laneNum) {
        List<TjResourcesDetail> details = new ArrayList<>();
        QueryWrapper<TjResourcesDetail> detailQueryWrapper = new QueryWrapper();
        if (StringUtils.isNotEmpty(type)) {
            QueryWrapper<TjResources> resourceQueryWrapper = new QueryWrapper();
            resourceQueryWrapper.eq(ColumnName.TYPE_COLUMN, type).select(ColumnName.ID_COLUMN);
            List<TjResources> tjResources = resourcesMapper.selectList(resourceQueryWrapper);
            if (CollectionUtils.isEmpty(tjResources)) {
                return details;
            }

            detailQueryWrapper.in(ColumnName.RESOURCES_ID_COLUMN,
                    tjResources.stream().map(TjResources::getId).collect(Collectors.toList()));
        }
        details = this.list(detailQueryWrapper);
        return CollectionUtils.emptyIfNull(details).stream().filter(item -> {
                    boolean a = StringUtils.isEmpty(sceneTreeType) || (!ObjectUtils.isEmpty(item.getAttribute1())
                            && StringUtils.equals(item.getAttribute1(), sceneTreeType));
                    boolean b = StringUtils.isEmpty(roadWayType) || (!ObjectUtils.isEmpty(item.getAttribute2())
                            && StringUtils.equals(item.getAttribute2(), roadWayType));
                    boolean c = ObjectUtils.isEmpty(laneNum) || (!ObjectUtils.isEmpty(item.getAttribute3())
                            && StringUtils.equals(item.getAttribute3(), String.valueOf(laneNum)));
                    return a && b && c;
                }).sorted(Comparator.comparingInt(TjResourcesDetail::getCollectStatus)
                        .reversed()
                        .thenComparing(TjResourcesDetail::getCreatedDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TjResourcesDetail> selectAllResource() {
        return this.list(null);
    }
}
