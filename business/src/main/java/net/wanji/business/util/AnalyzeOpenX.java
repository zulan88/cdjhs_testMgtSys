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
            return 13;
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

    private WoPostion takeEndPosition(String comment) {
        // 定义正则表达式
        String patternX = "x_target = \\((\\d+\\.\\d+), (\\d+\\.\\d+)\\)";
        String patternY = "y_target = \\((\\d+\\.\\d+), (\\d+\\.\\d+)\\)";

        // 编译正则表达式
        Pattern regexPatternX = Pattern.compile(patternX);
        Pattern regexPatternY = Pattern.compile(patternY);

        // 创建Matcher对象
        Matcher matcherX = regexPatternX.matcher(comment);
        Matcher matcherY = regexPatternY.matcher(comment);

        // 查找匹配
        if (matcherX.find() && matcherY.find()) {
            // 提取x_target的值
            double x1 = Double.parseDouble(matcherX.group(1));
            double x2 = Double.parseDouble(matcherX.group(2));

            // 提取y_target的值
            double y1 = Double.parseDouble(matcherY.group(1));
            double y2 = Double.parseDouble(matcherY.group(2));

            WoPostion woPostion = new WoPostion();
            // 输出结果
            woPostion.setXTarget("x_target = (" + x1 + ", " + x2 + ")");
            woPostion.setYTarget("y_target = (" + y1 + ", " + y2 + ")");
            return woPostion;
        } else {
            System.out.println("未找到匹配的数据。");
        }
        return null;
    }
}
