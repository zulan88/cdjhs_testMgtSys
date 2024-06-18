package net.wanji.web.controller.business;

import com.github.pagehelper.PageHelper;
import net.wanji.approve.service.RecordReService;
import net.wanji.business.common.Constants.BatchGroup;
import net.wanji.business.common.Constants.ContentTemplate;
import net.wanji.business.common.Constants.DeleteGroup;
import net.wanji.business.common.Constants.InsertGroup;
import net.wanji.business.common.Constants.QueryGroup;
import net.wanji.business.common.Constants.UpdateGroup;
import net.wanji.business.domain.BusinessTreeSelect;
import net.wanji.business.domain.dto.TjCaseDto;
import net.wanji.business.domain.dto.TjDeviceDetailDto;
import net.wanji.business.domain.dto.TjDeviceDto;
import net.wanji.business.domain.dto.TreeTypeDto;
import net.wanji.business.domain.vo.CaseVo;
import net.wanji.business.domain.vo.DeviceDetailVo;
import net.wanji.business.entity.TjCase;
import net.wanji.business.entity.TjDevice;
import net.wanji.business.entity.TjFragmentedScenes;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.service.TjDeviceDetailService;
import net.wanji.business.service.TjDeviceService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/29 13:23
 * @Descriptoin:
 */
@ApiIgnore
@RestController
@RequestMapping("/devices")
public class DeviceController extends BaseController {

    @Autowired
    private TjDeviceService deviceService;

    @Autowired
    private TjDeviceDetailService deviceDetailService;

    @Autowired
    RecordReService recordReService;

    //@PreAuthorize("@ss.hasPermi('devices:init')")
    @GetMapping("/init")
    public AjaxResult init() {
        return AjaxResult.success(deviceService.init());
    }

    //@PreAuthorize("@ss.hasPermi('devices:saveTreeType')")
    @PostMapping("/saveTreeType")
    public AjaxResult saveTreeType(@Validated @RequestBody TreeTypeDto treeTypeDto) throws BusinessException {
        return deviceService.saveTreeType(treeTypeDto)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

    //@PreAuthorize("@ss.hasPermi('devices:deleteTreeType')")
    @GetMapping("/deleteTreeType/{dictCode}")
    public AjaxResult deleteTreeType(@PathVariable("dictCode") Long dictCode) throws BusinessException {
        return deviceService.deleteTreeType(dictCode)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

    //@PreAuthorize("@ss.hasPermi('devices:selectTree')")
    @GetMapping("/selectTree")
    public AjaxResult selectTree(@RequestParam(value = "type") String type,
                                 @RequestParam(value = "name", required = false) String name) {
        List<TjDevice> usingDevices = deviceService.selectUsingDeviceTree(type);
        List<BusinessTreeSelect> tree = deviceService.buildDevicesTreeSelect(usingDevices, name);
        return AjaxResult.success(tree);
    }

    //@PreAuthorize("@ss.hasPermi('devices:saveDevicesTree')")
    @PostMapping("/saveDevicesTree")
    public AjaxResult saveDevicesTree(@Validated @RequestBody TjDeviceDto deviceDto) {
        return deviceService.saveDevicesTree(deviceDto)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

    //@PreAuthorize("@ss.hasPermi('devices:deleteDeviceTree')")
    //@PreAuthorize("@ss.hasPermi('devices:deleteDeviceTree')")
    @GetMapping("/deleteDeviceTree/{id}")
    public AjaxResult deleteDeviceTree(@PathVariable("id") Integer id) throws BusinessException {
        return deviceService.deleteDeviceTree(id)
                ? AjaxResult.success("删除成功")
                : AjaxResult.error("删除失败");
    }
    
    //@PreAuthorize("@ss.hasPermi('devices:pageForDevice')")
    @PostMapping("/pageForDevice")
    public TableDataInfo pageForDevice(@Validated(value = QueryGroup.class) @RequestBody TjDeviceDetailDto deviceDto) {
        PageHelper.startPage(deviceDto.getPageNum(), deviceDto.getPageSize());
        return getDataTable(deviceDetailService.getAllDevices(deviceDto));
    }

    //@PreAuthorize("@ss.hasPermi('devices:save')")
    @PostMapping("/save")
    public AjaxResult save(@Validated(value = {InsertGroup.class, UpdateGroup.class})
                               @RequestBody TjDeviceDetailDto deviceDto) {
        return deviceDetailService.saveDevice(deviceDto) ? AjaxResult.success("保存成功") : AjaxResult.error("保存失败");
    }

    //@PreAuthorize("@ss.hasPermi('devices:detail')")
    @PostMapping("/detail")
    public AjaxResult detail(@Validated(value = DeleteGroup.class) @RequestBody TjDeviceDetailDto deviceDto) {
        return AjaxResult.success(deviceDetailService.getDeviceDetail(deviceDto));
    }

    //@PreAuthorize("@ss.hasPermi('devices:delete')")
    @PostMapping("/delete")
    public AjaxResult delete(@Validated(value = DeleteGroup.class) @RequestBody TjDeviceDetailDto deviceDto) {
        List<Integer> ids = recordReService.getrecordBydevice(deviceDto.getDeviceId());
        if (ids.size() > 0) {
            return AjaxResult.error("该设备已被预约使用，无法删除");
        }
        return deviceDetailService.deleteDevice(deviceDto.getDeviceId())
                ? AjaxResult.success("删除成功")
                : AjaxResult.error("删除失败");
    }

    //@PreAuthorize("@ss.hasPermi('devices:batchDelete')")
    @PostMapping("/batchDelete")
    public AjaxResult batchDelete(@Validated(value = BatchGroup.class) @RequestBody TjDeviceDetailDto deviceDto) {
        return deviceDetailService.batchDeleteDevice(deviceDto.getDeviceIds())
                ? AjaxResult.success("删除成功")
                : AjaxResult.error("删除失败");
    }

    //@PreAuthorize("@ss.hasPermi('devices:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response,
                       @Validated(value = BatchGroup.class) @RequestBody TjDeviceDetailDto tjCaseDto) {
        List<DeviceDetailVo> devices = deviceDetailService.getAllDevices(tjCaseDto);
        String fileName = StringUtils.format(ContentTemplate.EXPORT_NAME_TEMPLATE, devices.get(0).getDeviceName(),
                DateUtils.getNowSecondString());
        ExcelUtil<DeviceDetailVo> util = new ExcelUtil<>(DeviceDetailVo.class);
        util.exportExcel(response, devices, fileName);
    }
}
