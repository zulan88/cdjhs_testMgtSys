package net.wanji.business.mapper;

import net.wanji.business.domain.CdjhsTeamInfo;

import java.util.List;

/**
 * 团队信息Mapper接口
 * 
 * @author ruoyi
 * @date 2024-08-02
 */
public interface CdjhsTeamInfoMapper 
{
    /**
     * 查询团队信息
     * 
     * @param id 团队信息主键
     * @return 团队信息
     */
    public CdjhsTeamInfo selectCdjhsTeamInfoById(Long id);

    /**
     * 查询团队信息列表
     * 
     * @param cdjhsTeamInfo 团队信息
     * @return 团队信息集合
     */
    public List<CdjhsTeamInfo> selectCdjhsTeamInfoList(CdjhsTeamInfo cdjhsTeamInfo);

    /**
     * 新增团队信息
     * 
     * @param cdjhsTeamInfo 团队信息
     * @return 结果
     */
    public int insertCdjhsTeamInfo(CdjhsTeamInfo cdjhsTeamInfo);

    /**
     * 修改团队信息
     * 
     * @param cdjhsTeamInfo 团队信息
     * @return 结果
     */
    public int updateCdjhsTeamInfo(CdjhsTeamInfo cdjhsTeamInfo);

    /**
     * 删除团队信息
     * 
     * @param id 团队信息主键
     * @return 结果
     */
    public int deleteCdjhsTeamInfoById(Long id);

    /**
     * 批量删除团队信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCdjhsTeamInfoByIds(Long[] ids);
}
