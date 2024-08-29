package net.wanji.business.exercise.dto.luansheng;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jenny
 * @create: 2024-08-28 11:11 上午
 */
@Data
public class StatResult {
    private List<StatDto> speed = new ArrayList<>();

    private List<StatDto> lonAcc = new ArrayList<>();

    private int lonAccOverLimit = 0;

    private List<StatDto> lonAcc2 = new ArrayList<>();

    private int lonAcc2OverLimit = 0;

    private List<StatDto> latAcc = new ArrayList<>();

    private int latAccOverLimit = 0;

    private List<StatDto> latAcc2 = new ArrayList<>();

    private int latAcc2OverLimit = 0;

    private List<StatDto> angularVelocity = new ArrayList<>();

    private int angularVelocityOverLimit = 0;
}
