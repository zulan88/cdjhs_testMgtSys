package net.wanji.business.service;

import net.wanji.business.domain.CdjhsUserTeam;

import java.util.List;

/**
 * 用户团队关联Service接口
 * 
 * @author ruoyi
 * @date 2024-08-02
 */
public interface ICdjhsUserTeamService 
{
    /**
     * 查询用户团队关联
     * 
     * @param id 用户团队关联主键
     * @return 用户团队关联
     */
    public CdjhsUserTeam selectCdjhsUserTeamById(Long id);

    /**
     * 查询用户团队关联列表
     * 
     * @param cdjhsUserTeam 用户团队关联
     * @return 用户团队关联集合
     */
    public List<CdjhsUserTeam> selectCdjhsUserTeamList(CdjhsUserTeam cdjhsUserTeam);

    /**
     * 新增用户团队关联
     * 
     * @param cdjhsUserTeam 用户团队关联
     * @return 结果
     */
    public int insertCdjhsUserTeam(CdjhsUserTeam cdjhsUserTeam);

    /**
     * 修改用户团队关联
     * 
     * @param cdjhsUserTeam 用户团队关联
     * @return 结果
     */
    public int updateCdjhsUserTeam(CdjhsUserTeam cdjhsUserTeam);

    /**
     * 批量删除用户团队关联
     * 
     * @param ids 需要删除的用户团队关联主键集合
     * @return 结果
     */
    public int deleteCdjhsUserTeamByIds(Long[] ids);

    /**
     * 删除用户团队关联信息
     * 
     * @param id 用户团队关联主键
     * @return 结果
     */
    public int deleteCdjhsUserTeamById(Long id);
}