package net.wanji.business.service;

import java.util.List;
import net.wanji.business.domain.Label;

/**
 * labelsService接口
 * 
 * @author wanji
 * @date 2023-10-16
 */
public interface ILabelsService 
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
     * 批量删除labels
     * 
     * @param ids 需要删除的labelsID
     * @return 结果
     */
    public int deleteLabelsByIds(Long[] ids);

    /**
     * 删除labels信息
     * 
     * @param id labelsID
     * @return 结果
     */
    public int deleteLabelsById(Long id);
}
