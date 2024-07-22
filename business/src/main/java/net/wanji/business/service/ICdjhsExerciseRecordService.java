package net.wanji.business.service;

import net.wanji.business.domain.CdjhsExerciseRecord;
import net.wanji.business.domain.evaluation.EvaluationReport;
import net.wanji.business.exception.BusinessException;

import java.io.IOException;
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
    public int insertCdjhsExerciseRecord(CdjhsExerciseRecord cdjhsExerciseRecord) throws BusinessException;

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

    public EvaluationReport reviewReport(Long taskId);

    public void playback(Integer taskId, Integer action) throws BusinessException, IOException;

    public void putIntoTaskQueue(CdjhsExerciseRecord record) throws BusinessException;

    public List<CdjhsExerciseRecord> selectUnexecutedExercises();

    public List<CdjhsExerciseRecord> selectCdjhsExerciseRecordByStatusAndIds(Integer status, Long[] ids);

    public int updateBatch(List<CdjhsExerciseRecord> list);

    public String queryEvaluationStatus(Long id, String evaluationUrl);
}
