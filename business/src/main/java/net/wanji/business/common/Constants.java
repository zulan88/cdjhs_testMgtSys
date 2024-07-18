package net.wanji.business.common;

import net.wanji.common.core.domain.SimpleSelect;
import net.wanji.common.utils.StringUtils;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.weekend.Fn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: guanyuduo
 * @Date: 2023/6/28 10:32
 * @Descriptoin:
 */

public interface Constants {

    class RedisMessageType {
        public static final String START = "start";
        public static final String SCORE = "score";
        public static final String OPTIMIZE = "optimize";
        public static final String TRAJECTORY = "trajectory";
        public static final String END = "end";
        public static final String SHARDING = "sharding";
    }

    class MethodType {
        public static final int REAL = 1;
        public static final int TASK = 2;
    }

    /**
     * 默认标签
     */
    class DefaultLabel {
        public static final String straightway = "直道";
        public static final String goStraight = "直行";
        public static final String vehicles = "机动车";

        public static List<String> getDefaultLabel() {
            return Arrays.asList(straightway, goStraight, vehicles);
        }
    }

    /**
     * 测试模式
     */
    class TestMode {
        /**
         * 单用例测试（实车试验）
         */
        public static final int CASE_TEST = 0;
        /**
         * 连续性场景测试（多场景任务一次启停）
         */
        public static final int CONTINUOUS_TEST = 1;
        /**
         * 批量测试（n场景任务n次启停）
         */
        public static final int BATCH_TEST = 2;
        /**
         * 无线里程
         */
        public static final int INFINITY_TEST = 3;
    }

    /**
     * 内容模板
     */
    class ContentTemplate {
        public static final String SIMULATION_KEY_TEMPLATE = "{}_{}_{}";
        public static final String REAL_KEY_TEMPLATE = "{}_{}_{}_{}";
        public static final String EXPORT_NAME_TEMPLATE = "{}_{}";
        public static final String SCENE_NUMBER_TEMPLATE = "SC{}{}";

        public static final String INFINTE_NUMBER_TEMPLATE = "IM{}{}";
        public static final String CASE_NUMBER_TEMPLATE = "CASE{}{}";
        public static final String ONSITE_NUMBER_TEMPLATE = "ONST{}{}";
        public static final String TASK_NUMBER_TEMPLATE = "TA{}{}";
        public static final String SCENE_NAME_TEMPLATE = "{}_{}";
        public static final String DEVICE_OFFLINE_TEMPLATE = "设备{}离线；";
        public static final String DEVICE_POS_ERROR_TEMPLATE = "{}未达到规定位置；";
        public static final String SCENE_FORM_TEMPLATE = "该场景包含 AV车数量：{}；仿真车数量：{}；行人数量：{}；仿真频率：10HZ；";
        public static final String CASE_ROLE_TEMPLATE = "{}*{} ";
        public static final String CASE_ROLE_DEVICE_TEMPLATE = "{}-{}*{} ";
    }

    class ChannelBuilder {
        /**
         * 实车试验状态通道模板：用户名_任务ID_用例ID
         */
        public static final String TESTING_CHANNEL_TEMPLATE = "{}_{}_{}_{}_{}";
        public static final String SIMULATION_CHANNEL_TEMPLATE = "{}_{}_{}";
        public static final String CDJHS_EVALUATION_KAFKA_TOPIC_TEMPLATE = "CDJHS_EVALUATION_TASK_{}";

        public static final String CDJHS_TESS_DATA_TOPIC_TEMPLATE = "cdjhs_tess_{}";
        public static final int SCENE_PREVIEW = 1;
        public static final int SIMULATION = 2;
        public static final int REAL = 3;
        public static final int PLAN = 4;
        public static final int TASK = 5;
        public static final int TASK_PREVIEW = 6;
        public static final int TESTING_PREVIEW = 7;
        public static final int INFINITE_SIMULATION = 8;
        public static final int WS_PLAYBACK = 9;
        public static final int ONLINE_TASK_PLAYBACK = 10;
        public static final String STATUS_SUFFIX = "status";
        public static final String CONTROL_SUFFIX = "control";
        public static final String DATA_SUFFIX = "data";
        public static final String EVALUATE_SUFFIX = "evaluate";
        public static final String DEFAULT_STATUS_CHANNEL = "STATUSResult";
        public static final String MIXED_SUFFIX = "mixed";

