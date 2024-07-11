package net.wanji.business.exercise;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.exercise.utils.ToBuildOpenXTransUtil;

import java.io.IOException;

/**
 * @author: jenny
 * @create: 2024-06-23 7:17 下午
 */
@Slf4j
public class Test {
    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
        String proj = "+proj=tmerc +lon_0=108.90577060170472 +lat_0=34.37650478465651 +ellps=WGS84";
        Double longitude = 108.89839855344397;
        Double latitude = 34.37506097171245;
        Double x = -678.0572205556866;
        Double y = -160.1363736604608;
        JSONObject worldPosition = ToBuildOpenXTransUtil.retotrans(x,y,proj);
        System.out.println(worldPosition);
        JSONObject worldPosition1 = ToBuildOpenXTransUtil.totrans(longitude,latitude,proj);
        System.out.println(worldPosition1);

    }
}
