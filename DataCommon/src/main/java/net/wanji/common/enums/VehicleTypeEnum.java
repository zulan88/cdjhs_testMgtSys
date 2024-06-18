package net.wanji.common.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum  VehicleTypeEnum {


    //未知
    未知("0"),
    未识别("255"),
    //小客车
    一型客车("1"),
    //大货车
    二型客车("2"),
    //大巴车
    三型客车("3"),
    //行人
    四型客车("4"),
    //中货车
    一型货车("11"),
    //救护车
    二型货车("12"),
    //消防车
    三型货车("13"),
    //机动摩托车
    四型货车("14"),
    //外卖车
    五型货车("15"),
    //外卖车-美团
    六型货车("16"),
    //外卖车-饿了么
    一型专项作业车("21"),
    //外卖车-肯德基
    二型专项作业车("22"),
    //外卖车-麦当劳
    三型专项作业车("23"),
    //快递车
    四型专项作业车("24"),
    //快递车-京东
    五型专项作业车("25"),
    //快递车-顺丰
    六型专项作业车("26");

    String type;

    VehicleTypeEnum(String b) {
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