        public static boolean validClientType(Integer clientType) {
            return
                !ObjectUtils.isEmpty(clientType) && (clientType == SCENE_PREVIEW
                    || clientType == SIMULATION || clientType == REAL
                    || clientType == PLAN || clientType == TASK
                    || clientType == TASK_PREVIEW
                    || clientType == TESTING_PREVIEW
                    || clientType == INFINITE_SIMULATION
                    || clientType == WS_PLAYBACK
                    || clientType == ONLINE_TASK_PLAYBACK);
        }


        /**
         * 创建场景预览使用的channel
         * -- 用户名_场景详情ID_1
         * @param username
         * @param sceneDetailId
         * @return
         */
        public static String buildScenePreviewChannel(String username, Integer sceneDetailId) {
            return StringUtils.format(SIMULATION_CHANNEL_TEMPLATE, username, sceneDetailId, SCENE_PREVIEW);
        }


        /**
         * 创建任务回放使用的channel
         * -- 用户名_任务ID_6
         * @param username
         * @param taskId
         * @return
         */
        public static String buildTaskPreviewChannel(String username, Integer taskId, Integer caseId) {
            return ObjectUtils.isEmpty(caseId)
                ? StringUtils.format("{}_{}_{}", username, taskId, TASK_PREVIEW)
                : StringUtils.format("{}_{}_{}_{}", username, taskId, caseId, TASK_PREVIEW);
        }



        /**
         * 创建实车试验回放使用的channel
         * -- 用户名_任务ID_7
         * @param username
         * @param caseRecordId
         * @return
         */
        public static String buildTestingPreviewChannel(String username, Integer caseRecordId) {
            return StringUtils.format("{}_{}_{}", username, caseRecordId, TESTING_PREVIEW);
        }


        /**
         * 创建仿真验证时使用的channel
         * -- 用户名_场景详情ID_2
         * @param username
         * @param sceneNumber
         * @return
         */
        public static String buildSimulationChannel(String username, String sceneNumber) {
            return StringUtils.format(SIMULATION_CHANNEL_TEMPLATE, username, sceneNumber, SIMULATION);
        }

        /**
         * 创建仿真验证时使用的channel
         * -- 用户名_场景详情ID_8
         * @param username
         * @param sceneNumber
         * @return
         */
        public static String buildInfiniteSimulationChannel(String username, String sceneNumber) {
            return StringUtils.format(SIMULATION_CHANNEL_TEMPLATE, username, sceneNumber, INFINITE_SIMULATION);
        }

        /**
         * 历史回放channel
         * 会有冲突需要ws请求添加taskId,caseId
         * @param id recordId
         * @return
         */
        public static String buildWebSocketPlaybackChannel(String id){
            return "WS_" + id;
        }

        /**
         * 创建路径规划时使用的channel
         * -- 用户名_任务ID_4
         * @param username
         * @param taskId
         * @return
         */
        public static String buildRoutingPlanChannel(String username, Integer taskId) {
            return StringUtils.format(SIMULATION_CHANNEL_TEMPLATE, username, taskId, PLAN);
        }

        /**
         * 创建实车试验状态channel
         * -- 用户名_任务ID(0)_用例ID_3_status
         * @param username
         * @param caseId
         * @return
         */
        public static String buildTestingStatusChannel(String username, Integer caseId) {
            return StringUtils.format(TESTING_CHANNEL_TEMPLATE, username, 0, caseId, REAL, STATUS_SUFFIX);
        }

        /**
         * 创建任务测试状态channel
         * -- 用户名_任务ID_用例ID(0)_5_status
         * @param username
         * @param taskId
         * @return
         */
        public static String buildTaskStatusChannel(String username, Integer taskId) {
            return StringUtils.format(TESTING_CHANNEL_TEMPLATE,  username, taskId, 0, TASK, STATUS_SUFFIX);
        }


        /**
         * 创建实车试验控制channel
         * -- 用户名_任务ID(0)_用例ID_3_control
         * @param username
         * @param caseId
         * @return
         */
        public static String buildTestingControlChannel(String username, Integer caseId) {
            return StringUtils.format(TESTING_CHANNEL_TEMPLATE, username, 0, caseId, REAL, CONTROL_SUFFIX);
        }

