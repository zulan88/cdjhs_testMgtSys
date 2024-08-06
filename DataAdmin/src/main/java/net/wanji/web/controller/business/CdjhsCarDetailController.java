package net.wanji.web.controller.business;

import java.util.List;

import net.wanji.business.domain.CdjhsCarDetail;
import net.wanji.business.service.ICdjhsCarDetailService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
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
 * 实车信息Controller
 * 
 * @author ruoyi
 * @date 2024-08-02
 */
@RestController
@RequestMapping("/car")
public class CdjhsCarDetailController extends BaseController
{
    @Autowired
    private ICdjhsCarDetailService cdjhsCarDetailService;

    /**
     * 查询实车信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CdjhsCarDetail cdjhsCarDetail)
    {
        startPage();
        List<CdjhsCarDetail> list = cdjhsCarDetailService.selectCdjhsCarDetailList(cdjhsCarDetail);
        return getDataTable(list);
    }

    /**
     * 导出实车信息列表
     */
    @GetMapping("/export")
    public AjaxResult export(CdjhsCarDetail cdjhsCarDetail)
    {
        List<CdjhsCarDetail> list = cdjhsCarDetailService.selectCdjhsCarDetailList(cdjhsCarDetail);
        ExcelUtil<CdjhsCarDetail> util = new ExcelUtil<CdjhsCarDetail>(CdjhsCarDetail.class);
        return util.exportExcel(list, "实车信息数据");
    }

    /**
     * 获取实车信息详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cdjhsCarDetailService.selectCdjhsCarDetailById(id));
    }

    /**
     * 新增实车信息
     */
    @PostMapping("/add")
    public AjaxResult add(@RequestBody CdjhsCarDetail cdjhsCarDetail)
    {
        return toAjax(cdjhsCarDetailService.insertCdjhsCarDetail(cdjhsCarDetail));
    }

    /**
     * 修改实车信息
     */
    @PutMapping("/update")
    public AjaxResult edit(@RequestBody CdjhsCarDetail cdjhsCarDetail)
    {
        return toAjax(cdjhsCarDetailService.updateCdjhsCarDetail(cdjhsCarDetail));
    }

    /**
     * 删除实车信息
     */
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cdjhsCarDetailService.deleteCdjhsCarDetailByIds(ids));
    }
}
