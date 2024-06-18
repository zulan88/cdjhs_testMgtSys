package net.wanji.business.util;

import net.wanji.business.domain.Tjshape;
import net.wanji.business.domain.WoPostion;
import net.wanji.openx.generated.*;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Component
public class AnalyzeOpenX {

    public List<Tjshape> analyze(String xocsPath) {
        File file = new File(xocsPath);
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(OpenScenario.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            OpenScenario openScenario = (OpenScenario) jaxbUnmarshaller.unmarshal(file);
            HashMap<String, List<WoPostion>> woPostionMap = new LinkedHashMap<>(150);
            for (Act act : openScenario.getStoryboard().getStory().get(0).getAct()){
                String enity = act.getManeuverGroup().get(0).getActors().getEntityRef().get(0).getEntityRef();
                Polyline polyline = act.getManeuverGroup().get(0).getManeuver().get(0).getEvent().get(0).getAction().get(0).getPrivateAction().getRoutingAction().getFollowTrajectoryAction().getTrajectory().getShape().getPolyline();
                for (Vertex vertex : polyline.getVertex()){
                    if (woPostionMap.containsKey(vertex.getTime())){
                        WorldPosition worldPosition = vertex.getPosition().getWorldPosition();
                        woPostionMap.get(vertex.getTime()).add(new WoPostion(enity,worldPosition.getX(), worldPosition.getY(), worldPosition.getH(), taketype(enity)));
                    }else {
                        WorldPosition worldPosition = vertex.getPosition().getWorldPosition();
                        List<WoPostion> res = new ArrayList<>();
                        res.add(new WoPostion(enity,worldPosition.getX(), worldPosition.getY(), worldPosition.getH(), taketype(enity)));
                        woPostionMap.put(vertex.getTime(), res);
                    }
                }
            }
            List<Tjshape> tjshapes = new ArrayList<>();
            for (String key : woPostionMap.keySet()){
                List<WoPostion> woPostions = woPostionMap.get(key);
                Tjshape tjshape = new Tjshape();
                tjshape.setDuration((int) (Double.parseDouble(key)*1000));
                tjshape.setWoPostionList(woPostions);
                tjshapes.add(tjshape);
            }
            return tjshapes;

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    private Integer taketype(String entity){
        if (entity.contains("pedestrian")){
            return 4;
        }else if (entity.contains("bicycle")){
            return 13;
        }else {
            return 1;
        }
    }

}
