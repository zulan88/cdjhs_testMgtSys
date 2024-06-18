package net.wanji.makeanappointment.service;

import net.wanji.makeanappointment.domain.vo.TestObjectVo;

import java.util.List;

public interface TestObjectService {

    /**
     * 新增被测对象
     * @param testeeObjectVo 被测对象信息
     * @author liruitao
     * @date 2023-12-06
     */
    void addTesteeObject(TestObjectVo testeeObjectVo);

    /**
     * 修改被测对象
     * @param id 被测对象id
     * @author liruitao
     * @date 2023-12-06
     */
    void deleteTesteeObject(Integer id);

    /**
     * 修改被测对象
     * @param testeeObjectVo 被测对象信息
     * @author liruitao
     * @date 2023-12-06
     */
    void updateTesteeObject(TestObjectVo testeeObjectVo);

    /**
     * 根据id查询被测对象
     * @param id 被测对象id
     * @return {@link TestObjectVo}
     * @author liruitao
     * @date 2023-12-06
     */
    TestObjectVo queryTesteeObjectById(Integer id);

    /**
     * 查询被测对象列表
     * @param testeeObjectVo 被测对象信息
     * @return {@link List<TestObjectVo>}
     * @author liruitao
     * @date 2023-12-06
     */
    List<TestObjectVo> queryTesteeObjectList(TestObjectVo testeeObjectVo);
}
