package net.wanji.business.component;

import net.wanji.business.domain.dto.CountDownDto;
import net.wanji.business.util.LongitudeLatitudeUtils;

import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import java.awt.geom.Point2D;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author glace
 * @version 1.0
 * @className CountDown
 * @description TODO
 * @date 2023/8/31 13:48
 **/
@NotThreadSafe
public class CountDown {
  private final List<Point2D.Double> points;
  private List<Double> distanceFromStartingPoint;
  private final double fullLength;
  private int currentPointI = 1;
  private int dataI = 0;
  private BigDecimal radius = new BigDecimal(4);

  private final List<Point2D.Double> rangeData = new ArrayList<>();

  public CountDown(List<Point2D.Double> points) {
    this.points = points;
    fullLength = LongitudeLatitudeUtils.fullLength(points);
    distanceFromStartingPoint = LongitudeLatitudeUtils.distanceFromStartingPoint(
        points);
  }

  /**
   * 倒计时
   *
   * @param currentSpeed KM/H
   * @param currentPoint
   * @return
   */
  @Nullable
  public CountDownDto countDown(double currentSpeed,
      Point2D.Double currentPoint) {
    currentSpeed = speedToms(currentSpeed);
    rangeData.add(currentPoint);
    if (dataI % 10 == 0) {
      dataI = 0;
      if (LongitudeLatitudeUtils.isCrossSection(rangeData,
          points.get(getNextPointIndex()), radius)) {
        currentPointI++;
      }
      rangeData.clear();
      CountDownDto countDownDto = new CountDownDto();
      countDownDto.setFullLength(fullLength);
      double v = lengthRemaining(currentPoint);
      countDownDto.setRemainLength(v);
      countDownDto.setTimeRemaining(timeRemaining(v, currentSpeed));
      dataI++;
      return countDownDto;
    }
    dataI++;
    return null;
  }

  protected Long timeRemaining(double lengthRemaining, double currentSpeed) {
    double v = currentSpeed > 0 ? lengthRemaining / currentSpeed : 0;
    return (long) v;
  }

  protected double lengthRemaining(Point2D.Double currentPoint) {
    int index = currentPointI - 1;
    double v = LongitudeLatitudeUtils.ellipsoidalDistance(currentPoint,
        points.get(index));
    return fullLength - (v + distanceFromStartingPoint.get(index));
  }

  protected int getNextPointIndex() {
    if (currentPointI >= points.size()) {
      return points.size() - 1;
    }
    return currentPointI;
  }

  protected double speedToms(double currentSpeed) {
    return currentSpeed / 3.6;
  }

  public void setRadius(BigDecimal radius) {
    this.radius = radius;
  }

  public static void main(String[] args) {
    ArrayList<Point2D.Double> doubles = new ArrayList<>();
    doubles.add(new Point2D.Double(121.20139045333559, 31.291346840918422));
    doubles.add(new Point2D.Double(121.20180991330014, 31.29134514856418));
    CountDown countDown = new CountDown(doubles);
    for (int i = 0; i < 20; i++) {
      CountDownDto countDownDto = countDown.countDown(0,
          new Point2D.Double(1.1, 1.1));
      System.out.println(countDownDto);
    }
  }
}
