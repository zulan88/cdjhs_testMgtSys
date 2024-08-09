package net.wanji.business.service.impl;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import net.wanji.business.domain.CdjhsMirrorMgt;
import net.wanji.business.mapper.CdjhsMirrorMgtMapper;
import net.wanji.business.oss.FileService;
import net.wanji.business.service.ICdjhsMirrorMgtService;
import net.wanji.common.core.domain.entity.SysUser;
import net.wanji.common.utils.ConvertUtil;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 镜像列Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-06-20
 */
@Service
@Slf4j
public class CdjhsMirrorMgtServiceImpl implements ICdjhsMirrorMgtService
{
    @Autowired
    private CdjhsMirrorMgtMapper cdjhsMirrorMgtMapper;

    @Autowired
    private FileService fileService;

    /**
     * 查询镜像列
     * 
     * @param id 镜像列主键
     * @return 镜像列
     */
    @Override
    public CdjhsMirrorMgt selectCdjhsMirrorMgtById(Long id)
    {
        return cdjhsMirrorMgtMapper.selectCdjhsMirrorMgtById(id);
    }

    /**
     * 查询镜像列列表
     * 
     * @param cdjhsMirrorMgt 镜像列
     * @return 镜像列
     */
    @Override
    public List<CdjhsMirrorMgt> selectCdjhsMirrorMgtList(CdjhsMirrorMgt cdjhsMirrorMgt)
    {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        boolean student = SecurityUtils.isStudent(user);
        if(student){
            String username = SecurityUtils.getUsername();
            cdjhsMirrorMgt.setCreateBy(username);
        }
        return cdjhsMirrorMgtMapper.selectCdjhsMirrorMgtList(cdjhsMirrorMgt);
    }

    /**
     * 新增镜像列
     * 
     * @param cdjhsMirrorMgt 镜像列
     * @return 结果
     */
    @Override
    public int insertCdjhsMirrorMgt(CdjhsMirrorMgt cdjhsMirrorMgt)
    {
        cdjhsMirrorMgt.setCreateBy(SecurityUtils.getUsername());
        cdjhsMirrorMgt.setCreateTime(DateUtils.getNowDate());
        //格式化文件大小
        String size = ConvertUtil.convertFileSize(cdjhsMirrorMgt.getTotalSize());
        cdjhsMirrorMgt.setMirrorSize(size);
        cdjhsMirrorMgt.setUploadStatus(2);
        int rows = cdjhsMirrorMgtMapper.insertCdjhsMirrorMgt(cdjhsMirrorMgt);
        fileService.saveLocalFile(cdjhsMirrorMgt);
        return rows;
    }

    /**
     * 修改镜像列
     * 
     * @param cdjhsMirrorMgt 镜像列
     * @return 结果
     */
    @Override
    public int updateCdjhsMirrorMgt(CdjhsMirrorMgt cdjhsMirrorMgt)
    {
        cdjhsMirrorMgt.setUpdateBy(SecurityUtils.getUsername());
        cdjhsMirrorMgt.setUpdateTime(DateUtils.getNowDate());
        String pathLocal = cdjhsMirrorMgt.getMirrorPathLocal();
        String pathCloud = cdjhsMirrorMgt.getMirrorPathCloud();
        boolean match = StringUtils.isEmpty(pathLocal) || fileService.check(pathLocal, pathCloud);
        if(!match){
            fileService.saveLocalFile(cdjhsMirrorMgt);
        }
        return cdjhsMirrorMgtMapper.updateCdjhsMirrorMgt(cdjhsMirrorMgt);
    }

    /**
     * 批量删除镜像列
     * 
     * @param ids 需要删除的镜像列主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsMirrorMgtByIds(Long[] ids)
    {
        //查询待删除镜像信息
        List<CdjhsMirrorMgt> cdjhsMirrorMgts = cdjhsMirrorMgtMapper.selectCdjhsMirrorMgtByIds(ids);
        int i = cdjhsMirrorMgtMapper.deleteCdjhsMirrorMgtByIds(ids);
        fileService.deleteMirrors(cdjhsMirrorMgts);
        return i;
    }

    /**
     * 删除镜像列信息
     * 
     * @param id 镜像列主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsMirrorMgtById(Long id)
    {
        return cdjhsMirrorMgtMapper.deleteCdjhsMirrorMgtById(id);
    }

    @Override
    public Map<String, String> initialMultipartUpload(String fileName) {
        return fileService.initMultipartUpload(fileName);
    }

    @Override
    public boolean chunkUpload(String uploadId, String objectName, Long totalSize, Integer totalChunks, MultipartFile multipartFile, Long chunkSize, Integer chunkIndex) {
        return fileService.uploadPart(uploadId, objectName, totalSize, totalChunks, multipartFile, chunkSize, chunkIndex);
    }

    @Override
    public Map<String, String> chunkMerge(String objectName, String uploadId) {
        return fileService.chunkMerge(objectName, uploadId);
    }

    @Override
    public Map<String, String> upload(MultipartFile multipartFile, String requestId) {
        return fileService.upload(multipartFile, requestId);
    }

    @Override
    public double getUploadFileProgress(String requestId) {
        return fileService.getUploadFileProgress(requestId);
    }
}
