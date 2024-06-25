package net.wanji.business.domain.param;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import net.wanji.business.common.Constants.ChannelBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: guanyuduo
 * @date: 2023/12/7 15:23
 * @descriptoin: 与tess交互的参数
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
public class TessParam {

    /**
     * 请求类型
     * 2：仿真验证；3：实车试验；4：路径规划；5：多场景试验; 6: 无限里程仿真运行; 7: 无限里程测试任务
     */
    private Integer simulateType;

    /**
     * 路网编号（simulateType = 2,3,4,5）
     */
    private Integer roadNum;

    private String infiniteId;

    /**
     * 数据通道
     */
    private String dataChannel;

    /**
     * 控制通道（simulateType = 3,5）
     */
    private String commandChannel;

    /**
     * 评价数据通道
     */
    private String evaluateChannel;

    /**
     * 路径规划通道simulateType = 4
     */
    private String routingChannel;

    /**
     * 状态通道（simulateType = 3,5）
     */
    private String statusChannel;

    /**
     * 自定义参数（simulateType = 2,4）
     */
    private Object params;

    private List<String> mapList;

    public TessParam() {
    }

    /**
     * 构建模拟参数对象。（对应请求类型2）
     * 该方法用于配置模拟运行所需的各类参数，包括道路编号、数据通道、额外参数以及地图列表。
     * 配置完成后返回配置好的TessParam对象，以便于进一步操作。
     *
     * @param roadNum 道路编号，用于指定模拟运行的道路。
     * @param dataChannel 数据通道，用于指定模拟过程中数据传输的通道。
     * @param params 其他参数，对应 net.wanji.business.domain.param.TestStartParam。
     * @param mapList 地图id列表，用于指定模拟过程中所需的地图集合。
     * @return 返回配置好的TessParam对象。
     */
    public TessParam buildSimulationParam(Integer roadNum, String dataChannel, Object params, List<String> mapList) {
        this.simulateType = ChannelBuilder.SIMULATION;
        this.roadNum = roadNum;
        this.dataChannel = dataChannel;
        this.params = params;

        this.commandChannel = "1";
        this.statusChannel = "1";
        this.routingChannel = "1";
        this.evaluateChannel = "1";
        this.mapList = mapList;

        return this;
    }

    /**
     * 构建无限模拟参数 （对应请求类型6）
     *
     * @param commandChannel 指定命令通道
     * @param dataChannel 数据传输通道
     * @param params 模拟参数 对应 net.wanji.business.domain.InfiniteTessParm
     * @param mapList 地图id列表
     * @return 返回配置好的TessParam对象
     */
    public TessParam buildnfiniteSimulationParam(String commandChannel, String dataChannel, Object params, List<String> mapList) {
        this.simulateType = 6;
        this.roadNum = 1;
        this.dataChannel = dataChannel;
        this.params = params;

        this.commandChannel = commandChannel;
        this.statusChannel = "1";
        this.routingChannel = "1";
        this.evaluateChannel = "1";
        this.mapList = mapList;
        this.infiniteId = dataChannel;

        return this;
    }

    /**
     * 构建用于真实测试的TessParam参数对象。 （对应请求类型3）
     *
     * @param roadNum 道路编号，用于标识测试的特定道路。
     * @param dataChannel 数据通道，用于接收测试数据。
     * @param commandChannel 命令通道，用于发送控制命令。
     * @param evaluateChannel 评估通道，用于接收测试评估结果。
     * @param statusChannel 状态通道，用于接收测试过程中的状态信息。
     * @param mapList 地图id列表，包含测试中使用的地图信息。
     * @param simulateType 请求类型
     * @param params 自定义参数
     * @return 返回配置好的TessParam对象，可用于后续链式调用。
     */
    public TessParam buildRealTestParam(Integer roadNum, String dataChannel, String commandChannel, String evaluateChannel,
                                   String statusChannel, List<String> mapList, Integer simulateType, Object params) {
        this.simulateType = simulateType;
        this.roadNum = roadNum;
        this.dataChannel = dataChannel;
        this.infiniteId = dataChannel;
        this.commandChannel = commandChannel;
        this.evaluateChannel = evaluateChannel;
        this.statusChannel = statusChannel;

        this.routingChannel = "1";
        this.params = params;
        this.mapList = mapList;
        return this;
    }

    /**
     * 构建路径计划参数 （对应请求类型4）
     *
     * 该方法用于根据提供的道路数量、路由通道和额外参数，构建并初始化路由计划的参数配置。
     *
     * @param roadNum 道路数量，用于路由计划的配置
     * @param routingChannel 路由通道，指定路由计划使用的通道
     * @param params 额外参数 类型为 Map<String, Object>
     * @return 返回配置好的TessParam实例，可用于进一步的设置或执行路由计划
     */
    public TessParam buildRoutingPlanParam(Integer roadNum, String routingChannel, Object params, String mapId) {
        this.simulateType = ChannelBuilder.PLAN;
        this.roadNum = roadNum;
        this.routingChannel = routingChannel;
        this.params = params;
        this.mapList = new ArrayList<>();
        if (mapId == null) {
            this.mapList.add("10");
        }else {
            this.mapList.add(mapId);
        }

        this.dataChannel = "1";
        this.commandChannel = "1";
        this.evaluateChannel = "1";
        this.statusChannel = "1";
        return this;
    }

    /**
     * 构建任务参数对象。 （对应请求类型5）
     * 该方法用于配置TessParam对象，设定任务相关的各种参数，包括道路编号、数据通道、命令通道、评估通道、状态通道以及地图列表。
     *
     * @param roadNum 道路编号，用于标识任务所归属的道路。
     * @param dataChannel 数据通道，用于任务数据的传输。
     * @param commandChannel 命令通道，用于向任务发送命令。
     * @param evaluateChannel 评估通道，用于任务的评估信息传输。
     * @param statusChannel 状态通道，用于任务状态的报告。
     * @param mapList 地图id列表，任务所使用的一系列地图。
     * @return 返回配置好的TessParam对象，允许链式调用。
     */
    public TessParam buildTaskParam(Integer roadNum, String dataChannel, String commandChannel, String evaluateChannel,
                                    String statusChannel, List<String> mapList) {
        this.simulateType = ChannelBuilder.TASK;
        this.roadNum = roadNum;
        this.dataChannel = dataChannel;
        this.commandChannel = commandChannel;
        this.evaluateChannel = evaluateChannel;
        this.statusChannel = statusChannel;

        this.routingChannel = "1";
        this.params = JSONObject.parseObject("{\"params\":{\"params\": []}}");
        this.mapList = mapList;
        return this;
    }

    public TessParam buildTaskStartParam(Integer roadNum, String dataChannel, String commandChannel,
                                    String statusChannel, List<String> mapList) {
        this.simulateType = ChannelBuilder.TASK;
        this.roadNum = roadNum;
        this.dataChannel = dataChannel;
        this.commandChannel = commandChannel;
        this.statusChannel = statusChannel;
        this.routingChannel = "1";
        this.mapList = mapList;
        return this;
    }
}
