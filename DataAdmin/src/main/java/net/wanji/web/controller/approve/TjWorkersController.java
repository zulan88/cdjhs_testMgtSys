package net.wanji.web.controller.approve;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.wanji.approve.entity.AppointmentRecord;
import net.wanji.approve.entity.TjWorkers;
import net.wanji.approve.entity.vo.TjWorkersVo;
import net.wanji.approve.service.AppointmentRecordService;
import net.wanji.approve.service.RecordReService;
import net.wanji.approve.service.TjWorkersService;
import net.wanji.business.exception.BusinessException;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workers")
@Api(tags = "工作人员管理")  // 添加Controller的Swagger标签
public class TjWorkersController extends BaseController {

    @Autowired
    private TjWorkersService workersService;

    @Autowired
    RecordReService recordReService;

    @Autowired
    private AppointmentRecordService appointmentRecordService;

    // 分页查询，带条件
    @GetMapping("/list")
    @ApiOperation("分页查询工作人员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", dataType = "int", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "type", value = "人员类型", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "姓名", dataType = "String", paramType = "query")
    })
    public AjaxResult listWorkers(@RequestParam(defaultValue = "1") int pageNum,
                                  @RequestParam(defaultValue = "10") int pageSize,
                                  @RequestParam(required = false) String type,
                                  @RequestParam(required = false) String name) {
        return AjaxResult.success(workersService.listWorkers(pageNum, pageSize, type, name));
    }

    // 根据id查询
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询工作人员")
    @ApiImplicitParam(name = "id", value = "工作人员ID", dataType = "Integer", paramType = "path")
    public AjaxResult getWorkerById(@PathVariable Integer id) {
        return AjaxResult.success(workersService.getWorkerById(id));
    }

    // 新增
    @PostMapping("/add")
    @ApiOperation("新增工作人员")
    @ApiImplicitParam(name = "worker", value = "工作人员信息", dataType = "TjWorkers", paramType = "body")
    public AjaxResult addWorker(@RequestBody TjWorkers worker) {
        return workersService.addWorker(worker) ?
                AjaxResult.success("新增成功")
                : AjaxResult.error("新增失败");
    }

    // 更新
    @PutMapping("/update")
    @ApiOperation("更新工作人员信息")
    @ApiImplicitParam(name = "worker", value = "工作人员信息", dataType = "TjWorkers", paramType = "body")
    public AjaxResult updateWorker(@RequestBody TjWorkers worker) {
        return workersService.updateWorker(worker) ?
                AjaxResult.success("更新成功")
                : AjaxResult.error("更新失败");
    }

    // 根据id删除
    @DeleteMapping("/delete/{id}")
    @ApiOperation("根据ID删除工作人员")
    @ApiImplicitParam(name = "id", value = "工作人员ID", dataType = "Integer", paramType = "path")
    public AjaxResult deleteWorkerById(@PathVariable Integer id) {
        List<Integer> ids = recordReService.getrecordByperson(id);
        if (ids.size() > 0) {
            return AjaxResult.error("该人员已被排期，无法删除");
        }
        return workersService.deleteWorkerById(id) ?
                AjaxResult.success("删除成功")
                : AjaxResult.error("删除失败");
    }

    // 批量删除
    @DeleteMapping("/deleteBatch")
    @ApiOperation("批量删除工作人员")
    @ApiImplicitParam(name = "ids", value = "工作人员ID列表", dataType = "List<Integer>", paramType = "body")
    public AjaxResult deleteWorkersByIds(@RequestBody List<Integer> ids) {
        return workersService.deleteWorkersByIds(ids) ?
                AjaxResult.success("删除成功")
                : AjaxResult.error("删除失败");
    }

    @GetMapping("/persontoschedule")
    public TableDataInfo devicetoschedule(Integer personId) throws BusinessException {
        List<Integer> ids = recordReService.getrecordByperson(personId);
        startPage();
        List<AppointmentRecord> list = appointmentRecordService.getByids(ids);
        return getDataTable(list);
    }

    @GetMapping("/listbyrecord")
    public AjaxResult listbyrecord(Integer recordId) throws BusinessException {
        List<TjWorkersVo> list = workersService.listbyrecord(recordId);
        return AjaxResult.success(list);
    }
}
