package net.wanji.approve.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.wanji.approve.entity.TjWorkers;
import com.baomidou.mybatisplus.extension.service.IService;
import net.wanji.approve.entity.vo.TjWorkersVo;

import java.util.List;

/**
 * <p>
 * 工作人员表 服务类
 * </p>
 *
 * @author wj
 * @since 2023-12-05
 */
public interface TjWorkersService extends IService<TjWorkers> {
    // 分页查询
    IPage<TjWorkers> listWorkers(int pageNum, int pageSize, String type, String name);

    // 根据id查询
    TjWorkers getWorkerById(Integer id);

    // 插入
    boolean addWorker(TjWorkers worker);

    // 更新
    boolean updateWorker(TjWorkers worker);

    // 根据id删除
    boolean deleteWorkerById(Integer id);

    // 批量删除
    boolean deleteWorkersByIds(List<Integer> ids);

    List<TjWorkersVo> listbyrecord(Integer recordId);
}
