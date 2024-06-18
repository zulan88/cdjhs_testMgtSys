package net.wanji.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import net.wanji.common.proto.E1FrameProto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/7 15:15
 * @Descriptoin:
 */

public class DataUtils {

    public static List<List<Map>> e1Bytes2List(List<byte[]> bytes) {
        return bytes.stream().map(DataUtils::e1Byte2List).collect(Collectors.toList());
    }

    public static List<Map> e1Byte2List(byte[] bytes) {
        List<Map> mapList = new ArrayList<>();
        try {
            E1FrameProto.E1 parse = E1FrameProto.E1.parseFrom(bytes);
            mapList = JSONObject.parseArray(parse.getValue(), Map.class);
        } catch (InvalidProtocolBufferException e) {
            System.out.println(e.getMessage());
        }
        return mapList;
    }

    public static String convertUnicodeToChinese(String unicodeString) {
        if (!unicodeString.contains("\\u")) {
            return unicodeString;
        }
        StringBuilder chineseBuilder = new StringBuilder();
        String[] hexCodes = unicodeString.split("\\\\u");
        for (int i = 1; i < hexCodes.length; i++) {
            int codePoint = Integer.parseInt(hexCodes[i], 16);
            chineseBuilder.append((char) codePoint);
        }
        return chineseBuilder.toString();
    }
}
