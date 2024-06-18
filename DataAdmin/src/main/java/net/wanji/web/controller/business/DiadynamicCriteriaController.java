package net.wanji.web.controller.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import net.wanji.business.domain.bo.DiadynamicCriteriaBo;
import net.wanji.business.entity.TjDiadynamicCriteria;
import net.wanji.business.service.TjDiadynamicCriteriaService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author: guowenhao
 * @date: 2023/8/30 16:10
 * @description: 诊断指标控制器
 */
@ApiIgnore
@RestController
@RequestMapping("/diadynamicCriteria")
public class DiadynamicCriteriaController  extends BaseController {

    @Autowired
    private TjDiadynamicCriteriaService tjDiadynamicCriteriaService;

    /**
     * 页面列表
     * @param dcBo
     * @return
     */
    //@PreAuthorize("@ss.hasPermi('diadynamicCriteria:pageList')")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DiadynamicCriteriaBo dcBo)
    {
        startPage();
        List<TjDiadynamicCriteria> list = tjDiadynamicCriteriaService.selectPageList(dcBo);
        return getDataTable(list);
    }

    /**
     * 列表
     * @return
     */
    //@PreAuthorize("@ss.hasPermi('diadynamicCriteria:list')")
    @GetMapping("/list")
    public TableDataInfo list(DiadynamicCriteriaBo dcBo)
    {
        List<TjDiadynamicCriteria> list = tjDiadynamicCriteriaService.selectPageList(dcBo);
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setData(list);
        tableDataInfo.setTotal(list.size());
        tableDataInfo.setCode(200);
        tableDataInfo.setMsg("查询成功");
        return tableDataInfo;
    }

    /**
     * 计算公式修改
     * @return
     */
    //@PreAuthorize("@ss.hasPermi('diadynamicCriteria:edit')")
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody DiadynamicCriteriaBo dcBo)
    {
        tjDiadynamicCriteriaService.editDesignConditions(dcBo);
        return AjaxResult.success();
    }

}
