package net.wanji.web.controller.business;

import lombok.extern.slf4j.Slf4j;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.domain.vo.CdjhsErSort;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.exercise.dto.luansheng.StatResult;
import net.wanji.business.exercise.dto.luansheng.TaskCacheDto;
import net.wanji.business.exercise.enums.TaskStatusEnum;
import net.wanji.business.service.ICdjhsExerciseRecordService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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

    @Autowired
    private RedisCache redisCache;

    @GetMapping("/list")
    public TableDataInfo list(CdjhsExerciseRecord cdjhsExerciseRecord){
        startPage();
        List<CdjhsExerciseRecord> list = cdjhsExerciseRecordService.selectCdjhsCompetitionRecordList(cdjhsExerciseRecord);
        return getDataTable(list);
    }

    @CrossOrigin
    @GetMapping("/listTW")
    public TableDataInfo listTW(CdjhsExerciseRecord cdjhsExerciseRecord){
        startPage();
        List<CdjhsExerciseRecord> list = cdjhsExerciseRecordService.selectCdjhsCompetitionRecordListTW(cdjhsExerciseRecord);
        return getDataTable(list);
    }

    @CrossOrigin
    @PutMapping("/updateTW")
    public AjaxResult edit(@RequestBody CdjhsExerciseRecord cdjhsExerciseRecord)
    {
        return toAjax(cdjhsExerciseRecordService.updateCdjhsExerciseRecord(cdjhsExerciseRecord));
    }

    @PostMapping("/add")
    public AjaxResult add(@RequestBody CdjhsExerciseRecord cdjhsExerciseRecord){
        try {
            //当是C卷时判断当前任务缓存是否打分完成
            if(cdjhsExerciseRecord.getTestPaperType() == 3){
                TaskCacheDto cache = redisCache.getCacheObject(RedisKeyUtils.CDJHS_CURRENT_TASK_CACHE);
                if(Objects.nonNull(cache)){
                    Integer scoreStatus = cache.getScoreStatus();
                    if(scoreStatus != 1){
                        return AjaxResult.error("当前任务打分未完成,请等待...");
                    }
                }
            }
            return toAjax(cdjhsExerciseRecordService.createCompetitionRecord(cdjhsExerciseRecord));
        }catch (Exception e){
            return AjaxResult.error("创建比赛任务失败");
        }
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

    @GetMapping("/getsort")
    public AjaxResult getsort(CdjhsExerciseRecord cdjhsExerciseRecord){
        List<CdjhsErSort> list = cdjhsExerciseRecordService.selectSortByScore(cdjhsExerciseRecord);
        return AjaxResult.success(list);
    }

    @GetMapping("/statTW")
    public AjaxResult stat(Long taskId){
        if(Objects.isNull(taskId)){
            return AjaxResult.error("任务id不能为空");
        }
        StatResult stat = cdjhsExerciseRecordService.stat(taskId);
        return AjaxResult.success(stat);
    }

    @GetMapping("/playbackTW")
    public AjaxResult playbackTW(Long taskId, String topic, Integer action){
        try {
            if(Objects.isNull(taskId) || Objects.isNull(action)){
                return AjaxResult.error("参数错误");
            }
            cdjhsExerciseRecordService.playbackTW(taskId, topic, action);
        } catch (BusinessException | IOException e) {
            return AjaxResult.error("回放失败");
        }
        return AjaxResult.success();
    }
}
