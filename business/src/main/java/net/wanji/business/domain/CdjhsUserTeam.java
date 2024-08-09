package net.wanji.business.domain;

import net.wanji.common.annotation.Excel;
import net.wanji.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户团队关联对象 cdjhs_user_team
 * 
 * @author ruoyi
 * @date 2024-08-02
 */
public class CdjhsUserTeam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户和团队关联记录id */
    private Long id;

    private String userName;

    /** 团队id */
    @Excel(name = "团队id")
    private Long teamId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTeamId(Long teamId)
    {
        this.teamId = teamId;
    }

    public Long getTeamId() 
    {
        return teamId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userName", getUserName())
            .append("teamId", getTeamId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
