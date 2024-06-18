package net.wanji.business.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.domain.param.TessParam;
import net.wanji.business.entity.SendTessNgRequest;
import net.wanji.business.mapper.SendTessNgRequestMapper;
import net.wanji.business.service.SendTessNgRequestService;
import net.wanji.common.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by guodejun on 2024/02/02.
 * 保存发送 tessng 请求参数及结果
 */
@Slf4j
@Service
public class SendTessNgRequestServiceImpl implements SendTessNgRequestService {

    @Resource
    private SendTessNgRequestMapper sendTessNgRequestMapper;

    @Override
    public void saveTessNgRequest(String result, String resultUrl, TessParam tessParam) {

        SendTessNgRequest sendTessNgRequest = new SendTessNgRequest();
        sendTessNgRequest.setResult(result);
        sendTessNgRequest.setResultUrl(resultUrl);
        sendTessNgRequest.setRequestDate(DateUtils.getTime());
        sendTessNgRequest.setTessParam(JSON.toJSONString(tessParam));

        sendTessNgRequestMapper.insert(sendTessNgRequest);
    }
}
