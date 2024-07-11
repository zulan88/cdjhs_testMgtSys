package net.wanji.business.exercise.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.proj4j.*;

@Slf4j
public class ToBuildOpenXTransUtil {

    private static final CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();

    private static final CRSFactory crsFactory = new CRSFactory();

    private static CoordinateReferenceSystem createCRS(String crsSpec) {
        CoordinateReferenceSystem crs = null;
        if (crsSpec.contains("+") || crsSpec.contains("=")) {
            crs = crsFactory.createFromParameters("Anon", crsSpec);
        } else {
            crs = crsFactory.createFromName(crsSpec);
        }

        return crs;
    }

    public static JSONObject totrans(Double lon, Double lat, String tgtCRS) {
        String WGS84_PARAM = "+proj=longlat +datum=WGS84 +no_defs ";
        CoordinateTransform trans = ctFactory
                .createTransform(createCRS(WGS84_PARAM), createCRS(tgtCRS));
        ProjCoordinate pout = new ProjCoordinate();
        ProjCoordinate p = new ProjCoordinate(lon, lat);
        trans.transform(p, pout);
        JSONObject jsonObject = new JSONObject();
        //转换结果
        Double x = pout.x;
        Double y = pout.y;
        jsonObject.put("x",x);
        jsonObject.put("y",y);
        return jsonObject;
    }

    public static JSONObject retotrans(Double x, Double y, String sourceCRS){
        String WGS84_PARAM = "+proj=longlat +datum=WGS84 +no_defs";
        CoordinateTransform trans = ctFactory
                .createTransform(createCRS(sourceCRS), createCRS(WGS84_PARAM));
        ProjCoordinate pout = new ProjCoordinate();
        ProjCoordinate p = new ProjCoordinate(x, y);
        trans.transform(p, pout);

        JSONObject jsonObject = new JSONObject();
        //转换结果
        Double longitude = pout.x;
        Double latitude = pout.y;
        jsonObject.put("longitude",longitude);
        jsonObject.put("latitude",latitude);
        return jsonObject;
    }

    public static void main(String[] args) {
//        108.89839855344397 34.37506097171245 -678.0572205556866 -160.1363736604608
        ToBuildOpenXTransUtil toBuildOpenXUtil = new ToBuildOpenXTransUtil();
        String proj = "+proj=tmerc +lon_0=108.90577060170472 +lat_0=34.37650478465651 +ellps=WGS84";
        Double longitude = 108.89839855344397;
        Double latitude = 34.37506097171245;
        Double x = -678.0572205556866;
        Double y = -160.1363736604608;
        JSONObject worldPosition = toBuildOpenXUtil.retotrans(x,y,proj);
        System.out.println(worldPosition);
        JSONObject worldPosition1 = toBuildOpenXUtil.totrans(longitude,latitude,proj);
        System.out.println(worldPosition1);
    }


}
