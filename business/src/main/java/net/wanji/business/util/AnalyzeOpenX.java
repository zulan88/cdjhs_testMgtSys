package net.wanji.business.util;

import net.wanji.business.domain.CdShape;
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
                    List<WoPostion> mainPostion = parseCommentsFromXML(xocsPath);
                    for (WoPostion woPostion : mainPostion){
                        if (woPostion.getX()!=null){
                            woPostion.setId("A0");
                            mainPostions.add(woPostion);
                        }else {
                            SceneLibMap.putEnd(xocsPath,woPostion);
                        }
                    }
                    Tjshape mainshape = new Tjshape();
                    mainshape.setDuration(0);
                    mainshape.setWoPostionList(mainPostions);
                    tjshapes.add(mainshape);
                }
                Tjshape tjshape = new Tjshape();
                int time = (int) ((Double.parseDouble(key)-mintime)*1000);
                tjshape.setDuration(time);
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
            return 5;
        }else {
            return 1;
        }
    }

    public  List<WoPostion> parseCommentsFromXML(String filePath) {
        List<WoPostion> woPostion = null;
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

    private List<WoPostion> traverseNodes(Node node) {
        NodeList nodeList = node.getChildNodes();
        List<WoPostion> woPostionList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.COMMENT_NODE) {
                Comment comment = (Comment) currentNode;
                String data = comment.getData().trim();
                // 检查是否是我们感兴趣的注释
                if (data.startsWith("[Initial State]")) {
                    System.out.println("Found Initial State Comment: " + data);
                    woPostionList.add(extractInitialState(data));
                }
                if(data.startsWith("[Driving Task]")){
                    System.out.println("Found Driving Task Comment: " + data);
                    woPostionList.add(takeEndPosition(data));
                }
            }
            // 如果当前节点下还有子节点，继续遍历
            if (currentNode.hasChildNodes()) {
                woPostionList = traverseNodes(currentNode);
                if(woPostionList.size() > 0) return woPostionList;
            }
        }
        return woPostionList;
    }

    private WoPostion extractInitialState(String comment) {
        // 使用正则表达式提取 x_init, y_init, heading_init 的值
        //Pattern pattern = Pattern.compile("x_init = ([\\d.]+), y_init = ([\\d.]+), heading_init = ([\\d.]+)");
        Pattern patternx = Pattern.compile("x_init = ([\\d.-]+)");
        Pattern patterny = Pattern.compile("y_init = ([\\d.-]+)");
        Pattern patternh = Pattern.compile("heading_init = ([\\d.-]+)");
        Matcher matcherX = patternx.matcher(comment);
        Matcher matcherY = patterny.matcher(comment);
        Matcher matcherH = patternh.matcher(comment);
        WoPostion woPostion = null;
        if (matcherX.find()) {
            String xInit = matcherX.group(1);
            if (matcherY.find()) {
                String yInit = matcherY.group(1);
                if (matcherH.find()) {
                    String headingInit = matcherH.group(1);
                    woPostion = new WoPostion("0",xInit,yInit,headingInit,1);
                }
            }
        }
        return woPostion;
    }

    private WoPostion takeEndPosition(String comment) {
        String[] x_target = extractCoordinates(comment, "x_target");
        String[] y_target = extractCoordinates(comment, "y_target");

        // 查找匹配
        if (x_target[0] != null) {

            WoPostion woPostion = new WoPostion();
            // 输出结果
            woPostion.setXTarget(x_target[0] + ", " + x_target[1]);
            woPostion.setYTarget(y_target[0] + ", " + y_target[1]);
            return woPostion;
        } else {
            System.out.println("未找到匹配的数据。");
        }
        return null;
    }

    private static String[] extractCoordinates(String input, String target) {
        String pattern =  target + " = \\((-?\\d+\\.\\d+), (-?\\d+\\.\\d+)\\)";
        java.util.regex.Pattern regex = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher matcher = regex.matcher(input);

        String[] coordinates = new String[2];

        if (matcher.find()) {
            coordinates[0] = matcher.group(1);
            coordinates[1] = matcher.group(2);
        } else {
            System.err.println("No match found for " + target);
        }

        return coordinates;
    }


    public List<CdShape> cdParseXML(String xocsPath) {
        File file = new File(xocsPath);
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(OpenScenario.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            OpenScenario openScenario = (OpenScenario) jaxbUnmarshaller.unmarshal(file);
            HashMap<String, CdShape> woPostionMap = new LinkedHashMap<>(20);
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
                CdShape cdShape = new CdShape();
                cdShape.setId(enity);
                List<WoPostion> woPostions = new ArrayList<>();
                for (Vertex vertex : polyline.getVertex()){
                    WorldPosition worldPosition = vertex.getPosition().getWorldPosition();
                    Double time = Double.parseDouble(vertex.getTime()) - mintime;
                    WoPostion woPostion = new WoPostion(enity,worldPosition.getX(), worldPosition.getY(), worldPosition.getH(), taketype(name));
                    woPostion.setTime(String.format("%.1f", time));
                    woPostions.add(woPostion);
                }
                cdShape.setWoPostionList(woPostions);
                woPostionMap.put(enity,cdShape);
            }
            List<CdShape> cdShapes = new ArrayList<>();
            //主车
            List<WoPostion> mainPostions = new ArrayList<>();
            List<WoPostion> mainPostion = parseCommentsFromXML(xocsPath);
            for (WoPostion woPostion : mainPostion){
                if (woPostion.getX()!=null){
                    woPostion.setId("A0");
                    woPostion.setTime("0");
                    mainPostions.add(woPostion);
                }else {
                    SceneLibMap.putEnd(xocsPath,woPostion);
                }
            }
            CdShape mainshape = new CdShape();
            mainshape.setWoPostionList(mainPostions);
            mainshape.setId("A0");
            cdShapes.add(mainshape);

            for (String key : woPostionMap.keySet()){
                cdShapes.add(woPostionMap.get(key));
            }

            return cdShapes;

        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
