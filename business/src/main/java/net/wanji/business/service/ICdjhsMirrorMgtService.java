package net.wanji.business.service;

import net.wanji.business.domain.CdjhsMirrorMgt;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 镜像列Service接口
 * 
 * @author ruoyi
 * @date 2024-06-20
 */
public interface ICdjhsMirrorMgtService 
{
    /**
     * 查询镜像列
     * 
     * @param id 镜像列主键
     * @return 镜像列
     */
    public CdjhsMirrorMgt selectCdjhsMirrorMgtById(Long id);

    /**
     * 查询镜像列列表
     * 
     * @param cdjhsMirrorMgt 镜像列
     * @return 镜像列集合
     */
    public List<CdjhsMirrorMgt> selectCdjhsMirrorMgtList(CdjhsMirrorMgt cdjhsMirrorMgt);

    /**
     * 新增镜像列
     * 
     * @param cdjhsMirrorMgt 镜像列
     * @return 结果
     */
    public int insertCdjhsMirrorMgt(CdjhsMirrorMgt cdjhsMirrorMgt);

    /**
     * 修改镜像列
     * 
     * @param cdjhsMirrorMgt 镜像列
     * @return 结果
     */
    public int updateCdjhsMirrorMgt(CdjhsMirrorMgt cdjhsMirrorMgt);

    /**
     * 批量删除镜像列
     * 
     * @param ids 需要删除的镜像列主键集合
     * @return 结果
     */
    public int deleteCdjhsMirrorMgtByIds(Long[] ids);

    /**
     * 删除镜像列信息
     * 
     * @param id 镜像列主键
     * @return 结果
     */
    public int deleteCdjhsMirrorMgtById(Long id);

    public Map<String, String> initialMultipartUpload(String fileName);

    public boolean chunkUpload(String uploadId, String objectName, Long totalSize, Integer totalChunks, MultipartFile multipartFile, Long chunkSize, Integer chunkIndex);

    public Map<String, String> chunkMerge(String objectName, String uploadId);

    public Map<String, String> upload(MultipartFile multipartFile, String requestId);

    public double getUploadFileProgress(String requestId);
}
