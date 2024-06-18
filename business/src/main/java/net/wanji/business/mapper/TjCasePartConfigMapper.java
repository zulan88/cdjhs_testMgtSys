package net.wanji.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wanji.business.domain.dto.CaseConfigDto;
import net.wanji.business.domain.param.TestStartParam;
import net.wanji.business.entity.TjCasePartConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2023-06-29
 */
public interface TjCasePartConfigMapper extends BaseMapper<TjCasePartConfig> {

    /**
     * 获取用例角色配置数量
     * @param caseId
     * @return
     */
    CaseConfigDto getCasePartNum(Integer caseId);

    int updateRole(TjCasePartConfig tjCasePartConfig);

    int updateDevice(TjCasePartConfig tjCasePartConfig);
}
