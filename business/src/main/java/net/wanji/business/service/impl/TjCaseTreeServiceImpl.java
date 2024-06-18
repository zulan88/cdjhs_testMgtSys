package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.common.Constants.YN;
import net.wanji.business.domain.dto.CaseQueryDto;
import net.wanji.business.domain.dto.CaseTreeDto;
import net.wanji.business.domain.vo.CaseDetailVo;
import net.wanji.business.domain.vo.CaseTreeVo;
import net.wanji.business.entity.TjCaseTree;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjCaseMapper;
import net.wanji.business.service.TjCaseTreeService;
import net.wanji.business.mapper.TjCaseTreeMapper;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.bean.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author guanyuduo
 * @description 针对表【tj_case_tree(片段式场景表)】的数据库操作Service实现
 * @createDate 2023-10-19 13:22:41
 */
@Service
public class TjCaseTreeServiceImpl extends ServiceImpl<TjCaseTreeMapper, TjCaseTree>
        implements TjCaseTreeService {

    @Autowired
    private TjCaseTreeMapper caseTreeMapper;

    @Autowired
    private TjCaseMapper caseMapper;

    @Override
    public List<CaseTreeVo> selectTree(String type, String name) {
        List<TjCaseTree> caseTrees = caseTreeMapper.selectByCondition(type, name);
        return CollectionUtils.emptyIfNull(caseTrees).stream().map(CaseTreeVo::new).collect(Collectors.toList());
    }

    @Override
    public boolean saveTree(CaseTreeDto caseTreeDto) throws BusinessException {
        TjCaseTree caseTree = new TjCaseTree();
        if (ObjectUtils.isEmpty(caseTreeDto.getId())) {
            BeanUtils.copyBeanProp(caseTree, caseTreeDto);
            caseTree.setCreatedBy(SecurityUtils.getUsername());
            caseTree.setCreatedDate(LocalDateTime.now());
        } else {
            caseTree = this.getById(caseTreeDto.getId());
            caseTree.setName(caseTreeDto.getName());
            caseTree.setType(caseTreeDto.getType());
            caseTree.setUpdatedBy(SecurityUtils.getUsername());
            caseTree.setUpdatedDate(LocalDateTime.now());
        }
        try {
            return saveOrUpdate(caseTree);
        } catch (DuplicateKeyException e) {
            throw new BusinessException("文件名称重复！");
        }
    }

    @Override
    public boolean deleteTree(Integer id) throws BusinessException {
        CaseQueryDto caseQueryDto = new CaseQueryDto();
        caseQueryDto.setTreeId(String.valueOf(id));
        List<CaseDetailVo> caseDetails = caseMapper.selectCases(caseQueryDto);
        if (CollectionUtils.isNotEmpty(caseDetails)) {
            throw new BusinessException("该目录下存在用例，不可删除！");
        }
        return this.removeById(id);
    }
}
