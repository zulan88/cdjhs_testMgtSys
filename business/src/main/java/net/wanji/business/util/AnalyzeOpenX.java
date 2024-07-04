package net.wanji.business.util;

import net.wanji.business.domain.Tjshape;
import net.wanji.business.domain.WoPostion;
import net.wanji.openx.generated.*;
import org.springframework.stereotype.Component;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            Double mintime = 500.0;
            for (Act act : openScenario.getStoryboard().getStory().get(0).getAct()){
                Polyline polyline = act.getManeuverGroup().get(0).getManeuver().get(0).getEvent().get(0).getAction().get(0).getPrivateAction().getRoutingAction().getFollowTrajectoryAction().getTrajectory().getShape().getPolyline();
                Vertex vertex = polyline.getVertex().get(0);
                if(mintime > Double.parseDouble(vertex.getTime())){
                    mintime = Double.parseDouble(vertex.getTime());
                }
            }
            for (Act act : openScenario.getStoryboard().getStory().get(0).getAct()){
                String enity = act.getManeuverGroup().get(0).getActors().getEntityRef().get(0).getEntityRef();
                String name = "car";
                ScenarioObject scenarioObject = openScenario.getEntities().getScenarioObject().stream().filter(e -> e.getName().equals(enity)).findFirst().get();
                if (scenarioObject.getVehicle() != null){
                    name = scenarioObject.getVehicle().getVehicleCategory();
                }else if (scenarioObject.getPedestrian() != null){
                    name = scenarioObject.getPedestrian().getPedestrianCategory();
                }
                Polyline polyline = act.getManeuverGroup().get(0).getManeuver().get(0).getEvent().get(0).getAction().get(0).getPrivateAction().getRoutingAction().getFollowTrajectoryAction().getTrajectory().getShape().getPolyline();
                for (Vertex vertex : polyline.getVertex()){
                    if (woPostionMap.containsKey(vertex.getTime())){
                        WorldPosition worldPosition = vertex.getPosition().getWorldPosition();
                        woPostionMap.get(vertex.getTime()).add(new WoPostion(enity,worldPosition.getX(), worldPosition.getY(), worldPosition.getH(), taketype(name)));
                    }else {
                        WorldPosition worldPosition = vertex.getPosition().getWorldPosition();
                        List<WoPostion> res = new ArrayList<>();
                        res.add(new WoPostion(enity,worldPosition.getX(), worldPosition.getY(), worldPosition.getH(), taketype(name)));
                        woPostionMap.put(vertex.getTime(), res);
                    }
                }
            }
            List<Tjshape> tjshapes = new ArrayList<>();

            int index = 0;
            for (String key : woPostionMap.keySet()){
                List<WoPostion> woPostions = woPostionMap.get(key);
                //主车
                if(index == 0){
                    List<WoPostion> mainPostions = new ArrayList<>();
                    WoPostion mainPostion = parseCommentsFromXML(xocsPath);
                    mainPostion.setId("A0");
                    mainPostions.add(mainPostion);
                    Tjshape mainshape = new Tjshape();
                    mainshape.setDuration(0);
                    mainshape.setWoPostionList(mainPostions);
                    tjshapes.add(mainshape);
                }
                Tjshape tjshape = new Tjshape();
                tjshape.setDuration((int) ((Double.parseDouble(key)-mintime)*1000));
                tjshape.setWoPostionList(woPostions);
                tjshapes.add(tjshape);
                index++;
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

    public  WoPostion parseCommentsFromXML(String filePath) {
        WoPostion woPostion = null;
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // 使用递归方法遍历所有节点，寻找注释节点
            woPostion = traverseNodes(doc);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return woPostion;
    }

    private WoPostion traverseNodes(Node node) {
        NodeList nodeList = node.getChildNodes();
        WoPostion woPostion = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.COMMENT_NODE) {
                Comment comment = (Comment) currentNode;
                String data = comment.getData().trim();
                // 检查是否是我们感兴趣的注释
                if (data.startsWith("[Initial State]")) {
                    System.out.println("Found Initial State Comment: " + data);
                    woPostion = extractInitialState(data);
                }
            }
            // 如果当前节点下还有子节点，继续遍历
            if (currentNode.hasChildNodes()) {
                WoPostion woPostion1 = traverseNodes(currentNode);
                if(woPostion1 != null) return woPostion1;
            }
        }
        return woPostion;
    }

    private WoPostion extractInitialState(String comment) {
        // 使用正则表达式提取 x_init, y_init, heading_init 的值
        //Pattern pattern = Pattern.compile("x_init = ([\\d.]+), y_init = ([\\d.]+), heading_init = ([\\d.]+)");
        Pattern pattern = Pattern.compile("v_init = ([\\d.]+), x_init = ([\\d.-]+), y_init = ([\\d.-]+), heading_init = ([\\d.-]+)");
        Matcher matcher = pattern.matcher(comment);
        WoPostion woPostion = null;
        if (matcher.find()) {
            String xInit = matcher.group(2);
            String yInit = matcher.group(3);
            String headingInit = matcher.group(4);
            woPostion = new WoPostion("0",xInit,yInit,headingInit,1);
        }
        return woPostion;
    }
}
