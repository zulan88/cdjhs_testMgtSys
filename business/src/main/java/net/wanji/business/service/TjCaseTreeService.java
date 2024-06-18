package net.wanji.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.domain.dto.CaseTreeDto;
import net.wanji.business.domain.vo.CaseTreeVo;
import net.wanji.business.entity.TjCaseTree;
import net.wanji.business.exception.BusinessException;

import java.util.List;

/**
* @author guanyuduo
* @description 针对表【tj_case_tree(片段式场景表)】的数据库操作Service
* @createDate 2023-10-19 13:22:41
*/
public interface TjCaseTreeService extends IService<TjCaseTree> {

    /**
     * 查询测试用例树
     * @param type
     * @param name
     * @return
     */
    List<CaseTreeVo> selectTree(String type, String name);

    /**
     * 保存测试用例树
     * @param caseTreeDto
     * @return
     */
    boolean saveTree(CaseTreeDto caseTreeDto) throws BusinessException;

    /**
     * 删除测试用例树
     * @param id
     * @return
     */
    boolean deleteTree(Integer id) throws BusinessException;

}
