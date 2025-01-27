package net.wanji.business.service.impl;

import net.wanji.business.domain.CdjhsTeamInfo;
import net.wanji.business.entity.CdjhsRefereeScoring;
import net.wanji.business.mapper.CdjhsRefereeScoringMapper;
import net.wanji.business.schedule.SynchronousScoring;
import net.wanji.business.service.CdjhsRefereeScoringService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.service.ICdjhsTeamInfoService;
import net.wanji.common.utils.bean.BeanUtils;
import net.wanji.onsite.entity.CdjhsRefereeMembers;
import net.wanji.onsite.entity.CdjhsRefereeScoringHistory;
import net.wanji.onsite.service.CdjhsRefereeMembersService;
import net.wanji.onsite.service.CdjhsRefereeScoringHistoryService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private CdjhsRefereeScoringHistoryService cdjhsRefereeScoringHistoryService;

    @Autowired
    private CdjhsRefereeMembersService cdjhsRefereeMembersService;

    @Autowired
    private SynchronousScoring synchronousScoring;

    @Autowired
    private ICdjhsTeamInfoService teamInfoService;

    @Async
    @Override
    public void notice(Integer recordId, String teamName) {
        //告知主观评分
        CdjhsTeamInfo teamInfo = new CdjhsTeamInfo();
        teamInfo.setTeamName(teamName);
        List<CdjhsTeamInfo> teamInfoList = teamInfoService.selectCdjhsTeamInfoList(teamInfo);
        if(CollectionUtils.isNotEmpty(teamInfoList)){
            teamInfo = teamInfoList.get(0);
            buildScoreData(recordId, Math.toIntExact(teamInfo.getId()), teamName, teamInfo.getSequence());
        }
    }

    @Override
    public Integer buildScoreData(Integer recordId, Integer teamId, String teamName, Integer entryOrder) {
        List<CdjhsRefereeMembers> members = cdjhsRefereeMembersService.list();
        if (ArrayUtils.isEmpty(members.toArray())) {
            return 0;
        }
        List<CdjhsRefereeScoring> list = this.list();
        Integer submitted = Math.toIntExact(list.stream().filter(item -> item.getScorePoint1() == null || item.getScorePoint2() == null).count());
        if (submitted > 0) {
            //TODO: 若需要添加裁判未打分校验，加在此处
        }else {
            List<CdjhsRefereeScoringHistory> histories = new ArrayList<>();
            for (CdjhsRefereeScoring item : list) {
                CdjhsRefereeScoringHistory history = new CdjhsRefereeScoringHistory();
                BeanUtils.copyBeanProp(history, item);
                history.setId(null);
                history.setRecordDate(LocalDateTime.now());
                histories.add(history);
            }
            cdjhsRefereeScoringHistoryService.saveBatch(histories);
        }
        this.removeBatchByIds(list);
        List<CdjhsRefereeScoring> newList = new ArrayList<>();
        int id = 1;
        for (CdjhsRefereeMembers member : members) {
            CdjhsRefereeScoring scoring = new CdjhsRefereeScoring();
            scoring.setId(id);
            scoring.setUserId(member.getUserId());
            scoring.setEntryOrder(String.valueOf(entryOrder));
            scoring.setTaskId(recordId);
            scoring.setTeamId(teamId);
            scoring.setTeamName(teamName);
            scoring.setUserName(member.getUserName());
            newList.add(scoring);
            id++;
        }
        this.saveBatch(newList);
        return 0;
    }

    @Override
    public Map<String, Object> getScoreData(Integer userId) {
        List<CdjhsRefereeScoring> list = this.list();
        if (ArrayUtils.isEmpty(list.toArray())) {
            return new HashMap<String, Object>() {
                {
                    put("data", "等待比赛开始");
                }
            };
        } else {
            String sort = list.get(0).getEntryOrder();
            Integer submit = list.size();
            Integer status = 0;
            Integer submitted = Math.toIntExact(list.stream().filter(item -> item.getScorePoint1() != null && item.getScorePoint2() != null).count());
            CdjhsRefereeScoring cdjhsRefereeScoring = list.stream().filter(item -> item.getUserId().equals(userId)).findFirst().orElse(null);
            if (cdjhsRefereeScoring != null) {
                if (cdjhsRefereeScoring.getScorePoint1() != null && cdjhsRefereeScoring.getScorePoint2() != null) {
                    status = 1;
                }
                Integer finalStatus = status;
                return new HashMap<String, Object>() {{
                    put("entryOrder", sort);
                    // 应提交
                    put("submit", submit);
                    // 已提交
                    put("submitted", submitted);
                    // 未提交
                    put("notSubmitted", submit - submitted);
                    put("status", finalStatus);
                    put("data", cdjhsRefereeScoring);
                }};
            } else {
                return new HashMap<String, Object>() {{
                    put("data", "等待比赛开始");
                }};
            }
        }
    }

    @Override
    public boolean submitScore(CdjhsRefereeScoring refereeScoring) {
        boolean res = this.updateById(refereeScoring);
        List<CdjhsRefereeScoring> list = this.list();
        if (list.size() == list.stream().filter(item -> item.getScorePoint1() != null && item.getScorePoint2() != null).count()) {
            // TODO: 确认计算方法，目前为求平均
            Double subScore = list.stream().mapToDouble(item -> item.getScorePoint1() + item.getScorePoint2()).sum()/(list.size()*2);
            synchronousScoring.takeTotalScore(refereeScoring.getTaskId(), subScore, refereeScoring.getTeamId());
        }
        return res;
    }
}
