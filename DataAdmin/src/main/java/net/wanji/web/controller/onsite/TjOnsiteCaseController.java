package net.wanji.web.controller.onsite;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.wanji.business.entity.TjCase;
import net.wanji.business.service.ITjScenelibService;
import net.wanji.business.service.TjCaseService;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import net.wanji.common.utils.DateUtils;
import net.wanji.onsite.entity.TjOnsiteCase;
import net.wanji.onsite.service.TjOnsiteRestService;
import net.wanji.onsite.service.TjOnsiteCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wj
 * @since 2024-06-04
 */
@RestController
@RequestMapping("/tj-onsite-case")
public class TjOnsiteCaseController extends BaseController {

    @Autowired
    private TjOnsiteCaseService tjOnsiteCaseService;

    @Autowired
    private TjOnsiteRestService tjOnsiteRestService;

    @Autowired
    private ITjScenelibService scenelibService;

    @Autowired
    private TjCaseService caseService;


    @GetMapping("/list")
    public TableDataInfo list(TjOnsiteCase tjOnsiteCase) {
        startPage();
        QueryWrapper<TjOnsiteCase> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("created_date");
        List<TjOnsiteCase> list = tjOnsiteCaseService.list(queryWrapper);
        return getDataTable(list);
    }

    @PostMapping("/commit")
    public AjaxResult commit(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            scenelibService.takeOnsiteCase(id);
        }
        return AjaxResult.success();
    }

    @GetMapping("/call")
    public AjaxResult call(String onsiteNumber) {
        String outputFolder = WanjiConfig.getScenePath() + File.separator + DateUtils.datePath();
        File folder = new File(outputFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String savepath = outputFolder + java.io.File.separator + onsiteNumber;
        tjOnsiteRestService.getOnsiteTrace(onsiteNumber, savepath);
        onsiteNumber = onsiteNumber.split("_")[0];
        //todo: 轨迹入库
        TjOnsiteCase tjOnsiteCase = tjOnsiteCaseService.getOnsiteCaseByNumId(onsiteNumber);
        tjOnsiteCase.setRoutefile(savepath);
        tjOnsiteCase.setStatus(1);
        tjOnsiteCaseService.updateById(tjOnsiteCase);
        TjCase tjCase = caseService.getById(tjOnsiteCase.getCaseId());
        tjCase.setRouteFile(savepath);
        caseService.updateById(tjCase);
        return AjaxResult.success();
    }


    @PostMapping("/uploadtest")
    public AjaxResult uploadtest(@RequestBody TjOnsiteCase tjOnsiteCase) {
        tjOnsiteCaseService.uploadToOnsite(tjOnsiteCase,tjOnsiteCase.getXodrfile(),tjOnsiteCase.getSceneLabel());
        return AjaxResult.success();
    }

    @GetMapping("/starttest")
    public AjaxResult start(String onsiteNumber) {
        return toAjax(tjOnsiteRestService.routePlanOnsite(onsiteNumber));
    }

}
