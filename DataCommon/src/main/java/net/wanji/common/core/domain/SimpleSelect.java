package net.wanji.common.core.domain;

import net.wanji.common.core.domain.entity.SysDictData;

import java.io.Serializable;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/26 11:14
 * @Descriptoin:
 */
public class SimpleSelect implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long dictCode;

    private Long sort;

    private String cssClass;

    private String dictLabel;

    private String dictValue;


    public SimpleSelect() {
    }

    public SimpleSelect(SysDictData dictData) {
        this.dictCode = dictData.getDictCode();
        this.sort = dictData.getDictSort();
        this.cssClass = dictData.getCssClass();
        this.dictLabel = dictData.getDictLabel();
        this.dictValue = dictData.getDictValue();
    }

    public SimpleSelect(String dictLabel, String dictValue){
        this.dictLabel = dictLabel;
        this.dictValue = dictValue;
    }

    public Long getSort() {
        return sort;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    public Long getDictCode() {
        return dictCode;
    }

    public void setDictCode(Long dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

}
