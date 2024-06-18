package net.wanji.makeanappointment.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.wanji.approve.entity.AppointmentRecord;
import net.wanji.approve.service.AppointmentRecordService;
import net.wanji.business.domain.dto.CaseQueryDto;
import net.wanji.business.domain.vo.CasePageVo;
import net.wanji.business.domain.vo.CaseTreeVo;

import net.wanji.business.service.TjCaseService;
import net.wanji.business.service.TjCaseTreeService;
import net.wanji.common.core.domain.entity.SysDictData;
import net.wanji.common.utils.StringUtils;
import net.wanji.makeanappointment.domain.vo.TestTypeVo;
import net.wanji.makeanappointment.mapper.TestReservationMapper;
import net.wanji.makeanappointment.service.TestReservationService;
import net.wanji.system.mapper.SysDictDataMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName TestReservationServiceImpl
 * @Description
 * @Author liruitao
 * @Date 2023-12-06
 * @Version 1.0
 **/
@Service
public class TestReservationServiceImpl implements TestReservationService {

    @Autowired
    private TestReservationMapper testReservationMapper;

    @Resource
    private SysDictDataMapper sysDictDataMapper;

    @Autowired
    private TjCaseTreeService caseTreeService;

    @Resource
    private AppointmentRecordService appointmentRecordService;

    @Autowired
    private TjCaseService tjCaseService;

    @Override
    public List<TestTypeVo> getTestType() {
        Wrapper<TestTypeVo> queryWrapper = new QueryWrapper<>();
        List<TestTypeVo> testTypeVos = testReservationMapper.selectList(queryWrapper);

        List<SysDictData> sysDictDatas = sysDictDataMapper.selectDictDataByType("test_type");

        for (TestTypeVo testTypeVo : testTypeVos) {
            Optional<SysDictData> result = sysDictDatas.stream().filter(
                    sysDictData -> sysDictData.getDictLabel().equals(testTypeVo.getTestTypeName())).findFirst();
            result.ifPresent(value -> {
                testTypeVo.setDictName(value.getDictValue());
            });
        }
        return testTypeVos;
    }

    @Override
    public List<CaseTreeVo> selectTree(String type, Integer id, Integer sellectOrAll) {
        // 获取 tree 列表
        List<CaseTreeVo> caseTree = caseTreeService.selectTree(type, null);

        AppointmentRecord appointmentRecord = appointmentRecordService.getById(id);
        String caseIds = appointmentRecord.getCaseIds();
        Map<Integer, Long> caseCountMap = new HashMap<>();
        // 获取所有用例的 treeId
        if (caseIds != null && !type.isEmpty()) {
            List<Integer> typeList = Arrays.stream(caseIds.split(",")).map(Integer::parseInt).collect(Collectors.toList());

            CaseQueryDto caseQueryDto = new CaseQueryDto();
            caseQueryDto.setSelectedIds(typeList);
            List<CasePageVo> notByUsername = tjCaseService.pageList(caseQueryDto, "notByUsername");

            caseCountMap = CollectionUtils.emptyIfNull(notByUsername).stream()
                    .collect(Collectors.groupingBy(CasePageVo::getTreeId, Collectors.counting()));
        }

        for (CaseTreeVo treeVo : CollectionUtils.emptyIfNull(caseTree)) {
            treeVo.setNumber(caseCountMap.containsKey(treeVo.getId()) ? caseCountMap.get(treeVo.getId()).intValue() : 0);
        }
        if (sellectOrAll!=null&&sellectOrAll==1){
            Map<Integer, Long> finalCaseCountMap = caseCountMap;
            return caseTree.stream()
                    .filter(treeVo -> finalCaseCountMap.containsKey(treeVo.getId()))
                    .collect(Collectors.toList());
        }

        return caseTree;
    }


    @Override
    public List<CasePageVo> pageList(CaseQueryDto caseQueryDto) {
        // 借用 CaseQueryDto id属性代替 AppointmentRecord id
        Integer id = caseQueryDto.getId();
        if (id == null) {
            return null;
        }
        AppointmentRecord byId = appointmentRecordService.getById(id);
        List<String> caseIdArray = new ArrayList<>();
        String caseIds = byId.getCaseIds();
        if (StringUtils.isNotEmpty(caseIds)) {
            caseIdArray = Arrays.asList(caseIds.split(","));
        }

        // 借用后 制空
        caseQueryDto.setId(null);

        // 用例状态必须有效
        caseQueryDto.setStatus("effective");
        List<CasePageVo> allCasePage = tjCaseService.pageList(caseQueryDto, "notByUsername");

        if (CollectionUtils.isEmpty(caseIdArray)) {
            return allCasePage;
        }

        for (CasePageVo casePageVo : allCasePage) {
            casePageVo.setSelected(caseIdArray.contains(String.valueOf(casePageVo.getId())));
        }
        if(caseQueryDto.getSellectOrAll()!=null && caseQueryDto.getSellectOrAll()==1){
            return allCasePage.stream().filter(casePageVo -> casePageVo.getSelected()).collect(Collectors.toList());
        }

        return allCasePage;
    }

    @Override
    public boolean choiceCase(Integer id, List<Integer> caseIds) {
        AppointmentRecord appointmentRecord = new AppointmentRecord();
        appointmentRecord.setId(id);

        String caseIdsStr = caseIds.stream().map(Object::toString).collect(Collectors.joining(","));
        appointmentRecord.setCaseIds(caseIdsStr);

        return appointmentRecordService.updateById(appointmentRecord);
    }
}
