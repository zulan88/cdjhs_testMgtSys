package net.wanji.web.controller.business;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.*;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.dto.TjAtlasTreeDto;
import net.wanji.business.domain.dto.TjAtlasVenueDto;
import net.wanji.business.entity.InfinteMileScence;
import net.wanji.business.entity.TjAtlasVenue;
import net.wanji.business.entity.TjFragmentedSceneDetail;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.service.ITjAtlasTreeService;
import net.wanji.business.service.ITjAtlasVenueService;
import net.wanji.business.service.InfinteMileScenceService;
import net.wanji.business.service.TjFragmentedSceneDetailService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地图库
 */
@Api(tags = "地图库模块相关接口")
@RestController
@RequestMapping("/atlas")
public class AtlasController extends BaseController {

    @Autowired
    private ITjAtlasTreeService tjAtlasTreeService;//地图树服务

    @Autowired
    private ITjAtlasVenueService tjAtlasVenueService;//地图模块场地服务

    @Autowired
    private InfinteMileScenceService infinteMileScenceService;

    @Autowired
    private TjFragmentedSceneDetailService tjFragmentedSceneDetailService;


    @ApiOperationSort(1)
    @ApiOperation(value = "地图模块初始化，加载左侧地图树")
    @GetMapping("/init")
    public AjaxResult init() {
        return AjaxResult.success(tjAtlasTreeService.init());
    }


    @ApiOperationSort(2)
    @ApiOperation(value = "保存修改地图树")
    @PostMapping("/saveTree")
    public AjaxResult saveTree(@Validated(Constants.InsertGroup.class) @RequestBody TjAtlasTreeDto tjAtlasTreeDto) {
        return tjAtlasTreeService.saveOrUpdateTree(tjAtlasTreeDto)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

    @ApiOperationSort(3)
    @ApiOperation(value = "删除地图库树")
    @GetMapping("/deleteTree")
    public AjaxResult deleteTree(@RequestParam("id") Integer id) throws BusinessException {
        return tjAtlasTreeService.deleteTree(id)
                ? AjaxResult.success("删除成功")
                : AjaxResult.error("删除失败");
    }

    @ApiOperationSort(4)
    @ApiOperation(value = "保存修改测试场地")
    @PostMapping("saveVenue")
    public AjaxResult saveVenue(@Validated(Constants.InsertGroup.class) @RequestBody TjAtlasVenueDto tjAtlasVenueDto){

        return tjAtlasVenueService.saveOrUpdateVenue(tjAtlasVenueDto)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

    @ApiOperationSort(5)
    @ApiOperation(value = "根据地图树查询测试场地")
    @PostMapping("getVenue")
    public AjaxResult getVenue(@Validated(Constants.QueryGroup.class) @RequestBody TjAtlasVenueDto tjAtlasVenueDto) throws BusinessException {

        List<TjAtlasVenue> atlasVenueList = tjAtlasVenueService.getAtlasVenueData(tjAtlasVenueDto);

        return AjaxResult.success(atlasVenueList);
    }

    @ApiOperationSort(6)
    @ApiOperation(value = "删除测试场地")
    @GetMapping("deleteVenue")
    public AjaxResult deleteVenue(@RequestParam("id") Integer id) throws BusinessException{
        QueryWrapper<TjFragmentedSceneDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("map_id", id);
        if (tjFragmentedSceneDetailService.count(queryWrapper) > 0) {
            return AjaxResult.error("该场地下有片段式场景，请先删除碎片场景");
        }
        QueryWrapper<InfinteMileScence> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("map_id", id);
        if (infinteMileScenceService.count(queryWrapper1) > 0) {
            return AjaxResult.error("该场地下有无限里程场景，请先删除无限里程场景");
        }
        return tjAtlasVenueService.deleteVenueById(id)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

}
