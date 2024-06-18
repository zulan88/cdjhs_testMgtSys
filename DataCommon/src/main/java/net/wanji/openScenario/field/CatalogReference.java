package net.wanji.openScenario.field;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/21 11:20
 * @Descriptoin:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CatalogReference {
    @XmlAttribute
    private String catalogName;

    @XmlAttribute
    private String entryName;

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }
}