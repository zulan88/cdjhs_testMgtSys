package net.wanji.web.controller.business;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.dto.TjAtlasTreeDto;
import net.wanji.business.entity.CdjhsRefereeScoring;
import net.wanji.business.service.CdjhsRefereeScoringService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wj
 * @since 2024-08-26
 */
@RestController
@RequestMapping("/referee-scoring")
public class CdjhsRefereeScoringController extends BaseController {

    @Resource
    private CdjhsRefereeScoringService refereeScoringService;


    @ApiOperationSort(1)
    @ApiOperation(value = "定时调用 根据任务表中任务状态，获取正在进行的任务")
    @GetMapping("/getPageShowType")
    public AjaxResult list() {
        // Integer taskId, Integer teamId
        // TODO 根据任务表中任务状态，获取正在进行的任务，有且只有一个，返回taskId,teamId
        // TODO taskId,teamId为空，裁判显示等待比赛开始页面，裁判长清空打分明细列表
        return AjaxResult.success();
    }

    @ApiOperationSort(1)
    @ApiOperation(value = "裁判长：根据 getPageShowType 获取任务id，队伍id，获取打分明细")
    @GetMapping("/list")
    public AjaxResult list(Integer taskId, Integer teamId) {
        return AjaxResult.success(refereeScoringService.list(taskId, teamId));
    }

    @ApiOperationSort(2)
    @ApiOperation(value = "裁判员：打分")
    @PostMapping("/save")
    public AjaxResult saveTree(@RequestBody CdjhsRefereeScoring refereeScoring) {
        return refereeScoringService.save(refereeScoring)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

    @ApiOperationSort(3)
    @ApiOperation(value = "裁判员：打分进度")
    @PostMapping("/getScoreData")
    public AjaxResult getScoreData(Integer taskId, Integer teamId) {
        return AjaxResult.success(refereeScoringService.getScoreData(taskId, teamId));
    }

}
