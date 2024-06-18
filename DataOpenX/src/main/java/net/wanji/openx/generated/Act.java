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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Act enity
 * 
 * 
 * 
 * <pre>
 * complexType name="Act">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="ManeuverGroup" type="{}ManeuverGroup" maxOccurs="unbounded"/>
 *         element name="StartTrigger" type="{}Trigger"/>
 *         element name="StopTrigger" type="{}Trigger" minOccurs="0"/>
 *       /sequence>
 *       attribute name="name" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Act", propOrder = {
    "maneuverGroup",
    "startTrigger",
    "stopTrigger"
})
public class Act {

    @XmlElement(name = "ManeuverGroup", required = true)
    protected List<ManeuverGroup> maneuverGroup;
    @XmlElement(name = "StartTrigger", required = true)
    protected Trigger startTrigger;
    @XmlElement(name = "StopTrigger")
    protected Trigger stopTrigger;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the maneuverGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the maneuverGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getManeuverGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ManeuverGroup }
     * 
     * 
     */
    public List<ManeuverGroup> getManeuverGroup() {
        if (maneuverGroup == null) {
            maneuverGroup = new ArrayList<ManeuverGroup>();
        }
        return this.maneuverGroup;
    }

    /**
     * ��ȡstartTrigger���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Trigger }
     *     
     */
    public Trigger getStartTrigger() {
        return startTrigger;
    }

    /**
     * ����startTrigger���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Trigger }
     *     
     */
    public void setStartTrigger(Trigger value) {
        this.startTrigger = value;
    }

    /**
     * ��ȡstopTrigger���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Trigger }
     *     
     */
    public Trigger getStopTrigger() {
        return stopTrigger;
    }

    /**
     * ����stopTrigger���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Trigger }
     *     
     */
    public void setStopTrigger(Trigger value) {
        this.stopTrigger = value;
    }

    /**
     * ��ȡname���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * ����name���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
