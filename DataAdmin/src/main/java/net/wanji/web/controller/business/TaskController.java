package net.wanji.web.controller.business;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.wanji.business.common.Constants.TaskStatusEnum;
import net.wanji.business.domain.bo.SaveCustomIndexWeightBo;
import net.wanji.business.domain.bo.SaveCustomScenarioWeightBo;
import net.wanji.business.domain.bo.SaveTaskSchemeBo;
import net.wanji.business.domain.bo.TaskBo;
import net.wanji.business.domain.dto.RoutingPlanDto;
import net.wanji.business.domain.dto.TaskDto;
import net.wanji.business.domain.dto.device.TaskSaveDto;
import net.wanji.business.domain.param.TessTrackParam;
import net.wanji.business.domain.vo.PlatformSSDto;
import net.wanji.business.domain.vo.TaskListVo;
import net.wanji.business.domain.vo.task.infinity.SelectRecordIdVo;
import net.wanji.business.entity.TjTask;
import net.wanji.business.entity.TjTaskCase;
import net.wanji.business.entity.TjTaskCaseRecord;
import net.wanji.business.entity.infity.TjInfinityTask;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.service.*;
import net.wanji.business.service.impl.TjInfinityTaskServiceImpl;
import net.wanji.common.constant.CacheConstants;
import net.wanji.common.constant.HttpStatus;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author: guowenhao
 * @date: 2023/8/30 16:10
 * @description: 测试任务控制器
 */
