package net.wanji.web.controller.business;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import net.wanji.business.entity.TjCaseRealRecord;
import net.wanji.business.entity.TjTaskCaseRecord;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.service.TestingService;
import net.wanji.business.service.TjCaseRealRecordService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/24 9:13
 * @Descriptoin:
 */
@Api(tags = "测试流程")
@RestController
@RequestMapping("/testing")
public class TestingController extends BaseController {

    @Autowired
    private TestingService testingService;

    @Autowired
    private TjCaseRealRecordService tjCaseRealRecordService;

    @ApiOperationSort(1)
    @ApiOperation(value = "1.重置状态")
    @GetMapping("/resetStatus")
    @ApiImplicitParam(name = "caseId", value = "用例ID", required = true, dataType = "Integer", paramType = "query", example = "278")
    public AjaxResult resetStatus(Integer caseId) throws BusinessException {
        testingService.getStatus(caseId, true);
        return AjaxResult.success();
    }

    @ApiOperationSort(2)
    @ApiOperation(value = "2.获取状态")
    @GetMapping("/getStatus")
    @ApiImplicitParam(name = "caseId", value = "用例ID", required = true, dataType = "Integer", paramType = "query", example = "278")
    public AjaxResult getStatus(Integer caseId) throws BusinessException {
        return AjaxResult.success(testingService.getStatus(caseId, false));
    }

    @ApiOperationSort(3)
    @ApiOperation(value = "3.准备")
    @GetMapping("/prepare")
    @ApiImplicitParam(name = "caseId", value = "用例ID", required = true, dataType = "Integer", paramType = "query", example = "278")
    public AjaxResult prepare(Integer caseId) throws BusinessException {
        return AjaxResult.success(testingService.prepare(caseId));
    }

    @ApiOperationSort(4)
    @ApiOperation(value = "4.开始/结束")
    @GetMapping("/controlTask")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recordId", value = "测试记录ID", required = true, dataType = "Integer", paramType = "query", example = "499"),
            @ApiImplicitParam(name = "action", value = "动作（1：开始；2：结束）", required = true, dataType = "Integer", paramType = "query", example = "1")
    })
    public AjaxResult start(Integer recordId, Integer action) throws BusinessException, IOException {
        return AjaxResult.success(testingService.controlTask(recordId));
    }

//    @GetMapping("/hjktest")
//    public AjaxResult test(Integer recordId) throws BusinessException {
//        return  AjaxResult.success(testingService.hjktest(recordId));
//    }

    @ApiOperationSort(5)
    @ApiOperation(value = "5.回放")
    @GetMapping("/playback")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recordId", value = "测试记录ID", required = true, dataType = "Integer", paramType = "query", example = "499"),
            @ApiImplicitParam(name = "action", value = "动作（1：开始；2：暂停；3：继续；4：结束）", required = true, dataType = "Integer", paramType = "query", example = "1")
    })
    public AjaxResult playback(@RequestParam("recordId") Integer recordId, @RequestParam("action") Integer action)
            throws BusinessException, IOException {
        testingService.playback(recordId, action);
        return AjaxResult.success();
    }

    @ApiOperationSort(6)
    @ApiOperation(value = "6.获取测试结果")
    @GetMapping("/getResult")
    @ApiImplicitParam(name = "recordId", value = "测试记录ID", required = true, dataType = "Integer", paramType = "query", example = "499")
    public AjaxResult getResult(@RequestParam("recordId") Integer recordId) throws BusinessException {
        return AjaxResult.success(testingService.getResult(recordId));
    }

    @ApiOperationSort(7)
    @ApiOperation(value = "7.图形列表")
    @GetMapping("/communicationDelay")
    @ApiImplicitParam(name = "recordId", value = "测试记录ID", required = true, dataType = "Integer", paramType = "query", example = "499")
    public AjaxResult communicationDelayVo(@RequestParam Integer recordId) {
        return AjaxResult.success(testingService.communicationDelayVo(recordId));
    }

    @ApiOperationSort(8)
    @ApiOperation(value = "8.保存记录状态")
    @GetMapping("/saveRecordStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recordId", value = "测试记录ID", dataType = "Integer", paramType = "query", example = "499"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "Integer", paramType = "query", example = "1")
    })
    public AjaxResult saveRecordStatus(@RequestParam("recordId") Integer recordId, @RequestParam("status") Integer status) throws BusinessException {

        TjCaseRealRecord record = tjCaseRealRecordService.getById(recordId);
        if (ObjectUtils.isEmpty(record)) {
            return AjaxResult.error("记录不存在");
        }
        if (status == 3) {
            return tjCaseRealRecordService.removeById(recordId) ? AjaxResult.success("已废弃") : AjaxResult.error("删除失败");
        }
        record.setStatus(status);
        return tjCaseRealRecordService.updateById(record) ? AjaxResult.success("保存成功") : AjaxResult.error("保存失败");
    }
}
