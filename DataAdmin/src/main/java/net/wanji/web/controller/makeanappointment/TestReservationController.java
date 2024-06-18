package net.wanji.web.controller.makeanappointment;

import com.github.pagehelper.PageHelper;
import io.swagger.annotations.*;
import net.wanji.approve.entity.AppointmentRecord;
import net.wanji.approve.service.AppointmentRecordService;
import net.wanji.business.domain.dto.CaseQueryDto;
import net.wanji.business.domain.vo.SceneDetailVo;
import net.wanji.business.service.TjFragmentedSceneDetailService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import net.wanji.makeanappointment.domain.vo.TestObjectVo;
import net.wanji.makeanappointment.domain.vo.TestTypeVo;
import net.wanji.makeanappointment.service.TestReservationService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName TestReservationController
 * @Description
 * @Author liruitao
 * @Date 2023-12-06
 * @Version 1.0
 **/
@Api(tags = "测试预约平台-测试预约申请")
@RestController
@RequestMapping("/testReservation")
public class TestReservationController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(TestReservationController.class);

    @Autowired
    private TestReservationService testReservationService;

    @Autowired
    private AppointmentRecordService appointmentRecordService;

    @Autowired
    private TjFragmentedSceneDetailService sceneDetailService;

    /**
     * 获取测试类型
     */
    @ApiOperationSort(1)
    @ApiOperation(value = "1.获取测试类型")
    @GetMapping("/getTestType")
    public AjaxResult getTestType() {
        try {
            return AjaxResult.success(testReservationService.getTestType());
        }catch (Exception e){
            logger.error("获取测试类型失败", e);
            return AjaxResult.error("获取测试类型失败!");
        }
    }

    @ApiOperation(value = "2.新增预约")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appointmentRecord", value = "预约信息", required = true, dataType = "AppointmentRecord")
    })
    @PostMapping("/addApprove")
    public AjaxResult addOrUpdateApprove(@RequestBody AppointmentRecord appointmentRecord) {
        try {
            return AjaxResult.success(appointmentRecordService.addApprove(appointmentRecord));
        }catch (Exception e){
            logger.error("新增预约失败", e);
            return AjaxResult.error("新增预约失败!");
        }
    }

    @ApiOperation(value = "3.根据用例id计算测试费用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "caseIds", value = "用例id", required = true)
    })
    @GetMapping("/getExpenseByCaseIds")
    public AjaxResult getExpenseByCaseIds(String caseIds) {
        try {
            return AjaxResult.success(appointmentRecordService.getExpenseByCaseIds(caseIds));
        }catch (Exception e){
            logger.error("计算测试费用失败", e);
            return AjaxResult.error("计算测试费用失败!");
        }
    }

    @ApiOperation(value = "4.查询任务用例树")
    @GetMapping("/selectTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "String", paramType = "query", example = "virtualRealFusion"),
            @ApiImplicitParam(name = "id", value = "任务ID", required = true, dataType = "Integer", paramType = "query", example = "1")
    })
    public AjaxResult selectTree(String type, Integer id, Integer sellectOrAll) {
        return AjaxResult.success(testReservationService.selectTree(type, id, sellectOrAll));
    }

    @ApiOperationSort(5)
    @ApiOperation(value = "5.测试用例列表页查询")
    @PostMapping("/pageForCase")
    public TableDataInfo pageForCase(@Validated @RequestBody CaseQueryDto caseQueryDto) {
        if (CollectionUtils.isNotEmpty(caseQueryDto.getLabelList())) {
            List<SceneDetailVo> sceneDetails;
            if (ObjectUtils.isEmpty(caseQueryDto) || 0 == caseQueryDto.getChoice()) {
                sceneDetails = sceneDetailService.selectTjSceneDetailListOr(caseQueryDto.getLabelList(), null);
            } else {
                sceneDetails = sceneDetailService.selectTjSceneDetailListAnd(caseQueryDto.getLabelList(), null);
            }
            List<Integer> sceneDetailIds = CollectionUtils.emptyIfNull(sceneDetails).stream().map(SceneDetailVo::getId)
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(sceneDetailIds)) {
                return getDataTable(sceneDetailIds);
            }
            caseQueryDto.setSceneDetailIds(sceneDetailIds);
        }
        PageHelper.startPage(caseQueryDto.getPageNum(), caseQueryDto.getPageSize());
        return getDataTable(testReservationService.pageList(caseQueryDto));
    }


    @ApiOperation(value = "6.选择任务用例")
    @GetMapping("/choiceCase")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "预约ID", required = true, dataType = "Integer", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "caseIds", value = "用例ID", required = true, dataType = "List", paramType = "query", example = "1,2,3"),
    })
    public AjaxResult choiceCase(@RequestParam("id") Integer id,
                                 @RequestParam("caseIds") List<Integer> caseIds) {
        return AjaxResult.success(testReservationService.choiceCase(id, caseIds));
    }

}
