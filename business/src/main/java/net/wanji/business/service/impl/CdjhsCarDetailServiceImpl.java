package net.wanji.business.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.wanji.business.domain.CdjhsCarDetail;
import net.wanji.business.exercise.ExerciseHandler;
import net.wanji.business.exercise.dto.YkscResultDto;
import net.wanji.business.exercise.enums.CarStatusEnum;
import net.wanji.business.mapper.CdjhsCarDetailMapper;
import net.wanji.business.mapper.CdjhsUserTeamMapper;
import net.wanji.business.service.ICdjhsCarDetailService;
import net.wanji.common.core.domain.entity.SysUser;
import net.wanji.common.core.redis.RedisCache;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.RedisKeyUtils;
import net.wanji.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 实车信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-08-02
 */
@Service
public class CdjhsCarDetailServiceImpl implements ICdjhsCarDetailService
{
    @Autowired
    private CdjhsCarDetailMapper cdjhsCarDetailMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CdjhsUserTeamMapper cdjhsUserTeamMapper;

    /**
     * 查询实车信息
     * 
     * @param id 实车信息主键
     * @return 实车信息
     */
    @Override
    public CdjhsCarDetail selectCdjhsCarDetailById(Long id)
    {
        return cdjhsCarDetailMapper.selectCdjhsCarDetailById(id);
    }

    /**
     * 查询实车信息列表
     * 
     * @param cdjhsCarDetail 实车信息
     * @return 实车信息
     */
    @Override
    public List<CdjhsCarDetail> selectCdjhsCarDetailList(CdjhsCarDetail cdjhsCarDetail)
    {
        List<CdjhsCarDetail> cdjhsCarDetails = cdjhsCarDetailMapper.selectCdjhsCarDetailList(cdjhsCarDetail);
        for(CdjhsCarDetail carDetail: cdjhsCarDetails){
            //默认状态空闲
            carDetail.setStatus(CarStatusEnum.IDLE.getStatus());
            //查询绑定域控是否有上报绑定关系
            String deviceId = carDetail.getDeviceCode();
            String key = RedisKeyUtils.getCdjhsYkscResultKey(deviceId);
            YkscResultDto ykReportInfo = redisCache.getCacheObject(key);
            if(Objects.nonNull(ykReportInfo)){
                String username = ykReportInfo.getTeamName();
                String teamName = cdjhsUserTeamMapper.selectTeamNameByUserName(username);
                carDetail.setUserName(username);
                carDetail.setTeamName(Objects.isNull(teamName) ? username : teamName);
                carDetail.setImageId(ykReportInfo.getImageId());
                carDetail.setImageName(ykReportInfo.getImageName());
                carDetail.setMd5(ykReportInfo.getMd5());
                carDetail.setReportTime(DateUtils.getDateString(new Date(ykReportInfo.getTimestamp())));

                int status = ykReportInfo.getStatus() == 2 && !ExerciseHandler.occupationMap.containsKey(deviceId) ? CarStatusEnum.PREPARE.getStatus() : CarStatusEnum.RUNNING.getStatus();
                carDetail.setStatus(status);
            }
        }
        if(Objects.nonNull(cdjhsCarDetail.getStatus())){
            cdjhsCarDetails = cdjhsCarDetails.stream()
                    .filter(car -> car.getStatus().compareTo(cdjhsCarDetail.getStatus()) == 0)
                    .collect(Collectors.toList());
        }
        SysUser user = SecurityUtils.getLoginUser().getUser();
        boolean student = SecurityUtils.isStudent(user);
        if(student){
            String username = SecurityUtils.getUsername();
            cdjhsCarDetails = cdjhsCarDetails.stream()
                    .filter(car -> username.equals(car.getUserName()))
                    .collect(Collectors.toList());
        }
        return cdjhsCarDetails;
    }

    /**
     * 新增实车信息
     * 
     * @param cdjhsCarDetail 实车信息
     * @return 结果
     */
    @Override
    public int insertCdjhsCarDetail(CdjhsCarDetail cdjhsCarDetail)
    {
        cdjhsCarDetail.setCreateTime(DateUtils.getNowDate());
        return cdjhsCarDetailMapper.insertCdjhsCarDetail(cdjhsCarDetail);
    }

    /**
     * 修改实车信息
     * 
     * @param cdjhsCarDetail 实车信息
     * @return 结果
     */
    @Override
    public int updateCdjhsCarDetail(CdjhsCarDetail cdjhsCarDetail)
    {
        cdjhsCarDetail.setUpdateTime(DateUtils.getNowDate());
        return cdjhsCarDetailMapper.updateCdjhsCarDetail(cdjhsCarDetail);
    }

    /**
     * 批量删除实车信息
     * 
     * @param ids 需要删除的实车信息主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsCarDetailByIds(Long[] ids)
    {
        return cdjhsCarDetailMapper.deleteCdjhsCarDetailByIds(ids);
    }

    /**
     * 删除实车信息信息
     * 
     * @param id 实车信息主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsCarDetailById(Long id)
    {
        return cdjhsCarDetailMapper.deleteCdjhsCarDetailById(id);
    }

    @Override
    public boolean isUnique(CdjhsCarDetail cdjhsCarDetail) {
        List<CdjhsCarDetail> list = cdjhsCarDetailMapper.check(cdjhsCarDetail.getCarCode(), cdjhsCarDetail.getDeviceCode());
        if(Objects.isNull(cdjhsCarDetail.getId())){
            //新增时判断实车编号和域控编号是否唯一
            return list.isEmpty();
        }
        return list.isEmpty() || (list.size() == 1 && list.get(0).getId().compareTo(cdjhsCarDetail.getId()) == 0);
    }
}
