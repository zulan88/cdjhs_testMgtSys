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
 * <p>Event enity
 * 
 * 
 * 
 * <pre>
 * complexType name="Event">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       sequence>
 *         element name="Action" type="{}Action" maxOccurs="unbounded"/>
 *         element name="StartTrigger" type="{}Trigger"/>
 *       /sequence>
 *       attribute name="maximumExecutionCount" type="{}UnsignedInt" />
 *       attribute name="name" use="required" type="{}String" />
 *       attribute name="priority" use="required" type="{}Priority" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Event", propOrder = {
    "action",
    "startTrigger"
})
public class Event {

    @XmlElement(name = "Action", required = true)
    protected List<Action> action;
    @XmlElement(name = "StartTrigger", required = true)
    protected Trigger startTrigger;
    @XmlAttribute(name = "maximumExecutionCount")
    protected String maximumExecutionCount;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "priority", required = true)
    protected String priority;

    /**
     * Gets the value of the action property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the action property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAction().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Action }
     * 
     * 
     */
    public List<Action> getAction() {
        if (action == null) {
            action = new ArrayList<Action>();
        }
        return this.action;
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
     * ��ȡmaximumExecutionCount���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaximumExecutionCount() {
        return maximumExecutionCount;
    }

    /**
     * ����maximumExecutionCount���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaximumExecutionCount(String value) {
        this.maximumExecutionCount = value;
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

    /**
     * ��ȡpriority���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * ����priority���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

}
