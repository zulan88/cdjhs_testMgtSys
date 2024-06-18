package net.wanji.web.controller.business;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import net.wanji.business.common.Constants.InsertGroup;
import net.wanji.business.common.Constants.OtherGroup;
import net.wanji.business.common.Constants.UpdateGroup;
import net.wanji.business.domain.BusinessTreeSelect;
import net.wanji.business.domain.dto.TjResourcesDetailDto;
import net.wanji.business.domain.dto.TjResourcesDto;
import net.wanji.business.entity.TjResources;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.service.TjResourcesDetailService;
import net.wanji.business.service.TjResourcesService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/27 16:25
 * @Descriptoin: 场景库控制器
 */
@Api(tags = "场景库-资源管理")
@RestController
@RequestMapping("/resource")
public class ResourcesController extends BaseController {

    @Autowired
    private TjResourcesService tjResourcesService;

    @Autowired
    private TjResourcesDetailService tjResourcesDetailService;

    //@PreAuthorize("@ss.hasPermi('resource:init')")
    @ApiOperationSort(1)
    @ApiOperation(value = "1.初始化")
    @GetMapping("/init")
    public AjaxResult init() {
        return AjaxResult.success(tjResourcesService.init());
    }

    //@PreAuthorize("@ss.hasPermi('resource:selectTree')")
    @ApiOperationSort(2)
    @ApiOperation(value = "2.查询文件树")
    @GetMapping("/selectTree")
    public AjaxResult selectTree(@RequestParam("type") String type,
                                 @RequestParam(value = "name", required = false) String name) {
        List<TjResources> usingResources = tjResourcesService.selectUsingResources(type);
        List<BusinessTreeSelect> tree = tjResourcesService.buildResourcesTreeSelect(usingResources, name);
        return AjaxResult.success(tree);
    }

    //@PreAuthorize("@ss.hasPermi('resource:deleteTree')")
    @ApiOperationSort(3)
    @ApiOperation(value = "3.删除文件树")
    @GetMapping("/deleteTree")
    public AjaxResult deleteTree(@RequestParam("resourceId") Integer resourceId) throws BusinessException {
        return tjResourcesService.deleteTree(resourceId)
                ? AjaxResult.success("删除成功")
                : AjaxResult.error("删除失败");
    }

    //@PreAuthorize("@ss.hasPermi('resource:saveTree')")
    @ApiOperationSort(4)
    @ApiOperation(value = "4.保存文件树")
    @PostMapping("/saveTree")
    public AjaxResult saveTree(@Validated(InsertGroup.class) @RequestBody TjResourcesDto resourcesDto) {
        return tjResourcesService.saveTree(resourcesDto)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

    //@PreAuthorize("@ss.hasPermi('resources:getDetailList')")
    @ApiOperationSort(5)
    @ApiOperation(value = "5.获取文件树下资源列表")
    @GetMapping("/getDetailList")
    public AjaxResult getDetailList(@RequestParam("resourceId") Integer resourceId,
                                    @RequestParam(value = "name", required = false) String name) {
        return AjaxResult.success(tjResourcesDetailService.getDetailList(resourceId, name));
    }

    //@PreAuthorize("@ss.hasPermi('resource:preview')")
    @ApiOperationSort(6)
    @ApiOperation(value = "6.预览")
    @PostMapping("/preview")
    public AjaxResult preview(@Validated(value = OtherGroup.class) @RequestBody TjResourcesDetailDto resourcesDetailDto)
            throws BusinessException, IOException {
        return AjaxResult.success(tjResourcesDetailService.preview(resourcesDetailDto));
    }

    //@PreAuthorize("@ss.hasPermi('resource:saveResourceDetail')")
    @ApiOperationSort(7)
    @ApiOperation(value = "7.保存资源详情")
    @PostMapping("/saveResourceDetail")
    public AjaxResult saveResourceDetail(@Validated(value = {InsertGroup.class, UpdateGroup.class})
                                             @RequestBody TjResourcesDetailDto resourcesDetailDto)
            throws BusinessException{
        return tjResourcesDetailService.saveResourcesDetail(resourcesDetailDto)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

    //@PreAuthorize("@ss.hasPermi('resource:deleteResourceDetail')")
    @ApiOperationSort(8)
    @ApiOperation(value = "8.删除资源详情")
    @GetMapping("/deleteResourceDetail")
    public AjaxResult deleteResourceDetail(@RequestParam("id") Integer id)
            throws BusinessException{
        return tjResourcesDetailService.deleteByDetailId(id)
                ? AjaxResult.success("删除成功")
                : AjaxResult.error("删除失败");
    }

    //@PreAuthorize("@ss.hasPermi('resource:collectResourceDetail')")
    @ApiOperationSort(9)
    @ApiOperation(value = "9.关联资源详情")
    @GetMapping("/collectResourceDetail")
    public AjaxResult collectResourceDetail(@RequestParam("id") Integer id) {
        return tjResourcesDetailService.collectByDetailId(id)
                ? AjaxResult.success("收藏成功")
                : AjaxResult.error("收藏失败");
    }

    //@PreAuthorize("@ss.hasPermi('resource:getResourceSelect')")
    @ApiOperationSort(10)
    @ApiOperation(value = "10.获取资源详情下拉列表")
    @GetMapping("/getResourceSelect")
    public AjaxResult getResourceSelect(@RequestParam(value = "type", required = false) String type,
                                        @RequestParam(value = "sceneTreeType", required = false) String sceneTreeType,
                                        @RequestParam(value = "roadWayType", required = false) String roadWayType,
                                        @RequestParam(value = "laneNum", required = false) Integer laneNum) {
        return AjaxResult.success(tjResourcesDetailService.getMapSelect(type, sceneTreeType, roadWayType, laneNum));
    }

}
