package net.wanji.web.controller.makeanappointment;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.wanji.approve.entity.AppointmentRecord;
import net.wanji.approve.service.AppointmentRecordService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.makeanappointment.domain.vo.TestObjectVo;
import net.wanji.makeanappointment.service.TestObjectService;
import com.github.pagehelper.page.PageMethod;
import io.swagger.annotations.*;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName MakeAnAppointmentController
 * @Description
 * @Author liruitao
 * @Date 2023-12-05
 * @Version 1.0
 **/
@Api(tags = "测试预约平台-预约申请(被测对象列表)")
@RestController
@RequestMapping("/testObject")
public class TestObjectController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TestObjectController.class);

    @Resource
    private TestObjectService testObjectService;

    @Autowired
    private AppointmentRecordService appointmentRecordService;

    /**
     * 新增被测对象
     * @param testeeObjectVo 被测对象信息
     * @return {@link AjaxResult}
     * @author liruitao
     * @date 2023-12-05
     */
    @ApiOperationSort(1)
    @ApiOperation(value = "1.新增被测对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "testeeObjectVo", value = "被测对象信息", required = true, dataType = "TesteeObjectVo")
    })
    @PostMapping("/addTesteeObject")
    public AjaxResult addTesteeObject(@RequestBody TestObjectVo testeeObjectVo) {
        if(testeeObjectVo == null){
            return AjaxResult.error("被测对象信息不能为空!");
        }
        try {
            testObjectService.addTesteeObject(testeeObjectVo);
        }catch (Exception e){
            logger.error("新增被测对象失败", e);
            return AjaxResult.error("新增被测对象失败!");
        }
        return AjaxResult.success();
    }

    /**
     * 删除被测对象
     * @param id
     * @return {@link AjaxResult}
     * @date 2023-12-06
     */
    @ApiOperationSort(2)
    @ApiOperation(value = "2.删除被测对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "被测对象id", required = true, dataType = "Integer")
    })
    @DeleteMapping("/deleteTesteeObject")
    public AjaxResult deleteTesteeObject(Integer id) {
        if(id == null){
            return AjaxResult.error("被测对象id不能为空!");
        }
        QueryWrapper<AppointmentRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("measurand_id", id);
        if(appointmentRecordService.count(queryWrapper) > 0){
            return AjaxResult.error("被测对象已被预约，不能删除!");
        }
        try {
            testObjectService.deleteTesteeObject(id);
        }catch (Exception e){
            logger.error("删除被测对象失败", e);
            return AjaxResult.error("删除被测对象失败!");
        }
        return AjaxResult.success();
    }

    /**
     * 修改被测对象
     */
    @ApiOperationSort(3)
    @ApiOperation(value = "3.修改被测对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "testeeObjectVo", value = "被测对象信息", required = true, dataType = "TesteeObjectVo")
    })
    @PostMapping("/updateTesteeObject")
    public AjaxResult updateTesteeObject(@RequestBody TestObjectVo testeeObjectVo) {
        if(testeeObjectVo == null){
            return AjaxResult.error("被测对象信息不能为空!");
        }
        if(testeeObjectVo.getId() == null){
            return AjaxResult.error("被测对象id不能为空!");
        }
        try {
            testObjectService.updateTesteeObject(testeeObjectVo);
        }catch (Exception e){
            logger.error("修改被测对象失败", e);
            return AjaxResult.error("修改被测对象失败!");
        }
        return AjaxResult.success();
    }

    /**
     * 根据id查询被测对象详情
     */
    @ApiOperationSort(4)
    @ApiOperation(value = "4.根据id查询被测对象详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "被测对象id", required = true, dataType = "Integer")
    })
    @GetMapping("/queryTesteeObjectById")
    public AjaxResult queryTesteeObjectById(Integer id) {
        if(id == null){
            return AjaxResult.error("被测对象id不能为空!");
        }
        TestObjectVo testeeObjectVo;
        try {
            testeeObjectVo = testObjectService.queryTesteeObjectById(id);
        }catch (Exception e){
            logger.error("根据id查询被测对象详情失败", e);
            return AjaxResult.error("根据id查询被测对象详情失败!");
        }
        return AjaxResult.success(testeeObjectVo);
    }

    /**
     * 分页查询被测对象列表
     */
    @ApiOperationSort(5)
    @ApiOperation(value = "5.分页查询被测对象列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "testeeObjectVo", value = "被测对象信息", required = true, dataType = "TesteeObjectVo")
    })
    @PostMapping("/queryTesteeObjectList")
    public TableDataInfo queryTesteeObjectList(@RequestBody TestObjectVo testeeObjectVo) {
        PageMethod.startPage(testeeObjectVo.getPageNum(), testeeObjectVo.getPageSize());
        return getDataTable(testObjectService.queryTesteeObjectList(testeeObjectVo));
    }

}
