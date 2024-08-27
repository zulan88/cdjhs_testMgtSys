package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.wanji.business.common.Constants;
import net.wanji.business.entity.CdjhsRefereeScoring;
import net.wanji.business.entity.TjAtlasTree;
import net.wanji.business.mapper.CdjhsRefereeScoringMapper;
import net.wanji.business.service.CdjhsRefereeScoringService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.common.core.domain.entity.SysRole;
import net.wanji.common.core.domain.entity.SysUser;
import net.wanji.common.utils.StringUtils;
import net.wanji.system.service.ISysRoleService;
import net.wanji.system.service.ISysUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wj
 * @since 2024-08-26
 */
@Service
public class CdjhsRefereeScoringServiceImpl extends ServiceImpl<CdjhsRefereeScoringMapper, CdjhsRefereeScoring> implements CdjhsRefereeScoringService {

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Override
    public List<CdjhsRefereeScoring> list(Integer taskId, Integer teamId) {
        LambdaQueryWrapper<CdjhsRefereeScoring> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CdjhsRefereeScoring::getTaskId, taskId);
        queryWrapper.eq(CdjhsRefereeScoring::getTeamId, teamId);
        queryWrapper.orderByAsc(CdjhsRefereeScoring::getUserId);
        return this.list(queryWrapper);
    }

    @Override
    public Map<String, Object> getScoreData(Integer taskId, Integer teamId, List<SysRole> roles) {

        SysUser sysUser = new SysUser();
        sysUser.setRoleIds(ArrayUtils.toArray(roles.stream().map(SysRole::getRoleId).toArray(Long[]::new)));
        List<SysUser> refereeList = userService.selectUserList(sysUser);
        Integer submit = refereeList == null ? 0 : refereeList.size();

        // 已提交
        List<CdjhsRefereeScoring> list = this.list(taskId, teamId);
        Integer submitted = list == null ? 0 : list.size();

        return new HashMap<String, Object>() {{
            put("teamId", teamId);
            put("taskId", taskId);
            // 应提交
            put("submit", submit);
            // 已提交
            put("submitted", submitted);
            // 未提交
            put("notSubmitted", submit - submitted);
        }};
    }
}
