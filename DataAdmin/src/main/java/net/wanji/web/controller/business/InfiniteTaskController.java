package net.wanji.web.controller.business;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.InfinteMileScenceExo;
import net.wanji.business.domain.bo.SaveCustomIndexWeightBo;
import net.wanji.business.domain.bo.SaveCustomScenarioWeightBo;
import net.wanji.business.domain.bo.SaveTaskSchemeBo;
import net.wanji.business.domain.dto.TaskDto;
import net.wanji.business.domain.dto.TjTessngShardingChangeDto;
import net.wanji.business.domain.vo.task.infinity.*;
import net.wanji.business.entity.InfinteMileScence;
import net.wanji.business.entity.infity.TjInfinityTask;
import net.wanji.business.entity.infity.TjInfinityTaskRecord;
import net.wanji.business.entity.infity.TjShardingChangeRecord;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.service.*;
import net.wanji.business.service.record.DataFileService;
import net.wanji.business.service.record.impl.ExtendedDataWrapper;
import net.wanji.business.util.RedisLock;
import net.wanji.common.constant.HttpStatus;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author hcy
 * @version 1.0
 * @className InfiniteTaskController
 * @description TODO
 * @date 2024/3/11 10:54
 **/
@Api(tags = "特色测试服务-测试任务-无限里程")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/taskInfinite")
public class InfiniteTaskController {

    private final RedisLock redisLock;
    private final RestService restService;
    private final TjInfinityTaskService tjInfinityTaskService;
    private final TjShardingChangeRecordService tjShardingChangeRecordService;
    private final TjInfinityTaskRecordService tjInfinityTaskRecordService;
    private final DataFileService dataFileService;
    private final TjShardingChangeRecordService shardingChangeRecordService;
    private final InfinteMileScenceService infinteMileScenceService;

    @ApiOperationSort(5)
    @ApiOperation(value = "5.列表")
    @PostMapping("/pageList")
    public Map<String, Object> pageList(@Validated @RequestBody TaskDto taskDto, HttpServletRequest request) {
        taskDto.setCreatedBy(SecurityUtils.getUsername());
        Map<String, Object> result = new HashMap<>();
        Map<String, Long> countMap = tjInfinityTaskService.selectCount(taskDto);
        result.put("statistics", countMap);
        PageHelper.startPage(taskDto.getPageNum(), taskDto.getPageSize());
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setCode(HttpStatus.SUCCESS);
        tableDataInfo.setMsg("查询成功");
        List<Map<String, Object>> list = tjInfinityTaskService.pageList(taskDto);
        tableDataInfo.setData(list);
        tableDataInfo.setTotal(new PageInfo(list).getTotal());
        result.put("tableData", tableDataInfo);
        // 添加测试报告的跳转外链
        result.put("testReportOuterChain", tjInfinityTaskService.getTestReportOuterChain(request));
        return result;
    }


    // 任务新增
    @ApiOperationSort(3)
    @ApiOperation(value = "3-1.保存")
    @PostMapping("/save")
    public AjaxResult save(@Validated @RequestBody Map<String, Object> taskData, HttpServletRequest request) throws BusinessException {
        try {
            taskData.put("createdBy", SecurityUtils.getUsername());
            taskData.put("status", "waiting");
            taskData.put("orderNumber", "task-" + DateUtils.getTime());
            taskData.put("createdDate", DateUtils.getTime());
            String taskId = String.valueOf(tjInfinityTaskService.saveTask(taskData));
            saveEvaluationScheme(taskData, taskId);
            return AjaxResult.success(0);

        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("保存失败");
        }
    }

