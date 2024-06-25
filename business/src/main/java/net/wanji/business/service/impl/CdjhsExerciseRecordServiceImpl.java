package net.wanji.business.service.impl;

import java.util.List;
import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.mapper.CdjhsExerciseRecordMapper;
import net.wanji.business.service.ICdjhsExerciseRecordService;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 练习记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-06-19
 */
@Service
public class CdjhsExerciseRecordServiceImpl implements ICdjhsExerciseRecordService
{
    @Autowired
    private CdjhsExerciseRecordMapper cdjhsExerciseRecordMapper;

    /**
     * 查询练习记录
     * 
     * @param id 练习记录主键
     * @return 练习记录
     */
    @Override
    public CdjhsExerciseRecord selectCdjhsExerciseRecordById(Long id)
    {
        return cdjhsExerciseRecordMapper.selectCdjhsExerciseRecordById(id);
    }

    /**
     * 查询练习记录列表
     * 
     * @param cdjhsExerciseRecord 练习记录
     * @return 练习记录
     */
    @Override
    public List<CdjhsExerciseRecord> selectCdjhsExerciseRecordList(CdjhsExerciseRecord cdjhsExerciseRecord)
    {
        Long userId = SecurityUtils.getLoginUser().getUser().getUserId();
        boolean admin = SecurityUtils.isAdmin(userId);
        if(!admin){
            String username = SecurityUtils.getUsername();
            cdjhsExerciseRecord.setUserName(username);
        }
        return cdjhsExerciseRecordMapper.selectCdjhsExerciseRecordList(cdjhsExerciseRecord);
    }

    /**
     * 新增练习记录
     * 
     * @param cdjhsExerciseRecord 练习记录
     * @return 结果
     */
    @Override
    public int insertCdjhsExerciseRecord(CdjhsExerciseRecord cdjhsExerciseRecord)
    {
        cdjhsExerciseRecord.setUserName(SecurityUtils.getUsername());
        cdjhsExerciseRecord.setCreateTime(DateUtils.getNowDate());
        return cdjhsExerciseRecordMapper.insertCdjhsExerciseRecord(cdjhsExerciseRecord);
    }

    /**
     * 修改练习记录
     * 
     * @param cdjhsExerciseRecord 练习记录
     * @return 结果
     */
    @Override
    public int updateCdjhsExerciseRecord(CdjhsExerciseRecord cdjhsExerciseRecord)
    {
        cdjhsExerciseRecord.setUpdateTime(DateUtils.getNowDate());
        return cdjhsExerciseRecordMapper.updateCdjhsExerciseRecord(cdjhsExerciseRecord);
    }

    /**
     * 批量删除练习记录
     * 
     * @param ids 需要删除的练习记录主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsExerciseRecordByIds(Long[] ids)
    {
        return cdjhsExerciseRecordMapper.deleteCdjhsExerciseRecordByIds(ids);
    }

    /**
     * 删除练习记录信息
     * 
     * @param id 练习记录主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsExerciseRecordById(Long id)
    {
        return cdjhsExerciseRecordMapper.deleteCdjhsExerciseRecordById(id);
    }
}
