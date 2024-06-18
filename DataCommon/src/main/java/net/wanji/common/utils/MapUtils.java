package net.wanji.common.utils;

/**
 * ClassName: MapUtils
 * Description:
 * date: 2021/08/16 21:29
 *
 * @author gdj
 */
public class MapUtils {

    public static int getDistance(double lng1,double lat1,double lng2,double lat2){
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1) - Math.toRadians(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6378137.0;// 取WGS84标准参考椭球中的地球长半径(单位:m)
        return (int) (Math.round(s * 10000) / 10000);
    }

}
