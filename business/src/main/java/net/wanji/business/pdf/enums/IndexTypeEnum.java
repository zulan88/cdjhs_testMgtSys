package net.wanji.business.pdf.enums;

import net.wanji.common.common.Constants;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: jenny
 * @create: 2024-06-28 2:41 下午
 */
public enum  IndexTypeEnum {
    COLLAPSE(1, "与其它车碰撞", Constants.SECURITY),
    SECURITY_INTERRUPT(2, "安全员介入", Constants.SECURITY),
    LESS_THAN_TTC_THRESOLD(3, "低于TTC阈值", Constants.SECURITY),
    GREATER_THAN_PEDESTRAIN_DISTANCE(4, "大于行人避撞距离", Constants.SECURITY),
    DRIVE_OUT_ROAD_BOUNDARY(5, "驶出道路边界", Constants.SECURITY),
    DRIVE_IN_OPPOSITE_LANE(6, "驶入对向车道", Constants.SECURITY),
    RED_LIGHT(7, "闯红灯", Constants.SECURITY),
    OVER_SPEED(8, "超出限速行驶", Constants.SECURITY),
    DOTTED_LINE(9, "压虚线行驶", Constants.SECURITY),
    TASK_FINISHED(10, "任务完成", Constants.EFFICENCY),
    TASK_TIME_CONSUMING(11, "任务耗时", Constants.EFFICENCY),
    REVERSING_CAR_REQUIREMENT(12, "非场景需要倒车", Constants.EFFICENCY),
    LONGITUDINAL_ACCELERATION(13, "纵向加速度", Constants.COMFORT),
    LONGITUDINAL_ACCELERATION_MORE(14, "纵向加加速度", Constants.COMFORT),
    LATERAL_ACCELERATION(15, "横向加速度", Constants.COMFORT),
    LATERAL_ACCELERATION_MORE(16, "横向加加速度", Constants.COMFORT),
    YAW_RATE(17, "横摆角速度", Constants.COMFORT),
    TTC(18, "TTC", Constants.COMFORT);

    IndexTypeEnum(Integer indexType, String indexName, String standard){
        this.indexType = indexType;
        this.indexName = indexName;
        this.standard = standard;
    }

    private final Integer indexType;

    private final String indexName;

    private final String standard;

    public Integer getIndexType() {
        return indexType;
    }

    public String getIndexName() {
        return indexName;
    }

    public String getStandard() {
        return standard;
    }

    public static Map<Integer, IndexTypeEnum> getIndexTypeByCategory(String standard){
        return Arrays.stream(values())
                .filter(index -> index.getStandard().equals(standard))
                .collect(Collectors.toMap(IndexTypeEnum::getIndexType, Function.identity()));
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
