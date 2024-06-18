package net.wanji.makeanappointment.service.impl;

import net.wanji.approve.service.TjTesteeObjectInfoService;
import net.wanji.common.utils.StringUtils;
import net.wanji.makeanappointment.domain.vo.TestObjectVo;
import net.wanji.makeanappointment.mapper.TestObjectMapper;
import net.wanji.makeanappointment.service.TestObjectService;
import net.wanji.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName TesteeObjectServiceImpl
 * @Description
 * @Author liruitao
 * @Date 2023-12-05
 * @Version 1.0
 **/
@Service("testObjectService")
public class TestObjectServiceImpl implements TestObjectService {

    private static Logger logger = LoggerFactory.getLogger(TestObjectServiceImpl.class);

    @Autowired
    private TestObjectMapper testeeObjectMapper;

    @Autowired
    TjTesteeObjectInfoService tjTesteeObjectInfoService;

    @Override
    public void addTesteeObject(TestObjectVo testeeObjectVo) {
        try {
            testeeObjectVo.setCreateBy(SecurityUtils.getUsername());
        } catch (Exception e) {
            logger.error("获取用户名异常", e);
            testeeObjectVo.setCreateBy("admin");
        }
        String dataChannel = "Result_" + StringUtils.generateRandomString(12);
        String commandChannel = "Command_" + StringUtils.generateRandomString(12);

        testeeObjectVo.setDataChannel(dataChannel);
        testeeObjectVo.setCommandChannel(commandChannel);
        testeeObjectMapper.addTesteeObject(testeeObjectVo);
        // 推送设备信息
        tjTesteeObjectInfoService.adddevice(testeeObjectVo.getId(), dataChannel, commandChannel);
    }

    @Override
    public void deleteTesteeObject(Integer id) {
        // 删除设备信息
        tjTesteeObjectInfoService.deletedevice(id);
        testeeObjectMapper.deleteTesteeObject(id);
    }

    @Override
    public void updateTesteeObject(TestObjectVo testeeObjectVo) {
        try {
            testeeObjectVo.setUpdateBy(SecurityUtils.getUsername());
        } catch (Exception e) {
            logger.error("获取用户名异常", e);
            testeeObjectVo.setUpdateBy("admin");
        }
        testeeObjectMapper.updateTesteeObject(testeeObjectVo);
    }

    @Override
    public TestObjectVo queryTesteeObjectById(Integer id) {
        return testeeObjectMapper.queryTesteeObjectById(id);
    }

    @Override
    public List<TestObjectVo> queryTesteeObjectList(TestObjectVo testeeObjectVo) {
        return testeeObjectMapper.queryTesteeObjectList(testeeObjectVo);
    }

}
