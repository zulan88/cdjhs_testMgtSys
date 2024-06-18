package net.wanji.openScenario.properties;


import com.alibaba.fastjson.JSONObject;
import net.wanji.common.utils.StringUtils;
import net.wanji.common.utils.file.FileUploadUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/10 11:42
 * @Descriptoin:
 */

public class OpenScenarioProject {

    JSONObject roadNetwork;
    Map<String, JSONObject> entities;
    StoryboardModule storyboard;

    public OpenScenarioProject(String path) {

//        entities = new JSONArray();
//        storyboard = new StoryboardMoudle();
        String file = FileUploadUtils.getAbsolutePathFileName(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            // 地图
            roadNetwork = new JSONObject();
            Element logicFileItem = (Element) doc.getElementsByTagName("LogicFile").item(0);
            String filepath = logicFileItem.getAttribute("filepath");
            roadNetwork.put("LogicFile", filepath);
            System.out.println(StringUtils.format("地图：{}", filepath));

            // 解析ScenarioObject
            entities = new HashMap<>();
            NodeList scenarioObjectList = doc.getElementsByTagName("ScenarioObject");
            System.out.println("参与者信息:");
            for (int i = 0; i < scenarioObjectList.getLength(); i++) {
                JSONObject scenarioObjectProperties = new JSONObject();
                Element scenarioObjectElement = (Element) scenarioObjectList.item(i);
                String name = scenarioObjectElement.getAttribute("name");
                System.out.print(StringUtils.format("\nname: {} ", name));

                Element vehicleElement = (Element) scenarioObjectElement.getElementsByTagName("Vehicle").item(0);
                String vehicleName = vehicleElement.getAttribute("name");
                String vehicleCategory = vehicleElement.getAttribute("vehicleCategory");
                System.out.println(StringUtils.format("车辆 名称:{} 类型:{}", vehicleName, vehicleCategory));
                scenarioObjectProperties.put("vehicleName", vehicleName);
                scenarioObjectProperties.put("vehicleCategory", vehicleCategory);

                Element boundingBoxElement = (Element) vehicleElement.getElementsByTagName("BoundingBox").item(0);
                Element centerElement = (Element) boundingBoxElement.getElementsByTagName("Center").item(0);
                double centerX = Double.parseDouble(centerElement.getAttribute("x"));
                double centerY = Double.parseDouble(centerElement.getAttribute("y"));
                double centerZ = Double.parseDouble(centerElement.getAttribute("z"));
                System.out.println(StringUtils.format("包围盒 中心点坐标 x:{} y:{} z:{}", centerX, centerY, centerZ));
                JSONObject boundingBox = new JSONObject();
                boundingBox.put("centerX", centerX);
                boundingBox.put("centerY", centerY);
                boundingBox.put("centerZ", centerZ);

                Element dimensionsElement = (Element) boundingBoxElement.getElementsByTagName("Dimensions").item(0);
                double width = Double.parseDouble(dimensionsElement.getAttribute("width"));
                double length = Double.parseDouble(dimensionsElement.getAttribute("length"));
                double height = Double.parseDouble(dimensionsElement.getAttribute("height"));
                System.out.println(StringUtils.format("尺寸 宽:{} 长:{} 高:{}", width, length, height));
                boundingBox.put("dimensionsWidth", width);
                boundingBox.put("dimensionsLength", length);
                boundingBox.put("dimensionsHeight", height);
                scenarioObjectProperties.put("boundingBox", boundingBox);

                Element performanceElement = (Element) vehicleElement.getElementsByTagName("Performance").item(0);
                double maxSpeed = Double.parseDouble(performanceElement.getAttribute("maxSpeed"));
                double maxAcceleration = Double.parseDouble(performanceElement.getAttribute("maxAcceleration"));
                double maxDeceleration = Double.parseDouble(performanceElement.getAttribute("maxDeceleration"));
                System.out.println(StringUtils.format("性能参数 最大速度:{}m/s 最大加速度:{}m/s2 最大减速度:{}m/s2",
                        maxSpeed, maxAcceleration, maxDeceleration));
                scenarioObjectProperties.put("maxSpeed", maxSpeed);
                scenarioObjectProperties.put("maxAcceleration", maxAcceleration);
                scenarioObjectProperties.put("maxDeceleration", maxDeceleration);

                JSONObject axles = new JSONObject();
                Element axlesElement = (Element) vehicleElement.getElementsByTagName("Axles").item(0);
                Element frontAxleElement = (Element) axlesElement.getElementsByTagName("FrontAxle").item(0);
                double frontMaxSteering = Double.parseDouble(frontAxleElement.getAttribute("maxSteering"));
                double frontWheelDiameter = Double.parseDouble(frontAxleElement.getAttribute("wheelDiameter"));
                double frontTrackWidth = Double.parseDouble(frontAxleElement.getAttribute("trackWidth"));
                double frontPositionX = Double.parseDouble(frontAxleElement.getAttribute("positionX"));
                double frontPositionZ = Double.parseDouble(frontAxleElement.getAttribute("positionZ"));
                System.out.println(StringUtils.format("前轮最大转向角度{}弧度 直径{}米 轮距{}米 前轴相对于车辆的x方向位置为{}米 前轴相对于车辆的z方向位置为{}米",
                        frontMaxSteering, frontWheelDiameter, frontTrackWidth, frontPositionX, frontPositionZ));
                axles.put("frontMaxSteering", frontMaxSteering);
                axles.put("frontWheelDiameter", frontWheelDiameter);
                axles.put("frontTrackWidth", frontTrackWidth);
                axles.put("frontPositionX", frontPositionX);
                axles.put("frontPositionZ", frontPositionZ);

                Element rearAxleElement = (Element) axlesElement.getElementsByTagName("RearAxle").item(0);
                double rearMaxSteering = Double.parseDouble(rearAxleElement.getAttribute("maxSteering"));
                double rearWheelDiameter = Double.parseDouble(rearAxleElement.getAttribute("wheelDiameter"));
                double rearTrackWidth = Double.parseDouble(rearAxleElement.getAttribute("trackWidth"));
                double rearPositionX = Double.parseDouble(rearAxleElement.getAttribute("positionX"));
                double rearPositionZ = Double.parseDouble(rearAxleElement.getAttribute("positionZ"));
                System.out.println(StringUtils.format("后轮最大转向角度{}弧度 直径{}米 轮距{}米 后轴相对于车辆的x方向位置为{}米 前轴相对于车辆的z方向位置为{}米",
                        rearMaxSteering, rearWheelDiameter, rearTrackWidth, rearPositionX, rearPositionZ));
                axles.put("rearMaxSteering", rearMaxSteering);
                axles.put("rearWheelDiameter", rearWheelDiameter);
                axles.put("rearTrackWidth", rearTrackWidth);
                axles.put("rearPositionX", rearPositionX);
                axles.put("rearPositionZ", rearPositionZ);
                scenarioObjectProperties.put("axlex", axles);


                entities.put(name, scenarioObjectProperties);
            }

            // 场景故事
            this.storyboard = new StoryboardModule(doc);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        System.out.println(JSONObject.toJSONString(this));
    }


    public JSONObject getRoadNetwork() {
        return roadNetwork;
    }

    public void setRoadNetwork(JSONObject roadNetwork) {
        this.roadNetwork = roadNetwork;
    }

    public Map<String, JSONObject> getEntities() {
        return entities;
    }

    public void setEntities(Map<String, JSONObject> entities) {
        this.entities = entities;
    }

    public StoryboardModule getStoryboard() {
        return storyboard;
    }

    public void setStoryboard(StoryboardModule storyboard) {
        this.storyboard = storyboard;
    }

    public Object before() {
        // todo 获取参与者信息
        return null;
    }

    public Object running(int time) {
        // todo 根据帧数获取位置信息

        return null;
    }

    public Object isEnd() {
        // todo 根据stoptrigger判断
        return false;
    }

}
