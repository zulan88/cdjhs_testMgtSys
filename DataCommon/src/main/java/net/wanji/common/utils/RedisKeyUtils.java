package net.wanji.common.utils;



public class RedisKeyUtils {

    //设备状态前缀
    public static final String DEVICE_STATUS_PRE = "CDJHS_DEVICE_STATUS";

    //镜像列表上报前缀
    public static final String IMAGE_LIST_REPORT = "CDJHS_IMAGE_LIST_REPORT";

    //镜像清除结果上报前缀
    public static final String IMAGE_DELETE_RESULT = "CDJHS_IMAGE_DELETE_RESULT";

    //镜像下发结果上报前缀
    public static final String IMAGE_ISSUE_RESULT = "CDJHS_IMAGE_ISSUE_RESULT";

    //练习任务下发结果上报前缀
    public static final String TEST_ISSUE_RESULT = "CDJHS_TEST_ISSUE_RESULT";

    //文件切片上传事件
    public static final String OSS_MULTIPART_UPLOAD = "OSS_MULTIPART_UPLOAD";

    public static final String OSS_PROGRESS_DETAIL = "OSS_PROGRESS_DETAIL";

    //设备状态连接符
    public static final String DEVICE_STATUS_PRE_LINK = ":";

    public static final String TOTAL_BYTES = "total";

    public static final String UPLOADED = "uploaded";

    public static String getDeviceStatusKey(String uniques){
        return DEVICE_STATUS_PRE + DEVICE_STATUS_PRE_LINK + uniques;
    }

    public static String getOssMultipartUploadKey(String uploadId){
        return OSS_MULTIPART_UPLOAD + DEVICE_STATUS_PRE_LINK + uploadId;
    }

    public static String getOssProgressDetailKey(String requestId){
        return OSS_PROGRESS_DETAIL + DEVICE_STATUS_PRE_LINK + requestId;
    }

    public static String getImageListReportChannel(String deviceId){
        return String.format("image_list_%s_req", deviceId);
    }

    public static String getImageListReportKey(String deviceId){
        return IMAGE_LIST_REPORT + DEVICE_STATUS_PRE_LINK + deviceId;
    }

    public static String getImageDelChannel(String deviceId){
        return String.format("image_del_%s_req", deviceId);
    }

    public static String getImageDeleteResultKey(String deviceId, String imageId){
        return IMAGE_DELETE_RESULT + DEVICE_STATUS_PRE_LINK + deviceId + DEVICE_STATUS_PRE_LINK + imageId;
    }

    public static String getImageIssueChannel(String deviceId){
        return String.format("image_%s_req", deviceId);
    }

    public static String getImageIssueResultKey(String deviceId, String imageId){
        return IMAGE_ISSUE_RESULT + DEVICE_STATUS_PRE_LINK + deviceId + DEVICE_STATUS_PRE_LINK + imageId;
    }

    public static String getTestIssueChannel(String deviceId){
        return String.format("test_%s_req", deviceId);
    }

    public static String getTestIssueResultKey(String deviceId){
        return TEST_ISSUE_RESULT + DEVICE_STATUS_PRE_LINK + deviceId;
    }
}
