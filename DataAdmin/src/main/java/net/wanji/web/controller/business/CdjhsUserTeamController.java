package net.wanji.web.controller.business;

import java.util.List;

import net.wanji.business.domain.CdjhsUserTeam;
import net.wanji.business.service.ICdjhsUserTeamService;
import net.wanji.common.annotation.Log;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import net.wanji.common.enums.BusinessType;
import net.wanji.common.utils.poi.ExcelUtil;
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
 * 用户团队关联Controller
 * 
 * @author ruoyi
 * @date 2024-08-02
 */
@RestController
@RequestMapping("/system/team")
public class CdjhsUserTeamController extends BaseController
{
    @Autowired
    private ICdjhsUserTeamService cdjhsUserTeamService;

    /**
     * 查询用户团队关联列表
     */
    // @PreAuthorize("@ss.hasPermi('system:team:list')")
    @GetMapping("/list")
    public TableDataInfo list(CdjhsUserTeam cdjhsUserTeam)
    {
        startPage();
        List<CdjhsUserTeam> list = cdjhsUserTeamService.selectCdjhsUserTeamList(cdjhsUserTeam);
        return getDataTable(list);
    }

    /**
     * 导出用户团队关联列表
     */
    // @PreAuthorize("@ss.hasPermi('system:team:export')")
    @Log(title = "用户团队关联", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(CdjhsUserTeam cdjhsUserTeam)
    {
        List<CdjhsUserTeam> list = cdjhsUserTeamService.selectCdjhsUserTeamList(cdjhsUserTeam);
        ExcelUtil<CdjhsUserTeam> util = new ExcelUtil<CdjhsUserTeam>(CdjhsUserTeam.class);
        return util.exportExcel(list, "用户团队关联数据");
    }

    /**
     * 获取用户团队关联详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:team:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cdjhsUserTeamService.selectCdjhsUserTeamById(id));
    }

    /**
     * 新增用户团队关联
     */
    // @PreAuthorize("@ss.hasPermi('system:team:add')")
    @Log(title = "用户团队关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CdjhsUserTeam cdjhsUserTeam)
    {
        return toAjax(cdjhsUserTeamService.insertCdjhsUserTeam(cdjhsUserTeam));
    }

    /**
     * 修改用户团队关联
     */
    // @PreAuthorize("@ss.hasPermi('system:team:edit')")
    @Log(title = "用户团队关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CdjhsUserTeam cdjhsUserTeam)
    {
        return toAjax(cdjhsUserTeamService.updateCdjhsUserTeam(cdjhsUserTeam));
    }

    /**
     * 删除用户团队关联
     */
    // @PreAuthorize("@ss.hasPermi('system:team:remove')")
    @Log(title = "用户团队关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cdjhsUserTeamService.deleteCdjhsUserTeamByIds(ids));
    }
}
