package net.wanji.common.common;

/**
 * Created by wanji_wangjisen on 2018/4/4.
 */

public class Constants {
    /*激光雷达检测时间*/
    public static final int LADER_TIME_TAG = 3;
    //13帧行人预警
    public static final int DENER_13_TIME_TAG = 1;
    /*协议帧数组的长度*/
    public static final int FRAME_PROTOCOL_LENGTH = 1024 * 8;

    /*最大帧长度*/
    public static final int MAX_FRAME_PROTOCOL_LENGTH = 500;


    /*10帧 自己和周围的车的信息*/
    public static final int CMDT_10 = 16;

    /*红绿灯和绿波引导*/
    public static final int CMDT_11 = 17;

    /*RSI消息*/
    public static final int CMDT_12 = 18;

    /*BSM点云数据*/
    public static final int CMDT_13 = 19;

    /*通讯侦听-RSI*/
    public static final int CMDT_14 = 20;

    /*通讯侦听-SPAT*/
    public static final int CMDT_15 = 21;

    /*通讯侦听-MAP*/
    public static final int CMDT_16 = 22;

    /*运行状态*/
    public static final int CMDT_17 = 23;

    public static final int CMDT_19 = 25;

    /*二期场景 组队*/
    public static final int CMDT_32 = 50;

    /*二期场景 收费*/
    public static final int CMDT_33 = 51;


    /*GPS 周边车辆*/
    public static final int CMDT_B6 = 182;

    /*算法参数 获取数据*/
    public static final int CMDT_F0 = 240;

    /*通讯参数 获取数据*/
    public static final int CMDT_F1 = 241;

    /*模拟CAN 发送数据*/
    public static final int CMDT_F2 = 242;

    /*运行参数 获取数据*/
    public static final int CMDT_F3 = 243;

    /*算法参数 发送数据*/
    public static final int CMDT_E0 = 224;

    /*通讯参数 发送数据*/
    public static final int CMDT_E1 = 225;

    /*运行参数 发送数据*/
    public static final int CMDT_E3 = 227;

    /* 14帧 RSI生命周期 */
    public static final int LIFE_CYCLE_14_RSI = 20;


    /*VAS 主动安全*/
    public static final int CMDT_B1 = 177;


    /*RSA 路况信息*/
    public static final int CMDT_BC = 188;

    /*SPAT 红绿灯信息*/
    public static final int CMDT_BD = 189;

    /* 紧急车辆信息*/
    public static final int CMDT_BE = 190;

    /* 车辆自身信息*/
    public static final int CMDT_D0 = 208;

    /* */
    public static final int CMDT_D1 = 209;

    /* */
    public static final int CMDT_D2 = 210;

    /* 紧急车辆信息*/
    public static final int CMDT_D3 = 211;

    /* 手动刷新时间 */
    public static final int CMDT_D4 = 212;

    /* 紧急车辆信息*/
    public static final int CMDT_D5 = 213;

    /* 紧急车辆信息*/
    public static final int CMDT_D6 = 214;

    /* 紧急车辆信息*/
    public static final int CMDT_D7 = 215;

    /* 紧急车辆信息*/
    public static final int CMDT_D8 = 216;

    /* RSI消息*/
    public static final int CMDT_D9 = 217;


    /* 场景8 场站路径引导服务*/
    public static final int CMDT_E8 = 232;

    /* 场景13 收费*/
    public static final int CMDT_ED = 237;

    public static final int MAP_TIME = 3;

    /*v2v 检测时间*/
    public static final int V2V_TIME_TAG = 1;

    /*v2v 超速播报事件*/
    public static final int V2V_TIME_SPEED_TAG = 5;

    /*SPAT检测时间*/
    public static final int SPAT_TIME_TAG = 3;

    /*D0检测时间*/
    public static final int D0_TIME_TAG = 3;

    /*D12检测时间*/
    public static final int D12_TIME_TAG = 3;

    /*数据检测时间*/
    public static final int DATA_TIME_TAG = 5;

    /* 清除地图icon的时间 */
    public static final int MAP_TIME_CLERN = 3;

    /*  地图marker 车 */
    public static final int MAP_MARKERS_CAR_MINE = 1000;

    public static final int MAP_MARKERS_CAR_YELLOW = 1001;

    //socket连接失败
    public static final int CONNECT_ERROR = 100;

    //rsm 样式
    public static final String RSM_STYLE = "RSM_STYLE";


    //显示关闭红框
    public static final String SHOW_RED_KUANG = "SHOW_RED_KUANG";

    //WiFi测试设置
    public static final String WIFI_SETTING = "WIFI_SETTING";

    //10帧数据测试设置
    public static final String DATA_10_TEST = "10_data_tast";

    //模拟数据
    public static final String SIMULATED_DATA = "simulated_data";

    //记录日志
    public static final String WRITE_LOG = "write_log";

    //设备的density值
    public static final String DENSITY = "density";

    //坐标系
    public static final String ZBX = "ZBC";

    //打开关闭菜单时间
    public static final int OPEN_CLOSE_MENU = 800;
    //打开关闭菜单 菜单旋转时间
    public static final int OPEN_CLOSE_MENU_ROUND = OPEN_CLOSE_MENU / 2;

    // loading 时间
    public static final int LOADING_TIEM = 20;

    public static final int TIEM_33 = 3;

    //菜单背景
    public static final int MENU_BG_H = 115;

    //菜单 车队
    public static final int MENU_CAR_H = -57;

    //经纬度精度
    public static final int LALO_PROGRESS = 10000000;

    //航向角精度
    public static final int TR_PROGRESS = 10;

