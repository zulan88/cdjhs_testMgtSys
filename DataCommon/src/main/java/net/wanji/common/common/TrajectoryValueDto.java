package net.wanji.common.common;

import java.util.List;
import java.util.Map;

/**
 * @Auther: guanyuduo
 * @Date: 2023/8/9 10:13
 * @Descriptoin:
 */
public class
TrajectoryValueDto {
    private String timestamp;
    private String globalTimeStamp;
    private Integer frameId;
    private String id;
    private String name;
    private String picLicense;
    private Integer originalColor;
    private Integer vehicleColor;
    private Integer vehicleType;
    private Integer length;
    private Integer width;
    private Integer height;
    private Integer driveType;
    private Double longitude;
    private Double latitude;
    private Double courseAngle;
    private Integer speed;
    private Integer lengthwayA;
    private Double lonAcc;
    private Double latAcc;
    private Double angularVelocityX;
    private Integer gear;
    private Integer steeringWheelAngle;
    private Integer acceleratorPedal;
    private Integer braking;
    private Integer obuStatus;
    private Integer locationStatus;
    private Integer chassisStatus;
    private Integer autoStatus;
    private Integer indicatorStatus;
    private Integer blinkerStatus;
    private List<Map<String, Double>> futurePlanList;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getGlobalTimeStamp() {
        return globalTimeStamp;
    }

    public void setGlobalTimeStamp(String globalTimeStamp) {
        this.globalTimeStamp = globalTimeStamp;
    }

    public Integer getFrameId() {
        return frameId;
    }

    public void setFrameId(Integer frameId) {
        this.frameId = frameId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicLicense() {
        return picLicense;
    }

    public void setPicLicense(String picLicense) {
        this.picLicense = picLicense;
    }

    public Integer getOriginalColor() {
        return originalColor;
    }

    public void setOriginalColor(Integer originalColor) {
        this.originalColor = originalColor;
    }

    public Integer getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(Integer vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getDriveType() {
        return driveType;
    }

    public void setDriveType(Integer driveType) {
        this.driveType = driveType;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getCourseAngle() {
        return courseAngle;
    }

    public void setCourseAngle(Double courseAngle) {
        this.courseAngle = courseAngle;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getLengthwayA() {
        return lengthwayA;
    }

    public void setLengthwayA(Integer lengthwayA) {
        this.lengthwayA = lengthwayA;
    }

    public Integer getSteeringWheelAngle() {
        return steeringWheelAngle;
    }

    public void setSteeringWheelAngle(Integer steeringWheelAngle) {
        this.steeringWheelAngle = steeringWheelAngle;
    }

    public Integer getAcceleratorPedal() {
        return acceleratorPedal;
    }

    public void setAcceleratorPedal(Integer acceleratorPedal) {
        this.acceleratorPedal = acceleratorPedal;
    }

    public Integer getBraking() {
        return braking;
    }

    public void setBraking(Integer braking) {
        this.braking = braking;
    }

    public Integer getObuStatus() {
        return obuStatus;
    }

    public void setObuStatus(Integer obuStatus) {
        this.obuStatus = obuStatus;
    }

    public Integer getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(Integer locationStatus) {
        this.locationStatus = locationStatus;
    }

    public Integer getChassisStatus() {
        return chassisStatus;
    }

    public void setChassisStatus(Integer chassisStatus) {
        this.chassisStatus = chassisStatus;
    }

    public Integer getAutoStatus() {
        return autoStatus;
    }

    public void setAutoStatus(Integer autoStatus) {
        this.autoStatus = autoStatus;
    }

    public Integer getIndicatorStatus() {
        return indicatorStatus;
    }

    public void setIndicatorStatus(Integer indicatorStatus) {
        this.indicatorStatus = indicatorStatus;
    }

    public Integer getBlinkerStatus() {
        return blinkerStatus;
    }

    public void setBlinkerStatus(Integer blinkerStatus) {
        this.blinkerStatus = blinkerStatus;
    }

    public List<Map<String, Double>> getFuturePlanList() {
        return futurePlanList;
    }

    public void setFuturePlanList(List<Map<String, Double>> futurePlanList) {
        this.futurePlanList = futurePlanList;
    }

    public Double getLonAcc() {
        return lonAcc;
    }

    public void setLonAcc(Double lonAcc) {
        this.lonAcc = lonAcc;
    }

    public Double getLatAcc() {
        return latAcc;
    }

    public void setLatAcc(Double latAcc) {
        this.latAcc = latAcc;
    }

    public Double getAngularVelocityX() {
        return angularVelocityX;
    }

    public void setAngularVelocityX(Double angularVelocityX) {
        this.angularVelocityX = angularVelocityX;
    }

    public Integer getGear() {
        return gear;
    }

    public void setGear(Integer gear) {
        this.gear = gear;
    }

    private Float size_length;

    private Float size_width;

    private Float size_height;

    private Integer subsType;

    private Float position_x;

    private Float position_y;

    private Float position_z;

    private Float vxabs;

    private Float vyabs;

    private Object polygon_points;

    private Integer polygon_point_length;

    public Float getPosition_x() {
        return position_x;
    }

    public void setPolygon_point_length(Integer polygon_point_length) {
        this.polygon_point_length = polygon_point_length;
    }

    public Float getPosition_y() {
        return position_y;
    }

    public Float getPosition_z() {
        return position_z;
    }

    public Float getSize_height() {
        return size_height;
    }

    public Float getSize_length() {
        return size_length;
    }

    public Float getSize_width() {
        return size_width;
    }

    public Float getVxabs() {
        return vxabs;
    }

    public Float getVyabs() {
        return vyabs;
    }

    public Integer getPolygon_point_length() {
        return polygon_point_length;
    }

    public Integer getSubsType() {
        return subsType;
    }

    public Object getPolygon_points() {
        return polygon_points;
    }

    public void setPolygon_points(Object polygon_points) {
        this.polygon_points = polygon_points;
    }

    public void setPosition_x(Float position_x) {
        this.position_x = position_x;
    }

    public void setPosition_y(Float position_y) {
        this.position_y = position_y;
    }

    public void setPosition_z(Float position_z) {
        this.position_z = position_z;
    }

    public void setSize_height(Float size_height) {
        this.size_height = size_height;
    }

    public void setSize_length(Float size_length) {
        this.size_length = size_length;
    }

    public void setSize_width(Float size_width) {
        this.size_width = size_width;
    }

    public void setSubsType(Integer subsType) {
        this.subsType = subsType;
    }

    public void setVxabs(Float vxabs) {
        this.vxabs = vxabs;
    }

    public void setVyabs(Float vyabs) {
        this.vyabs = vyabs;
    }
}
