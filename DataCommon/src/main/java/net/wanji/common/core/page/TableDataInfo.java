package net.wanji.common.core.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 * 
 * @author ruoyi
 */
@ApiModel(description = "表格分页数据对象")
public class TableDataInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    @ApiModelProperty(value = "总记录数")
    private long total;

    /** 列表数据 */
    @ApiModelProperty(value = "列表数据")
    private List<?> data;

    /** 消息状态码 */
    @ApiModelProperty(value = "消息状态码")
    private int code;

    /** 消息内容 */
    @ApiModelProperty(value = "消息内容")
    private String msg;

    /**
     * 表格数据对象
     */
    public TableDataInfo()
    {
    }

    /**
     * 分页
     * 
     * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, int total)
    {
        this.data = list;
        this.total = total;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<?> getData()
    {
        return data;
    }

    public void setData(List<?> rows)
    {
        this.data = rows;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
