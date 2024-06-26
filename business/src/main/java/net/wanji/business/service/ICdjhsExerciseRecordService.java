package net.wanji.business.service;

import net.wanji.business.domain.CdjhsExerciseRecord;

import java.util.List;

/**
 * 练习记录Service接口
 * 
 * @author ruoyi
 * @date 2024-06-19
 */
public interface ICdjhsExerciseRecordService 
{
    /**
     * 查询练习记录
     * 
     * @param id 练习记录主键
     * @return 练习记录
     */
    public CdjhsExerciseRecord selectCdjhsExerciseRecordById(Long id);

    /**
     * 查询练习记录列表
     * 
     * @param cdjhsExerciseRecord 练习记录
     * @return 练习记录集合
     */
    public List<CdjhsExerciseRecord> selectCdjhsExerciseRecordList(CdjhsExerciseRecord cdjhsExerciseRecord);

    /**
     * 新增练习记录
     * 
     * @param cdjhsExerciseRecord 练习记录
     * @return 结果
     */
    public int insertCdjhsExerciseRecord(CdjhsExerciseRecord cdjhsExerciseRecord);

    /**
     * 修改练习记录
     * 
     * @param cdjhsExerciseRecord 练习记录
     * @return 结果
     */
    public int updateCdjhsExerciseRecord(CdjhsExerciseRecord cdjhsExerciseRecord);

    /**
     * 批量删除练习记录
     * 
     * @param ids 需要删除的练习记录主键集合
     * @return 结果
     */
    public int deleteCdjhsExerciseRecordByIds(Long[] ids);

    /**
     * 删除练习记录信息
     * 
     * @param id 练习记录主键
     * @return 结果
     */
    public int deleteCdjhsExerciseRecordById(Long id);
}