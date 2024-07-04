package net.wanji.web.controller.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.domain.bo.SceneTrajectoryBo;
import net.wanji.business.domain.dto.device.DeviceStateDto;
import net.wanji.business.domain.evaluation.EvaluationReport;
import net.wanji.business.domain.param.TessParam;
import net.wanji.business.domain.vo.SceneDetailVo;
import net.wanji.business.exercise.ExerciseHandler;
import net.wanji.business.exercise.dto.evaluation.StartPoint;
import net.wanji.business.exercise.dto.simulation.SimulationSceneDto;
import net.wanji.business.pdf.PdfService;
import net.wanji.business.service.ICdjhsExerciseRecordService;
import net.wanji.business.service.RestService;
import net.wanji.business.util.InteractionFuc;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 练习记录Controller
 * 
 * @author ruoyi
 * @date 2024-06-19
 */
@Slf4j
@Api(tags = "练习记录管理")
@RestController
@RequestMapping("/exercise")
public class CdjhsExerciseRecordController extends BaseController
{
    @Autowired
    private ICdjhsExerciseRecordService cdjhsExerciseRecordService;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private InteractionFuc interactionFuc;

    @Autowired
    private RestService restService;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询练习记录列表
     */
    @ApiOperationSort(1)
    @ApiOperation(value = "练习记录查询")
    @GetMapping("/list")
    public TableDataInfo list(CdjhsExerciseRecord cdjhsExerciseRecord)
    {
        startPage();
        List<CdjhsExerciseRecord> list = cdjhsExerciseRecordService.selectCdjhsExerciseRecordList(cdjhsExerciseRecord);
        return getDataTable(list);
    }

