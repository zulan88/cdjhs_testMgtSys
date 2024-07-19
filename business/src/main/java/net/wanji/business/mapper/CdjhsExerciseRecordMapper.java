package net.wanji.business.mapper;

import net.wanji.business.domain.CdjhsExerciseRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 练习记录Mapper接口
 * 
 * @author ruoyi
 * @date 2024-06-19
 */
public interface CdjhsExerciseRecordMapper 
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
     * 删除练习记录
     * 
     * @param id 练习记录主键
     * @return 结果
     */
    public int deleteCdjhsExerciseRecordById(Long id);

    /**
     * 批量删除练习记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCdjhsExerciseRecordByIds(Long[] ids);

    public List<CdjhsExerciseRecord> selectUnexecutedExercises();

    public List<CdjhsExerciseRecord> selectCdjhsExerciseRecordByStatusAndIds(@Param("status") Integer status, @Param("ids") Long[] ids);

    public int updateBatch(List<CdjhsExerciseRecord> list);

    public List<CdjhsExerciseRecord> selectTasksByEvaluationStatus(String evalutionStatus);
}
