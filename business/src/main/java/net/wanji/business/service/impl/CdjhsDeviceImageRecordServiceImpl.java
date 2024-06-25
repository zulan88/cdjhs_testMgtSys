package net.wanji.business.service.impl;

import java.util.List;

import net.wanji.business.domain.CdjhsDeviceImageRecord;
import net.wanji.business.mapper.CdjhsDeviceImageRecordMapper;
import net.wanji.business.service.ICdjhsDeviceImageRecordService;
import net.wanji.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 域控设备下发镜像记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
@Service
public class CdjhsDeviceImageRecordServiceImpl implements ICdjhsDeviceImageRecordService
{
    @Autowired
    private CdjhsDeviceImageRecordMapper cdjhsDeviceImageRecordMapper;

    /**
     * 查询域控设备下发镜像记录
     * 
     * @param id 域控设备下发镜像记录主键
     * @return 域控设备下发镜像记录
     */
    @Override
    public CdjhsDeviceImageRecord selectCdjhsDeviceImageRecordById(Long id)
    {
        return cdjhsDeviceImageRecordMapper.selectCdjhsDeviceImageRecordById(id);
    }

    /**
     * 查询域控设备下发镜像记录列表
     * 
     * @param cdjhsDeviceImageRecord 域控设备下发镜像记录
     * @return 域控设备下发镜像记录
     */
    @Override
    public List<CdjhsDeviceImageRecord> selectCdjhsDeviceImageRecordList(CdjhsDeviceImageRecord cdjhsDeviceImageRecord)
    {
        return cdjhsDeviceImageRecordMapper.selectCdjhsDeviceImageRecordList(cdjhsDeviceImageRecord);
    }

    /**
     * 新增域控设备下发镜像记录
     * 
     * @param cdjhsDeviceImageRecord 域控设备下发镜像记录
     * @return 结果
     */
    @Override
    public int insertCdjhsDeviceImageRecord(CdjhsDeviceImageRecord cdjhsDeviceImageRecord)
    {
        cdjhsDeviceImageRecord.setCreateTime(DateUtils.getNowDate());
        return cdjhsDeviceImageRecordMapper.insertCdjhsDeviceImageRecord(cdjhsDeviceImageRecord);
    }

    /**
     * 修改域控设备下发镜像记录
     * 
     * @param cdjhsDeviceImageRecord 域控设备下发镜像记录
     * @return 结果
     */
    @Override
    public int updateCdjhsDeviceImageRecord(CdjhsDeviceImageRecord cdjhsDeviceImageRecord)
    {
        return cdjhsDeviceImageRecordMapper.updateCdjhsDeviceImageRecord(cdjhsDeviceImageRecord);
    }

    /**
     * 批量删除域控设备下发镜像记录
     * 
     * @param ids 需要删除的域控设备下发镜像记录主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsDeviceImageRecordByIds(Long[] ids)
    {
        return cdjhsDeviceImageRecordMapper.deleteCdjhsDeviceImageRecordByIds(ids);
    }

    /**
     * 删除域控设备下发镜像记录信息
     * 
     * @param id 域控设备下发镜像记录主键
     * @return 结果
     */
    @Override
    public int deleteCdjhsDeviceImageRecordById(Long id)
    {
        return cdjhsDeviceImageRecordMapper.deleteCdjhsDeviceImageRecordById(id);
    }

    @Override
    public String selectEarliestImage(String uniques, String[] images) {
        return cdjhsDeviceImageRecordMapper.selectEarliestImage(uniques, images);
    }
}
