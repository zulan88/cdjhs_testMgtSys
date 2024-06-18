package net.wanji.openScenario.field;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/21 11:19
 * @Descriptoin:
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ScenarioObject {
    @XmlAttribute
    private String name;

    @XmlElement(name = "CatalogReference")
    private CatalogReference catalogReference;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CatalogReference getCatalogReference() {
        return catalogReference;
    }

    public void setCatalogReference(CatalogReference catalogReference) {
        this.catalogReference = catalogReference;
    }
}