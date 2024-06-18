package net.wanji.common.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : yangliang
 * @create 2022/11/3 14:30
 */
public enum PassMediumEnum {
    SITE_TYPE_CPC("0", "CPC"),
    SITE_TYPE_OBU("1", "OBU");
    private final String code;
    private final String info;

    PassMediumEnum(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }

    /**
     * 转为数据
     * @return 枚举对象数组
     */
    public static List<Map<String, String>> toList() {
        List<Map<String, String>> list = new ArrayList<>();
        for (PassMediumEnum item : PassMediumEnum.values()) {
            Map<String, String> map = new HashMap<>();
            map.put("code", item.getCode());
            map.put("name", item.getInfo());
            list.add(map);
        }
        return list;
    }


}
