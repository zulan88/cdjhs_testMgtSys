package net.wanji.business.util;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hanchaoyong
 * @version 1.0
 * @className LongitudeLatitudeUtils
 * @description TODO
 * @date 2023-04-21 18:54
 **/
public class LongitudeLatitudeUtils {

  /**
   * 计算关键点路径长度
   *
   * @param points
   * @return
   */
  public static double fullLength(List<Point2D.Double> points) {
    double fullLength = 0.0;
    for (int i = 0; i < points.size() - 1; i++) {
      Point2D.Double current = points.get(i);
      Point2D.Double next = points.get(i + 1);
      fullLength += ellipsoidalDistance(current, next);
    }
    return fullLength;
  }

  public static List<Double> distanceFromStartingPoint(
      List<Point2D.Double> points) {
    ArrayList<Double> distance = new ArrayList<>();
    double distanceFromStart = 0.0;
    for (int i = 0; i < points.size() - 1; i++) {
      Point2D.Double current = points.get(i);
      Point2D.Double next = points.get(i + 1);
      distance.add(distanceFromStart);
      distanceFromStart += ellipsoidalDistance(current, next);
    }
    return distance;
  }

  /**
   * 计算是否经过门架
   * 算法：时间戳排序，顺序经过，圆外、圆内、圆外，认为已经通过门架
   *
   * @param points
   * @param portalLL 门架中心点
   * @param radius   半径
   * @return
   */
  public static boolean isCrossSection(List<Point2D.Double> points,
      Point2D.Double portalLL, BigDecimal radius) {

    int atLastThree = 3;
    if (points.size() < atLastThree) {
      return false;
    }
    boolean before = false;
    boolean in = false;
    boolean after = false;
    for (Point2D.Double point : points) {
      boolean inCycle = isInCycle(point, portalLL, radius);
      if (!before) {
        before = inCycle;
      } else if (!in) {
        in = inCycle;
      } else {
        after = !inCycle;
      }
    }

    return after;
  }

  /**
   * @param point
   * @param portalLL
   * @param radius   单位（m）
   * @return
   */
  public static boolean isInCycle(Point2D.Double point, Point2D.Double portalLL,
      BigDecimal radius) {
    GeodeticCurve geodeticCurve = new GeodeticCalculator().calculateGeodeticCurve(
        Ellipsoid.WGS84, new GlobalCoordinates(portalLL.x, portalLL.y),
        new GlobalCoordinates(point.x, point.y));
    return radius.compareTo(
        BigDecimal.valueOf(geodeticCurve.getEllipsoidalDistance())) >= 0;
  }

  public static double ellipsoidalDistance(Point2D.Double source,
      Point2D.Double target) {
    return ellipsoidalDistance(source.x, source.y, target.x, target.y);
  }

  public static double ellipsoidalDistance(double sourceX, double sourceY,
      double targetX, double targetY) {
    GeodeticCurve geodeticCurve = new GeodeticCalculator().calculateGeodeticCurve(
        Ellipsoid.WGS84, new GlobalCoordinates(sourceX, sourceY),
        new GlobalCoordinates(targetX, targetY));
    return geodeticCurve.getEllipsoidalDistance();
  }

  /**
   * 检测点位是否在某个区域内
   *
   * @param point
   * @param pts   顺时针或逆时针描述区域
   * @return
   */
  public static boolean isInPolygon(Point2D.Double point,
      List<Point2D.Double> pts) {
    int N = pts.size();
    boolean boundOrVertex = true;
    /**
     * 交叉点数量
     */
    int intersectCount = 0;
    /**
     * 浮点类型计算时候与0比较时候的容差
     */
    double precision = 2e-10;
    /**
     * 临近顶点
     */
    Point2D.Double p1, p2;
    /**
     * 当前点
     */
    Point2D.Double p = point;

    p1 = pts.get(0);
    for (int i = 1; i <= N; ++i) {
      if (p.equals(p1)) {
        return boundOrVertex;
      }

      p2 = pts.get(i % N);
      if (p.x < Math.min(p1.x, p2.x) || p.x > Math.max(p1.x, p2.x)) {
        p1 = p2;
        continue;
      }

      //射线穿过算法
      if (p.x > Math.min(p1.x, p2.x) && p.x < Math.max(p1.x, p2.x)) {
        if (p.y <= Math.max(p1.y, p2.y)) {
          if (p1.x == p2.x && p.y >= Math.min(p1.y, p2.y)) {
            return boundOrVertex;
          }

          if (p1.y == p2.y) {
            if (p1.y == p.y) {
              return boundOrVertex;
            } else {
              ++intersectCount;
            }
          } else {
            double xinters =
                (p.x - p1.x) * (p2.y - p1.y) / (p2.x - p1.x) + p1.y;
            if (Math.abs(p.y - xinters) < precision) {
              return boundOrVertex;
            }

            if (p.y < xinters) {
              ++intersectCount;
            }
          }
        }
      } else {
        if (p.x == p2.x && p.y <= p2.y) {
          Point2D.Double p3 = pts.get((i + 1) % N);
          if (p.x >= Math.min(p1.x, p3.x) && p.x <= Math.max(p1.x, p3.x)) {
            ++intersectCount;
          } else {
            intersectCount += 2;
          }
        }
      }
      p1 = p2;
    }
    if (intersectCount % 2 == 0) {
      //偶数在多边形外
      return false;
    } else {
      //奇数在多边形内
      return true;
    }
  }

}
