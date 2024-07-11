package net.wanji.business.exercise;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpiringMap;
import net.wanji.business.exercise.utils.ToBuildOpenXTransUtil;

import java.io.IOException;

/**
 * @author: jenny
 * @create: 2024-06-23 7:17 下午
 */
@Slf4j
public class Test {

    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
        double longitude = 0.1;
        double latitude = 0.02;
        double[] pos = {longitude, latitude};
        System.out.println(pos.length);
        System.out.println(pos[0]);
        System.out.println(pos[1]);
    }
}
