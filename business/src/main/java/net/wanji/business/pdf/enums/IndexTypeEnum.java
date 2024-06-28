package net.wanji.business.pdf.enums;

/**
 * @author: jenny
 * @create: 2024-06-28 2:41 下午
 */
public enum  IndexTypeEnum {
    COLLAPSE(1, "与其它车碰撞"),
    SECURITY_INTERRUPT(2, "安全员介入"),
    LESS_THAN_TTC_THRESOLD(3, "低于TTC阈值"),
    GREATER_THAN_PEDESTRAIN_DISTANCE(4, "大于行人避撞距离"),
    DRIVE_OUT_ROAD_BOUNDARY(5, "驶出道路边界"),
    DRIVE_IN_OPPOSITE_LANE(6, "驶入对向车道"),
    RED_LIGHT(7, "闯红灯"),
    OVER_SPEED(8, "超出限速行驶"),
    DOTTED_LINE(9, "压虚线行驶"),
    TASK_FINISHED(10, "任务完成"),
    TASK_TIME_CONSUMING(11, "任务耗时"),
    REVERSING_CAR_REQUIREMENT(12, "非场景需要倒车"),
    LONGITUDINAL_ACCELERATION(13, "纵向加速度"),
    LONGITUDINAL_ACCELERATION_MORE(14, "纵向加加速度"),
    LATERAL_ACCELERATION(15, "横向加速度"),
    LATERAL_ACCELERATION_MORE(16, "横向加加速度"),
    YAW_RATE(17, "横摆角速度"),
    TTC(18, "TTC");

    IndexTypeEnum(Integer indexType, String indexName){
        this.indexType = indexType;
        this.indexName = indexName;
    }

    private final Integer indexType;

    private final String indexName;

    public Integer getIndexType() {
        return indexType;
    }

    public String getIndexName() {
        return indexName;
    }

    public static String getIndexNameByType(Integer type){
        IndexTypeEnum[] values = values();
        for(IndexTypeEnum indexTypeEnum: values){
            if(indexTypeEnum.getIndexType().compareTo(type) == 0){
                return indexTypeEnum.getIndexName();
            }
        }
        return null;
    }
}
