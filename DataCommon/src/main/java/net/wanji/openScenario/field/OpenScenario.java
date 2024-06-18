package net.wanji.openScenario.field;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @Auther: guanyuduo
 * @Date: 2023/7/21 11:17
 * @Descriptoin:
 */
@XmlRootElement(name = "OpenSCENARIO")
@XmlAccessorType(XmlAccessType.FIELD)
public class OpenScenario {

    @XmlElementWrapper(name = "Entities")
    @XmlElement(name = "ScenarioObject")
    private List<ScenarioObject> scenarioObjects;

    public List<ScenarioObject> getScenarioObjects() {
        return scenarioObjects;
    }

    public void setScenarioObjects(List<ScenarioObject> scenarioObjects) {
        this.scenarioObjects = scenarioObjects;
    }
}