        /**
         * 创建任务测试控制channel
         * -- 用户名_任务ID_用例ID(0)_5_control
         * @param username
         * @param taskId
         * @return
         */
        public static String buildTaskControlChannel(String username, Integer taskId) {
            return StringUtils.format(TESTING_CHANNEL_TEMPLATE, username, taskId, 0, TASK, CONTROL_SUFFIX);
        }


        /**
         * 创建实车试验数据channel
         * -- 用户名_任务ID(0)_用例ID_3_data
         * @param username
         * @param caseId
         * @return
         */
        public static String buildTestingDataChannel(String username, Integer caseId) {
            return StringUtils.format(TESTING_CHANNEL_TEMPLATE, username, 0, caseId, REAL, DATA_SUFFIX);
        }

        /**
         * 创建任务测试数据channel
         * -- 用户名_任务ID_用例ID(0)_5_data
         * @param username
         * @param taskId
         * @return
         */
        public static String buildTaskDataChannel(String username, Integer taskId) {
            return StringUtils.format(TESTING_CHANNEL_TEMPLATE, username, taskId, 0, TASK, DATA_SUFFIX);
        }

        /**
         * 创建实车试验指标channel
         * -- 用户名_任务ID(0)_用例ID_3_evaluate
         * @param username
         * @param caseId
         * @return
         */
        public static String buildTestingEvaluateChannel(String username, Integer caseId) {
            return StringUtils.format(TESTING_CHANNEL_TEMPLATE, username, 0, caseId, REAL, EVALUATE_SUFFIX);
        }

        /**
         * 创建任务测试指标channel
         * -- 用户名_任务ID_用例ID(0)_5_evaluate
         * @param username
         * @param taskId
         * @return
         */
        public static String buildTaskEvaluateChannel(String username, Integer taskId) {
            return StringUtils.format(TESTING_CHANNEL_TEMPLATE, username, taskId, 0, TASK, EVALUATE_SUFFIX);
        }

        /**
         * 创建济达评分kafkaTopic
         * @param taskId
         * @return
         */
        public static String buildTaskEvaluationKafkaTopic(Long taskId){
            return StringUtils.format(CDJHS_EVALUATION_KAFKA_TOPIC_TEMPLATE, taskId);
        }

        public static String buildOnlineTaskPlaybackChannel(String username, Integer taskId) {
            return StringUtils.format(TESTING_CHANNEL_TEMPLATE, username, taskId, 0, ONLINE_TASK_PLAYBACK, DATA_SUFFIX);
        }
    }

    class FileExtension {
        public static final String XODR = "xodr";
        public static final String GEO_JSON = "json";
        public static final String ZIP = "zip";
    }

    class GeoJsonType {
        public static final String COMPRESSED = "compressed";
        public static final String LINES = "lines";
    }

    /**
     * 使用状态
     */
    class UsingStatus {
        public static final int ENABLE = 0;
        public static final int DISABLE = 1;
    }

    /**
     * 是否
     */
    class YN {
        public static final String Y = "Y";
        public static final String N = "N";
        public static final int Y_INT = 1;
        public static final int N_INT = 0;
    }

    /**
     * 公共列名
     */
    class ColumnName {
        public static final String ID_COLUMN = "id";
        public static final String STATUS_COLUMN = "status";
        public static final String PARENT_ID_COLUMN = "parent_id";
        public static final String IS_FOLDER_COLUMN = "is_folder";
        public static final String COMPLETED_COLUMN = "completed";
        public static final String NAME_COLUMN = "name";
        public static final String SCENE_ID_COLUMN = "fragmented_scene_id";
        public static final String SCENE_DETAIL_ID_COLUMN = "id";
        public static final String RESOURCES_ID_COLUMN = "resources_id";
        public static final String TYPE_COLUMN = "type";
        public static final String CASE_ID_COLUMN = "case_id";
        public static final String CASE_NUMBER_COLUMN = "case_number";
        public static final String TEST_TYPE_COLUMN = "test_type";
        public static final String DEVICE_ID_COLUMN = "device_id";
        public static final String ATTRIBUTE2_COLUMN = "attribute2";
        public static final String SUPPORT_ROLES_COLUMN = "support_roles";
        public static final String PARTICIPANT_ROLE_COLUMN = "participant_role";
        public static final String DEVICE_TYPE_COLUMN = "device_type";
        public static final String CREATED_DATE_COLUMN = "created_date";
        public static final String CREATE_TIME_COLUMN = "create_time";
        public static final String START_TIME_COLUMN = "start_time";
        public static final String SORT_COLUMN = "sort";
        public static final String TASK_ID = "task_id";
    }