@Api(tags = "特色测试服务-测试任务")
@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController extends BaseController {

    @Autowired
    private TjTaskService tjTaskService;

    @Autowired
    private RestService restService;

    @Autowired
    private TjTaskCaseService taskCaseService;

    @Autowired
    private TjTaskCaseRecordService taskCaseRecordService;

    @Autowired
    private TestingService testingService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private TjInfinityTaskService tjInfinityTaskService;

    @ApiOperationSort(1)
    @ApiOperation(value = "1.节点初始化")
    @GetMapping("/initProcessed")
    @ApiImplicitParam(name = "processNode", value = "节点", required = true, dataType = "Integer", paramType = "query", example = "1")
    public AjaxResult initProcessed(Integer processNode) {
        return AjaxResult.success(tjTaskService.initProcessed(processNode));
    }

    @ApiOperationSort(2)
    @ApiOperation(value = "2.节点信息")
    @PostMapping("/processedInfo")
    public AjaxResult processedInfo(@RequestBody TaskSaveDto taskSaveDto) throws BusinessException {
        return AjaxResult.success(tjTaskService.processedInfo(taskSaveDto));
    }

    @ApiOperationSort(3)
    @ApiOperation(value = "3.保存")
    @PostMapping("/save")
    public AjaxResult save(@Validated @RequestBody TaskBo dto) throws BusinessException {
        int id = tjTaskService.saveTask(dto);
        if (id == 0) {
            return AjaxResult.error("保存失败");
        }
        return AjaxResult.success(id);
    }

    @ApiOperationSort(4)
    @ApiOperation(value = "4.路径优化")
    @PostMapping("/routingPlan")
    public AjaxResult routingPlan(@Validated @RequestBody RoutingPlanDto routingPlanDto) throws BusinessException {
        String repeatKey = "ROUTING_TASK_" + routingPlanDto.getTaskId();
        if (redisCache.hasKey(repeatKey) && !redisCache.getCacheObject(repeatKey).equals(SecurityUtils.getUsername())) {
            return AjaxResult.error("有其他用户正在规划该任务路径，请稍后再试");
        }
        redisCache.setCacheObject(repeatKey, SecurityUtils.getUsername(), 3, TimeUnit.MINUTES);
        String key = "ROUTING_SUBMIT_" + routingPlanDto.getTaskId();
        if (!redisCache.lock(key, key, 10)) {
            return AjaxResult.error("正在连接仿真软件，请稍后再试");
        }
        tjTaskService.routingPlan(routingPlanDto);
        redisCache.unlock2(key, key);
        return AjaxResult.success("开始进行路径优规划");
    }

    @ApiOperationSort(5)
    @ApiOperation(value = "5.列表")
    @PostMapping("/pageList")
    public Map<String, Object> pageList(@Validated @RequestBody TaskDto taskDto, HttpServletRequest request) throws BusinessException {
        taskDto.setCreatedBy(SecurityUtils.getUsername());
        Map<String, Object> result = new HashMap<>();
        Map<String, Long> countMap = tjTaskService.selectCount(taskDto);
        result.put("statistics", countMap);

        PageHelper.startPage(taskDto.getPageNum(), taskDto.getPageSize());
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setCode(HttpStatus.SUCCESS);
        tableDataInfo.setMsg("查询成功");
        List<TaskListVo> list = tjTaskService.pageList(taskDto);
        tableDataInfo.setData(list);
        tableDataInfo.setTotal(new PageInfo(list).getTotal());
        result.put("tableData", tableDataInfo);
        // 添加测试报告的跳转外链
        result.put("testReportOuterChain", tjTaskService.getTestReportOuterChain(request));
        return result;
    }

    //孪生专用
    @CrossOrigin
    @PostMapping("/pageListTW")
    public Map<String, Object> pageListtw(@Validated @RequestBody TaskDto taskDto, HttpServletRequest request) throws BusinessException {
        taskDto.setCreatedBy("admin");
        Map<String, Object> result = new HashMap<>();
        Map<String, Long> countMap = tjTaskService.selectCount(taskDto);
        result.put("statistics", countMap);
        PageHelper.startPage(taskDto.getPageNum(), taskDto.getPageSize());
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setCode(HttpStatus.SUCCESS);
        tableDataInfo.setMsg("查询成功");
        List<TaskListVo> list = tjTaskService.pageList(taskDto);
        tableDataInfo.setData(list);
        tableDataInfo.setTotal(new PageInfo(list).getTotal());
        result.put("tableData", tableDataInfo);
        // 添加测试报告的跳转外链
        result.put("testReportOuterChain", tjTaskService.getTestReportOuterChain(request));
        return result;
    }

    @ApiOperationSort(6)
    @ApiOperation(value = "6.列表页初始化")
    @GetMapping("/initPage")
    public AjaxResult initPage() {
        return AjaxResult.success(tjTaskService.initPage());
    }

    @GetMapping("/initPageOp")
    public AjaxResult initPageOp() {
        return AjaxResult.success(tjTaskService.initPageOp());
    }

    @ApiOperationSort(7)
    @ApiOperation(value = "7.判断是否存在待提交的任务")
    @GetMapping("/hasSubmitTask")
    public AjaxResult hasSubmitTask() {
        TjTask task = tjTaskService.hasUnSubmitTask();
        if (task == null) {
            return AjaxResult.success(0);
        } else {
            return AjaxResult.success(task);
        }
    }

    @ApiOperationSort(8)
    @ApiOperation(value = "8.根据场景权重选择权重详情")
    @GetMapping("/getWeightDetailsById")
    public AjaxResult getWeightDetailsById(String id, Integer type) throws BusinessException {
        if (StringUtils.isEmpty(id)) {
            return AjaxResult.error("场景权重id为空!");
        }
        if (type == null) {
            return AjaxResult.error("指标或指标类型为空!");
        }
        //0 场景分类方案 1 指标方案
        if (type == 0) {
            return AjaxResult.success(restService.getSceneWeightDetailsById(id));
        } else if (type == 1) {
            return AjaxResult.success(restService.getIndexWeightDetailsById(id));
        }
        return AjaxResult.success();
    }

    @ApiOperationSort(8)
    @ApiOperation(value = "8.评价指标自定义评价权重")
    @GetMapping("/getValuationIndexCustomWeight")
    public AjaxResult getValuationIndexCustomWeight() throws BusinessException {
        return AjaxResult.success(restService.getValuationIndexCustomWeight());
    }

    @ApiOperationSort(9)
    @ApiOperation(value = "9.创建任务和方案关联")
    @PostMapping("/saveTaskScheme")
    public AjaxResult saveTaskScheme(@RequestBody SaveTaskSchemeBo saveTaskSchemeBo) throws BusinessException {
        Map<String, String> map = restService.saveTaskScheme(saveTaskSchemeBo);
        if ("500".equals(map.get("code"))) {
            return AjaxResult.error(map.get("msg"));
        }
//        TaskBo taskBo = new TaskBo();
//        taskBo.setId(Integer.valueOf(saveTaskSchemeBo.getTaskId()));
//        taskBo.setProcessNode(TaskProcessNode.VIEW_PLAN);
//        tjTaskService.saveTask(taskBo);
        return AjaxResult.success("成功");
    }

    @ApiOperationSort(10)
    @ApiOperation(value = "10.自定义-场景权重创建")
    @PostMapping("/saveCustomScenarioWeight")
    public AjaxResult saveCustomScenarioWeight(@RequestBody SaveCustomScenarioWeightBo saveCustomScenarioWeightBo) throws BusinessException {
        Map<String, String> map = restService.saveCustomScenarioWeight(saveCustomScenarioWeightBo);
        if ("500".equals(map.get("code"))) {
            return AjaxResult.error(map.get("msg"));
        }
        tjTaskService.saveCustomScenarioWeight(saveCustomScenarioWeightBo);
        return AjaxResult.success();
    }

    @ApiOperationSort(11)
    @ApiOperation(value = "11.自定义-指标权重创建")
    @PostMapping("/saveCustomIndexWeight")
    public AjaxResult saveCustomIndexWeight(@RequestBody SaveCustomIndexWeightBo saveCustomIndexWeightBo) throws BusinessException {
        Map<String, String> map = restService.saveCustomIndexWeight(saveCustomIndexWeightBo);
        if ("500".equals(map.get("code"))) {
            return AjaxResult.error(map.get("msg"));
        }
        tjTaskService.saveCustomIndexWeight(saveCustomIndexWeightBo);
        return AjaxResult.success();
    }

    @ApiOperationSort(12)
    @ApiOperation(value = "12.测试任务用例列表")
    @GetMapping("/getTaskCaseList")
    @ApiImplicitParam(name = "taskId", value = "任务id", required = true, dataType = "Integer", paramType = "query", example = "1")
    public AjaxResult getTaskCaseList(@RequestParam("taskId") Integer taskId) throws BusinessException {
        return AjaxResult.success(tjTaskService.getTaskCaseList(taskId));
    }

    @ApiOperationSort(13)
    @ApiOperation(value = "13.下载测试报告")
    @GetMapping("/downloadTestReport")
    public AjaxResult downloadTestReport(HttpServletResponse response, int taskId) throws BusinessException {
        restService.downloadTestReport(response, taskId);
        return AjaxResult.success();
    }

    @ApiOperationSort(14)
    @ApiOperation(value = "14.重置状态")
    @PostMapping("/resetStatus")
    public AjaxResult resetStatus(@RequestBody TjTaskCase param) throws BusinessException {
        if(param.getTaskId() != null) {
            TjTask task = tjTaskService.getById(param.getTaskId());
            if(StringUtils.isEmpty(task.getLastStatus()) || !task.getStatus().equals("prepping")) {
                task.setLastStatus(task.getStatus());
            }
            task.setStatus("prepping");
            tjTaskService.updateById(task);
        }
        taskCaseService.getStatus(param, SecurityUtils.getUsername(), true);
        return AjaxResult.success();
    }

    @ApiOperationSort(15)
    @ApiOperation(value = "15.获取状态")
    @PostMapping("/getStatus")
    public AjaxResult getStatus(@RequestBody TjTaskCase param) throws BusinessException {
        return AjaxResult.success(taskCaseService.getStatus(param, SecurityUtils.getUsername(), false));
    }

    //孪生专用
    @PostMapping("/getStatusTW")
    public AjaxResult getStatustw(@RequestBody TjTaskCase param) throws BusinessException {
        return AjaxResult.success(taskCaseService.getStatustw(param));
    }

    //
    @ApiOperationSort(16)
    @ApiOperation(value = "16.准备")
    @PostMapping("/prepare")
    public AjaxResult prepare(@RequestBody TjTaskCase param) throws BusinessException {
        if(param.getTaskId() != null) {
            TjTask task = tjTaskService.getById(param.getTaskId());
            Date data = new Date();
            data.setTime(data.getTime() + 3000);
            task.setStartTime(data);
            tjTaskService.updateById(task);
        }
        return AjaxResult.success(taskCaseService.prepare(param, SecurityUtils.getUsername()));
    }

    //
    @ApiOperationSort(17)
    @ApiOperation(value = "17.任务控制")
    @GetMapping("/controlTask")
    public AjaxResult controlTask(Integer taskId, Integer id, Integer action) throws BusinessException, IOException {
        return AjaxResult.success(taskCaseService.controlTask(taskId, id, action, SecurityUtils.getUsername(), null));
    }

    @ApiOperationSort(18)
    @ApiOperation(value = "18.测试用例开始结束控制接口")
    @PostMapping("/caseStartEnd")
    public AjaxResult caseStartEnd(@RequestBody PlatformSSDto platformSSDto) throws BusinessException, IOException {
        if (platformSSDto.getState() == -1) {
            log.error("用例异常结束原因：{}", platformSSDto.getMessage());
        }
        Integer testMode = platformSSDto.getTestMode();
        if (null != testMode && testMode.equals(3)) {
            tjInfinityTaskService.startStop(platformSSDto);
        }else {
            if (platformSSDto.getTaskId() == 0) {
                if (platformSSDto.getState() == 1) {
                    testingService.start(platformSSDto.getCaseId(),
                        platformSSDto.getState(),
                        (String) platformSSDto.getContext().get("user"));
                } else {
                    testingService.end(platformSSDto.getCaseId(),
                        platformSSDto.getState(),
                        (String) platformSSDto.getContext().get("user"));
                }
            } else {
                taskCaseService.caseStartEnd(platformSSDto.getTaskId(),
                    platformSSDto.getCaseId(), platformSSDto.getState(),
                    platformSSDto.isTaskEnd(), platformSSDto.getContext(),
                    platformSSDto.getTestMode());
            }
        }
        taskCaseCache(platformSSDto);
        return null;
    }

    @ApiOperationSort(19)
    @ApiOperation(value = "19.测试结果（已废弃）")
    @GetMapping("/getResult")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "Integer", paramType = "query", example = "499"),
            @ApiImplicitParam(name = "id", value = "任务用例ID", dataType = "Integer", paramType = "query", example = "499")
    })
    public AjaxResult getResult(Integer taskId, Integer id) throws BusinessException {
        return AjaxResult.success(taskCaseService.getResult(taskId, id));
    }

    @ApiOperationSort(20)
    @ApiOperation(value = "20.图形列表")
    @GetMapping("/communicationDelay")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "Integer", paramType = "query", example = "499"),
            @ApiImplicitParam(name = "id", value = "任务用例ID", dataType = "Integer", paramType = "query", example = "499")
    })
    public AjaxResult communicationDelayVo(Integer taskId, Integer id) throws BusinessException {
        return AjaxResult.success(taskCaseService.communicationDelayVo(taskId, id));
    }

    @ApiOperationSort(21)
    @ApiOperation(value = "21.删除任务")
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Integer id) {
        return tjTaskService.taskDelete(id.intValue()) ?
            AjaxResult.success("删除成功") :
            AjaxResult.error("删除失败");
    }

    @ApiOperationSort(22)
    @ApiOperation(value = "22.删除待提交任务")
    @PostMapping("/remove")
    public AjaxResult removeEnity() {
        LambdaQueryWrapper<TjTask> wrapper = new LambdaQueryWrapper<TjTask>();
        wrapper.eq(TjTask::getStatus, TaskStatusEnum.NO_SUBMIT.getCode()).eq(TjTask::getCreatedBy, SecurityUtils.getUsername());
        return tjTaskService.remove(wrapper)
                ? AjaxResult.success("成功")
                : AjaxResult.error("失败");
    }

    @ApiOperationSort(23)
    @ApiOperation(value = "23.查询任务信息")
    @GetMapping("/taskinfo")
    public AjaxResult taskinfo(@RequestParam Integer taskId) throws BusinessException {
        return AjaxResult.success(taskCaseService.getTaskInfo(taskId));
    }


    @ApiOperationSort(24)
    @ApiOperation(value = "24.停止任务")
    @GetMapping("/stop")
    public AjaxResult stop(Integer taskId, Integer id, Integer action) throws BusinessException {
        taskCaseService.stop(taskId, id, SecurityUtils.getUsername());
        return AjaxResult.success();
    }

    @ApiOperationSort(25)
    @ApiOperation(value = "25.查询任务用例树")
    @GetMapping("/selectTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型", required = true, dataType = "String", paramType = "query", example = "virtualRealFusion"),
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "Integer", paramType = "query", example = "1")
    })
    public AjaxResult selectTree(String type, Integer taskId) {
        return AjaxResult.success(taskCaseService.selectTree(type, taskId));
    }

    @ApiOperationSort(26)
    @ApiOperation(value = "26.选择任务用例")
    @GetMapping("/choiceCase")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "Integer", paramType = "query", example = "1"),
            @ApiImplicitParam(name = "caseIds", value = "用例ID", required = true, dataType = "List", paramType = "query", example = "1,2,3"),
            @ApiImplicitParam(name = "action", value = "选中(1)/取消(0)", required = true, dataType = "Integer", paramType = "query", example = "1")
    })
    public AjaxResult choiceCase(@RequestParam("taskId") Integer taskId,
                                 @RequestParam("caseIds") List<Integer> caseIds,
                                 @RequestParam("action") Integer action) throws BusinessException {
        if (1 == action) {
            return AjaxResult.success(taskCaseService.addTaskCase(taskId, caseIds));
        }
        if (0 == action) {
            return AjaxResult.success(taskCaseService.deleteTaskCase(taskId, caseIds));
        }
        return AjaxResult.error("操作失败");
    }

    @ApiOperationSort(27)
    @ApiOperation(value = "27.回放")
    @GetMapping("/playback")
    @ApiImplicitParams({ @ApiImplicitParam(name = "taskId",
        value = "任务ID",
        required = true,
        dataType = "Integer",
        paramType = "query",
        example = "1"), @ApiImplicitParam(name = "caseId",
        value = "任务用例ID",
        dataType = "Integer",
        paramType = "query",
        example = "1"), @ApiImplicitParam(name = "recordId",
        value = "测试记录ID",
        dataType = "Integer",
        paramType = "query",
        example = "1"), @ApiImplicitParam(name = "action",
        value = "1：开始；2：暂停；3：继续；4：结束",
        required = true,
        dataType = "Integer",
        paramType = "query",
        example = "1")
    })
    public AjaxResult playback(@RequestParam("taskId") Integer taskId,
        @RequestParam(value = "caseId", required = false) Integer caseId,
        @RequestParam(value = "recordId", required = false) Integer recordId,
        @RequestParam("action") Integer action) {
        try {
            taskCaseService.playback(taskId, caseId, recordId, action);
        } catch (Exception e) {
            log.error("回放失败:{}", e);
            return AjaxResult.error("回放失败:" + e.getMessage());
        }
        return AjaxResult.success("请等待...");
    }

    @ApiOperationSort(28)
    @ApiOperation(value = "28.测试评分结果")
    @GetMapping("/getEvaluationResult")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", dataType = "Integer", paramType = "query", example = "499"),
            @ApiImplicitParam(name = "id", value = "任务用例ID", dataType = "Integer", paramType = "query", example = "499")
    })
    public AjaxResult getEvaluationResult(Integer taskId, Integer id) throws BusinessException {
        return AjaxResult.success(taskCaseService.getEvaluation(taskId, id));
    }

    //孪生
    @GetMapping("/getEvaluationResultTW")
    public AjaxResult getEvaluationResultTW(Integer taskId, Integer type) throws BusinessException {
        int id = 0;
        if (type!=null && type == 1) {
            id = -1;
        }
        return AjaxResult.success(taskCaseService.getEvaluation(taskId, id));
    }

    @ApiOperationSort(29)
    @ApiOperation(value = "29.保存记录状态（废弃）")
    @GetMapping("/saveRecordStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "recordId", value = "测试记录ID", dataType = "Integer", paramType = "query", example = "499"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "Integer", paramType = "query", example = "1")
    })
    public AjaxResult saveRecordStatus(@RequestParam("recordId") Integer recordId, @RequestParam("status") Integer status) throws BusinessException {
        TjTaskCaseRecord record = taskCaseRecordService.getById(recordId);
        record.setStatus(status);
        return taskCaseRecordService.updateById(record) ? AjaxResult.success("保存成功") : AjaxResult.error("保存失败");
    }

    @ApiOperationSort(30)
    @ApiOperation(value = "30.手动终止")
    @GetMapping("/manualTermination")
    @ApiImplicitParams({ @ApiImplicitParam(name = "taskId",
        value = "任务ID",
        dataType = "Integer",
        paramType = "query",
        example = "499"), @ApiImplicitParam(name = "caseId",
        value = "用例ID",
        dataType = "Integer",
        paramType = "query",
        example = "1"), @ApiImplicitParam(name = "testModel",
        value = "测试类型：0：单用例测试（实车试验）；1：连续性场景测试（多场景任务一次启停）；2：批量测试（n场景任务n次启停）；3：无限里程",
        dataType = "Integer",
        paramType = "query",
        example = "1")
    })
    public AjaxResult manualTermination(@RequestParam("taskId") Integer taskId,
        @RequestParam(value = "caseId") Integer caseId,
        @RequestParam("testModel") Integer testModel) throws BusinessException {
        if (taskId > 0) {
            taskCaseService.manualTermination(taskId, 0, testModel);
        } else {
            testingService.manualTermination(caseId, testModel);
            if (testModel == 0){
                TessTrackParam tessTrackParam = redisCache.getCacheObject("svtrack_"+caseId+"_"+SecurityUtils.getUsername());
                if (tessTrackParam != null){
                    testingService.stopSvTrack(caseId,tessTrackParam);
                }
            }
        }
        return AjaxResult.success();
    }

    @GetMapping("/runningTaskTW")
    public AjaxResult getRunningTask() {
        QueryWrapper<TjTask> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "prepping");
        wrapper.or().eq("status", "running");
        List<TjTask> list = tjTaskService.list(wrapper);
        for (TjTask task : list) {
            if(task.getStatus().equals("prepping")){
                task.setStartTime(null);
            }
        }
        return AjaxResult.success(list);
    }

    @GetMapping("/playbackTW")
    public AjaxResult playbacktw(@RequestParam("taskId") Integer taskId, @RequestParam(value = "caseId", required = false) Integer caseId) throws BusinessException, IOException {
        Random random = new Random();
        int substring = random.nextInt(10);
        String topic = "tj_playback_tw_"+substring;
        taskCaseService.playbackTW(taskId,caseId,topic);
        return AjaxResult.success(topic);
    }

    @ApiOperation("设置回放测试记录ID")
    @PostMapping("/selectedRecordId")
    public AjaxResult selectedRecordId(
        @RequestBody SelectRecordIdVo selectRecordIdVo) {
        UpdateWrapper<TjTask> uw = new UpdateWrapper<>();
        uw.eq("id", selectRecordIdVo.getTaskId());
        uw.set("selected_record_id", selectRecordIdVo.getRecordId());
        return AjaxResult.success(tjTaskService.update(uw));
    }

    private void taskCaseCache(PlatformSSDto platformSSDto) {
        HashOperations hashOperations = redisCache.redisTemplate.opsForHash();
        String key = CacheConstants.USER_OF_CONTINUOUS_TASK_PREFIX + platformSSDto.getTaskId();
        String caseId = String.valueOf(platformSSDto.getCaseId());
        if (!platformSSDto.isTaskEnd()) {
            if(platformSSDto.getState() > 0){
                hashOperations.put(key, caseId, platformSSDto.getContext().get("user"));
            }else if(-1 == platformSSDto.getState()){
                redisCache.redisTemplate.delete(key);
            }
        }else {
            redisCache.redisTemplate.delete(key);
            taskCaseService.twStop(platformSSDto.getTaskId(), platformSSDto.getCaseId(),"finish");
        }
    }

    @GetMapping("/tasktolib")
    public AjaxResult tasktolib(@RequestParam("taskId") Integer taskId, @RequestParam("caseId") Integer caseId) {
        return AjaxResult.success(taskCaseService.tasktolib(taskId, caseId));
    }
}
