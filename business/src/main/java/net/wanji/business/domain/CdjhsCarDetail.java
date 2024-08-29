package net.wanji.business.domain;

import net.wanji.common.annotation.Excel;
import net.wanji.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 实车信息对象 cdjhs_car_detail
 * 
 * @author ruoyi
 * @date 2024-08-02
 */
public class CdjhsCarDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 实车id */
    private Long id;

    /** 实车编号 */
    @Excel(name = "实车编号")
    private String carCode;

    /** 绑定域控编号 */
    @Excel(name = "绑定域控编号")
    private String deviceCode;

    private String userName;

    private String teamName;

    private Long teamId;

    private String imageId;

    private String imageName;

    private String md5;

    private String reportTime;

    //1: 空闲 2:准备中 3:运行中
    private Integer status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCarCode(String carCode) 
    {
        this.carCode = carCode;
    }

    public String getCarCode() 
    {
        return carCode;
    }
    public void setDeviceCode(String deviceCode) 
    {
        this.deviceCode = deviceCode;
    }

    public String getDeviceCode() 
    {
        return deviceCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("carCode", getCarCode())
            .append("deviceCode", getDeviceCode())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