    @ApiOperationSort(4)
    @ApiOperation(value = "3-2.修改状态")
    @GetMapping("/updateTaskStatus")
    public AjaxResult updateTaskStatus(String status, int id) throws BusinessException {
        try {
            int msg = tjInfinityTaskService.updateTaskStatus(status, id);
            return AjaxResult.success(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("修改状态失败");
        }
    }

    @ApiOperationSort(9)
    @ApiOperation(value = "3-2.创建任务和方案关联")
    @PostMapping("/saveTaskScheme")
    public AjaxResult saveTaskScheme(@RequestBody SaveTaskSchemeBo saveTaskSchemeBo) throws BusinessException {
        Map<String, String> map = restService.saveTaskScheme(saveTaskSchemeBo);
        if ("500".equals(map.get("code"))) {
            return AjaxResult.error(map.get("msg"));
        }
        return AjaxResult.success("成功");
    }

    @ApiOperationSort(10)
    @ApiOperation(value = "3-3.自定义-场景权重创建")
    @PostMapping("/saveCustomScenarioWeight")
    public AjaxResult saveCustomScenarioWeight(@RequestBody SaveCustomScenarioWeightBo saveCustomScenarioWeightBo) throws BusinessException {
        Map<String, String> map = restService.saveCustomScenarioWeight(saveCustomScenarioWeightBo);
        if ("500".equals(map.get("code"))) {
            return AjaxResult.error(map.get("msg"));
        }
        tjInfinityTaskService.saveCustomScenarioWeight(saveCustomScenarioWeightBo);
        return AjaxResult.success();
    }

    @ApiOperationSort(11)
    @ApiOperation(value = "3-4.自定义-指标权重创建")
    @PostMapping("/saveCustomIndexWeight")
    public AjaxResult saveCustomIndexWeight(@RequestBody SaveCustomIndexWeightBo saveCustomIndexWeightBo) throws BusinessException {
        Map<String, String> map = restService.saveCustomIndexWeight(saveCustomIndexWeightBo);
        if ("500".equals(map.get("code"))) {
            return AjaxResult.error(map.get("msg"));
        }
        tjInfinityTaskService.saveCustomIndexWeight(saveCustomIndexWeightBo);
        return AjaxResult.success();
    }

    private void saveEvaluationScheme(Map<String, Object> taskData, String taskId) throws BusinessException {
        // 创建任务和方案关联
        if (taskData.containsKey("taskScheme")) {
            SaveTaskSchemeBo taskScheme = JSONObject.parseObject(JSONObject.toJSONString(
                    taskData.get("taskScheme")), SaveTaskSchemeBo.class);
            taskScheme.setTaskId(taskId);
            saveTaskScheme(taskScheme);
        }

        // 3-3.自定义-场景权重创建
        if (taskData.containsKey("customScenarioWeight")) {
            SaveCustomScenarioWeightBo customScenarioWeight = JSONObject.parseObject(JSONObject.toJSONString(
                    taskData.get("customScenarioWeight")), SaveCustomScenarioWeightBo.class);
            customScenarioWeight.setTask_id(taskId);
            saveCustomScenarioWeight(customScenarioWeight);
        }

        // 3-3.自定义-指标权重创建
        if (taskData.containsKey("customIndexWeight")) {
            SaveCustomIndexWeightBo customIndexWeight = JSONObject.parseObject(JSONObject.toJSONString(
                    taskData.get("customIndexWeight")), SaveCustomIndexWeightBo.class);
            customIndexWeight.setTask_id(taskId);
            saveCustomIndexWeight(customIndexWeight);
        }
    }

    @ApiOperation("删除")
    @GetMapping("/delete")
    public AjaxResult delete(Integer id) {
        return AjaxResult.success(tjInfinityTaskService.delete(id));
    }

    @ApiOperation("设置回放测试记录ID")
    @PostMapping("/selectedRecordId")
    public AjaxResult selectedRecordId(
        @RequestBody SelectRecordIdVo selectRecordIdVo) {
        UpdateWrapper<TjInfinityTask> uw = new UpdateWrapper<>();
        uw.eq("id", selectRecordIdVo.getTaskId());
        uw.set("selected_record_id", selectRecordIdVo.getRecordId());
        return AjaxResult.success(tjInfinityTaskService.update(uw));
    }

    @ApiOperationSort(12)
    @ApiOperation("分片进出通知")
    @PostMapping("/shardingInOut")
    public AjaxResult shardingRangeInOut(
        @RequestBody ShardingInOutVo shardingInOutVo) {
        try {
            Integer recordId = getLastRecordId(shardingInOutVo.getTaskId(),
                shardingInOutVo.getCaseId(), shardingInOutVo.getUsername());

            TjShardingChangeRecord tjShardingChangeRecord = new TjShardingChangeRecord();
            BeanUtils.copyProperties(shardingInOutVo, tjShardingChangeRecord);
            tjShardingChangeRecord.setCreateTimestamp(
                shardingInOutVo.getTimestamp());
            tjShardingChangeRecord.setRecordId(recordId);
            tjShardingChangeRecordService.saveShardingInOut(
                tjShardingChangeRecord);

            TjTessngShardingChangeDto tjTessngShardingChangeDto = new TjTessngShardingChangeDto();
            BeanUtils.copyProperties(tjShardingChangeRecord,
                tjTessngShardingChangeDto);
            tjTessngShardingChangeDto.setRecordId(recordId);
            tjTessngShardingChangeDto.setUsername(
                shardingInOutVo.getUsername());
            tjShardingChangeRecordService.tessngShardingInOutSend(
                tjTessngShardingChangeDto);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("shardingInOut taskId:[{}], caseId:[{}]",
                    shardingInOutVo.getTaskId(), shardingInOutVo.getCaseId(),
                    e);
            }
            return AjaxResult.error(e.getMessage());
        }

        return AjaxResult.success();
    }

