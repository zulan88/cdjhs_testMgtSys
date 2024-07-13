package net.wanji.business.exercise.utils;

import com.alibaba.fastjson.JSONObject;
import net.wanji.business.util.ToBuildOpenXUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimulationAreaCalculator {

    public static JSONObject getSimuArea(String inputFilename, double disRate) {
        double xInit = 0, yInit = 0, xTarget = 0, yTarget = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
            String line;
            Pattern xInitPattern = Pattern.compile("x_init = ([-+]?\\d+\\.\\d+), y_init = ([-+]?\\d+\\.\\d+)");
            Pattern xInitPattern1 = Pattern.compile("x_init = ([-+]?\\d+\\.\\d+) , y_init = ([-+]?\\d+\\.\\d+)");
//            Pattern xTargetPattern = Pattern.compile("x_target = \\(\\s*([-\\d.]+)\\s*,\\s*([-\\d.]+)\\s*\\)");
//            Pattern yTargetPattern = Pattern.compile("y_target = \\(\\s*([-\\d.]+)\\s*,\\s*([-\\d.]+)\\s*\\)");
            Pattern xTargetPattern = Pattern.compile("x_target\\s*=\\s*\\(([^,)]+),\\s*([^,)]+)\\s*\\)");
            Pattern yTargetPattern = Pattern.compile("y_target\\s*=\\s*\\(([^,)]+),\\s*([^,)]+)\\s*\\)");

            while ((line = reader.readLine()) != null) {
                if (line.contains("Initial State")) {
                    System.out.println(line);
                    Matcher matcher = xInitPattern.matcher(line);
                    Matcher matcher1 = xInitPattern1.matcher(line);
                    if (matcher.find()) {
                        xInit = Double.parseDouble(matcher.group(1));
                        yInit = Double.parseDouble(matcher.group(2));

                    }else if(matcher1.find()){
                        xInit = Double.parseDouble(matcher1.group(1));
                        yInit = Double.parseDouble(matcher1.group(2));
                    } else {
                        System.out.println("没有找到匹配的x_init和y_init值。");
                    }
                }

                if (line.contains("Driving Task")) {
                    System.out.println(line);
                    Matcher xMatcher = xTargetPattern.matcher(line);
                    Matcher yMatcher = yTargetPattern.matcher(line);

                    if (xMatcher.find()) {
                        xTarget = Double.parseDouble(xMatcher.group(1));
                    }

                    if (yMatcher.find()) {
                        yTarget = Double.parseDouble(yMatcher.group(1));
                    }

                    break; // 假设找到后直接跳出循环
                }
            }

            if (xInit != 0 && yInit != 0 && xTarget != 0 && yTarget != 0) {
                double xDis = Math.abs(xInit - xTarget);
                double yDis = Math.abs(yInit - yTarget);
//                System.out.println("dis: " + xDis + ", " + yDis);
                System.out.println(xInit + " " + yInit + " " + xTarget + " " + yTarget );
                double leftTopX, rightBottomX, leftTopY, rightBottomY;

                if (xInit > xTarget) {
                    leftTopX = xTarget - disRate * xDis;
                    rightBottomX = xInit + disRate * xDis;
                } else {
                    leftTopX = xInit - disRate * xDis;
                    rightBottomX = xTarget + disRate * xDis;
                }

                if (yInit > yTarget) {
                    leftTopY = yInit + disRate * yDis;
                    rightBottomY = yTarget - disRate * yDis;
                } else {
                    leftTopY = yTarget - disRate * yDis;
                    rightBottomY = yInit + disRate * yDis;
                }

                // 这里可以添加更多逻辑来处理leftTopX, rightBottomX, leftTopY, rightBottomY
                ToBuildOpenXUtil toBuildOpenXUtil = new ToBuildOpenXUtil();
                JSONObject worldPosition = toBuildOpenXUtil.retotrans(leftTopX, leftTopY,"+proj=tmerc +lon_0=108.90577060170472 +lat_0=34.37650478465651 +ellps=WGS84",0D);
                Double leftTopLongitude = (Double) worldPosition.get("longitude");
                Double leftTopLatitude = (Double) worldPosition.get("latitude");
                System.out.println(leftTopLongitude + " ," + leftTopLatitude );



//                JSONObject leftTopLatitudeWorldPosition = toBuildOpenXUtil.totrans(leftTopLongitude,leftTopLatitude,projStr);
//                Double leftTopX = (Double) leftTopLatitudeWorldPosition.get("x");
//                Double leftTopY = (Double) leftTopLatitudeWorldPosition.get("y");
//
                JSONObject worldPosition1 = toBuildOpenXUtil.retotrans(rightBottomX, rightBottomY,"+proj=tmerc +lon_0=108.90577060170472 +lat_0=34.37650478465651 +ellps=WGS84",0D);
                Double rightBottomLongitude1 = (Double) worldPosition1.get("longitude");
                Double rightBottomLatitude1 = (Double) worldPosition1.get("latitude");
                System.out.println(rightBottomLongitude1 + ", " + rightBottomLatitude1 );
//                JSONObject rightBottomWorldPosition = toBuildOpenXUtil.totrans(rightBottomLongitude1,rightBottomLatitude1,projStr);
                //left_top_lon, left_top_lat = transform(projection, wgs84_proj, left_top_x, left_top_y)
                //        right_bottom_lon, right_bottom_lat = transform(projection, wgs84_proj, right_bottom_x, right_bottom_y)
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("leftTopX", leftTopX);
                jsonObject.put("leftTopY", leftTopY);
                jsonObject.put("rightBottomX", rightBottomX);
                jsonObject.put("rightBottomY", rightBottomY);
                return jsonObject;


            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getSimuArea("G:\\xinyingjie\\software\\onsite\\onsite-structured-test-master\\convert\\changda\\temm\\535_output.xosc", 0.1));
    }
}