package net.wanji.approve.service;

import net.wanji.approve.entity.AppointmentRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.approve.entity.dto.AppointmentRecordDto;
import net.wanji.approve.entity.vo.AppointmentRecordVo;
import net.wanji.business.domain.vo.CasePageVo;
import net.wanji.business.exception.BusinessException;

import java.util.List;

/**
 * <p>
 * 预约记录表 服务类
 * </p>
 *
 * @author wj
 * @since 2023-12-05
 */
public interface AppointmentRecordService extends IService<AppointmentRecord> {

    List<AppointmentRecord> listByEntity(AppointmentRecordDto appointmentRecordDto);

    AppointmentRecordVo getInfoById(Integer id) throws BusinessException;

    List<CasePageVo> pageList(Integer id, Integer treeId);

    Long getExpense(Integer id);

    List<AppointmentRecord> getByids(List<Integer> ids);

    AppointmentRecord addApprove(AppointmentRecord appointmentRecord);

    Long getExpenseByCaseIds(String caseIds);

    List<Integer> getdeviceIdsByCase(Integer id);
}
