package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.wanji.business.domain.bo.ParticipantTrajectoryBo;
import net.wanji.business.domain.param.GeneralizeScene;
import net.wanji.business.domain.vo.ConflictInfo;
import net.wanji.business.domain.vo.TjGeneralizeSceneVo;
import net.wanji.business.entity.TjGeneralizeScene;
import net.wanji.business.mapper.TjGeneralizeSceneMapper;
import net.wanji.business.service.TjGeneralizeSceneService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wj
 * @since 2024-01-17
 */
@Service
public class TjGeneralizeSceneServiceImpl extends ServiceImpl<TjGeneralizeSceneMapper, TjGeneralizeScene> implements TjGeneralizeSceneService {

    @Override
    public List<TjGeneralizeSceneVo> selectList(TjGeneralizeScene tjGeneralizeScene) {
        QueryWrapper<TjGeneralizeScene> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(tjGeneralizeScene.getSceneId()!=null,"scene_id",tjGeneralizeScene.getSceneId());
        List<TjGeneralizeScene> tjGeneralizeSceneList = this.list(queryWrapper);
        List<TjGeneralizeSceneVo> res = tjGeneralizeSceneList.stream().map(tjGeneralizeScene1 -> {
            TjGeneralizeSceneVo tjGeneralizeSceneVo = new TjGeneralizeSceneVo();
            BeanUtils.copyBeanProp(tjGeneralizeSceneVo, tjGeneralizeScene1);
            return tjGeneralizeSceneVo;
        }).collect(Collectors.toList());
        for (TjGeneralizeSceneVo tjGeneralizeSceneVo : res) {
            for (ParticipantTrajectoryBo participantTrajectoryBo : tjGeneralizeSceneVo.getTrajectoryJson().getParticipantTrajectories()) {
                ConflictInfo conflictInfo = new ConflictInfo();
                conflictInfo.setId(participantTrajectoryBo.getId());
                conflictInfo.setType(participantTrajectoryBo.getType());
                conflictInfo.setName(participantTrajectoryBo.getName());
                participantTrajectoryBo.getTrajectory().forEach(trajectoryBo -> {
                    if (trajectoryBo.getType().equals("conflict")){
                        conflictInfo.setConflictSpeed(trajectoryBo.getSpeed());
                    }
                });
                tjGeneralizeSceneVo.getConflictInfos().add(conflictInfo);
            }
        }
        return res;
    }

}
