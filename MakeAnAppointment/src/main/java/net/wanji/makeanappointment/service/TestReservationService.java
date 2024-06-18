package net.wanji.makeanappointment.service;

import net.wanji.business.domain.dto.CaseQueryDto;
import net.wanji.business.domain.vo.CasePageVo;
import net.wanji.business.domain.vo.CaseTreeVo;
import net.wanji.makeanappointment.domain.vo.TestTypeVo;

import java.util.List;

public interface TestReservationService {

    /**
     * 获取测试类型
     * @return {@link List<TestTypeVo>}
     * @author liruitao
     * @date 2023-12-06
     */
    List<TestTypeVo> getTestType();

    List<CaseTreeVo> selectTree(String type, Integer id, Integer sellectOrAll);


    List<CasePageVo> pageList(CaseQueryDto caseQueryDto);

    boolean choiceCase(Integer id, List<Integer> caseIds);

}