    //地球半径
    public static final double R = 6378137.0;

    public static final Integer DEFAULT = 0;  //默认状态

    public static final Integer FORWARD_COLLISION_WARNING = 1;      //前向碰撞预警

    public static final Integer INTERSECTION_COLLISION_WARNING = 2; //交叉路口碰撞预警

    public static final Integer LEFT_TURN_ASSIST = 3;               //左转辅助

    public static final Integer BLIND_AREA_WARNING = 4;             //盲区预警

    public static final Integer LANE_CHANGE_WARNING = 5;            //变道预警

    public static final Integer REVERSE_OVERTAKING_WARNING = 6;     //逆向超车预警

    public static final Integer EMERGENCY_BRAKING_WARNING = 7;      //紧急制动预警

    public static final Integer ABNORMAL_VEHICLE_REMINDER = 8;      //异常车辆提醒

    public static final Integer OUT_OF_CONTROL_VEHICLE_WARNING = 9; //失控车辆预警

    public static final Integer EMERGENCY_VEHICLE_WARNING = 10;     //紧急车辆预警

    public static final Integer VULNERABLE_TRAFFIC_PARTICIPANTS_WARNING = 11;     //弱势交通参与者车辆预警   假设默认 行人

    public static final Integer CLOSE_CAR_WARNING = 12;             //跟车过近

    public static final Integer STATIONARY_VEHICL_WARNING = 13;     //静止车辆预警

    public static final Integer REAR_COLLISION_WARNING = 14;        //后方碰撞预警

    public static final Integer SLOW_VEHICLE_WARNING = 15;          //慢速车辆预警

    public static final Integer WATCH_PEDESTRIANS = 16;             //注意行人

    public static final Integer WATCH_PENGZHUANG = 18;             //合流区碰撞预警

    public static final Integer WATCH_ANIMALS = 19;             //注意牲畜

    public static final Integer NO_STOPPING =78;                    //禁止停车

    public static final Integer LIMIT_SPEED = 85;                   //限速

    public static final  String SIGNAL_START = "AABB";                //交通信号头 AABB

    public static final  String SIGNAL_END = "AACC";                //交通信号尾 AACC

    public static final  String SIGNAL_ACK = "AADD";                //确认包 AADD

    public static final  String SIGNAL_CIRCLE_QUERY = "AABB00013BAACC00";    //查配时周期命令

    public static final  Integer SIGNAL_STATUS = 88;                //交通信号数据58H

    public static final  Integer SIGNAL_CIRCLR_TIME = 60;           //目前配时方案查询  3CH

    public static final String OBU_12FRAME_PRE = "obu12Frame:";

    public static final String OBU_19FRAME_PRE = "obu19Frame:";

    public static final String OBU_10FRAME_PRE = "obu10Frame:";

    public static final String OBU_ADDRESS = "obuAddress:";

    public static final String RSU_MAC_CATCH = "rsumac:";

    public static final String QH_VTV_PRE = "qh_vtv_pre:";

    public static final String QH_VTI_PRE = "qh_vti_pre:";

    public static final String REMOVE_MATCH = "remove_match_data:";
    /**
     * 地图传递过来的原始点位数据
     */
    public static final String MAP_POINT_DATA = "map_point_data:";

    /**
     * 地图点位数据保存
     */
    public static final String MAP_POINT_DATA_SAVE = "map_point_data_save:";

    public static final String SCENE_DEVICE_ID = "scene_device_id:";

    public static final String SCENE_VTV_INFO = "scene_vtv_info:";

    public static final String SCENE_VTI_INFO = "scene_vti_info:";

    public static final String V2X_CAR_LIST = "v2x_car_list:";

    public static final String DEV_RSU_TYPE_CODE = "0103";

    public static final String DEV_OBU_TYPE_CODE = "0401";

    /**
     * 智慧锥桶
     */
    public static final String DEV_TRAFFIC_TYPE_CODE = "0203";
    /**
     * 可变情报板
     */
    public static final String INFORMATION_BOARD = "0201";
    /**
     * 可变限速牌
     */
    public static final String SPEED_LIMIT_BOARD = "0202";
    //RSU交通标志类型编码
    public static final String TRAFFIC_SIGN_CODE = "01";
    //RSU交通事件类型编码
    public static final String TRAFFIC_INCIDENT_CODE = "02";
    //OBU指令类型编码
    public static final String INSTRUCT_TYPE_CODE= "03";

    public static final String CAM_TYPE_CODE = "0101";

    public static final String SCREEN_TYPE_CODE = "0402";

    public static final String CHANGE_TYPE_CODE = "0201";

    public static final String LIMIT_TYPE_CODE = "0202";

    public static final String WHETHER_TYPE_CODE = "0301";

    public static final String SIGNAL_TYPE_CODE = "0204";

    public static final String TRAFFIC_DATA = "trafficData";

    public static final String DEVICE_ONLINE = "deviceonline:";

    /**
     * 不可信状态key
     */
    public static final String OBU_NO_CREDIBLE = "obuNoCredible:";

    /**
     * 可信状态key
     */
    public static final String OBU_CREDIBLE = "obuCredible:";

    /**
     * 上报obu可信状态key
     */
    public static final String OBU_REPORT_CREDIBLE = "obuReportCredible:";

    /**
     * 上报match可信状态key
     */
    public static final String MATCH_REPORT_CREDIBLE = "matchReportCredible:";

    /**
     * 是否联动控制状态
     */
    public static final String LINKED_CONTROL_STATUS = "linkedControlStatus";


    public static final String SIGNAL_CONFIG_INFO = "signalConfigInfo:";
}
