package net.wanji.business.mapper;

import net.wanji.business.domain.CdjhsMirrorMgt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 镜像列Mapper接口
 * 
 * @author ruoyi
 * @date 2024-06-20
 */
public interface CdjhsMirrorMgtMapper 
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
     * 删除镜像列
     * 
     * @param id 镜像列主键
     * @return 结果
     */
    public int deleteCdjhsMirrorMgtById(Long id);

    /**
     * 批量删除镜像列
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCdjhsMirrorMgtByIds(Long[] ids);

    public int update(@Param("id") Long id, @Param("localFilePath") String localFilePath, @Param("md5") String md5, @Param("uploadStatus") Integer uploadStatus);

    public List<CdjhsMirrorMgt> selectCdjhsMirrorMgtByIds(Long[] ids);
}
