package net.wanji.business.util;

import net.wanji.business.common.Constants.PointTypeEnum;
import net.wanji.business.domain.bo.ParticipantTrajectoryBo;
import net.wanji.business.domain.bo.TrajectoryDetailBo;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.utils.GeoUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/2 9:16
 * @Descriptoin:
 */

public class TrajectoryUtils {

    /**
     * 计算里程
     * @param data
     * @return
     */
    public static double getMileage(List<TrajectoryValueDto> data) {
        double mileage = 0;
        for (int i = 0; i < data.size() - 1; i++) {
            mileage += GeoUtil.calculateDistance(data.get(i).getLatitude(), data.get(i).getLongitude(),
                    data.get(i + 1).getLatitude(), data.get(i + 1).getLongitude());
        }
        return mileage;
    }

    /**
     * 获取所有参与者的起始点
     * @param participantTrajectories
     * @return
     */
    public static Map<String, String> getStartPoint(List<ParticipantTrajectoryBo> participantTrajectories) {
        return CollectionUtils.emptyIfNull(participantTrajectories).stream().collect(
                        Collectors.toMap(
                                ParticipantTrajectoryBo::getId,
                                item -> CollectionUtils.emptyIfNull(item.getTrajectory()).stream()
                                        .filter(t -> PointTypeEnum.START.getPointType().equals(t.getType())).findFirst()
                                        .orElse(new TrajectoryDetailBo()).getPosition()));
    }
}