    class TestType {
        /**
         * 虚实融合测试
         */
        public static final String VIRTUAL_REAL_FUSION = "virtualRealFusion";
    }

    class ResourceType {
        /**
         * 集成性大地图
         */
        public static final String INTEGRATION_MAP = "integrationMap";
        public static final String ATOM_MAP = "atomMap";
        public static final String BG_TRAFFIC_FLOW = "bgTrafficFlow";
        public static final String MAIN = "main";
        public static final String FACILITIES = "facilities";
    }

    /**
     * 系统字典
     */
    class SysType {
        /**
         * 场景树类型
         */
        public static final String SCENE_TREE_TYPE = "scene_tree_type";

        public static final String SCENE_LIB_TREE = "scenedb_tree_type";
        /**
         * 资源类型
         */
        public static final String RESOURCE_TYPE = "resource_type";
        /**
         * 标签类型
         */
        public static final String LABEL_TYPE = "label_type";
        /**
         * 车道朝向类型
         */
        public static final String ROAD_WAY_TYPE = "road_way_type";
        /**
         * 场景类型
         */
        public static final String SCENE_TYPE = "scene_type";
        /**
         * 场景复杂度
         */
        public static final String SCENE_COMPLEXITY = "scene_complexity";
        /**
         * 交通流状态
         */
        public static final String TRAFFIC_FLOW_STATUS = "traffic_flow_status";
        /**
         * 道路类型
         */
        public static final String ROAD_TYPE = "road_type";
        /**
         * 天气
         */
        public static final String WEATHER = "weather";
        /**
         * 路面状况
         */
        public static final String ROAD_CONDITION = "road_condition";
        /**
         * 参与者角色
         */
        public static final String PART_ROLE = "part_role";
        /**
         * 参与者类型
         */
        public static final String PART_TYPE = "part_type";
        /**
         * 设备类型
         */
        public static final String DEVICE_TYPE = "device_type";
        /**
         * 测试类型
         */
        public static final String TEST_TYPE = "test_type";
        /**
         * 用例状态
         */
        public static final String CASE_STATUS = "case_status";
    }


    /**
     * 参与者类型
     */
    class PartRole {
        /**
         * AV车
         */
        public static final String AV = "av";
        /**
         * AV-从车
         */
        public static final String AV_SLAVE = "avSlave";
        /**
         * MV-远程驾驶车
         */
        public static final String MV_REAL = "mvReal";
        /**
         * MV-虚拟驾驶车
         */
        public static final String MV_VIRTUAL = "mvVirtual";
        /**
         * MV-云控寻迹车
         */
        public static final String MV_TRACKING = "mvTracking";
        /**
         * SV-云控寻迹车
         */
        public static final String SV_TRACKING = "svTracking";
        /**
         * SV-仿真车
         */
        public static final String MV_SIMULATION = "mvSimulation";
        /**
         * SP-行人
         */
        public static final String SP = "sp";

        /**
         * CAVE-行人
         */
        public static final String CAVE = "cave";
    }


    /**
     * 场景类型
     */
    class PartType {
        /**
         * 主车
         */
        public static final String MAIN = "main";
        /**
         * 从车
         */
        public static final String SLAVE = "slave";
        /**
         * 行人
         */
        public static final String PEDESTRIAN = "pedestrian";
        /**
         * 障碍物
         */
        public static final String OBSTACLE = "obstacle";
    }

    class Extension {
        public static final String TXT = "txt";
    }

    class PlaybackAction {
        public static final int CALL = 0;
        public static final int START = 1;
        public static final int SUSPEND = 2;
        public static final int CONTINUE = 3;
        public static final int STOP = 4;
    }

    class TaskProcessNode {
        public static final int TASK_INFO = 1;
        public static final int SELECT_CASE = 2;
        public static final int CONFIG = 3;
        public static final int VIEW_PLAN = 4;
        public static final int WAIT_TEST = 5;
        public static final int FINISHED = 6;
    }

