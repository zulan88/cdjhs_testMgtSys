package net.wanji.web.controller.business;

import java.util.List;
import java.util.stream.Collectors;

import net.wanji.business.domain.CdjhsCarDetail;
import net.wanji.business.service.ICdjhsCarDetailService;
import net.wanji.common.constant.HttpStatus;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.PageDomain;
import net.wanji.common.core.page.TableDataInfo;
import net.wanji.common.core.page.TableSupport;
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
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        List<CdjhsCarDetail> list = cdjhsCarDetailService.selectCdjhsCarDetailList(cdjhsCarDetail);
        // 获取处理好的集合
        int num = list.size();
        list = list.stream().skip((long) (pageNum - 1) * pageSize).limit(pageSize).collect(Collectors.toList());

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setData(list);
        rspData.setTotal(num);
        rspData.setMsg("查询成功");
        return rspData;
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
        //实车编号和域控编号校验
        boolean isUnique = cdjhsCarDetailService.isUnique(cdjhsCarDetail);
        if(!isUnique){
            return AjaxResult.error("新增实车编号或绑定域控编号已存在");
        }
        return toAjax(cdjhsCarDetailService.insertCdjhsCarDetail(cdjhsCarDetail));
    }

    /**
     * 修改实车信息
     */
    @PutMapping("/update")
    public AjaxResult edit(@RequestBody CdjhsCarDetail cdjhsCarDetail)
    {
        boolean isUnique = cdjhsCarDetailService.isUnique(cdjhsCarDetail);
        if(!isUnique){
            return AjaxResult.error("待更新实车编号或域控编号已存在");
        }
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
