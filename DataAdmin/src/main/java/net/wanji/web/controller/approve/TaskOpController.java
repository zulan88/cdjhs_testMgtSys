package net.wanji.web.controller.approve;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.wanji.approve.entity.AppointmentRecord;
import net.wanji.approve.entity.TjTesteeObjectInfo;
import net.wanji.approve.service.AppointmentRecordService;
import net.wanji.approve.service.TjTesteeObjectInfoService;
import net.wanji.business.domain.bo.TaskBo;
import net.wanji.business.domain.vo.RoleVo;
import net.wanji.business.entity.TjTask;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.service.TjTaskCaseService;
import net.wanji.business.service.TjTaskService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/taskop")
public class TaskOpController extends BaseController {

    @Autowired
    private TjTaskService tjTaskService;

    @Autowired
    private AppointmentRecordService appointmentRecordService;

    @Autowired
    private TjTaskCaseService taskCaseService;

    @Autowired
    TjTesteeObjectInfoService tjTesteeObjectInfoService;

    @PostMapping("/prestore/{id}")
    public AjaxResult prestore(@PathVariable Integer id) throws ParseException, BusinessException {
        AppointmentRecord record = appointmentRecordService.getById(id);
        TjTesteeObjectInfo tjTesteeObjectInfo = tjTesteeObjectInfoService.getById(record.getMeasurandId());
        TaskBo taskBo = new TaskBo();
        taskBo.getAvDeviceIds().add(tjTesteeObjectInfo.getDeviceId());
        taskBo.setClient(record.getUnitName());
        taskBo.setConsigner(record.getContactPerson());
        taskBo.setContract(record.getPhoneNumber());
        List<String> dates = Arrays.asList(record.getExpectedDate().split(","));
        dates.sort(new StringDateComparator());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        taskBo.setStartTime(dateFormat.parse(dates.get(0)));
        taskBo.setEndTime(dateFormat.parse(dates.get(dates.size() - 1)));
        taskBo.setProcessNode(1);
        taskBo.setTestType("virtualRealFusion"); // 先写死
        taskBo.setIsInner(1);
        taskBo.setApprecordId(record.getId());
        taskBo.setMeasurandId(record.getMeasurandId());
        int taskId = tjTaskService.saveTask(taskBo);
        List<Integer> caseIds = Arrays.stream(record.getCaseIds().split(",")).map(Integer::parseInt).collect(Collectors.toList());
        taskCaseService.addTaskCase(taskId, caseIds);
        TaskBo taskBo2 = new TaskBo();
        taskBo2.setId(taskId);
        taskBo2.setProcessNode(2);
        tjTaskService.saveTask(taskBo2);
        return AjaxResult.success(taskId);
    }

    @PostMapping("/recheckorcancel")
    public AjaxResult recheckorcancel(@RequestBody TjTask tjTask) throws BusinessException {
        return toAjax(tjTaskService.updateById(tjTask));
    }

    @GetMapping("/detect")
    public AjaxResult detect() {
        QueryWrapper<TjTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_inner",1);
        List<TjTask> tjTaskList = tjTaskService.list(queryWrapper);
        List<RoleVo> roleVos = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0,0);
        map.put(1,0);
        map.put(4,0);
        map.put(5,0);
        map.put(6,0);
        for (TjTask tjTask : tjTaskList) {
            Integer count = map.getOrDefault(tjTask.getOpStatus(),0);
            map.put(tjTask.getOpStatus(),count+1);
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            switch (entry.getKey()) {
                case 0:
                    RoleVo roleVo = new RoleVo("待测试", entry.getValue().toString());
                    roleVos.add(roleVo);
                    break;
                case 1:
                    roleVos.add(new RoleVo("进行中", entry.getValue().toString()));
                    break;
                case 4:
                    roleVos.add(new RoleVo("逾期", entry.getValue().toString()));
                    break;
                case 5:
                    roleVos.add(new RoleVo("复审通过", entry.getValue().toString()));
                    break;
                case 6:
                    roleVos.add(new RoleVo("复审失败", entry.getValue().toString()));
                }
        }
        return AjaxResult.success(roleVos);
    }

}

class StringDateComparator implements Comparator<String> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public int compare(String date1, String date2) {
        try {
            // 将日期字符串解析为Date对象，然后比较
            return dateFormat.parse(date1).compareTo(dateFormat.parse(date2));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
    }
}