    /**
     * 被测车辆测试方式
     */
    class AvTestMethod {
        /**
         * 连续性测试（规划路径）
         */
        public static final String CONTINUITY = "1";
        /**
         * 自动化测试
         */
        public static final String AUTO = "2";
    }

    //连续式主车轨迹点的类型
    class MainTraceType {

        public static final int START = 0;

        public static final int END = 1;

        public static final int PASSWAY = 2;

        public static final int NECESSARY = 3;

    }

    class MasterContext{
        public static String RECORD_ID = "recordId";
        public static String USERNAME = "user";
        public static String TASK_CHAIN_NUMBER = "taskChainNumber";
    }

    class TessngInteraction{
        public static String RECORD_ID = "recordId";
        /**
         * 车辆评价信息
         */
        public static String EVALUATION_INFOS = "evaluationInfos";
        /**
         * 需要tessng过滤附近背景车的设备信息
         */
        public static String NEARBY_DATA_CHANNEL = "nearbyDataChannel";
        /**
         * 设备接收过滤背景车辆信息频道后缀
         */
        public static String NEARBY_DATA_CHANNEL_SUFFIX = "_nearby";

    }

    class UserInfo {
        public static String ADMIN_NAME = "admin";
    }


    /**
     * 试验记录状态
     */
    enum TestingStatusEnum {
        NO_PASS(1, "未通过"),
        PASS(2, "通过");

        private Integer code;

        private String value;

        TestingStatusEnum(Integer code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public Integer getCode() {
            return code;
        }

        public static String getValueByCode(Integer code) {
            if (ObjectUtils.isEmpty(code)) {
                return "-";
            }
            for (TestingStatusEnum testingStatusEnum : values()) {
                if (testingStatusEnum.getCode().equals(code)) {
                    return testingStatusEnum.getValue();
                }
            }
            return "-";
        }
    }

    /**
     * 任务状态
     */
    enum TaskStatusEnum {
        NO_SUBMIT("save", "待提交"),
        WAITING("waiting", "待测试"),
        RUNNING("running", "进行中"),
        FINISHED("finished", "已完成"),
        PAST_DUE("past_due", "已逾期");

        private String code;

        private String value;

        TaskStatusEnum(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getCode() {
            return code;
        }

        public static String getValueByCode(String code) {
            for (TaskStatusEnum taskStatusEnum : values()) {
                if (taskStatusEnum.getCode().equals(code)) {
                    return taskStatusEnum.getValue();
                }
            }
            return "-";
        }

        public static List<SimpleSelect> getSelectList() {
            List<SimpleSelect> list = new ArrayList<>();
            for (TaskStatusEnum taskStatusEnum : values()) {
                if (taskStatusEnum.getCode().equals(TaskStatusEnum.NO_SUBMIT.getCode())) {
                    continue;
                }
                SimpleSelect simpleSelect = new SimpleSelect();
                simpleSelect.setDictValue(taskStatusEnum.getCode());
                simpleSelect.setDictLabel(taskStatusEnum.getValue());
                list.add(simpleSelect);
            }
            return list;
        }

        public static List<String> getPageCountList() {
            List<String> result = new ArrayList<>();
            result.add(TaskStatusEnum.WAITING.getCode());
            result.add(TaskStatusEnum.RUNNING.getCode());
            result.add(TaskStatusEnum.PAST_DUE.getCode());
            return result;
        }
    }

    /**
     * 任务用例测试状态
     */
    enum TaskCaseStatusEnum {
        WAITING("waiting", "待测试"),
        PREPARING("preparing", "准备中"),
        RUNNING("running", "测试中"),
        FINISHED("finished", "已完成");

        private String code;

        private String value;

        TaskCaseStatusEnum(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public String getCode() {
            return code;
        }

        public static String getValueByCode(String code) {
            for (TaskCaseStatusEnum taskCaseStatusEnum : values()) {
                if (taskCaseStatusEnum.getCode().equals(code)) {
                    return taskCaseStatusEnum.getValue();
                }
            }
            return "-";
        }
    }

    /**
     * 场景车道方向
     */
    enum LanePointEnum {
        ONE_WAY("oneWay", "单向"),
        TWO_WAY("twoWay", "双向");

