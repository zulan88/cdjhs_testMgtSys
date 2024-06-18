package net.wanji.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 片段式场景表
 * @TableName tj_case_tree
 */
@TableName(value ="tj_case_tree")
public class TjCaseTree implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 测试类型（虚实融合测试：virtualRealFusion；虚实对比测试：virtualRealContrast；人在环路测试：mainInLoop；平行推演测试：parallelDeduction；三项映射测试：threeTermMapping）
     */
    private String type;

    /**
     * 状态 0：使用中；1：已删除
     */
    private Integer status;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 自定义字段1
     */
    private String attribute1;

    /**
     * 自定义字段2
     */
    private String attribute2;

    /**
     * 自定义字段3
     */
    private String attribute3;

    /**
     * 自定义字段4
     */
    private String attribute4;

    /**
     * 自定义字段5
     */
    private String attribute5;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    /**
     * 修改人
     */
    private String updatedBy;

    /**
     * 修改日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 测试类型（虚实融合测试：virtualRealFusion；虚实对比测试：virtualRealContrast；人在环路测试：mainInLoop；平行推演测试：parallelDeduction；三项映射测试：threeTermMapping）
     */
    public String getType() {
        return type;
    }

    /**
     * 测试类型（虚实融合测试：virtualRealFusion；虚实对比测试：virtualRealContrast；人在环路测试：mainInLoop；平行推演测试：parallelDeduction；三项映射测试：threeTermMapping）
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 状态 0：使用中；1：已删除
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 状态 0：使用中；1：已删除
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 父级id
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * 父级id
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 级别
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 级别
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 自定义字段1
     */
    public String getAttribute1() {
        return attribute1;
    }

    /**
     * 自定义字段1
     */
    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    /**
     * 自定义字段2
     */
    public String getAttribute2() {
        return attribute2;
    }

    /**
     * 自定义字段2
     */
    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    /**
     * 自定义字段3
     */
    public String getAttribute3() {
        return attribute3;
    }

    /**
     * 自定义字段3
     */
    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    /**
     * 自定义字段4
     */
    public String getAttribute4() {
        return attribute4;
    }

    /**
     * 自定义字段4
     */
    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    /**
     * 自定义字段5
     */
    public String getAttribute5() {
        return attribute5;
    }

    /**
     * 自定义字段5
     */
    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    /**
     * 创建人
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * 创建人
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 创建日期
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * 创建日期
     */
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * 修改人
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * 修改人
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * 修改日期
     */
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    /**
     * 修改日期
     */
    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TjCaseTree other = (TjCaseTree) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getLevel() == null ? other.getLevel() == null : this.getLevel().equals(other.getLevel()))
            && (this.getAttribute1() == null ? other.getAttribute1() == null : this.getAttribute1().equals(other.getAttribute1()))
            && (this.getAttribute2() == null ? other.getAttribute2() == null : this.getAttribute2().equals(other.getAttribute2()))
            && (this.getAttribute3() == null ? other.getAttribute3() == null : this.getAttribute3().equals(other.getAttribute3()))
            && (this.getAttribute4() == null ? other.getAttribute4() == null : this.getAttribute4().equals(other.getAttribute4()))
            && (this.getAttribute5() == null ? other.getAttribute5() == null : this.getAttribute5().equals(other.getAttribute5()))
            && (this.getCreatedBy() == null ? other.getCreatedBy() == null : this.getCreatedBy().equals(other.getCreatedBy()))
            && (this.getCreatedDate() == null ? other.getCreatedDate() == null : this.getCreatedDate().equals(other.getCreatedDate()))
            && (this.getUpdatedBy() == null ? other.getUpdatedBy() == null : this.getUpdatedBy().equals(other.getUpdatedBy()))
            && (this.getUpdatedDate() == null ? other.getUpdatedDate() == null : this.getUpdatedDate().equals(other.getUpdatedDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getLevel() == null) ? 0 : getLevel().hashCode());
        result = prime * result + ((getAttribute1() == null) ? 0 : getAttribute1().hashCode());
        result = prime * result + ((getAttribute2() == null) ? 0 : getAttribute2().hashCode());
        result = prime * result + ((getAttribute3() == null) ? 0 : getAttribute3().hashCode());
        result = prime * result + ((getAttribute4() == null) ? 0 : getAttribute4().hashCode());
        result = prime * result + ((getAttribute5() == null) ? 0 : getAttribute5().hashCode());
        result = prime * result + ((getCreatedBy() == null) ? 0 : getCreatedBy().hashCode());
        result = prime * result + ((getCreatedDate() == null) ? 0 : getCreatedDate().hashCode());
        result = prime * result + ((getUpdatedBy() == null) ? 0 : getUpdatedBy().hashCode());
        result = prime * result + ((getUpdatedDate() == null) ? 0 : getUpdatedDate().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", type=").append(type);
        sb.append(", status=").append(status);
        sb.append(", parentId=").append(parentId);
        sb.append(", level=").append(level);
        sb.append(", attribute1=").append(attribute1);
        sb.append(", attribute2=").append(attribute2);
        sb.append(", attribute3=").append(attribute3);
        sb.append(", attribute4=").append(attribute4);
        sb.append(", attribute5=").append(attribute5);
        sb.append(", createdBy=").append(createdBy);
        sb.append(", createdDate=").append(createdDate);
        sb.append(", updatedBy=").append(updatedBy);
        sb.append(", updatedDate=").append(updatedDate);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}