package net.wanji.business.service;

import net.wanji.business.domain.CdjhsDeviceImageRecord;

import java.util.List;

/**
 * 域控设备下发镜像记录Service接口
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
public interface ICdjhsDeviceImageRecordService 
{
    /**
     * 查询域控设备下发镜像记录
     * 
     * @param id 域控设备下发镜像记录主键
     * @return 域控设备下发镜像记录
     */
    public CdjhsDeviceImageRecord selectCdjhsDeviceImageRecordById(Long id);

    /**
     * 查询域控设备下发镜像记录列表
     * 
     * @param cdjhsDeviceImageRecord 域控设备下发镜像记录
     * @return 域控设备下发镜像记录集合
     */
    public List<CdjhsDeviceImageRecord> selectCdjhsDeviceImageRecordList(CdjhsDeviceImageRecord cdjhsDeviceImageRecord);

    /**
     * 新增域控设备下发镜像记录
     * 
     * @param cdjhsDeviceImageRecord 域控设备下发镜像记录
     * @return 结果
     */
    public int insertCdjhsDeviceImageRecord(CdjhsDeviceImageRecord cdjhsDeviceImageRecord);

    /**
     * 修改域控设备下发镜像记录
     * 
     * @param cdjhsDeviceImageRecord 域控设备下发镜像记录
     * @return 结果
     */
    public int updateCdjhsDeviceImageRecord(CdjhsDeviceImageRecord cdjhsDeviceImageRecord);

    /**
     * 批量删除域控设备下发镜像记录
     * 
     * @param ids 需要删除的域控设备下发镜像记录主键集合
     * @return 结果
     */
    public int deleteCdjhsDeviceImageRecordByIds(Long[] ids);

    /**
     * 删除域控设备下发镜像记录信息
     * 
     * @param id 域控设备下发镜像记录主键
     * @return 结果
     */
    public int deleteCdjhsDeviceImageRecordById(Long id);

    public String selectEarliestImage(String uniques, String[] images);
}
