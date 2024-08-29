package net.wanji.web.controller.business;

import java.util.List;

import net.wanji.business.domain.CdjhsTeamInfo;
import net.wanji.business.service.ICdjhsTeamInfoService;
import net.wanji.common.annotation.Log;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.enums.BusinessType;
import net.wanji.common.utils.poi.ExcelUtil;
import org.aspectj.weaver.loadtime.Aj;
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
 * 团队信息Controller
 * 
 * @author ruoyi
 * @date 2024-08-02
 */
@RestController
@RequestMapping("/team")
public class CdjhsTeamInfoController extends BaseController
{
    @Autowired
    private ICdjhsTeamInfoService cdjhsTeamInfoService;

    /**
     * 查询团队信息列表
     */
    @GetMapping("/list")
    public AjaxResult list(CdjhsTeamInfo cdjhsTeamInfo)
    {
        List<CdjhsTeamInfo> list = cdjhsTeamInfoService.selectCdjhsTeamInfoList(cdjhsTeamInfo);
        return AjaxResult.success(list);
    }

    /**
     * 获取比赛评分排行榜
     */
    @GetMapping("/getScoreRank")
    public AjaxResult getScoreRank(){
        List<CdjhsTeamInfo> list = cdjhsTeamInfoService.getScoreRank();
        return AjaxResult.success(list);
    }

    /**
     * 导出团队信息列表
     */
    // @PreAuthorize("@ss.hasPermi('system:info:export')")
    @Log(title = "团队信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CdjhsTeamInfo cdjhsTeamInfo)
    {
        List<CdjhsTeamInfo> list = cdjhsTeamInfoService.selectCdjhsTeamInfoList(cdjhsTeamInfo);
        ExcelUtil<CdjhsTeamInfo> util = new ExcelUtil<CdjhsTeamInfo>(CdjhsTeamInfo.class);
        return util.exportExcel(list, "团队信息数据");
    }

    /**
     * 获取团队信息详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:info:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cdjhsTeamInfoService.selectCdjhsTeamInfoById(id));
    }

    /**
     * 新增团队信息
     */
    // @PreAuthorize("@ss.hasPermi('system:info:add')")
    @Log(title = "团队信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CdjhsTeamInfo cdjhsTeamInfo)
    {
        return toAjax(cdjhsTeamInfoService.insertCdjhsTeamInfo(cdjhsTeamInfo));
    }

    /**
     * 修改团队信息
     */
    // @PreAuthorize("@ss.hasPermi('system:info:edit')")
    @Log(title = "团队信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CdjhsTeamInfo cdjhsTeamInfo)
    {
        return toAjax(cdjhsTeamInfoService.updateCdjhsTeamInfo(cdjhsTeamInfo));
    }

    /**
     * 删除团队信息
     */
    // @PreAuthorize("@ss.hasPermi('system:info:remove')")
    @Log(title = "团队信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cdjhsTeamInfoService.deleteCdjhsTeamInfoByIds(ids));
    }
}
