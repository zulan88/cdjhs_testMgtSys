package net.wanji.business.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.wanji.business.domain.dto.CountDownDto;
import net.wanji.common.common.SimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author glace
 * @version 1.0
 * @className CountDownTest
 * @description TODO
 * @date 2023/9/4 10:55
 **/
public class CountDownTest {
  public static void main(String[] args) {
    ArrayList<Point2D.Double> doubles = new ArrayList<>();
    doubles.add(new Point2D.Double(121.20302391316832, 31.29201859146071));
    doubles.add(new Point2D.Double(121.20294859869418, 31.292095490627762));
    doubles.add(new Point2D.Double(121.20275143148014, 31.29228016035883));
    doubles.add(new Point2D.Double(121.20262645092153, 31.29241040607111));
    doubles.add(new Point2D.Double(121.20247620661955, 31.29255200779399));
    CountDown countDown = new CountDown(doubles);

    try {
      FileInputStream inputStream = new FileInputStream(
          "E:\\Subjects\\wj\\同济大学\\虚拟主控\\车辆运行信息\\27e7ab08-e921-4803-89fa-4974eeea5e08.txt");
      BufferedReader bufferedReader = new BufferedReader(
          new InputStreamReader(inputStream));
      String str = null;
      ObjectMapper objectMapper = new ObjectMapper();
      while ((str = bufferedReader.readLine()) != null) {
        // Thread.sleep(1000);
        SimulationTrajectoryDto simulationTrajectoryDto = objectMapper.readValue(
            str, SimulationTrajectoryDto.class);
        List<TrajectoryValueDto> value = simulationTrajectoryDto.getValue();
        List<TrajectoryValueDto> main = value.stream()
            .filter(e -> e.getName().equals("主车1"))
            .collect(Collectors.toList());
        TrajectoryValueDto trajectoryValueDto = main.get(0);
        CountDownDto countDownDto = countDown.countDown(
            trajectoryValueDto.getSpeed(),
            new Point2D.Double(trajectoryValueDto.getLongitude(),
                trajectoryValueDto.getLatitude()));
        if (null != countDownDto) {
          System.out.println(countDownDto.getRemainLength() + "-"
              + countDownDto.getFullLength() + "--"
              + countDownDto.getTimeRemaining() + "--"
              + countDownDto.getArrivalTime());
        }

      }

      //close
      inputStream.close();
      bufferedReader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
