package net.wanji.web.controller.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import net.wanji.business.common.Constants;
import net.wanji.business.common.Constants.InsertGroup;
import net.wanji.business.common.Constants.UpdateGroup;
import net.wanji.business.domain.PartConfigSelect;
import net.wanji.business.domain.dto.CaseQueryDto;
import net.wanji.business.domain.dto.CaseTreeDto;
import net.wanji.business.domain.dto.TjCaseDto;
import net.wanji.business.domain.vo.CaseDetailVo;
import net.wanji.business.domain.vo.CaseOpVo;
import net.wanji.business.domain.vo.RoleVo;
import net.wanji.business.domain.vo.SceneDetailVo;
import net.wanji.business.entity.TjCaseOp;
import net.wanji.business.entity.TjTaskCaseRecord;
import net.wanji.business.entity.evaluation.single.TjTestSingleSceneScore;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.service.*;
import net.wanji.business.service.evaluation.TjTestSingleSceneScoreService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/29 13:23
 * @Descriptoin:
 */
@Api(tags = "特色测试服务-测试配置")
@RequiredArgsConstructor
@RestController
@RequestMapping("/case")
public class CaseController extends BaseController {

    private final TjCaseTreeService caseTreeService;
    private final TjCaseService caseService;
    private final TjCasePartConfigService casePartConfigService;
    private final TjFragmentedSceneDetailService sceneDetailService;
    private final TjTestSingleSceneScoreService tjTestSingleSceneScoreService;
    private final TjTaskCaseRecordService tjTaskCaseRecordService;

