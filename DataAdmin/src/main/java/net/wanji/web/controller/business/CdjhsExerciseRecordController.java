package net.wanji.web.controller.business;

import java.util.List;

import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.service.ICdjhsExerciseRecordService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 练习记录Controller
 * 
 * @author ruoyi
 * @date 2024-06-19
 */
@RestController
@RequestMapping("/exercise")
public class CdjhsExerciseRecordController extends BaseController
{
    @Autowired
    private ICdjhsExerciseRecordService cdjhsExerciseRecordService;

    /**
     * 查询练习记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CdjhsExerciseRecord cdjhsExerciseRecord)
    {
        startPage();
        List<CdjhsExerciseRecord> list = cdjhsExerciseRecordService.selectCdjhsExerciseRecordList(cdjhsExerciseRecord);
        return getDataTable(list);
    }

    /**
     * 获取练习记录详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cdjhsExerciseRecordService.selectCdjhsExerciseRecordById(id));
    }

    /**
     * 新增练习记录
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody CdjhsExerciseRecord cdjhsExerciseRecord)
    {
        return toAjax(cdjhsExerciseRecordService.insertCdjhsExerciseRecord(cdjhsExerciseRecord));
    }

    /**
     * 修改练习记录
     */
    @PutMapping("/update")
    public AjaxResult edit(@RequestBody CdjhsExerciseRecord cdjhsExerciseRecord)
    {
        return toAjax(cdjhsExerciseRecordService.updateCdjhsExerciseRecord(cdjhsExerciseRecord));
    }

    /**
     * 删除练习记录
     */
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cdjhsExerciseRecordService.deleteCdjhsExerciseRecordByIds(ids));
    }
}
