package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.common.Constants;
import net.wanji.business.common.Constants.ColumnName;
import net.wanji.business.common.Constants.ModelEnum;
import net.wanji.business.common.Constants.SysType;
import net.wanji.business.domain.PartConfigSelect;
import net.wanji.business.domain.bo.ParticipantTrajectoryBo;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.business.domain.vo.CasePartConfigVo;
import net.wanji.business.entity.TjCasePartConfig;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjCasePartConfigMapper;
import net.wanji.business.service.TjCasePartConfigService;
import net.wanji.common.core.domain.entity.SysDictData;
import net.wanji.system.service.ISysDictDataService;
import net.wanji.system.service.ISysDictTypeService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wj
 * @since 2023-06-29
 */
@Service
public class TjCasePartConfigServiceImpl extends ServiceImpl<TjCasePartConfigMapper, TjCasePartConfig>
        implements TjCasePartConfigService {

    private static final Logger log = LoggerFactory.getLogger("business");

    @Autowired
    private ISysDictTypeService dictTypeService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Override
    public Map<String, List<CasePartConfigVo>> getConfigInfo(Integer caseId) throws BusinessException {
        List<SysDictData> partRole = dictTypeService.selectDictDataByType(SysType.PART_ROLE);
        if (CollectionUtils.isEmpty(partRole)) {
            throw new BusinessException("请配置业务字典：参与者角色part_role");
        }
        QueryWrapper<TjCasePartConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ColumnName.CASE_ID_COLUMN, caseId);
        List<TjCasePartConfig> configs = this.list(queryWrapper);
        Map<String, List<CasePartConfigVo>> roleMap = CollectionUtils.emptyIfNull(configs).stream()
                .map(item -> {
                    CasePartConfigVo casePartConfigVo = new CasePartConfigVo();
                    BeanUtils.copyProperties(item, casePartConfigVo);
                    casePartConfigVo.setParticipantRoleName(dictDataService.selectDictLabel(SysType.PART_ROLE,
                            item.getParticipantRole()));
                    casePartConfigVo.setBusinessTypeName(dictDataService.selectDictLabel(SysType.PART_TYPE,
                            item.getBusinessType()));
                    casePartConfigVo.setSelected(Boolean.TRUE);
                    return casePartConfigVo;
                }).collect(Collectors.groupingBy(TjCasePartConfig::getParticipantRole));
        Map<String, List<CasePartConfigVo>> result = new HashMap<>(partRole.size());
        for (SysDictData role : partRole) {
            result.put(role.getDictValue(), roleMap.get(role.getDictValue()));
        }
        return result;
    }

    @Override
    public List<CasePartConfigVo> getConfigInfoByCaseId(Integer caseId) throws BusinessException {
        QueryWrapper<TjCasePartConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ColumnName.CASE_ID_COLUMN, caseId);
        List<TjCasePartConfig> configs = this.list(queryWrapper);
        List<CasePartConfigVo> casePartConfigVos = configs.stream().map(item -> {
            CasePartConfigVo casePartConfigVo = new CasePartConfigVo();
            BeanUtils.copyProperties(item, casePartConfigVo);
            return casePartConfigVo;
        }).collect(Collectors.toList());
        return casePartConfigVos;
    }

    @Override
    public Map<String, List<CasePartConfigVo>> trajectory2Config(SceneTrajectoryBo sceneTrajectoryBo) {
        if (ObjectUtils.isEmpty(sceneTrajectoryBo)) {
            return new HashMap<>();
        }
        List<ParticipantTrajectoryBo> participantTrajectories = sceneTrajectoryBo.getParticipantTrajectories();
        List<CasePartConfigVo> result = CollectionUtils.emptyIfNull(participantTrajectories).stream().map(part -> {
            CasePartConfigVo config = new CasePartConfigVo();
            config.setBusinessId(part.getId());
            config.setBusinessType(part.getType());
            config.setName(part.getName());
            config.setModel(part.getModel());
            config.setModelName(ModelEnum.getDescByCode(part.getModel()));
            return config;
        }).collect(Collectors.toList());
        return result.stream().collect(Collectors.groupingBy(CasePartConfigVo::getBusinessType));
    }

    @Override
    public boolean saveFromSelected(List<PartConfigSelect> partConfigSelects) {
        List<TjCasePartConfig> saveList = new ArrayList<>();
        for (PartConfigSelect configSelect : CollectionUtils.emptyIfNull(partConfigSelects)) {
            List<TjCasePartConfig> configs = CollectionUtils.emptyIfNull(configSelect.getParts()).stream()
                    .filter(CasePartConfigVo::isSelected)
                    .map(part -> {
                        TjCasePartConfig config = new TjCasePartConfig();
                        BeanUtils.copyProperties(part, config);
                        return config;
            }).collect(Collectors.toList());
            saveList.addAll(configs);
        }
        return !CollectionUtils.isEmpty(saveList) && this.saveOrUpdateBatch(saveList);
    }

    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public boolean removeThenSave(Integer caseId, List<TjCasePartConfig> configs)
            throws BusinessException {
        if (ObjectUtils.isEmpty(caseId) || CollectionUtils.isEmpty(configs)) {
            return false;
        }
        QueryWrapper<TjCasePartConfig> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq(ColumnName.CASE_ID_COLUMN, caseId);
        this.remove(deleteWrapper);
        boolean saveBatch = this.saveBatch(configs);
        if (!saveBatch) {
            throw new BusinessException("保存角色配置失败");
        }
        return true;
    }

    @Override
    public boolean saveAndupdate(Integer caseId, List<TjCasePartConfig> configs) throws BusinessException {
        if (ObjectUtils.isEmpty(caseId) || CollectionUtils.isEmpty(configs)) {
            return false;
        }
        for (TjCasePartConfig config : configs) {
            if(config.getCaseId() == null) {
                config.setCaseId(caseId);
            }
            if(config.getId()==null && config.getParticipantRole() == null) {
                switch (config.getBusinessType()) {
                    case Constants.PartType.MAIN:
                        config.setParticipantRole(Constants.PartRole.AV);
                        break;
                    case Constants.PartType.SLAVE:
                        config.setParticipantRole(Constants.PartRole.MV_SIMULATION);
                        break;
                    case Constants.PartType.PEDESTRIAN:
                        config.setParticipantRole(Constants.PartRole.SP);
                        break;
                    default:
                        break;
                }
            }
        }
        boolean saveBatch = this.saveOrUpdateBatch(configs);
        if (!saveBatch) {
            throw new BusinessException("保存角色配置失败");
        }
        return true;
    }
}
