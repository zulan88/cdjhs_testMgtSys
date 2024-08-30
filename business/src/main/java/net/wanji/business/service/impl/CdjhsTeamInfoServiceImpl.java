package net.wanji.business.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.wanji.business.domain.CdjhsTeamInfo;
import net.wanji.business.mapper.CdjhsTeamInfoMapper;
import net.wanji.business.service.ICdjhsTeamInfoService;
import net.wanji.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 团队信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-08-02
 */
@Service
public class CdjhsTeamInfoServiceImpl implements ICdjhsTeamInfoService
{
    @Autowired
    private CdjhsTeamInfoMapper cdjhsTeamInfoMapper;

    /**
     * 查询团队信息
     * 
     * @param id 团队信息主键
     * @return 团队信息
     */
    @Override
    public CdjhsTeamInfo selectCdjhsTeamInfoById(Long id)
    {
        return cdjhsTeamInfoMapper.selectCdjhsTeamInfoById(id);
    }

    /**
     * 查询团队信息列表
     * 
     * @param cdjhsTeamInfo 团队信息
     * @return 团队信息
     */
    @Override
    public List<CdjhsTeamInfo> selectCdjhsTeamInfoList(CdjhsTeamInfo cdjhsTeamInfo)
    {
        List<CdjhsTeamInfo> list = cdjhsTeamInfoMapper.selectCdjhsTeamInfoList(cdjhsTeamInfo);
        //按照比赛顺序增序排列
        list = list.stream()
                .sorted(Comparator.comparingInt(CdjhsTeamInfo::getSequence))
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 新增团队信息
     * 
     * @param cdjhsTeamInfo 团队信息
     * @return 结果
     */
    @Override
    public int insertCdjhsTeamInfo(CdjhsTeamInfo cdjhsTeamInfo)
    {
        cdjhsTeamInfo.setCreateTime(DateUtils.getNowDate());
        return cdjhsTeamInfoMapper.insertCdjhsTeamInfo(cdjhsTeamInfo);
    }

    /**
     * 修改团队信息
     * 
     * @param cdjhsTeamInfo 团队信息
     * @return 结果
     */
    @Override
    public int updateCdjhsTeamInfo(CdjhsTeamInfo cdjhsTeamInfo)
    {
        cdjhsTeamInfo.setUpdateTime(DateUtils.getNowDate());
        return cdjhsTeamInfoMapper.updateCdjhsTeamInfo(cdjhsTeamInfo);
    }

    /**
     * 批量删除团队信息
     * 
     * @param ids 需要删除的团队信息主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsTeamInfoByIds(Long[] ids)
    {
        return cdjhsTeamInfoMapper.deleteCdjhsTeamInfoByIds(ids);
    }

    /**
     * 删除团队信息信息
     * 
     * @param id 团队信息主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsTeamInfoById(Long id)
    {
        return cdjhsTeamInfoMapper.deleteCdjhsTeamInfoById(id);
    }

    @Override
    public List<CdjhsTeamInfo> getScoreRank() {
        CdjhsTeamInfo teamInfo = new CdjhsTeamInfo();
        List<CdjhsTeamInfo> list = selectCdjhsTeamInfoList(teamInfo);
        return list.stream()
                .sorted(Comparator.comparingDouble((CdjhsTeamInfo item) -> {
                    if (Objects.nonNull(item.getScore())) {
                        return item.getScore();
                    } else {
                        return -1;
                    }
                }).reversed().thenComparingInt(CdjhsTeamInfo::getSequence))
                .collect(Collectors.toList());
    }
}
