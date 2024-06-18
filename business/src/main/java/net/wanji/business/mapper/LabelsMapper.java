package net.wanji.business.mapper;

import java.util.List;
import net.wanji.business.domain.Label;

/**
 * labelsMapper接口
 * 
 * @author wanji
 * @date 2023-10-16
 */
public interface LabelsMapper 
{
    /**
     * 查询labels
     * 
     * @param id labelsID
     * @return labels
     */
    public Label selectLabelsById(Long id);

    /**
     * 查询labels列表
     * 
     * @param label labels
     * @return labels集合
     */
    public List<Label> selectLabelsList(Label label);

    /**
     * 新增labels
     * 
     * @param label labels
     * @return 结果
     */
    public int insertLabels(Label label);

    /**
     * 修改labels
     * 
     * @param label labels
     * @return 结果
     */
    public int updateLabels(Label label);

    /**
     * 删除labels
     * 
     * @param id labelsID
     * @return 结果
     */
    public int deleteLabelsById(Long id);

    /**
     * 批量删除labels
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLabelsByIds(Long[] ids);
}
