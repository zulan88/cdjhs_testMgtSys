package net.wanji.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.business.domain.BusinessTreeSelect;
import net.wanji.business.domain.dto.TjDeviceDto;
import net.wanji.business.domain.dto.TreeTypeDto;
import net.wanji.business.entity.TjDevice;
import net.wanji.business.exception.BusinessException;
import net.wanji.common.core.domain.SimpleSelect;

import java.util.List;
import java.util.Map;

/**
* @author guanyuduo
* @description 针对表【tj_device】的数据库操作Service
* @createDate 2023-08-17 10:56:39
*/
public interface TjDeviceService extends IService<TjDevice> {

    /**
     * 初始化
     * @return
     */
    Map<String, List<SimpleSelect>> init();


    /**
     * 添加树类型
     * @param treeTypeDto
     * @return
     */
    boolean saveTreeType(TreeTypeDto treeTypeDto) throws BusinessException;

    /**
     * 删除树类型
     * @param dictCode
     * @return
     */
    boolean deleteTreeType(Long dictCode) throws BusinessException;

    /**
     * 查询使用中的设备类型树
     * @param type
     * @return
     */
    List<TjDevice> selectUsingDeviceTree(String type);

    /**
     * 构建树结构
     * @param devices
     * @return
     */
    List<TjDevice> buildSceneTree(List<TjDevice> devices);

    /**
     * 查找文件夹下所有设备
     * @param deviceId
     * @param devices
     */
    void selectChildrenFromFolder(Integer deviceId, List<TjDevice> devices);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param devices 设备列表
     * @return 下拉树结构列表
     */
    List<BusinessTreeSelect> buildDevicesTreeSelect(List<TjDevice> devices, String name);

    /**
     * 根据id删除设备树节点
     * @param id
     * @return
     */
    boolean deleteDeviceTree(Integer id) throws BusinessException;

    /**
     * 保存设备树
     * @param deviceDto
     * @return
     */
    boolean saveDevicesTree(TjDeviceDto deviceDto);
}
