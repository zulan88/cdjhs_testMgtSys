package net.wanji.business.exercise;

import com.alibaba.fastjson.JSONObject;
import net.wanji.business.common.Constants;
import net.wanji.business.domain.param.TessParam;
import net.wanji.business.exercise.dto.ImageListReportReq;
import net.wanji.business.exercise.dto.ImageListResultDto;
import net.wanji.business.exercise.dto.TestStartReqDto;
import net.wanji.common.utils.StringUtils;
import org.springframework.data.redis.listener.PatternTopic;

import java.io.File;
import java.util.Arrays;

/**
 * @author: jenny
 * @create: 2024-06-23 7:17 下午
 */
public class Test {
    public static void main(String[] args){
        String tessStart = "{\"params\":{\"protocols\":[{\"channel\":\"CDJHS_GKQResult_YK001\",\"type\":0},{\"channel\":\"admin_1_0_5_data\",\"type\":1}],\"taskType\":1},\"timestamp\":1719977115554,\"type\":2}";
        TestStartReqDto dto = JSONObject.parseObject(tessStart, TestStartReqDto.class);
        String string = JSONObject.toJSONString(dto);
        System.out.println(string);

    }
}
