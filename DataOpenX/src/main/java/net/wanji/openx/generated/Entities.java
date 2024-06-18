//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Entities enity
 * 
 * 
 * 
 * <pre>
 * complexType name="Entities">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="ScenarioObject" type="{}ScenarioObject" maxOccurs="unbounded" minOccurs="0"/>
 *         element name="EntitySelection" type="{}EntitySelection" maxOccurs="unbounded" minOccurs="0"/>
 *       /sequence>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Entities", propOrder = {
    "scenarioObject",
    "entitySelection"
})
public class Entities {

    @XmlElement(name = "ScenarioObject")
    protected List<ScenarioObject> scenarioObject;
    @XmlElement(name = "EntitySelection")
    protected List<EntitySelection> entitySelection;

    /**
     * Gets the value of the scenarioObject property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scenarioObject property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScenarioObject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ScenarioObject }
     * 
     * 
     */
    public List<ScenarioObject> getScenarioObject() {
        if (scenarioObject == null) {
            scenarioObject = new ArrayList<ScenarioObject>();
        }
        return this.scenarioObject;
    }

    /**
     * Gets the value of the entitySelection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entitySelection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntitySelection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EntitySelection }
     * 
     * 
     */
    public List<EntitySelection> getEntitySelection() {
        if (entitySelection == null) {
            entitySelection = new ArrayList<EntitySelection>();
        }
        return this.entitySelection;
    }

}
