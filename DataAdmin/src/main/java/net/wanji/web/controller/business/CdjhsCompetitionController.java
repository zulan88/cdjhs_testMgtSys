package net.wanji.web.controller.business;

import lombok.extern.slf4j.Slf4j;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.exercise.enums.TaskStatusEnum;
import net.wanji.business.service.ICdjhsExerciseRecordService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: jenny
 * @create: 2024-08-05 9:00 上午
 */
@Slf4j
@RestController
@RequestMapping("/competition")
public class CdjhsCompetitionController extends BaseController {
    @Autowired
    private ICdjhsExerciseRecordService cdjhsExerciseRecordService;

    @GetMapping("/list")
    public TableDataInfo list(CdjhsExerciseRecord cdjhsExerciseRecord){
        startPage();
        List<CdjhsExerciseRecord> list = cdjhsExerciseRecordService.selectCdjhsCompetitionRecordList(cdjhsExerciseRecord);
        return getDataTable(list);
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody CdjhsExerciseRecord cdjhsExerciseRecord){
        return toAjax(cdjhsExerciseRecordService.createCompetitionRecord(cdjhsExerciseRecord));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult delete(@PathVariable Long[] ids){
        //校验是否存在进行中的任务
        List<CdjhsExerciseRecord> results = cdjhsExerciseRecordService.selectCdjhsExerciseRecordByStatusAndIds(TaskStatusEnum.RUNNING.getStatus(), ids);
        if(!results.isEmpty()){
            return AjaxResult.error("待删除记录中存在进行中的任务");
        }
        return toAjax(cdjhsExerciseRecordService.deleteCompetitionRecordByIds(ids));
    }
}
