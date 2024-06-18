package net.wanji.openScenario.properties;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.wanji.common.utils.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.DecimalFormat;

/**
 * @Auther: guanyuduo
 * @Date: 2023/9/10 11:49
 * @Descriptoin:
 */

public class StoryboardModule {
    JSONObject init;
    JSONObject story;
    JSONObject stopTrigger;

    public StoryboardModule(Document doc) {
        DecimalFormat timeFormat = new DecimalFormat("0.0");
        DecimalFormat speedFormat = new DecimalFormat("0.00");
        DecimalFormat posFormat = new DecimalFormat("0.000000");

        Element storyboard = (Element) doc.getElementsByTagName("Storyboard").item(0);
        System.out.println("Storyboard:");
        // 解析Init部分
        Element init = (Element) storyboard.getElementsByTagName("Init").item(0);
        System.out.println("  Init:");

        // 解析Actions部分
        Element actions = (Element) init.getElementsByTagName("Actions").item(0);
        System.out.println("    Actions:");

        this.init = new JSONObject();
        // 解析GlobalAction部分
        NodeList globalActionList = actions.getElementsByTagName("GlobalAction");
        if (globalActionList.getLength() > 0) {
            Element globalAction = (Element) globalActionList.item(0);
            System.out.println("      GlobalAction:");

            // 解析EnvironmentAction部分
            NodeList environmentActionList = globalAction.getElementsByTagName("EnvironmentAction");
            if (environmentActionList.getLength() > 0) {
                Element environmentAction = (Element) environmentActionList.item(0);
                System.out.println("        EnvironmentAction:");

                // 解析TimeOfDay部分
                NodeList timeOfDayList = environmentAction.getElementsByTagName("TimeOfDay");
                if (timeOfDayList.getLength() > 0) {
                    Element timeOfDay = (Element) timeOfDayList.item(0);
                    String dateTime = timeOfDay.getAttribute("dateTime");
                    System.out.println("          TimeOfDay: " + dateTime);
                    this.init.put("dateTime", dateTime);
                }

                // 解析Weather部分
                NodeList weatherList = environmentAction.getElementsByTagName("Weather");
                if (weatherList.getLength() > 0) {
                    Element weather = (Element) weatherList.item(0);
                    String cloudState = weather.getAttribute("cloudState");
                    // 解析Sun部分
                    NodeList sunList = weather.getElementsByTagName("Sun");
                    if (sunList.getLength() > 0) {
                        Element sun = (Element) sunList.item(0);
                        String intensity = sun.getAttribute("intensity");
                        String azimuth = sun.getAttribute("azimuth");
                        String elevation = sun.getAttribute("elevation");
                    }
                    // 解析Fog部分
                    NodeList fogList = weather.getElementsByTagName("Fog");
                    if (fogList.getLength() > 0) {
                        Element fog = (Element) fogList.item(0);
                        String visualRange = fog.getAttribute("visualRange");
                    }
                    // 解析Precipitation部分
                    NodeList precipitationList = weather.getElementsByTagName("Precipitation");
                    if (precipitationList.getLength() > 0) {
                        Element precipitation = (Element) precipitationList.item(0);
                        String precipitationType = precipitation.getAttribute("precipitationType");
                        String intensity = precipitation.getAttribute("intensity");
                    }
                }

                // 解析RoadCondition部分
                NodeList roadConditionList = environmentAction.getElementsByTagName("RoadCondition");
                if (roadConditionList.getLength() > 0) {
                    Element roadCondition = (Element) roadConditionList.item(0);
                    String frictionScaleFactor = roadCondition.getAttribute("frictionScaleFactor");
                }
            }
        }

        // 解析private部分
        NodeList privateList = actions.getElementsByTagName("Private");
        JSONArray privateArray = new JSONArray();
        for (int i = 0; i < privateList.getLength(); i++) {
            JSONObject privateItem = new JSONObject();
            Element privateElement = (Element) privateList.item(i);
            String entityRef = privateElement.getAttribute("entityRef");
            privateItem.put("ref", entityRef);
            // 解析注释
            Node commentNode = privateElement.getPreviousSibling();
            short nodeType = commentNode.getNodeType();
            String nodeValue = commentNode.getNodeValue().trim();
            if (commentNode != null && commentNode.getNodeType() == Node.COMMENT_NODE) {
                String comment = commentNode.getNodeValue().trim();
                // 解析注释中的内容
                if (comment.startsWith("[Initial State]")) {
                    // TODO: 解析初始状态信息
                    System.out.println(StringUtils.format("初始状态：{}", comment));
                    privateItem.put("initialState", comment);
                } else if (comment.startsWith("[Driving Task]")) {
                    // TODO: 解析驾驶任务信息
                    System.out.println(StringUtils.format("驾驶任务：{}", comment));
                    privateItem.put("drivingTask", comment);
                }
            }

            // 解析 <PrivateAction> 元素
            NodeList privateActionList = privateElement.getElementsByTagName("PrivateAction");
            JSONObject actionItem = new JSONObject();
            for (int j = 0; j < privateActionList.getLength(); j++) {

                Element privateActionElement = (Element) privateActionList.item(j);

                // 解析 <LongitudinalAction> 元素
                NodeList longitudinalActionList = privateActionElement.getElementsByTagName("LongitudinalAction");
                if (longitudinalActionList.getLength() > 0) {
                    Element longitudinalActionElement = (Element) longitudinalActionList.item(0);

                    // 解析 <SpeedAction> 元素
                    NodeList speedActionList = longitudinalActionElement.getElementsByTagName("SpeedAction");
                    if (speedActionList.getLength() > 0) {
                        Element speedActionElement = (Element) speedActionList.item(0);

                        // 解析 <SpeedActionDynamics> 元素
                        JSONObject speedAction = new JSONObject();
                        NodeList speedActionDynamicsList = speedActionElement.getElementsByTagName("SpeedActionDynamics");
                        if (speedActionDynamicsList.getLength() > 0) {
                            Element speedActionDynamicsElement = (Element) speedActionDynamicsList.item(0);
                            String dynamicsShape = speedActionDynamicsElement.getAttribute("dynamicsShape");
                            String value = speedActionDynamicsElement.getAttribute("value");
                            String dynamicsDimension = speedActionDynamicsElement.getAttribute("dynamicsDimension");
                            // TODO: 处理 SpeedActionDynamics 元素
                            speedAction.put("dynamicsShape", dynamicsShape);
                            speedAction.put("value", value);
                            speedAction.put("dynamicsDimension", dynamicsDimension);
                        }

                        // 解析 <SpeedActionTarget> 元素
                        NodeList speedActionTargetList = speedActionElement.getElementsByTagName("SpeedActionTarget");
                        if (speedActionTargetList.getLength() > 0) {
                            Element speedActionTargetElement = (Element) speedActionTargetList.item(0);

                            // 解析 <AbsoluteTargetSpeed> 元素
                            NodeList absoluteTargetSpeedList = speedActionTargetElement.getElementsByTagName("AbsoluteTargetSpeed");
                            if (absoluteTargetSpeedList.getLength() > 0) {
                                Element absoluteTargetSpeedElement = (Element) absoluteTargetSpeedList.item(0);
                                String value = speedFormat.format(Double.parseDouble(absoluteTargetSpeedElement.getAttribute("value")));
                                // TODO: 处理 AbsoluteTargetSpeed 元素
                                speedAction.put("absoluteTargetSpeed", value);
                            }
                        }
                        actionItem.put("longitudinalSpeedAction", speedAction);
                    }
                }

                // 解析 <TeleportAction> 元素
                NodeList teleportActionList = privateActionElement.getElementsByTagName("TeleportAction");
                if (teleportActionList.getLength() > 0) {
                    Element teleportActionElement = (Element) teleportActionList.item(0);

                    // 解析 <Position> 元素
                    NodeList positionList = teleportActionElement.getElementsByTagName("Position");
                    if (positionList.getLength() > 0) {
                        Element positionElement = (Element) positionList.item(0);

                        // 解析 <WorldPosition> 元素
                        NodeList worldPositionList = positionElement.getElementsByTagName("WorldPosition");
                        if (worldPositionList.getLength() > 0) {
                            JSONObject position = new JSONObject();
                            Element worldPositionElement = (Element) worldPositionList.item(0);
                            String x = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("x")));
                            String y = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("y")));
                            String z = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("z")));
                            String h = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("h")));
                            String p = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("p")));
                            String r = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("r")));
                            // TODO: 处理 WorldPosition 元素
                            position.put("x", x);
                            position.put("y", y);
                            position.put("z", z);
                            position.put("h", h);
                            position.put("p", p);
                            position.put("r", r);
                            actionItem.put("teleportAction", position);
                        }
                    }
                }

            }
            privateItem.put("privateActions", actionItem);
            privateArray.add(privateItem);
        }
        this.init.put("private", privateArray);


        // 解析Story部分
        this.story = new JSONObject();
        Element story = (Element) storyboard.getElementsByTagName("Story").item(0);
        System.out.println("  Story:");
        String name = story.getAttribute("name");
        System.out.println("name:" + name);

        this.story.put("name", name);


        JSONArray actArray = new JSONArray();
        NodeList actList = story.getElementsByTagName("Act");
        for (int i = 0; i < actList.getLength(); i++) {
            JSONObject actItem = new JSONObject();
            Element actElement = (Element) actList.item(i);
            String actName = actElement.getAttribute("name");
            System.out.println(StringUtils.format("Act name", actName));
            actItem.put("name", actName);

            // 获取 ManeuverGroup 元素
            Element maneuverGroupElement = (Element) actElement.getElementsByTagName("ManeuverGroup").item(0);
            String maneuverGroupName = maneuverGroupElement.getAttribute("name");
            System.out.println("ManeuverGroup name: " + maneuverGroupName);

            // 获取 Actors 元素
            Element actorsElement = (Element) maneuverGroupElement.getElementsByTagName("Actors").item(0);
            boolean selectTriggeringEntities = Boolean.parseBoolean(actorsElement.getAttribute("selectTriggeringEntities"));
            System.out.println("Select triggering entities: " + selectTriggeringEntities);

            // 获取 EntityRef 元素
            Element entityRefElement = (Element) actorsElement.getElementsByTagName("EntityRef").item(0);
            String entityRef = entityRefElement.getAttribute("entityRef");
            System.out.println("EntityRef: " + entityRef);
            actItem.put("entityRef", entityRef);

            // 获取 Maneuver 元素
            Element maneuverElement = (Element) maneuverGroupElement.getElementsByTagName("Maneuver").item(0);
            String maneuverName = maneuverElement.getAttribute("name");
            System.out.println("Maneuver name: " + maneuverName);

            // 获取 Event 元素
            Element eventElement = (Element) maneuverElement.getElementsByTagName("Event").item(0);
            String eventName = eventElement.getAttribute("name");
            String priority = eventElement.getAttribute("priority");
            System.out.println("Event name: " + eventName);
            System.out.println("Priority: " + priority);
            actItem.put("eventName", eventName);
            actItem.put("priority", priority);

            // 获取 Action 元素
            Element actionElement = (Element) eventElement.getElementsByTagName("Action").item(0);
            String actionName = actionElement.getAttribute("name");
            System.out.println("Action name: " + actionName);
            actItem.put("actionName", actionName);

            // 获取 Trajectory 元素
            Element trajectoryElement = (Element) actionElement.getElementsByTagName("Trajectory").item(0);
            String trajectoryName = trajectoryElement.getAttribute("name");
            boolean isClosed = Boolean.parseBoolean(trajectoryElement.getAttribute("closed"));
            System.out.println("Trajectory name: " + trajectoryName);
            System.out.println("Is closed: " + isClosed);
            actItem.put("trajectoryName", trajectoryName);

            // 获取 Polyline 元素
            Element polylineElement = (Element) trajectoryElement.getElementsByTagName("Polyline").item(0);

            // 获取所有 Vertex 元素
            JSONArray vertexArray = new JSONArray();
            NodeList vertexList = polylineElement.getElementsByTagName("Vertex");
            for (int j = 0; j < vertexList.getLength(); j++) {
                JSONObject vertexItem = new JSONObject();
                Element vertexElement = (Element) vertexList.item(j);
                String time = timeFormat.format(Double.parseDouble(vertexElement.getAttribute("time")));

                // 获取 Position 元素
                Element positionElement = (Element) vertexElement.getElementsByTagName("Position").item(0);

                // 获取 WorldPosition 元素
                Element worldPositionElement = (Element) positionElement.getElementsByTagName("WorldPosition").item(0);
                String x = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("x")));
                String y = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("y")));
                String z = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("z")));
                String h = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("h")));
                String p = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("p")));
                String r = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("r")));
                System.out.println(net.wanji.common.utils.StringUtils.format("{\"time\":{}, \"x\":{}, \"y\":{}, \"z\":{}, \"h\":{}, \"p\":{}, \"r\":{}}", time, x, y, z, h, p, r));
                vertexItem.put("time", time);
                vertexItem.put("x", x);
                vertexItem.put("y", y);
                vertexItem.put("z", z);
                vertexItem.put("h", h);
                vertexItem.put("p", p);
                vertexItem.put("r", r);
                vertexArray.add(vertexItem);
            }
            actItem.put("polyline", vertexArray);

            // 获取 StartTrigger 元素
            JSONObject startTriggerItem = new JSONObject();
            Element startTriggerElement = (Element) eventElement.getElementsByTagName("StartTrigger").item(0);
            Element startConditionElement = (Element) startTriggerElement.getElementsByTagName("Condition").item(0);
            String delay = startConditionElement.getAttribute("delay");
            String conditionEdge = startConditionElement.getAttribute("conditionEdge");
            startTriggerItem.put("delay", delay);
            startTriggerItem.put("conditionEdge", conditionEdge);
            NodeList byValueConditionList = startConditionElement.getElementsByTagName("ByValueCondition");
            if (byValueConditionList.getLength() > 0) {
                startTriggerItem.put("type", "ByValueCondition");
                Element conditionElement = (Element) byValueConditionList.item(0);
                NodeList simulationTimeCondition = conditionElement.getElementsByTagName("SimulationTimeCondition");
                if (simulationTimeCondition.getLength() > 0) {
                    Element simulationConditionElement = (Element) simulationTimeCondition.item(0);
                    startTriggerItem.put("target", "SimulationTimeCondition");
                    startTriggerItem.put("value", Double.parseDouble(simulationConditionElement.getAttribute("value")));
                    startTriggerItem.put("rule", simulationConditionElement.getAttribute("rule"));
                }
            }
            actItem.put("startTrigger", startTriggerItem);


            actArray.add(actItem);
        }
        this.story.put("acts", actArray);

        this.stopTrigger = new JSONObject();
        // 解析StopTrigger部分
        NodeList stopTriggerList = storyboard.getElementsByTagName("StopTrigger");
        if (stopTriggerList.getLength() > 0) {
            Element stopTrigger = (Element) stopTriggerList.item(0);
            System.out.println("  StopTrigger:");

            // 解析ConditionGroup部分
            NodeList conditionGroupList = stopTrigger.getElementsByTagName("ConditionGroup");
            if (conditionGroupList.getLength() > 0) {
                Element conditionGroup = (Element) conditionGroupList.item(0);
                System.out.println("    ConditionGroup:");

                // 解析Condition部分
                NodeList conditionList = conditionGroup.getElementsByTagName("Condition");
                if (conditionList.getLength() > 0) {
                    Element condition = (Element) conditionList.item(0);
                    System.out.println("      Condition:");

                    this.stopTrigger.put("delay", condition.getAttribute("delay"));
                    this.stopTrigger.put("conditionEdge", condition.getAttribute("conditionEdge"));

                    // 解析ByEntityCondition部分
                    NodeList byEntityConditionList = condition.getElementsByTagName("ByEntityCondition");
                    if (byEntityConditionList.getLength() > 0) {
                        Element byEntityCondition = (Element) byEntityConditionList.item(0);
                        System.out.println("        ByEntityCondition:");
                        this.stopTrigger.put("type", "ByEntityCondition");
                        // 解析TriggeringEntities部分
                        NodeList triggeringEntitiesList = byEntityCondition.getElementsByTagName("TriggeringEntities");
                        if (triggeringEntitiesList.getLength() > 0) {
                            Element triggeringEntities = (Element) triggeringEntitiesList.item(0);
                            String triggeringEntitiesRule = triggeringEntities.getAttribute("TriggeringEntitiesRule");
                            System.out.println("          TriggeringEntities Rule: " + triggeringEntitiesRule);

                            // 解析EntityRef部分
                            NodeList entityRefList = triggeringEntities.getElementsByTagName("EntityRef");
                            for (int i = 0; i < entityRefList.getLength(); i++) {
                                Element entityRef = (Element) entityRefList.item(i);
                                String entityRefValue = entityRef.getAttribute("entityRef");
                                System.out.println("          EntityRef: " + entityRefValue);
                                this.stopTrigger.put("entityRef", entityRefValue);
                            }
                        }

                        // 解析EntityCondition部分
                        NodeList entityConditionList = byEntityCondition.getElementsByTagName("EntityCondition");
                        if (entityConditionList.getLength() > 0) {
                            Element entityCondition = (Element) entityConditionList.item(0);

                            // 解析ReachPositionCondition部分
                            NodeList reachPositionConditionList = entityCondition.getElementsByTagName("ReachPositionCondition");
                            if (reachPositionConditionList.getLength() > 0) {
                                Element reachPositionCondition = (Element) reachPositionConditionList.item(0);
                                String tolerance = reachPositionCondition.getAttribute("tolerance");
                                System.out.println("        ReachPositionCondition Tolerance: " + tolerance);
                                this.stopTrigger.put("target", "ReachPositionCondition");
                                this.stopTrigger.put("tolerance", "tolerance");
                                // 解析Position部分
                                NodeList positionList = reachPositionCondition.getElementsByTagName("Position");
                                if (positionList.getLength() > 0) {
                                    Element positionElement = (Element) positionList.item(0);
                                    NodeList worldPositionList = positionElement.getElementsByTagName("WorldPosition");
                                    if (worldPositionList.getLength() > 0) {
                                        Element worldPositionElement = (Element) worldPositionList.item(0);
                                        String x = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("x")));
                                        String y = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("y")));
                                        String z = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("z")));
                                        String h = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("h")));
                                        String p = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("p")));
                                        String r = posFormat.format(Double.parseDouble(worldPositionElement.getAttribute("r")));
                                        System.out.println("          Position X: " + x);
                                        System.out.println("          Position Y: " + y);
                                        System.out.println("          Position Z: " + z);
                                        System.out.println("          Position H: " + h);
                                        System.out.println("          Position P: " + p);
                                        System.out.println("          Position R: " + r);
                                        JSONObject worldPosition = new JSONObject();
                                        worldPosition.put("x", x);
                                        worldPosition.put("y", y);
                                        worldPosition.put("z", z);
                                        worldPosition.put("h", h);
                                        worldPosition.put("p", p);
                                        worldPosition.put("r", r);
                                        this.stopTrigger.put("position", worldPosition);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }


    }


    public JSONObject getInit() {
        return init;
    }

    public void setInit(JSONObject init) {
        this.init = init;
    }

    public JSONObject getStory() {
        return story;
    }

    public void setStory(JSONObject story) {
        this.story = story;
    }

    public JSONObject getStopTrigger() {
        return stopTrigger;
    }

    public void setStopTrigger(JSONObject stopTrigger) {
        this.stopTrigger = stopTrigger;
    }

}
