//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Condition enity
 * 
 * 
 * 
 * <pre>
 * complexType name="Condition">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="ByEntityCondition" type="{}ByEntityCondition" minOccurs="0"/>
 *         element name="ByValueCondition" type="{}ByValueCondition" minOccurs="0"/>
 *       /choice>
 *       attribute name="conditionEdge" use="required" type="{}ConditionEdge" />
 *       attribute name="delay" use="required" type="{}Double" />
 *       attribute name="name" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Condition", propOrder = {
    "byEntityCondition",
    "byValueCondition"
})
public class Condition {

    public Condition(){}

    public Condition(String conditionEdge, String simulationTime){
        this.name = "";
        this.delay = "0";
        this.conditionEdge=conditionEdge;
        this.byValueCondition = new ByValueCondition();
        SimulationTimeCondition simulationTimeCondition =new SimulationTimeCondition();
        simulationTimeCondition.setValue(simulationTime);
        simulationTimeCondition.setRule("greaterThan");
        this.byValueCondition.setSimulationTimeCondition(simulationTimeCondition);
    }

    @XmlElement(name = "ByEntityCondition")
    protected ByEntityCondition byEntityCondition;
    @XmlElement(name = "ByValueCondition")
    protected ByValueCondition byValueCondition;
    @XmlAttribute(name = "conditionEdge", required = true)
    protected String conditionEdge;
    @XmlAttribute(name = "delay", required = true)
    protected String delay;
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * ��ȡbyEntityCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ByEntityCondition }
     *     
     */
    public ByEntityCondition getByEntityCondition() {
        return byEntityCondition;
    }

    /**
     * ����byEntityCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ByEntityCondition }
     *     
     */
    public void setByEntityCondition(ByEntityCondition value) {
        this.byEntityCondition = value;
    }

    /**
     * ��ȡbyValueCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ByValueCondition }
     *     
     */
    public ByValueCondition getByValueCondition() {
        return byValueCondition;
    }

    /**
     * ����byValueCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ByValueCondition }
     *     
     */
    public void setByValueCondition(ByValueCondition value) {
        this.byValueCondition = value;
    }

    /**
     * ��ȡconditionEdge���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConditionEdge() {
        return conditionEdge;
    }

    /**
     * ����conditionEdge���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConditionEdge(String value) {
        this.conditionEdge = value;
    }

    /**
     * ��ȡdelay���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDelay() {
        return delay;
    }

    /**
     * ����delay���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDelay(String value) {
        this.delay = value;
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
