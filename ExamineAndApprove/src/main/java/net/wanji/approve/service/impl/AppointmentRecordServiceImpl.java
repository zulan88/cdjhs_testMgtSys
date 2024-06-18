package net.wanji.approve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.wanji.approve.entity.AppointmentRecord;
import net.wanji.approve.entity.TjTesteeObjectInfo;
import net.wanji.approve.entity.TjWorkers;
import net.wanji.approve.entity.dto.AppointmentRecordDto;
import net.wanji.approve.entity.vo.AppointmentRecordVo;
import net.wanji.approve.mapper.AppointmentRecordMapper;
import net.wanji.approve.service.AppointmentRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.approve.service.TjTesteeObjectInfoService;
import net.wanji.approve.service.TjWorkersService;
import net.wanji.business.domain.vo.CasePageVo;
import net.wanji.business.entity.TjCasePartConfig;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.service.TjCasePartConfigService;
import net.wanji.business.service.TjCaseService;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 预约记录表 服务实现类
 * </p>
 *
 * @author wj
 * @since 2023-12-05
 */
@Service
public class AppointmentRecordServiceImpl extends ServiceImpl<AppointmentRecordMapper, AppointmentRecord> implements AppointmentRecordService {

    @Autowired
    TjTesteeObjectInfoService tjTesteeObjectInfoService;
    ;

    @Autowired
    TjCaseService tjCaseService;

    @Autowired
    TjCasePartConfigService tjCasePartConfigService;


    @Override
    public List<AppointmentRecord> listByEntity(AppointmentRecordDto appointmentRecord) {
        if (appointmentRecord == null) {
            return this.list();
        } else {
            QueryWrapper<AppointmentRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper
                    .eq(!StringUtils.isEmpty(appointmentRecord.getUnitName()), "unit_name", appointmentRecord.getUnitName())
                    .eq(!StringUtils.isEmpty(appointmentRecord.getContactPerson()), "contact_person", appointmentRecord.getContactPerson())
                    .eq(!StringUtils.isEmpty(appointmentRecord.getMeasurandType()), "measurand_type", appointmentRecord.getMeasurandType())
                    .eq(!StringUtils.isEmpty(appointmentRecord.getMeasurandName()), "measurand_name", appointmentRecord.getMeasurandName())
                    .between(appointmentRecord.dateExists(), "commit_date", appointmentRecord.getStartDate(), appointmentRecord.getEndDate())
                    .between(appointmentRecord.appDateExists(), "last_approval_time", appointmentRecord.getAppstartDate(), appointmentRecord.getAppendDate())
                    .eq(!StringUtils.isEmpty(appointmentRecord.getUnitName()), "create_by", appointmentRecord.getCreateBy())
                    .eq(appointmentRecord.getStatus() != null && !appointmentRecord.getStatus().equals(99), "status", appointmentRecord.getStatus())
                    .ne(appointmentRecord.getStatus() != null && appointmentRecord.getStatus().equals(99), "status", 4);

            queryWrapper.orderByDesc("commit_date");
            return this.list(queryWrapper);
        }
    }

    @Override
    public AppointmentRecordVo getInfoById(Integer id) throws BusinessException {
        AppointmentRecordVo appointmentRecordVo = new AppointmentRecordVo();
        if (id != null) {
            AppointmentRecord appointmentRecord = this.getById(id);
            BeanUtils.copyBeanProp(appointmentRecordVo, appointmentRecord);
        } else {
            throw new BusinessException("ID为空");
        }
        if (appointmentRecordVo.getMeasurandId() != null) {
            appointmentRecordVo.setTjTesteeObjectInfo(tjTesteeObjectInfoService.getById(appointmentRecordVo.getMeasurandId()));
        } else {
            throw new BusinessException("测量对象ID为空");
        }

        if(appointmentRecordVo.getCaseIds() == null){
            return appointmentRecordVo;
        }

        List<Integer> ids = Arrays.stream(appointmentRecordVo.getCaseIds().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Long expense = tjCaseService.takeExpense(ids);
        if (expense != null) {
            appointmentRecordVo.setExpense(Math.toIntExact(expense));
        } else {
            appointmentRecordVo.setExpense(0);
        }
        return appointmentRecordVo;
    }

    @Override
    public Long getExpense(Integer id) {
        AppointmentRecord appointmentRecord = this.getById(id);
        if (appointmentRecord == null) {
            return 0L;
        }

        if (StringUtils.isEmpty(appointmentRecord.getCaseIds())) {
            return 0L;
        }

        List<Integer> ids = Arrays.stream(appointmentRecord.getCaseIds().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return tjCaseService.takeExpense(ids);
    }

    @Override
    public List<AppointmentRecord> getByids(List<Integer> ids) {
        if (ids == null || ids.size() == 0) {
            return new ArrayList<>();
        }
        QueryWrapper<AppointmentRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        return this.list(queryWrapper);
    }

    @Override
    public List<CasePageVo> pageList(Integer id, Integer treeId) {
        AppointmentRecord appointmentRecord = this.getById(id);
        if (appointmentRecord.getCaseIds() == null) {
            return new ArrayList<>();
        }
        if (appointmentRecord.getCaseIds().isEmpty()) {
            return new ArrayList<>();
        }
        List<Integer> ids = Arrays.stream(appointmentRecord.getCaseIds().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        List<CasePageVo> casePageVos = tjCaseService.pageListByIds(ids, treeId);
        return casePageVos;
    }

    @Override
    public AppointmentRecord addApprove(AppointmentRecord appointmentRecord) {
        AppointmentRecord byId = getById(appointmentRecord.getId());

        // 新增
        if (byId == null) {
            String recordId = StringUtils.generateRandomString(12);
            appointmentRecord.setRecordId(recordId);
            appointmentRecord.setCreateBy(SecurityUtils.getUsername());
            appointmentRecord.setStatus(4);
            save(appointmentRecord);

            QueryWrapper<AppointmentRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("record_id", recordId);
            return getOne(queryWrapper);
        } else {
            // 修改
            updateById(appointmentRecord);
            return getById(appointmentRecord.getId());
        }
    }

    @Override
    public Long getExpenseByCaseIds(String caseIds) {
        if (StringUtils.isEmpty(caseIds)) {
            return 0L;
        }

        List<Integer> ids = Arrays.stream(caseIds.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return tjCaseService.takeExpense(ids);
    }

    @Override
    public List<Integer> getdeviceIdsByCase(Integer id) {
        AppointmentRecord appointmentRecord = this.getById(id);
        List<Integer> caseIds = Arrays.stream(appointmentRecord.getCaseIds().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        if (caseIds.size() > 0) {
            QueryWrapper<TjCasePartConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("case_id", caseIds);
            List<Integer> deviceIds = tjCasePartConfigService.list(queryWrapper).stream().map(TjCasePartConfig::getDeviceId).collect(Collectors.toList());
            List<Integer> distinctDeviceIds = deviceIds.stream().distinct().collect(Collectors.toList());
            return distinctDeviceIds;
        } else {
            return null;
        }
    }


}
