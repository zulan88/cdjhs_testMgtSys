package net.wanji.business.component;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import net.wanji.business.common.Constants.PointTypeEnum;
import net.wanji.business.domain.bo.TrajectoryDetailBo;
import net.wanji.common.utils.GeoUtil;
import net.wanji.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/2 11:45
 * @Descriptoin:
 */

public class PathwayPoints {

    private static final Logger log = LoggerFactory.getLogger("business");

    private static final int MAX_PROMPT_DIST = 50;

    private static final int MIN_PROMPT_DIST = 3;

    /**
     * 全部点位
     */
    private final List<TrajectoryDetailBo> pathWayPoints;

    private TrajectoryDetailBo nearestPoint;

    private double distance;

    private int index;

    public PathwayPoints(List<TrajectoryDetailBo> pathWayPoints) {
        this.pathWayPoints = pathWayPoints;
        this.index = 0;
    }

    public boolean hasTips() {
        return !ObjectUtils.isEmpty(nearestPoint);
    }

    public String getPointName() {
        String name = PointTypeEnum.getDescByPointType(nearestPoint.getType());
        return ObjectUtil.isEmpty(nearestPoint) ? "" : (PointTypeEnum.END.getPointType().equals(nearestPoint.getType())
                ? name
                : name + this.index + 1);
    }

    public double getPointSpeed() {
        return ObjectUtil.isEmpty(nearestPoint) ? 0 : nearestPoint.getSpeed();
    }

    public double getDistance() {
        return distance;
    }

    public void reset() {
        this.nearestPoint = null;
        this.distance = 0;
    }

    /**
     * 判断点位处于哪两个点之间
     *
     * @param longitude
     * @param latitude
     * @return
     */
    public PathwayPoints findNearestPoint(double longitude, double latitude) {
        if (index == pathWayPoints.size() - 1) {
            return this;
        }
        TrajectoryDetailBo detailBo1 = pathWayPoints.get(index);
        List<Double> pos1 = Arrays.stream(detailBo1.getPosition().split(",")).map(Double::parseDouble).collect(Collectors.toList());
        TrajectoryDetailBo detailBo2 = pathWayPoints.get(index + 1);
        List<Double> pos2 = Arrays.stream(detailBo2.getPosition().split(",")).map(Double::parseDouble).collect(Collectors.toList());
        Point2D.Double p1 = new Point2D.Double(pos1.get(0), pos1.get(1));
        Point2D.Double p2 = new Point2D.Double(pos2.get(0), pos2.get(1));
        Point2D.Double p = new Point2D.Double(longitude, latitude);
        double dist = GeoUtil.calculateDistance(p.getY(), p.getX(), p2.getY(), p2.getX());
        if (ObjectUtils.isEmpty(this.nearestPoint)) {
            if (!this.isPointBetweenPoints(p, p1, p2)) {
                return this;
            }
            if (dist < MAX_PROMPT_DIST && dist > MIN_PROMPT_DIST) {
                this.nearestPoint = detailBo2;
                this.distance = dist;
            }
        } else {
            if (dist < MAX_PROMPT_DIST && dist > MIN_PROMPT_DIST) {
                this.distance = dist;
            } else {
                this.reset();
                this.index += 1;
            }
        }
        return this;
    }

    /**
     * 点是否处于两点之间
     *
     * @param point  待判断点
     * @param point1
     * @param point2
     * @return
     */
    public boolean isPointBetweenPoints(Point2D.Double point, Point2D.Double point1, Point2D.Double point2) {
        double crossProduct = (point.getY() - point1.getY()) * (point2.getX() - point1.getX())
                - (point.getX() - point1.getX()) * (point2.getY() - point1.getY());

        if (Math.abs(crossProduct) > 0.000001) {
            return false;
        }

        double dotProduct = (point.getX() - point1.getX()) * (point2.getX() - point1.getX())
                + (point.getY() - point1.getY()) * (point2.getY() - point1.getY());

        if (dotProduct < 0) {
            return false;
        }

        double squaredLength = (point2.getX() - point1.getX()) * (point2.getX() - point1.getX())
                + (point2.getY() - point1.getY()) * (point2.getY() - point1.getY());

        if (dotProduct > squaredLength) {
            return false;
        }

        return true;
    }


}
