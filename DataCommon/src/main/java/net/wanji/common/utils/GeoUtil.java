package net.wanji.common.utils;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/15 14:50
 * @Descriptoin:
 */

public class GeoUtil {

    public static double ellipsoidalDistance(Point2D.Double point, double targetX,
                                             double targetY) {
        GeodeticCurve geodeticCurve = new GeodeticCalculator().calculateGeodeticCurve(
                Ellipsoid.WGS84, new GlobalCoordinates(point.x, point.y),
                new GlobalCoordinates(targetX, targetY));
        return geodeticCurve.getEllipsoidalDistance();
    }

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c * 1000; // convert to meters

        return distance;
    }

    public static void main(String[] args) {
        System.out.println(GeoUtil.calculateDistance(31.292096767720004,121.20294625561048, 31.29228016035883, 121.20275143148014));
    }
}
