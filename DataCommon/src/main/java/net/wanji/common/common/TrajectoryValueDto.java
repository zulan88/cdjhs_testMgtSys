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
    //0-实车 1-仿真背景交通流2-模拟器 3-实车障碍物
    private Integer dataType;
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

    // 0-开始，1-结束 2-普通 3-必经点
    private Integer siteType;

    //0-模拟器 1-背景交通流
    private Integer carSource;

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

    public Integer getDataType() {
        return dataType;
    }

    public void setDataType(Integer dataType) {
        this.dataType = dataType;
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

    public Integer getSiteType() {
        return siteType;
    }

    public void setSiteType(Integer siteType) {
        this.siteType = siteType;
    }

    public Integer getCarSource() {
        return carSource;
    }

    public void setCarSource(Integer carSource) {
        this.carSource = carSource;
    }
}