    @ApiOperation("1、初始化")
    @PostMapping("/init")
    @ApiImplicitParam(name = "taskId",
        value = "任务ID",
        required = true,
        dataType = "integer")
    public AjaxResult init(Integer taskId) {
        try {
            InfinityTaskInitVo infinityTaskInitVo = tjInfinityTaskService.init(
                taskId);
            if (CollectionUtils.isEmpty(
                infinityTaskInitVo.getShardingInfos())) {
                throw new BusinessException("分片信息未设置");
            }
            TjInfinityTask tjInfinityTask = tjInfinityTaskService.getById(taskId);
            if (!tjInfinityTask.getStatus().equals("prepping")){
                tjInfinityTask.setLastStatus(tjInfinityTask.getStatus());
                tjInfinityTask.setStatus("prepping");
                tjInfinityTaskService.updateById(tjInfinityTask);
            }
            redisLock.setUser("twin_" + taskId, SecurityUtils.getUsername());
            return AjaxResult.success(infinityTaskInitVo);
        } catch (BusinessException be) {
            return AjaxResult.error(be.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("查询任务配置信息异常！");
        }
    }

    @ApiOperation("2、准备（状态检查）")
    @PostMapping("/prepare")
    public AjaxResult prepare(Integer taskId) {
        try {
            return AjaxResult.success(tjInfinityTaskService.prepare(taskId));
        } catch (BusinessException e) {
            if (log.isErrorEnabled()) {
                log.error("prepare error!", e);
            }
            InfinityTaskPreparedVo infinityTaskPreparedVo = new InfinityTaskPreparedVo();
            infinityTaskPreparedVo.setCanStart(false);
            infinityTaskPreparedVo.setMessage(e.getMessage());
            return AjaxResult.error("准备状态异常！", infinityTaskPreparedVo);
        }
        // 1、状态检查
            //	1、设备在线状态 #平台定时获取，存在redis
            //	2、获取设备准备状态 #发送至主控
            //	3、获取任务运行状态 #平台自己管理
        // 2、状态校验
            // 1、可测试，发送重置状态
            // 2、返回不可开始状态
    }

    @ApiOperation("3、任务控制-预开始")
    @PostMapping("/preStart")
    public  AjaxResult preStart(Integer taskId) {
        //	1、发送任务轨迹/规则到主控
        try{
            return AjaxResult.success(tjInfinityTaskService.preStart(taskId));
        }catch (Exception e){
            if(log.isErrorEnabled()){
                log.error("preStart error!", e);
            }
        }
        return AjaxResult.error("无限里程开始异常！");
    }

    //4、主控接管任务
        //	1、开始任务
        //	2、结束任务

    @ApiOperationSort(28)
    @ApiOperation(value = "测试评分结果")
    @GetMapping("/evaluationResult")
    @ApiImplicitParams({ @ApiImplicitParam(name = "taskId",
        value = "任务ID",
        dataType = "Integer",
        paramType = "query",
        example = "499"), @ApiImplicitParam(name = "caseId",
        value = "任务用例ID",
        dataType = "Integer",
        paramType = "query",
        example = "499")
    })
    public AjaxResult getEvaluationResult(Integer taskId, Integer caseId) {
        try {
            return AjaxResult.success(
                tjShardingChangeRecordService.shardingResult(0, taskId));
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("分片进出结果查询异常!", e);
            }
            return AjaxResult.error("分片进出结果查询异常！");
        }
    }

