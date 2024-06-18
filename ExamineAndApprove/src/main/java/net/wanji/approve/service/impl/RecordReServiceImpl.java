package net.wanji.approve.service.impl;

import net.wanji.approve.entity.AppointmentRecord;
import net.wanji.approve.entity.RecordRe;
import net.wanji.approve.mapper.RecordReMapper;
import net.wanji.approve.service.AppointmentRecordService;
import net.wanji.approve.service.RecordReService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wj
 * @since 2023-12-07
 */
@Service
public class RecordReServiceImpl extends ServiceImpl<RecordReMapper, RecordRe> implements RecordReService {

    @Override
    public List<Integer> getrecordBydevice(Integer deviceId) {
        List<Integer> ids = baseMapper.selectBydevice(deviceId);
        return ids;
    }

    @Override
    public List<Integer> getrecordByperson(Integer personId) {
        List<Integer> ids = baseMapper.selectByperson(personId);
        return ids;
    }
}
