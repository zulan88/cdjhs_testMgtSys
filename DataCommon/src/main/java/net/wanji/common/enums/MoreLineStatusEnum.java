package net.wanji.common.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : yangliang
 * @create 2022/12/23 15:14
 * 车辆多卡多签
 */
public enum MoreLineStatusEnum {
    一车多OBU("010101"),

    //小客车
    一车多CPC("010102"),
    //大货车
    一车多OBU和CPC("010103");

    String type;

    MoreLineStatusEnum(String b) {
        this.type=b;
        //命令字
    }
    public String getType(){
        return type;
    }
    public static String getValue(String code) {
        for (VehicleTypeEnum value : VehicleTypeEnum.values()) {
            if (code.equals(value.getType())) {
                return value.name();
            }
        }
        return null;
    }
    /**
     * 转为数据
     * @return 枚举对象数组
     */
    public static List<Map<String,String>> toList() {
        List<Map<String, String>> list = new ArrayList<>();
        for (VehicleTypeEnum item : VehicleTypeEnum.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("code", String.valueOf(item.getType()));
            map.put("name", item.name());
            list.add(map);
        }
        return list;
    }
}