        private String value;

        private String label;

        LanePointEnum(String value, String label) {
            this.value = value;
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public String getLabel() {
            return label;
        }

        public static List<Map<String, String>> buildSelect() {
            return Arrays.stream(values()).map(item -> {
                Map<String, String> map = new HashMap<>(2);
                map.put("dictValue", item.value);
                map.put("dictLabel", item.label);
                return map;
            }).collect(Collectors.toList());
        }
    }

    /**
     * 用例状态枚举
     */
    enum PointTypeEnum {
        START("start", "开始"),
        PATH_WAY("pathway", "途径点"),
        CONFLICT("conflict", "冲突点"),
        END("end", "结束点"),;

        private String pointType;

        private String desc;

        PointTypeEnum(String pointType, String desc) {
            this.pointType = pointType;
            this.desc = desc;
        }

        public String getPointType() {
            return pointType;
        }

        public String getDesc() {
            return desc;
        }

        public static String getDescByPointType(String pointType) {
            for (PointTypeEnum pointTypeEnum : values()) {
                if (pointType.equals(pointTypeEnum.getPointType())) {
                    return pointTypeEnum.getDesc();
                }
            }
            return "";
        }
    }

    /**
     * 用例状态枚举
     */
    enum CaseStatusEnum {
        WAIT_CONFIG("wait_config", "待配置"),
        WAIT_TEST("wait_test", "待试验"),
        INVALID("invalid", "无效"),
        EFFECTIVE("effective", "有效");

        private String code;

        private String desc;

        CaseStatusEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getDescByCode(String code) {
            for (CaseStatusEnum caseStatusEnum : values()) {
                if (code.equals(caseStatusEnum.getCode())) {
                    return caseStatusEnum.getDesc();
                }
            }
            return "";
        }
    }



    /**
     * 用例状态枚举
     */
    enum ModelEnum {
        CAR(1, "小客车"),
        TRUCK(2, "大货车"),
        BUS(3, "大巴车"),
        PEDESTRIAN(4, "行人"),
        BIKE(5, "自行车");

        private Integer model;

        private String modelName;

        ModelEnum(Integer model, String modelName) {
            this.model = model;
            this.modelName = modelName;
        }

        public Integer getModel() {
            return model;
        }

        public String getModelName() {
            return modelName;
        }

        public static String getDescByCode(Integer code) {
            if (ObjectUtils.isEmpty(code)) {
                return null;
            }
            for (ModelEnum modelEnum : values()) {
                if (code.equals(modelEnum.getModel())) {
                    return modelEnum.getModelName();
                }
            }
            return "";
        }
    }


    /**
     * 设备状态枚举
     */
    enum DeviceStatusEnum {
        OFFLINE(0, "离线"),
        ONLINE(1, "在线");

        private Integer status;

        private String desc;

        DeviceStatusEnum(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public Integer getStatus() {
            return status;
        }

        public String getDesc() {
            return desc;
        }

        public static String getDescByCode(Integer status) {
            for (DeviceStatusEnum deviceStatusEnum : values()) {
                if (status.equals(deviceStatusEnum.getStatus())) {
                    return deviceStatusEnum.getDesc();
                }
            }
            return "";
        }
    }

    enum FileTypeEnum {
        OpenDrive("xodr", "OpenDrive"),
        OpenSCENARIO("xosc", "OpenSCENARIO");

        private String extension;

        private String type;

        FileTypeEnum(String extension, String type) {
            this.extension = extension;
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public String getExtension() {
            return extension;
        }

        public static String getTypeByExt(String extension) {
            for (FileTypeEnum fileTypeEnum : values()) {
                if (extension.equals(fileTypeEnum.getExtension())) {
                    return fileTypeEnum.getType();
                }
            }
            return "";
        }
    }

    /**
     * 添加校验分组
     */
    interface InsertGroup {}
    /**
     * 修改校验分组
     */
    interface UpdateGroup {}
    /**
     * 查询校验分组
     */
    interface QueryGroup {}
    /**
     * 删除校验分组
     */
    interface DeleteGroup {}
    /**
     * 批量校验分组
     */
    interface BatchGroup {}
    /**
     * 其他分组
     */
    interface OtherGroup {}
}
