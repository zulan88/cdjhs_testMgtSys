package net.wanji.business.util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.wanji.business.domain.InfinteMileScenceExo;
import net.wanji.business.domain.bo.ParticipantTrajectoryBo;
import net.wanji.business.domain.vo.FragmentedScenesDetailVo;
import net.wanji.business.entity.TjAtlasVenue;
import net.wanji.business.entity.TjScenelib;
import net.wanji.business.entity.TjTaskCase;
import net.wanji.business.exception.BusinessException;
import net.wanji.business.mapper.TjTaskCaseMapper;
import net.wanji.business.service.ITjAtlasVenueService;
import net.wanji.business.service.ITjScenelibService;
import net.wanji.business.service.TjFragmentedSceneDetailService;
import net.wanji.common.common.ClientSimulationTrajectoryDto;
import net.wanji.common.common.TrajectoryValueDto;
import net.wanji.common.config.WanjiConfig;
import net.wanji.common.constant.Constants;
import net.wanji.common.utils.DateUtils;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.file.FileUploadUtils;
import net.wanji.openx.generated.File;
import net.wanji.openx.generated.*;
import org.locationtech.proj4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Component
public class ToBuildOpenXUtil {


    private static final CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
    private static final CRSFactory crsFactory = new CRSFactory();


    private  void zipfile(java.io.File file, ZipOutputStream zos) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            zos.write(buffer, 0, length);
        }

        fis.close();
    }


    private  CoordinateReferenceSystem createCRS(String crsSpec) {
        CoordinateReferenceSystem crs = null;
        if (crsSpec.contains("+") || crsSpec.contains("=")) {
            crs = crsFactory.createFromParameters("Anon", crsSpec);
        } else {
            crs = crsFactory.createFromName(crsSpec);
        }

        return crs;
    }

    private  WorldPosition totrans(Double lon, Double lat, String tgtCRS, Double angle) {
        String WGS84_PARAM = "+proj=longlat +datum=WGS84 +no_defs ";
        CoordinateTransform trans = ctFactory
                .createTransform(createCRS(WGS84_PARAM), createCRS(tgtCRS));
        ProjCoordinate pout = new ProjCoordinate();
        ProjCoordinate p = new ProjCoordinate(lon, lat);
        trans.transform(p, pout);
        double angleInRadians = Math.toRadians(angle);
//        angleInRadians = (angleInRadians + Math.PI) % (2 * Math.PI);
        angleInRadians = -angleInRadians;
        angleInRadians += Math.PI / 2;
        return new WorldPosition(String.format("%.16e", pout.x), String.format("%.16e", pout.y), String.format("%.16e", angleInRadians));
    }

    public  JSONObject retotrans(Double x, Double y, String sourceCRS, Double angle){
        String WGS84_PARAM = "+proj=longlat +datum=WGS84 +no_defs ";
        CoordinateTransform trans = ctFactory
                .createTransform(createCRS(sourceCRS), createCRS(WGS84_PARAM));
        ProjCoordinate pout = new ProjCoordinate();
        ProjCoordinate p = new ProjCoordinate(x, y);
        trans.transform(p, pout);

        JSONObject jsonObject = new JSONObject();
        //转换结果
        Double longitude = pout.x;
        Double latitude = pout.y;
        Double degree = 90 - angle * 180/Math.PI;
        jsonObject.put("longitude",longitude);
        jsonObject.put("latitude",latitude);
        jsonObject.put("degree",degree);
        return jsonObject;
    }

//    public static void main(String[] args) {
//        String proj = "+proj=tmerc +lon_0=121.20585769414902 +lat_0=31.290823210868965 +ellps=WGS84";
//        String WGS84_PARAM = "+proj=longlat +datum=WGS84 +no_defs ";
//        CoordinateTransform trans = ctFactory
//                .createTransform(createCRS(proj), createCRS(WGS84_PARAM));
//        ProjCoordinate pout = new ProjCoordinate();
//        ProjCoordinate p = new ProjCoordinate(40.46, -3.9);
//        trans.transform(p, pout);
//        System.out.println(pout.x + "," + pout.y);
//    }


}
