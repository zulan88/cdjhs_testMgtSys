package net.wanji.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wanji.business.entity.TjCaseTree;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author guanyuduo
* @description 针对表【tj_case_tree(片段式场景表)】的数据库操作Mapper
* @createDate 2023-10-19 13:22:41
* @Entity net.wanji.business.entity.TjCaseTree
*/
public interface TjCaseTreeMapper extends BaseMapper<TjCaseTree> {

    /**
     * 条件查询测试用例树
     * @param type
     * @param name
     * @return
     */
    List<TjCaseTree> selectByCondition(@Param("type") String type, @Param("name") String name);

}
