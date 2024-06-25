package net.wanji.business.domain;

import net.wanji.common.annotation.Excel;
import net.wanji.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 域控设备下发镜像记录对象 cdjhs_device_image_record
 * 
 * @author ruoyi
 * @date 2024-06-24
 */
public class CdjhsDeviceImageRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 域控设备唯一标识 */
    @Excel(name = "域控设备唯一标识")
    private String uniques;

    /** 镜像id */
    @Excel(name = "镜像id")
    private String imageId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUniques(String uniques) 
    {
        this.uniques = uniques;
    }

    public String getUniques() 
    {
        return uniques;
    }
    public void setImageId(String imageId) 
    {
        this.imageId = imageId;
    }

    public String getImageId() 
    {
        return imageId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("uniques", getUniques())
            .append("imageId", getImageId())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .toString();
    }
}
