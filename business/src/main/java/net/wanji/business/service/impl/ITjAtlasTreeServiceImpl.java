package net.wanji.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wanji.business.common.Constants.UsingStatus;
import net.wanji.business.domain.dto.TjAtlasTreeDto;
import net.wanji.business.domain.dto.TjAtlasVenueDto;
import net.wanji.business.entity.TjAtlasTree;
import net.wanji.business.entity.TjAtlasVenue;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjAtlasMapper;
import net.wanji.business.service.ITjAtlasTreeService;
import net.wanji.business.service.ITjAtlasVenueService;
import net.wanji.common.utils.SecurityUtils;
import net.wanji.common.utils.bean.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ITjAtlasTreeServiceImpl extends ServiceImpl<TjAtlasMapper,TjAtlasTree> implements ITjAtlasTreeService {

    private static final Logger log = LoggerFactory.getLogger(ITjAtlasTreeServiceImpl.class);

    @Autowired
    private ITjAtlasVenueService tjAtlasVenueService;

    private static final String TREE_KEY = "tree";//返回集合中树的key

    private static final String VENUE_KEY = "venue";//返回集合中场景的key

    /**
     * 初始化左侧地图树
     * @return
     */
    @Override
    public Map<String, Object> init() {

        Map<String,Object> resultMap = new HashMap<>();
        
        //左侧地图树
        List<TjAtlasTree> treeList = this.getTreeInit();
        resultMap.put(TREE_KEY,treeList);

        if(ObjectUtils.isEmpty(treeList)){
            return resultMap;
        }
        //测试场地信息
        TjAtlasTree tjAtlasTree = treeList.get(0);//默认展示第一个
        TjAtlasVenueDto tjAtlasVenueDto = new TjAtlasVenueDto();
        tjAtlasVenueDto.setTreeId(tjAtlasTree.getId());
        List<TjAtlasVenue> atlasVenueList = new ArrayList<>();
        try {
            atlasVenueList = tjAtlasVenueService.getAtlasVenueData(tjAtlasVenueDto);
        } catch (BusinessException e) {
            log.info("没有创建地图树下场景");
        }
        resultMap.put(VENUE_KEY,atlasVenueList);
        return resultMap;
    }
    
    private List<TjAtlasTree> getTreeInit(){

        LambdaQueryWrapper<TjAtlasTree> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TjAtlasTree::getStatus,UsingStatus.ENABLE);
        queryWrapper.orderByAsc(TjAtlasTree::getId);
        List<TjAtlasTree> treeList = this.list(queryWrapper);
        return treeList;
    }

    /**
     * 保存、修改树信息
     *
     * @param tjAtlasTreeDto
     * @return
     */
    @Override
    public boolean saveOrUpdateTree(TjAtlasTreeDto tjAtlasTreeDto) {

        //判断新增还是修改，根据是否有id
        if(ObjectUtils.isEmpty(tjAtlasTreeDto.getId())){

            TjAtlasTree tjAtlasTree = new TjAtlasTree();
            BeanUtils.copyBeanProp(tjAtlasTree, tjAtlasTreeDto);
            tjAtlasTree.setStatus(UsingStatus.ENABLE);
            tjAtlasTree.setCreatedBy(SecurityUtils.getUsername());
            tjAtlasTree.setCreatedDate(LocalDateTime.now());
            return this.save(tjAtlasTree);
        } else{

            TjAtlasTree tjAtlasTree = this.getById(tjAtlasTreeDto.getId());
            tjAtlasTree.setName(tjAtlasTreeDto.getName());
            tjAtlasTree.setUpdatedBy(SecurityUtils.getUsername());
            tjAtlasTree.setUpdatedDate(LocalDateTime.now());
            return this.updateById(tjAtlasTree);
        }

    }

    /**
     * 事务删除
     * @param treeId
     * @return
     * @throws BusinessException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteTree(Integer treeId) throws BusinessException {

        TjAtlasTree tjAtlasTree = this.getById(treeId);
        if (ObjectUtils.isEmpty(tjAtlasTree)) {
            throw new BusinessException("未查询到对应节点");
        }
        //关联删除
        boolean success = tjAtlasVenueService.deleteVenueByTreeId(treeId);
        if (!success) {
            throw new BusinessException("删除资源失败");
        }
        return this.removeById(treeId);
    }


}