    /**
     * 获取练习记录详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cdjhsExerciseRecordService.selectCdjhsExerciseRecordById(id));
    }

    /**
     * 新增练习记录
     */
    @ApiOperationSort(2)
    @ApiOperation(value = "记录查询新增")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody CdjhsExerciseRecord cdjhsExerciseRecord)
    {
        return toAjax(cdjhsExerciseRecordService.insertCdjhsExerciseRecord(cdjhsExerciseRecord));
    }

    /**
     * 修改练习记录
     */
    @PutMapping("/update")
    public AjaxResult edit(@RequestBody CdjhsExerciseRecord cdjhsExerciseRecord)
    {
        return toAjax(cdjhsExerciseRecordService.updateCdjhsExerciseRecord(cdjhsExerciseRecord));
    }

    /**
     * 删除练习记录
     */
    @ApiOperationSort(3)
    @ApiOperation(value = "练习记录删除")
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cdjhsExerciseRecordService.deleteCdjhsExerciseRecordByIds(ids));
    }

    @GetMapping("/reviewReport")
    public AjaxResult reviewReport(Long taskId){
        if(Objects.isNull(taskId)){
            return AjaxResult.error("请求参数不能为空");
        }
        EvaluationReport report = cdjhsExerciseRecordService.reviewReport(taskId);
        if(Objects.isNull(report)){
            return AjaxResult.error("报告不存在");
        }
        return AjaxResult.success(report);
    }

    @GetMapping("/playback")
    public AjaxResult playback(@RequestParam("taskId") Integer taskId, @RequestParam("action") Integer action){
        try {
            cdjhsExerciseRecordService.playback(taskId, action);
        }catch (Exception e){
            log.error("回放失败: {}", e.getMessage());
            return AjaxResult.error("回放失败");
        }
        return AjaxResult.success("请等待...");
    }

    @GetMapping("/downloadPdf")
    public ResponseEntity<InputStreamSource> downloadPdf(Long taskId){
        try {
            CdjhsExerciseRecord record = cdjhsExerciseRecordService.selectCdjhsExerciseRecordById(taskId);
            if(Objects.isNull(record) || StringUtils.isEmpty(record.getEvaluationOutput())){
                return ResponseEntity.status(500).build();
            }

            ByteArrayOutputStream outputStream = pdfService.generatePdf(record);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Disposition", "attachment; filename=test.pdf");

            return ResponseEntity
                    .ok()
                    .headers(httpHeaders)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(byteArrayInputStream));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/test")
    public AjaxResult test(Long taskId){
        try {
            CdjhsExerciseRecord record = cdjhsExerciseRecordService.selectCdjhsExerciseRecordById(taskId);
            ExerciseHandler.taskQueue.put(record);
            return AjaxResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error("算法入队列失败");
        }
    }

    @GetMapping("/getSceneDetailsByTestId")
    public AjaxResult getSceneDetailsByTestId(Integer testId){
        List<SceneDetailVo> sceneDetail = interactionFuc.findSceneDetail(testId);
        return AjaxResult.success(sceneDetail);
    }

    @GetMapping("/getSceneTrajectory")
    public AjaxResult getSceneTrajectory(Integer sceneId){
        try {
            SceneTrajectoryBo sceneTrajectory = interactionFuc.getSceneTrajectory(sceneId);
            return AjaxResult.success(sceneTrajectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return AjaxResult.error();
    }

    @GetMapping("/testSceneDetails")
    public AjaxResult testSceneDetails(Integer testId){
        SimulationSceneDto simulationSceneInfo = interactionFuc.getSimulationSceneInfo(testId);
        return AjaxResult.success(simulationSceneInfo);
    }

    @GetMapping("/testStartPoints")
    public AjaxResult testStartPoints(Integer testId){
        List<StartPoint> sceneStartPoints = interactionFuc.getSceneStartPoints(testId);
        return AjaxResult.success(sceneStartPoints);
    }

    private String ip = "http://129.211.28.237";

    private Integer port = 8079;

    @GetMapping("/startTessServer")
    public AjaxResult startTessServer(){
        String json = "{\"commandChannel\":\"admin_1_0_5_control\",\"dataChannel\":\"admin_1_0_5_data\",\"evaluateChannel\":\"admin_1_0_5_evaluate\",\"mapList\":[\"21\"],\"params\":{\"params\":{\"params\":[]}},\"roadNum\":21,\"routingChannel\":\"1\",\"simulateType\":5,\"statusChannel\":\"admin_1_0_5_status\"}";
        TessParam tessParam = JSONObject.parseObject(json, TessParam.class);
        String commandChannel = tessParam.getCommandChannel();
        int result = restService.startServer(ip, port, tessParam);
        if(result == 1){
            //下发设备状态上报请求
            DeviceStateDto deviceStateDto = new DeviceStateDto();
            deviceStateDto.setTimestamp(System.currentTimeMillis());
            deviceStateDto.setType(0);
            deviceStateDto.setDeviceId(4);
            String message = JSONObject.toJSONString(deviceStateDto);
            JSONObject statusReq = JSONObject.parseObject(message);
            redisCache.publishMessage(commandChannel, statusReq);
            log.info("请求仿真上报状态已下发:{}", message);
        }
        return AjaxResult.success(result);
    }

    @GetMapping("/issueTessScenes")
    public AjaxResult issueTessScenes(Long taskId){
        String json = "{\"commandChannel\":\"admin_1_0_5_control\",\"dataChannel\":\"admin_1_0_5_data\",\"evaluateChannel\":\"admin_1_0_5_evaluate\",\"mapList\":[\"21\"],\"params\":{\"params\":{\"params\":[]}},\"roadNum\":21,\"routingChannel\":\"1\",\"simulateType\":5,\"statusChannel\":\"admin_1_0_5_status\"}";
        TessParam tessParam = JSONObject.parseObject(json, TessParam.class);

        CdjhsExerciseRecord record = cdjhsExerciseRecordService.selectCdjhsExerciseRecordById(taskId);
        String tessCommandChannel = tessParam.getCommandChannel();
        SimulationSceneDto simulationSceneInfo = interactionFuc.getSimulationSceneInfo(record.getTestId().intValue());
        String message = JSONObject.toJSONString(simulationSceneInfo);
        JSONObject issueMessage = JSONObject.parseObject(message);
        redisCache.publishMessage(tessCommandChannel, issueMessage);
        log.info("下发仿真片段式场景成功");
        return AjaxResult.success();
    }

    @PostMapping("/startScene")
    public AjaxResult startScene(){
        String json = "{\"commandChannel\":\"admin_1_0_5_control\",\"dataChannel\":\"admin_1_0_5_data\",\"evaluateChannel\":\"admin_1_0_5_evaluate\",\"mapList\":[\"21\"],\"params\":{\"params\":{\"params\":[]}},\"roadNum\":21,\"routingChannel\":\"1\",\"simulateType\":5,\"statusChannel\":\"admin_1_0_5_status\"}";
        TessParam tessParam = JSONObject.parseObject(json, TessParam.class);
        String commandChannel = tessParam.getCommandChannel();

        String tessStart = "{\"params\":{\"protocols\":[{\"channel\":\"CDJHS_GKQResult_YK001\",\"params\":{},\"type\":0},{\"channel\":\"admin_1_0_5_data\",\"params\":{},\"type\":1}],\"taskType\":1},\"timestamp\":1719977115554,\"type\":2}";
        JSONObject message = JSONObject.parseObject(tessStart);
        redisCache.publishMessage(commandChannel, message);
        log.info("下发场景开始指令成功");

        return AjaxResult.success();
    }
}