    @ApiOperationSort(1)
    @ApiOperation(value = "1.查询测试用例树")
    @GetMapping("/selectTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "String", paramType = "query", example = "virtualRealFusion"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "String", paramType = "query", example = "场景")
    })
    public AjaxResult selectTree(String type, String name) {
        return AjaxResult.success(caseTreeService.selectTree(type, name));
    }

    @ApiOperationSort(2)
    @ApiOperation(value = "2.保存测试用例树")
    @PostMapping("/saveTree")
    public AjaxResult saveTree(@Validated @RequestBody CaseTreeDto caseTreeDto) throws BusinessException {
        return AjaxResult.success(caseTreeService.saveTree(caseTreeDto));
    }

    @ApiOperationSort(3)
    @ApiOperation(value = "3.删除测试用例树")
    @GetMapping("/deleteTree")
    @ApiImplicitParam(name = "treeId", value = "树节点ID", required = true, dataType = "Integer", paramType = "query", example = "28")
    public AjaxResult deleteTree(Integer treeId) throws BusinessException {
        return AjaxResult.success(caseTreeService.deleteTree(treeId));
    }

    @ApiOperationSort(4)
    @ApiOperation(value = "4.测试用例列表页初始化")
    @GetMapping("/init")
    public AjaxResult init() {
        return AjaxResult.success(caseService.init());
    }

    @ApiOperationSort(5)
    @ApiOperation(value = "5.测试用例列表页查询")
    @PostMapping("/pageForCase")
    public TableDataInfo pageForCase(@Validated @RequestBody CaseQueryDto caseQueryDto) {
        if (CollectionUtils.isNotEmpty(caseQueryDto.getLabelList())) {
            List<SceneDetailVo> sceneDetails = null;
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
        return getDataTable(caseService.pageList(caseQueryDto, "not",null));
    }

    @ApiOperationSort(6)
    @ApiOperation(value = "6.查询用例详情")
    @GetMapping("/selectDetail")
    @ApiImplicitParam(name = "caseId", value = "用例id", required = true, dataType = "Integer", paramType = "query", example = "276")
    public AjaxResult selectDetail(Integer caseId) {
        return AjaxResult.success(caseService.selectCaseDetail(caseId));
    }

    //孪生专用
    @GetMapping("/selectDetailTW")
    public AjaxResult selectDetailtw(Integer taskId) {
        Integer caseId = 0;
        List<TjCaseOp> list = caseService.selectCaseOp(taskId);
        if(list.size() > 0){
            caseId = list.get(0).getId();
        }else {
            return AjaxResult.error("改任务异常，请重新进行任务配置");
        }
        CaseDetailVo caseDetailVo = caseService.selectCaseDetail(caseId);
        for (PartConfigSelect partConfig : caseDetailVo.getPartConfigSelects()){
            if(partConfig.getParts().size() > 0){
                partConfig.setParts(partConfig.getParts().stream().filter(part -> part.getId()!= null).collect(Collectors.toList()));
            }
        }
        return AjaxResult.success(caseDetailVo);
    }

    @ApiOperationSort(7)
    @ApiOperation(value = "7.创建用例")
    @PostMapping("/createCase")
    public AjaxResult createCase(@Validated(value = InsertGroup.class) @RequestBody TjCaseDto tjCaseDto)
            throws BusinessException {
        return AjaxResult.success(caseService.saveCase(tjCaseDto) ? "创建成功" : "创建失败");
    }

    @ApiOperationSort(8)
    @ApiOperation(value = "8.修改用例")
    @PostMapping("/updateCase")
    public AjaxResult updateCase(@Validated(value = UpdateGroup.class) @RequestBody TjCaseDto tjCaseDto)
            throws BusinessException {
        return AjaxResult.success(caseService.saveCase(tjCaseDto) ? "修改成功" : "修改失败");
    }

    @ApiOperationSort(9)
    @ApiOperation(value = "9.启停")
    @GetMapping("/updateStatus")
    @ApiImplicitParam(name = "caseId", value = "用例id", required = true, dataType = "Integer", paramType = "query", example = "276")
    public AjaxResult updateStatus(Integer caseId)
            throws BusinessException {
        return AjaxResult.success(caseService.updateStatus(caseId) ? "成功" : "失败");
    }

    @ApiOperationSort(10)
    @ApiOperation(value = "10.批量启停")
    @GetMapping("/batchUpdateStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "caseIds", value = "用例id", required = true, dataType = "List", paramType = "query", example = "[1,2,3]"),
            @ApiImplicitParam(name = "action", value = "动作（1：启用；2：停用）", required = true, dataType = "Integer", paramType = "query", example = "1")
    })
    public AjaxResult batchUpdateStatus(@RequestParam("caseIds") List<Integer> caseIds, @RequestParam("action") Integer action)
            throws BusinessException {
        return AjaxResult.success(caseService.batchUpdateStatus(caseIds, action) ? "成功" : "失败");
    }

    @ApiOperationSort(11)
    @ApiOperation(value = "11.删除")
    @GetMapping("/delete")
    @ApiImplicitParam(name = "caseId", value = "用例id", required = true, dataType = "Integer", paramType = "query", example = "276")
    public AjaxResult delete(Integer caseId)
            throws BusinessException {
        return caseService.batchDelete(Collections.singletonList(caseId))
                ? AjaxResult.success("删除成功")
                : AjaxResult.error("删除失败");
    }

    @ApiOperationSort(12)
    @ApiOperation(value = "12.批量删除")
    @GetMapping("/batchDelete")
    @ApiImplicitParam(name = "caseIds", value = "用例id", required = true, dataType = "List", paramType = "query", example = "1,2,3")
    public AjaxResult batchDelete(@RequestParam("caseIds") List<Integer> caseIds)
            throws BusinessException {
        return AjaxResult.success(caseService.batchDelete(caseIds) ? "成功" : "失败");
    }

    @ApiOperationSort(13)
    @ApiOperation(value = "13.查询用例设备配置")
    @GetMapping("/configDetail")
    @ApiImplicitParam(name = "id", value = "用例id", required = true, dataType = "Integer", paramType = "query", example = "276")
    public AjaxResult configDetail(@RequestParam("id") Integer id) throws BusinessException, InterruptedException,
            ExecutionException {
        return AjaxResult.success(caseService.getConfigDetail(id));
    }

    @ApiOperationSort(14)
    @ApiOperation(value = "14.保存用例设备配置")
    @PostMapping("/saveCaseDevice")
    public AjaxResult saveCaseDevice(@RequestBody List<PartConfigSelect> partConfigSelects) {
        return AjaxResult.success(casePartConfigService.saveFromSelected(partConfigSelects));
    }

    @ApiOperationSort(15)
    @ApiOperation(value = "15.编辑页初始化")
    @GetMapping("/initEdit")
    @ApiImplicitParam(name = "caseId", value = "用例ID", required = true, dataType = "Integer", paramType = "query", example = "280")
    public AjaxResult initEdit(Integer caseId) {
        return AjaxResult.success(caseService.initEdit(caseId));
    }

    @ApiOperationSort(16)
    @ApiOperation(value = "16.预览(未使用)")
    @GetMapping("/preview")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用例ID", required = true, dataType = "Integer", paramType = "query", example = "280"),
            @ApiImplicitParam(name = "action", value = "动作（1:开始; 2:暂停; 3:继续; 4:结束）", required = true, dataType = "Integer", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "vehicleId", value = "参与者ID", required = false, dataType = "Integer", paramType = "query", example = "1")
    })
    public AjaxResult preview(@RequestParam(value = "id") Integer id,
                              @RequestParam(value = "action") int action,
                              @RequestParam(value = "vehicleId", required = false) String vehicleId)
            throws BusinessException, IOException {
        caseService.playback(id, vehicleId, action);
        return AjaxResult.success();
    }

    @ApiOperationSort(17)
    @ApiOperation(value = "17.删除任务记录")
    @GetMapping("/deleteRecord")
    @ApiImplicitParam(name = "recordId", value = "测试记录ID", required = true, dataType = "Integer", paramType = "query", example = "499")
    public AjaxResult deleteRecord(Integer recordId) throws BusinessException {
        return caseService.deleteRecord(recordId) ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
    }

    @ApiOperationSort(18)
    @ApiOperation(value = "18.获取角色字典")
    @GetMapping("/getroledict")
    public AjaxResult getRoleDict(String type) {
        List<RoleVo> roleList = new ArrayList<>();
        if (type.equals(Constants.PartType.MAIN)) {
            roleList.add(new RoleVo("AV车", Constants.PartRole.AV));
        } else if (type.equals(Constants.PartType.SLAVE)) {
            roleList.add(new RoleVo("AV车", Constants.PartRole.AV_SLAVE));
            roleList.add(new RoleVo("MV-远程驾驶车", Constants.PartRole.MV_REAL));
            roleList.add(new RoleVo("MV-虚拟驾驶车", Constants.PartRole.MV_VIRTUAL));
            roleList.add(new RoleVo("SV-仿真车", Constants.PartRole.MV_SIMULATION));
            roleList.add(new RoleVo("MV-云控寻迹车", Constants.PartRole.MV_TRACKING));
            roleList.add(new RoleVo("SV-云控寻迹车", Constants.PartRole.SV_TRACKING));
        } else if (type.equals(Constants.PartType.PEDESTRIAN)) {
            roleList.add(new RoleVo("SP-行人", Constants.PartRole.SP));
            roleList.add(new RoleVo("CAVE-行人", Constants.PartRole.CAVE));
        } else {
            roleList.add(new RoleVo("AV车", Constants.PartRole.AV));
            roleList.add(new RoleVo("AV车", Constants.PartRole.AV_SLAVE));
            roleList.add(new RoleVo("MV-远程驾驶车", Constants.PartRole.MV_REAL));
            roleList.add(new RoleVo("MV-虚拟驾驶车", Constants.PartRole.MV_VIRTUAL));
            roleList.add(new RoleVo("SV-仿真车", Constants.PartRole.MV_SIMULATION));
            roleList.add(new RoleVo("SP-行人", Constants.PartRole.SP));
            roleList.add(new RoleVo("CAVE-行人", Constants.PartRole.CAVE));
            roleList.add(new RoleVo("MV-云控寻迹车", Constants.PartRole.MV_TRACKING));
            roleList.add(new RoleVo("SV-云控寻迹车", Constants.PartRole.SV_TRACKING));
        }
        return AjaxResult.success(roleList);
    }

    @ApiOperationSort(19)
    @ApiOperation(value = "19.编辑页初始化（新）")
    @GetMapping("/initEditNew")
    public AjaxResult initEditNew(Integer caseId) throws BusinessException {
        return AjaxResult.success(caseService.initEditNew(caseId));
    }

    @ApiOperationSort(20)
    @ApiOperation(value = "20.获取配置详情（新）")
    @GetMapping("/configDetailNew")
    public AjaxResult configDetailNew(@RequestParam("id") Integer id) throws BusinessException {
        return AjaxResult.success(caseService.getConfigDetailNew(id));
    }

    @ApiOperationSort(21)
    @ApiOperation(value = "21.查询")
    @GetMapping("/getCasesByTaskId")
    public AjaxResult saveCaseDeviceNew(Integer taskId)
        throws BusinessException {
        CaseOpVo caseOpVo = new CaseOpVo();
        List<TjCaseOp> list = caseService.selectCaseOp(taskId);
        if(CollectionUtils.isEmpty(list)){
            throw new BusinessException("地图库关联异常");
        }
        caseOpVo.setCaseOpList(list);
        for (TjCaseOp tjCaseOp : list) {
            tjCaseOp.setScore(taskCaseCurrentScore(taskId, tjCaseOp.getId()));
            if (!tjCaseOp.getOpStatus().equals("finished")) {
                caseOpVo.setNowMapId(tjCaseOp.getMapId());
                break;
            }
        }
        if(caseOpVo.getNowMapId() == null){
            caseOpVo.setNowMapId(list.get(0).getMapId());
        }
        return AjaxResult.success(caseOpVo);
    }

    //孪生专用
    @GetMapping("/getCasesByTaskIdTW")
    public AjaxResult saveCaseDeviceNewtw(Integer taskId) {
        return AjaxResult.success(caseService.selectCaseOp(taskId));
    }

    private String taskCaseCurrentScore(Integer taskId, Integer caseId) {
        // recordId
        LambdaQueryWrapper<TjTaskCaseRecord> rlqw = new LambdaQueryWrapper<TjTaskCaseRecord>().eq(
                TjTaskCaseRecord::getTaskId, taskId)
            .eq(TjTaskCaseRecord::getCaseId, caseId);
        rlqw.orderByDesc(TjTaskCaseRecord::getId);
        Page<TjTaskCaseRecord> recordPage = tjTaskCaseRecordService.page(
            new Page<>(0, 1), rlqw);
        if (!CollectionUtils.isEmpty(recordPage.getRecords())) {
            TjTaskCaseRecord tjTaskCaseRecord = recordPage.getRecords().get(0);
            Integer recordId = tjTaskCaseRecord.getRecordId();
            Integer evaluativeId = tjTaskCaseRecord.getId();
            LambdaQueryWrapper<TjTestSingleSceneScore> slqw = new LambdaQueryWrapper<TjTestSingleSceneScore>().eq(
                    TjTestSingleSceneScore::getRecordId, recordId)
                .eq(TjTestSingleSceneScore::getEvaluativeId, evaluativeId)
                .eq(TjTestSingleSceneScore::getCaseId, caseId)
                .eq(TjTestSingleSceneScore::getTaskId, taskId)
                .orderByDesc(TjTestSingleSceneScore::getId);
            // score
            Page<TjTestSingleSceneScore> scorePage = tjTestSingleSceneScoreService.page(
                new Page<>(0, 1), slqw);
            if (!CollectionUtils.isEmpty(scorePage.getRecords())) {
                return scorePage.getRecords().get(0).getSenceScore();
            }
        }
        return null;
    }

}
