package net.wanji.business.service;

import net.wanji.business.domain.CdjhsCarDetail;

import java.util.List;

/**
 * 实车信息Service接口
 * 
 * @author ruoyi
 * @date 2024-08-02
 */
public interface ICdjhsCarDetailService 
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
     * 批量删除实车信息
     * 
     * @param ids 需要删除的实车信息主键集合
     * @return 结果
     */
    public int deleteCdjhsCarDetailByIds(Long[] ids);

    /**
     * 删除实车信息信息
     * 
     * @param id 实车信息主键
     * @return 结果
     */
    public int deleteCdjhsCarDetailById(Long id);

    public boolean isUnique(CdjhsCarDetail cdjhsCarDetail);

    public List<CdjhsCarDetail> queryByCondition(CdjhsCarDetail cdjhsCarDetail);
}
