package net.wanji.web.controller.business;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.dto.TjAtlasTreeDto;
import net.wanji.business.entity.CdjhsRefereeScoring;
import net.wanji.business.service.CdjhsRefereeScoringService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.domain.entity.SysRole;
import net.wanji.common.core.domain.model.LoginUser;
import net.wanji.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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


    @GetMapping("/test")
    public AjaxResult test(Integer taskId, Integer teamId, Integer entryOrder) {
        refereeScoringService.buildScoreData(taskId,teamId,entryOrder);
        return AjaxResult.success();
    }

    @ApiOperationSort(1)
    @ApiOperation(value = "裁判长：根据 getPageShowType 获取任务id，队伍id，获取打分明细")
    @GetMapping("/list")
    public AjaxResult listMaster() {
        LambdaQueryWrapper<CdjhsRefereeScoring> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(CdjhsRefereeScoring::getUserId);
        return AjaxResult.success(refereeScoringService.list(queryWrapper));
    }

    @ApiOperationSort(2)
    @ApiOperation(value = "裁判员：打分")
    @PostMapping("/save")
    public AjaxResult saveTree(@RequestBody CdjhsRefereeScoring refereeScoring) {
        return refereeScoringService.submitScore(refereeScoring)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

    @ApiOperationSort(3)
    @ApiOperation(value = "裁判员：打分进度")
    @GetMapping("/getScoreData")
    public AjaxResult getScoreData() {
        LoginUser loginUser = getLoginUser();
        Long userId = loginUser.getUserId();
        return AjaxResult.success(refereeScoringService.getScoreData(Math.toIntExact(userId)));
    }

}
