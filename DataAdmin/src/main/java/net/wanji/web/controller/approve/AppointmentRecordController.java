package net.wanji.web.controller.approve;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import net.wanji.approve.entity.AppointmentRecord;
import net.wanji.approve.entity.TjDeviceDetail;
import net.wanji.approve.entity.dto.AppointmentRecordDto;
import net.wanji.approve.service.AppointmentRecordService;
import net.wanji.approve.service.RecordReService;
import net.wanji.approve.service.TjDeviceDetailApService;
import net.wanji.approve.utils.CacheTools;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.vo.CasePageVo;
import net.wanji.business.domain.vo.RoleVo;
import net.wanji.business.exception.BusinessException;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/approve")
public class AppointmentRecordController extends BaseController {

    @Autowired
    private AppointmentRecordService appointmentRecordService;

    @Autowired
    TjDeviceDetailApService tjDeviceDetailApService;

    @Autowired
    RecordReService recordReService;

    @PostConstruct
    public void init() {
        List<AppointmentRecord> list = appointmentRecordService.list();
        for (AppointmentRecord appointmentRecord : list){
            CacheTools.put(appointmentRecord.getId(), appointmentRecord.getUnitName()+" "+appointmentRecord.getType()+" "+appointmentRecord.getMeasurandType());
        }
    }

    @GetMapping("/list")
    public TableDataInfo list(AppointmentRecordDto dto) {
        startPage();
        List<AppointmentRecord> list = appointmentRecordService.listByEntity(dto);
        return getDataTable(list);
    }

    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody AppointmentRecord appointmentRecord) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(appointmentRecord.getStatus()!=null){
            appointmentRecord.setLastApprovalTime(LocalDateTime.now().format(formatter));
            if(appointmentRecord.getStatus()==1){
                UpdateWrapper<AppointmentRecord> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("expense",null);
                updateWrapper.eq("id",appointmentRecord.getId());
                updateWrapper.set("status",1);
                boolean i = appointmentRecordService.update(appointmentRecord,updateWrapper);
                return toAjax(i);
            }
        }
        return toAjax(appointmentRecordService.updateById(appointmentRecord));
    }

    @GetMapping("/getinfo/{id}")
    public AjaxResult getinfo(@PathVariable Integer id) throws BusinessException {
        return AjaxResult.success(appointmentRecordService.getInfoById(id));
    }

    @GetMapping("/getcase")
    public TableDataInfo getcase(Integer id) throws BusinessException {
        startPage();
        List<CasePageVo> list = appointmentRecordService.pageList(id, -1);
        return getDataTable(list);
    }

    @GetMapping("/expense")
    public AjaxResult getcaselist(Integer id) throws BusinessException {
        return AjaxResult.success(appointmentRecordService.getExpense(id));
    }

    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Integer id) throws BusinessException {
        CacheTools.remove(id);
        return toAjax(appointmentRecordService.removeById(id));
    }

    @GetMapping("/devicetoschedule")
    public TableDataInfo devicetoschedule(Integer deviceId) throws BusinessException {
        List<Integer> ids = recordReService.getrecordBydevice(deviceId);
        startPage();
        List<AppointmentRecord> list = appointmentRecordService.getByids(ids);
        return getDataTable(list);
    }

    /**
     * 获取字典
     * 获取测试类型和被测对象类型的字典数据
     */
    @GetMapping("/getdict")
    public AjaxResult getdict(String type) throws BusinessException {
        List<RoleVo> roleList = new ArrayList<>();
        if(type.equals("task")){
            roleList.add(new RoleVo("虚实融合", Constants.TestType.VIRTUAL_REAL_FUSION));
        }else if(type.equals("measurand")){
            roleList.add(new RoleVo("自动驾驶车辆", "自动驾驶车辆"));
            roleList.add(new RoleVo("域控制器", "域控制器"));
        }
        return AjaxResult.success(roleList);
    }

    @GetMapping("/pageForDevice")
    public TableDataInfo pageForDevice() throws BusinessException {
        startPage();
        QueryWrapper<TjDeviceDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("is_inner", 1);
        List<TjDeviceDetail> list = tjDeviceDetailApService.list(queryWrapper);
        return getDataTable(list);
    }

}
