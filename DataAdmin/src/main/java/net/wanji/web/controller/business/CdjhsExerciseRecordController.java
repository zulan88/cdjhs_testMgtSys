package net.wanji.web.controller.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.domain.evaluation.EvaluationReport;
import net.wanji.business.pdf.PdfService;
import net.wanji.business.service.ICdjhsExerciseRecordService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
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
}
