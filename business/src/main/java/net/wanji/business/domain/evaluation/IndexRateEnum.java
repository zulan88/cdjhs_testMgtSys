package net.wanji.business.domain.evaluation;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: jenny
 * @create: 2024-07-01 10:03 上午
 */
public enum IndexRateEnum {
    EXCELLENT(0),
    GOOD(1),
    NORMAL(2),
    BAD(3),
    VERY_BAD(4);

    IndexRateEnum(Integer rate){
        this.rate = rate;
    }

    private final Integer rate;

    public Integer getRate() {
        return rate;
    }

    public static Map<Integer, IndexRateEnum> getIndexRateEnumMap(){
        return Arrays.stream(values())
                .collect(Collectors.toMap(IndexRateEnum::getRate, Function.identity()));
    }
}
