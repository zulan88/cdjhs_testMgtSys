package net.wanji.business.domain;

import net.wanji.common.annotation.Excel;
import net.wanji.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * 团队信息对象 cdjhs_team_info
 * 
 * @author ruoyi
 * @date 2024-08-02
 */
public class CdjhsTeamInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 团队id */
    private Long id;

    /** 团队名称 */
    @Excel(name = "团队名称")
    private String teamName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTeamName(String teamName) 
    {
        this.teamName = teamName;
    }

    public String getTeamName() 
    {
        return teamName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("teamName", getTeamName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