    @GetMapping("/evaluationResultTW")
    public AjaxResult getEvaluationResultTW(Integer taskId) {
        try {
            return AjaxResult.success(
                    tjShardingChangeRecordService.shardingResult(0, taskId));
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("分片进出结果查询异常!", e);
            }
            return AjaxResult.error("分片进出结果查询异常！");
        }
    }

    /**
     * 获取正在运行的任务列表(孿生)
     *
     * 该接口不接受任何参数，返回当前状态为"prepping"或"running"的任务列表。
     * 对于状态为"prepping"的任务，会将其测试开始时间设为null。
     *
     * @return AjaxResult 包含任务列表的成功响应对象。
     */
    @GetMapping("/runningTaskTW")
    public AjaxResult getRunningTask() {
        QueryWrapper<TjInfinityTask> wrapper = new QueryWrapper<>();
        wrapper.eq("status", "prepping");
        wrapper.or().eq("status", "running");
        List<TjInfinityTask> list = tjInfinityTaskService.list(wrapper);
        for (TjInfinityTask task : list) {
            if(task.getStatus().equals("prepping")){
                task.setTestStartTime(null);
            }
        }
        return AjaxResult.success(list);
    }

    /**
     * 获取任务的状态(孿生)
     *
     * @param taskId 任务ID，用于查询任务的状态。
     * @return 返回一个AjaxResult对象，其中包含了任务的状态信息。
     */
    @GetMapping("/getStatusTW")
    public AjaxResult getStatus(Integer taskId) {
        return prepare(taskId);
    }

    @ApiOperation("测试记录信息")
    @GetMapping("/records")
    @ApiImplicitParams({ @ApiImplicitParam(name = "taskId",
        value = "任务ID",
        dataType = "Long",
        required = true), @ApiImplicitParam(name = "pageNumber",
        value = "页码",
        dataType = "Long"), @ApiImplicitParam(name = "pageSize",
        value = "页大小",
        dataType = "Long")
    })
    public AjaxResult records(int taskId, int pageNumber, int pageSize) {
        QueryWrapper<TjInfinityTaskRecord> qw = new QueryWrapper<>();
        qw.eq("task_id", taskId);
        qw.orderByDesc("created_date");
        Page<TjInfinityTaskRecord> recordPage = new Page<>(
            pageNumber, pageSize);
        return AjaxResult.success(tjInfinityTaskRecordService.page(
            recordPage, qw));
    }

    @ApiOperation("回放")
    @ApiImplicitParams({ @ApiImplicitParam(name = "taskId",
        value = "任务ID",
        dataType = "Integer",
        required = true), @ApiImplicitParam(name = "caseId",
        value = "场景ID",
        dataType = "Integer",
        required = true), @ApiImplicitParam(name = "recordId",
        value = "测试记录ID",
        dataType = "Integer",
        required = true), @ApiImplicitParam(name = "startTimestamp",
        value = "回放开始时间",
        dataType = "Long"), @ApiImplicitParam(name = "endTimestamp",
        value = "回放结束时间",
        dataType = "Long")
    })
    @GetMapping("/playback")
    public AjaxResult playback(Integer taskId, Integer caseId, Integer recordId,
        Long startTimestamp, Long endTimestamp) {
        try {
            TjInfinityTask infinityTask = tjInfinityTaskService.getById(caseId);
            if (null == recordId) {
                recordId = infinityTask.getSelectedRecordId();
                if(null == recordId){
                    recordId = getLastRecordId(taskId, caseId, SecurityUtils.getUsername());
                }
            }
            // 暂时只能一处回放，多处回放需要修改websocket处理请求参数
            // 文件id
            TjInfinityTaskRecord record = tjInfinityTaskRecordService.getById(
                recordId);
            // 文件读取
            Integer dataFileId = record.getDataFileId();
            if (null == startTimestamp) {
                startTimestamp = 0L;
            }
            if (null == endTimestamp) {
                endTimestamp = 0L;
            }
            HashMap<String, List<? extends ExtendedDataWrapper>> extendDataWrappers = new HashMap<>();
            extendDataWrappers.put(Constants.RedisMessageType.SHARDING,
                shardingChangeRecord(0, caseId, recordId, shardingInName(infinityTask.getCaseId())));
            dataFileService.playback(
                Constants.ChannelBuilder.buildWebSocketPlaybackChannel(
                    String.valueOf(recordId)), dataFileId, startTimestamp,
                endTimestamp, extendDataWrappers);
            return AjaxResult.success(recordId);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("playback [{}] error!", recordId, e);
            }
            return AjaxResult.error(e.getMessage());
        }
    }

    private List<? extends ExtendedDataWrapper> shardingChangeRecord(
        Integer taskId, Integer caseId, Integer recordId,
        Map<Integer, String> shardingIdName) {
        QueryWrapper<TjShardingChangeRecord> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.eq("task_id", taskId);
        recordQueryWrapper.eq("case_id", caseId);
        recordQueryWrapper.eq("record_id", recordId);
        recordQueryWrapper.orderByAsc("create_timestamp");
        List<TjShardingChangeRecord> resutlList = shardingChangeRecordService.list(
            recordQueryWrapper);

        List<ExtendedDataWrapper<List<ShardingResultVo>>> wrappers = new ArrayList<>();
        for (TjShardingChangeRecord record : resutlList) {
            if (wrappers.isEmpty()) {
                ShardingResultVo shardingResultVo = new ShardingResultVo();
                shardingResultVo.setShardingName(
                    shardingIdName.get(record.getShardingId()));
                shardingResultVo.setTime(0);
                shardingResultVo.setEvaluationScore(
                    record.getEvaluationScore());
                shardingResultVo.setShardingId(record.getShardingId());
                shardingResultVo.setState(record.getState());
                wrappers.add(
                    new ExtendedDataWrapper<>(record.getCreateTimestamp(),
                        Arrays.asList(shardingResultVo)));
            } else {
                ExtendedDataWrapper<List<ShardingResultVo>> lastWrapper = wrappers.get(
                    wrappers.size() - 1);
                List<ShardingResultVo> data = lastWrapper.getData();
                ArrayList<ShardingResultVo> newSV = new ArrayList<>();
                for (ShardingResultVo sv : data) {
                    if (sv.getShardingId() == record.getShardingId()) {
                        ShardingResultVo shardingResultVo = new ShardingResultVo();
                        if(sv.getState() != record.getState() && record.getState() == 0){
                            shardingResultVo.setTime(sv.getTime() + 1);
                        }else {
                            shardingResultVo.setTime(sv.getTime());
                        }
                        shardingResultVo.setShardingName(sv.getShardingName());
                        shardingResultVo.setState(record.getState());
                        shardingResultVo.setEvaluationScore(
                            record.getEvaluationScore());
                        shardingResultVo.setShardingId(sv.getShardingId());
                        newSV.add(shardingResultVo);
                    } else {
                        newSV.add(sv);
                    }
                }
                wrappers.add(
                    new ExtendedDataWrapper<>(record.getCreateTimestamp(),
                        newSV));
            }
        }
        return wrappers;
    }

    @GetMapping("/playbackStop")
    @ApiImplicitParam(name = "recordId",
        required = true,
        value = "记录ID",
        dataType = "Long")
    public AjaxResult playbackStop(Integer recordId) {
        try{
            boolean result = dataFileService.playbackStop(
                Constants.ChannelBuilder.buildWebSocketPlaybackChannel(
                    String.valueOf(recordId)));
            return AjaxResult.success(result);
        }catch (Exception e){
            if(log.isErrorEnabled()){
                log.error("playbackStop error!", e);
            }
            return AjaxResult.error("playbackStop error!", e);
        }
    }

    @GetMapping("/playbackPause")
    @ApiImplicitParams({@ApiImplicitParam(name = "state",
        value = "状态,true：暂停，false：开始",
        dataType = "Long"), @ApiImplicitParam(name = "recordId",
        value = "记录ID",
        dataType = "Long",
        required = true)
    })
    public AjaxResult playbackPause(Boolean state, Integer recordId) {
        try{
            boolean result = dataFileService.playbackPause(state,
                Constants.ChannelBuilder.buildWebSocketPlaybackChannel(
                    String.valueOf(recordId)));
            return AjaxResult.success(result);
        }catch (Exception e){
            if(log.isErrorEnabled()){
                log.error("playbackPause error!", e);
            }
            return AjaxResult.error(e.getMessage());
        }
    }

    private Integer getLastRecordId(Integer taskId, Integer caseId,
        String username) {
        QueryWrapper<TjInfinityTaskRecord> recordQW = new QueryWrapper<>();
        if(null != taskId){
            recordQW.eq("task_id", taskId);
        }
        recordQW.eq("case_id", caseId);
        recordQW.orderByDesc("created_date");
        recordQW.eq("created_by", username);
        Page<TjInfinityTaskRecord> recordPage = new Page<>(0,1);
        Page<TjInfinityTaskRecord> pageRecord = tjInfinityTaskRecordService.page(
            recordPage, recordQW);
        if(CollectionUtils.isEmpty(pageRecord.getRecords())){
            return null;
        }else {
            return pageRecord.getRecords().get(0).getId();
        }
    }

    private Map<Integer, String> shardingInName(Integer caseId) {
        InfinteMileScenceExo infinteMileScenceExo = infinteMileScenceService.selectInfinteMileScenceById(
            caseId);
        return infinteMileScenceExo.getSiteSlices().stream()
            .collect(HashMap::new,
                (m, v) -> m.put(v.getSliceId(), v.getSliceName()),
                HashMap::putAll);
    }

    @GetMapping("/getMapfile")
    public AjaxResult getMapfile(Integer taskId) {
        TjInfinityTask task = tjInfinityTaskService.getById(taskId);
        InfinteMileScence infinteMileScence = infinteMileScenceService.getById(task.getCaseId());
        JSONObject jsonObject = new JSONObject();
        if(infinteMileScence.getMapFile() != null){
            jsonObject.put("mapFile", infinteMileScence.getMapFile());
        }
        return AjaxResult.success(jsonObject);
    }

}
