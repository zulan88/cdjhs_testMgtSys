package net.wanji.approve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.wanji.approve.entity.TjApprecordPerson;
import net.wanji.approve.entity.TjWorkers;
import net.wanji.approve.entity.vo.TjWorkersVo;
import net.wanji.approve.mapper.TjWorkersMapper;
import net.wanji.approve.service.TjApprecordPersonService;
import net.wanji.approve.service.TjWorkersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.common.utils.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 工作人员表 服务实现类
 * </p>
 *
 * @author wj
 * @since 2023-12-05
 */
@Service
public class TjWorkersServiceImpl extends ServiceImpl<TjWorkersMapper, TjWorkers> implements TjWorkersService {

    @Autowired
    TjApprecordPersonService tjApprecordPersonService;

    // 分页查询
    public IPage<TjWorkers> listWorkers(int pageNum, int pageSize, String type, String name) {
        Page<TjWorkers> page = new Page<>(pageNum, pageSize);

        QueryWrapper<TjWorkers> queryWrapper = new QueryWrapper<>();
        if (type != null && !type.isEmpty()) {
            List<String> typeList = Arrays.asList(type.split(","));
            queryWrapper.in("type", typeList);
        }
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }

        return page(page, queryWrapper);
    }

    // 根据id查询
    public TjWorkers getWorkerById(Integer id) {
        return getById(id);
    }

    // 插入
    public boolean addWorker(TjWorkers worker) {
        String workerId = StringUtils.generateRandomString(10);
        worker.setWorkerId(workerId);
        return save(worker);
    }

    // 更新
    public boolean updateWorker(TjWorkers worker) {
        return updateById(worker);
    }

    // 根据id删除
    public boolean deleteWorkerById(Integer id) {
        return removeById(id);
    }

    // 批量删除
    public boolean deleteWorkersByIds(List<Integer> ids) {
        return removeByIds(ids);
    }

    @Override
    public List<TjWorkersVo> listbyrecord(Integer recordId) {
        List<TjApprecordPerson> tjApprecordPersons = tjApprecordPersonService.list(new QueryWrapper<TjApprecordPerson>().eq("record_id", recordId));
        List<TjWorkersVo> tjWorkersVos = this.list().stream().map(item -> {
            TjWorkersVo tjWorkersVo = new TjWorkersVo();
            BeanUtils.copyProperties(item, tjWorkersVo);
            TjApprecordPerson tjApprecordPerson = new TjApprecordPerson();
            tjApprecordPerson.setPersonId(item.getId());
            if(tjApprecordPersons.contains(tjApprecordPerson)){
                tjWorkersVo.setIsSelect(1);
                TjApprecordPerson person = tjApprecordPersons.stream().filter(item1 -> item1.getPersonId().equals(item.getId())).findFirst().get();
                tjWorkersVo.setDeviceId(person.getDeviceId());
                tjWorkersVo.setDeviceName(person.getDeviceName());
                tjWorkersVo.setOtherTask(person.getOtherTask());
            }else {
                tjWorkersVo.setIsSelect(0);
            }
            return tjWorkersVo;
        }).collect(java.util.stream.Collectors.toList());
        return tjWorkersVos;
    }


}
