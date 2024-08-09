package net.wanji.business.service.impl;

import java.util.List;

import net.wanji.business.domain.CdjhsUserTeam;
import net.wanji.business.mapper.CdjhsUserTeamMapper;
import net.wanji.business.service.ICdjhsUserTeamService;
import net.wanji.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 用户团队关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-08-02
 */
@Service
public class CdjhsUserTeamServiceImpl implements ICdjhsUserTeamService
{
    @Autowired
    private CdjhsUserTeamMapper cdjhsUserTeamMapper;

    /**
     * 查询用户团队关联
     * 
     * @param id 用户团队关联主键
     * @return 用户团队关联
     */
    @Override
    public CdjhsUserTeam selectCdjhsUserTeamById(Long id)
    {
        return cdjhsUserTeamMapper.selectCdjhsUserTeamById(id);
    }

    /**
     * 查询用户团队关联列表
     * 
     * @param cdjhsUserTeam 用户团队关联
     * @return 用户团队关联
     */
    @Override
    public List<CdjhsUserTeam> selectCdjhsUserTeamList(CdjhsUserTeam cdjhsUserTeam)
    {
        return cdjhsUserTeamMapper.selectCdjhsUserTeamList(cdjhsUserTeam);
    }

    /**
     * 新增用户团队关联
     * 
     * @param cdjhsUserTeam 用户团队关联
     * @return 结果
     */
    @Override
    public int insertCdjhsUserTeam(CdjhsUserTeam cdjhsUserTeam)
    {
        cdjhsUserTeam.setCreateTime(DateUtils.getNowDate());
        return cdjhsUserTeamMapper.insertCdjhsUserTeam(cdjhsUserTeam);
    }

    /**
     * 修改用户团队关联
     * 
     * @param cdjhsUserTeam 用户团队关联
     * @return 结果
     */
    @Override
    public int updateCdjhsUserTeam(CdjhsUserTeam cdjhsUserTeam)
    {
        cdjhsUserTeam.setUpdateTime(DateUtils.getNowDate());
        return cdjhsUserTeamMapper.updateCdjhsUserTeam(cdjhsUserTeam);
    }

    /**
     * 批量删除用户团队关联
     * 
     * @param ids 需要删除的用户团队关联主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsUserTeamByIds(Long[] ids)
    {
        return cdjhsUserTeamMapper.deleteCdjhsUserTeamByIds(ids);
    }

    /**
     * 删除用户团队关联信息
     * 
     * @param id 用户团队关联主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsUserTeamById(Long id)
    {
        return cdjhsUserTeamMapper.deleteCdjhsUserTeamById(id);
    }
}
