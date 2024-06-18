package net.wanji.approve.service.impl;

import net.wanji.approve.entity.TjDeviceDetail;
import net.wanji.approve.entity.TjTesteeObjectInfo;
import net.wanji.approve.mapper.TjTesteeObjectInfoMapper;
import net.wanji.approve.service.TjDeviceDetailApService;
import net.wanji.approve.service.TjTesteeObjectInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.dto.TjDeviceDetailDto;
import net.wanji.business.service.TjDeviceDetailService;
import net.wanji.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 测试预约申请-被测对象列表 服务实现类
 * </p>
 *
 * @author wj
 * @since 2023-12-06
 */
@Service
public class TjTesteeObjectInfoServiceImpl extends ServiceImpl<TjTesteeObjectInfoMapper, TjTesteeObjectInfo> implements TjTesteeObjectInfoService {


    @Autowired
    TjDeviceDetailApService tjDeviceDetailApService;

    @Override
    public void adddevice(Integer id, String dataChannel, String commandChannel) {
        TjTesteeObjectInfo tjTesteeObjectInfo = this.getById(id);
        TjDeviceDetail deviceDetail = new TjDeviceDetail();
        deviceDetail.setDeviceName(tjTesteeObjectInfo.getTesteeObjectName());
        deviceDetail.setDeviceType(tjTesteeObjectInfo.getTesteeObjectType());
        deviceDetail.setSupportRoles("av");
        deviceDetail.setServiceAddress(tjTesteeObjectInfo.getVehicleLicense());
        deviceDetail.setDataChannel(dataChannel);
        deviceDetail.setCommandChannel(commandChannel);
        deviceDetail.setStatus(Constants.YN.Y_INT);
        deviceDetail.setLastOnlineDate(LocalDateTime.now());
        deviceDetail.setCreatedBy(SecurityUtils.getUsername());
        deviceDetail.setAttribute2(SecurityUtils.getUsername());
        deviceDetail.setCreatedDate(LocalDateTime.now());
        deviceDetail.setIsInner(1);
        tjDeviceDetailApService.save(deviceDetail);
        tjTesteeObjectInfo.setDeviceId(deviceDetail.getDeviceId());
        this.updateById(tjTesteeObjectInfo);
    }

    @Override
    public boolean deletedevice(Integer id) {
        TjTesteeObjectInfo tjTesteeObjectInfo = this.getById(id);
        if (null == tjTesteeObjectInfo.getDeviceId()){
            return false;
        }
        return tjDeviceDetailApService.removeById(tjTesteeObjectInfo.getDeviceId());
    }
}
