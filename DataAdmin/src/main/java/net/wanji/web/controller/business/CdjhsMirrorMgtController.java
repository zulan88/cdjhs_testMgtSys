package net.wanji.web.controller.business;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSort;
import net.wanji.business.domain.CdjhsMirrorMgt;
import net.wanji.business.domain.ChunkMergeReq;
import net.wanji.business.service.ICdjhsMirrorMgtService;
import net.wanji.common.core.controller.BaseController;
import net.wanji.common.core.domain.AjaxResult;
import net.wanji.common.core.page.TableDataInfo;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 镜像列Controller
 * 
 * @author ruoyi
 * @date 2024-06-20
 */
@Api(tags = "镜像管理")
@RestController
@RequestMapping("/mirror")
public class CdjhsMirrorMgtController extends BaseController
{
    @Autowired
    private ICdjhsMirrorMgtService cdjhsMirrorMgtService;

    /**
     * 查询镜像列列表
     */
    @ApiOperationSort(1)
    @ApiOperation(value = "镜像列表查询")
    @GetMapping("/list")
    public TableDataInfo list(CdjhsMirrorMgt cdjhsMirrorMgt)
    {
        startPage();
        List<CdjhsMirrorMgt> list = cdjhsMirrorMgtService.selectCdjhsMirrorMgtList(cdjhsMirrorMgt);
        return getDataTable(list);
    }

    /**
     * 导出镜像列列表
     */
    @GetMapping("/export")
    public AjaxResult export(CdjhsMirrorMgt cdjhsMirrorMgt)
    {
        List<CdjhsMirrorMgt> list = cdjhsMirrorMgtService.selectCdjhsMirrorMgtList(cdjhsMirrorMgt);
        ExcelUtil<CdjhsMirrorMgt> util = new ExcelUtil<CdjhsMirrorMgt>(CdjhsMirrorMgt.class);
        return util.exportExcel(list, "镜像列数据");
    }

    /**
     * 获取镜像列详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(cdjhsMirrorMgtService.selectCdjhsMirrorMgtById(id));
    }

    /**
     * 新增镜像列
     */
    @ApiOperationSort(2)
    @ApiOperation(value = "新增镜像")
    @PostMapping("/add")
    public AjaxResult add(@RequestBody CdjhsMirrorMgt cdjhsMirrorMgt)
    {
        return toAjax(cdjhsMirrorMgtService.insertCdjhsMirrorMgt(cdjhsMirrorMgt));
    }

    /**
     * 修改镜像列
     */
    @ApiOperationSort(3)
    @ApiOperation(value = "编辑镜像")
    @PutMapping("/update")
    public AjaxResult edit(@RequestBody CdjhsMirrorMgt cdjhsMirrorMgt)
    {
        return toAjax(cdjhsMirrorMgtService.updateCdjhsMirrorMgt(cdjhsMirrorMgt));
    }

    /**
     * 删除镜像列
     */
    @ApiOperationSort(4)
    @ApiOperation(value = "删除镜像")
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(cdjhsMirrorMgtService.deleteCdjhsMirrorMgtByIds(ids));
    }

    @ApiOperationSort(5)
    @ApiOperation(value = "镜像分片上传初始化")
    @GetMapping("/initialMultipartUpload")
    public AjaxResult initialMultipartUpload(String fileName){
	    if(StringUtils.isEmpty(fileName)){
	        return AjaxResult.error("文件名称不能为空");
        }
        Map<String, String> map = cdjhsMirrorMgtService.initialMultipartUpload(fileName);
	    if(map.containsKey("uploadId")){
	        return AjaxResult.success(map);
        }
	    return AjaxResult.error("文件分片上传初始化失败");
    }

    @ApiOperationSort(6)
    @ApiOperation(value = "镜像分片上传")
    @PostMapping("/chunkUpload")
    public AjaxResult chunkUpload(String uploadId, String objectName, Long totalSize, Integer totalChunks, MultipartFile file, Long chunkSize, Integer chunkIndex){
	    if(StringUtils.isEmpty(uploadId) || StringUtils.isEmpty(objectName)
        || file.isEmpty() || Objects.isNull(chunkSize) || Objects.isNull(chunkIndex)){
	        return AjaxResult.error("参数错误");
        }
        boolean success = cdjhsMirrorMgtService.chunkUpload(uploadId, objectName, totalSize, totalChunks, file, chunkSize, chunkIndex);
	    if(success){
	        return AjaxResult.success("文件切片上传成功");
        }
	    return AjaxResult.error("文件切片上传失败");
    }

    @ApiOperationSort(7)
    @ApiOperation(value = "镜像分片文件合并")
    @PostMapping("/chunkMerge")
    public AjaxResult chunkMerge(@RequestBody @Validated ChunkMergeReq chunkMergeReq){
	    if(StringUtils.isEmpty(chunkMergeReq.getObjectName()) || StringUtils.isEmpty(chunkMergeReq.getUploadId())){
	        return AjaxResult.error("参数错误");
        }
        Map<String, String> map = cdjhsMirrorMgtService.chunkMerge(chunkMergeReq.getObjectName(), chunkMergeReq.getUploadId());
	    if(map.isEmpty()){
	        return AjaxResult.error("分片合并失败");
        }
	    return AjaxResult.success(map);
    }


    @ApiOperationSort(9)
    @ApiOperation(value = "镜像文件上传-小文件上传直接调用")
    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile multipartFile, String requestId){
	    if(multipartFile.isEmpty()){
	        return AjaxResult.error("文件内容为空");
        }
        Map<String, String> map = cdjhsMirrorMgtService.upload(multipartFile, requestId);
	    if(map.isEmpty()){
	        return AjaxResult.error("文件上传失败");
        }
	    return AjaxResult.success(map);
    }

    @ApiOperationSort(8)
    @ApiOperation(value = "获取镜像上传进度条")
    @GetMapping("/getUploadFileProgress")
    public AjaxResult getUploadFileProgress(String requestId){
	    if(StringUtils.isEmpty(requestId)){
	        return AjaxResult.error("请求id不能为空");
        }
        String progress = cdjhsMirrorMgtService.getUploadFileProgress(requestId);
	    return AjaxResult.success(progress);
    }
}
