package net.wanji.business.mapper;

import net.wanji.business.domain.CdjhsCarDetail;

import java.util.List;

/**
 * 实车信息Mapper接口
 * 
 * @author ruoyi
 * @date 2024-08-02
 */
public interface CdjhsCarDetailMapper 
{
    /**
     * 查询实车信息
     * 
     * @param id 实车信息主键
     * @return 实车信息
     */
    public CdjhsCarDetail selectCdjhsCarDetailById(Long id);

    /**
     * 查询实车信息列表
     * 
     * @param cdjhsCarDetail 实车信息
     * @return 实车信息集合
     */
    public List<CdjhsCarDetail> selectCdjhsCarDetailList(CdjhsCarDetail cdjhsCarDetail);

    /**
     * 新增实车信息
     * 
     * @param cdjhsCarDetail 实车信息
     * @return 结果
     */
    public int insertCdjhsCarDetail(CdjhsCarDetail cdjhsCarDetail);

    /**
     * 修改实车信息
     * 
     * @param cdjhsCarDetail 实车信息
     * @return 结果
     */
    public int updateCdjhsCarDetail(CdjhsCarDetail cdjhsCarDetail);

    /**
     * 删除实车信息
     * 
     * @param id 实车信息主键
     * @return 结果
     */
    public int deleteCdjhsCarDetailById(Long id);

    /**
     * 批量删除实车信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCdjhsCarDetailByIds(Long[] ids);
}
