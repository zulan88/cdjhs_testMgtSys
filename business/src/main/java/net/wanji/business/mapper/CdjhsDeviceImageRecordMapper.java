package net.wanji.business.mapper;

import net.wanji.business.domain.CdjhsDeviceImageRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 域控设备下发镜像记录Mapper接口
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
public interface CdjhsDeviceImageRecordMapper 
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
     * 删除域控设备下发镜像记录
     * 
     * @param id 域控设备下发镜像记录主键
     * @return 结果
     */
    public int deleteCdjhsDeviceImageRecordById(Long id);

    /**
     * 批量删除域控设备下发镜像记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCdjhsDeviceImageRecordByIds(Long[] ids);

    //查询镜像列表中下发记录最早的镜像
    public String selectEarliestImage(@Param("uniques") String uniques, @Param("images") String[] images);
}
