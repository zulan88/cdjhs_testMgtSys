package net.wanji.common.enums;


/**
 * @author wanji
 */

public enum ConstantsEnum {

    /**
     * 设备类别编码
     */
    DEV_TYPE_CODE("0", "设备类别编码"),
    /**
     * 站点编码
     */
    SITE_CODE("-1", "站点编码"),
    /**
     * 组织机构编码
     */
    ORG_CODE("0", "组织机构编码"),
    /**
     * 字符串长度
     */
    STR_LENGTH_ONE("1", "字符串长度1"),
    /**
     * 字符串长度2
     */
    STR_LENGTH_TWO("2", "字符串长度2"),
    /**
     * 字符串长度2
     */
    STR_LENGTH_THREE("3", "字符串长度3"),
    /**
     * 字符串长度4
     */
    STR_LENGTH_FOUR("4", "字符串长度4"),
    /**
     * 设备类型等级0
     */
    DEV_TYPE_LEVEL_ZERO("0", "设备类型等级0"),
    /**
     * 设备类型等级1
     */
    DEV_TYPE_LEVEL_ONE("1", "设备类型等级1"),
    /**
     * 设备类型等级2
     */
    DEV_TYPE_LEVEL_TWO("2", "设备类型等级2"),

    /**
     * 事件信息发送中
     */
    INCIDENT_IS_SEND("1","事件信息发送中"),

    /**
     * 事件信息未发送
     */
    INCIDENT_NO_SEND("0","事件信息未发送");

    private final String code;
    private final String info;

    ConstantsEnum(String code, String info)
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

}
