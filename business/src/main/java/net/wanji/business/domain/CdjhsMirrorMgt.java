package net.wanji.business.domain;

import net.wanji.common.annotation.Excel;
import net.wanji.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 镜像列对象 cdjhs_mirror_mgt
 * 
 * @author ruoyi
 * @date 2024-06-20
 */
public class CdjhsMirrorMgt extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 镜像id */
    private Long id;

    /** 镜像名称 */
    @Excel(name = "镜像名称")
    private String mirrorName;

    /** 镜像版本 */
    @Excel(name = "镜像版本")
    private String mirrorVersion;

    /** 镜像阿里云存储路径 */
    @Excel(name = "镜像阿里云存储路径")
    private String mirrorPathCloud;

    //镜像id 云端该镜像eTag
    private String imageId;

    /** 镜像本地存储路径 */
    @Excel(name = "镜像本地存储路径")
    private String mirrorPathLocal;

    //镜像文件大小
    private String mirrorSize;

    //文件大小
    private Long totalSize;

    //上传状态 0: 上传成功 1:上传失败
    private Integer uploadStatus;

    //md5值
    private String md5;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMirrorName(String mirrorName) 
    {
        this.mirrorName = mirrorName;
    }

    public String getMirrorName() 
    {
        return mirrorName;
    }
    public void setMirrorVersion(String mirrorVersion) 
    {
        this.mirrorVersion = mirrorVersion;
    }

    public String getMirrorVersion() 
    {
        return mirrorVersion;
    }
    public void setMirrorPathCloud(String mirrorPathCloud) 
    {
        this.mirrorPathCloud = mirrorPathCloud;
    }

    public String getMirrorPathCloud() 
    {
        return mirrorPathCloud;
    }
    public void setMirrorPathLocal(String mirrorPathLocal) 
    {
        this.mirrorPathLocal = mirrorPathLocal;
    }

    public String getMirrorPathLocal() 
    {
        return mirrorPathLocal;
    }

    public String getMirrorSize() {
        return mirrorSize;
    }

    public void setMirrorSize(String mirrorSize) {
        this.mirrorSize = mirrorSize;
    }

    public Integer getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(Integer uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("mirrorName", getMirrorName())
            .append("mirrorVersion", getMirrorVersion())
            .append("mirrorPathCloud", getMirrorPathCloud())
            .append("mirrorPathLocal", getMirrorPathLocal())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
