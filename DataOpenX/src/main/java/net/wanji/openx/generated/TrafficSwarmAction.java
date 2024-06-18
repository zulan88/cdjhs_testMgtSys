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
 * <p>TrafficSwarmAction enity
 * 
 * 
 * 
 * <pre>
 * complexType name="TrafficSwarmAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="CentralObject" type="{}CentralSwarmObject"/>
 *         element name="TrafficDefinition" type="{}TrafficDefinition"/>
 *       /all>
 *       attribute name="innerRadius" use="required" type="{}Double" />
 *       attribute name="numberOfVehicles" use="required" type="{}UnsignedInt" />
 *       attribute name="offset" use="required" type="{}Double" />
 *       attribute name="semiMajorAxis" use="required" type="{}Double" />
 *       attribute name="semiMinorAxis" use="required" type="{}Double" />
 *       attribute name="velocity" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrafficSwarmAction", propOrder = {

})
public class TrafficSwarmAction {

    @XmlElement(name = "CentralObject", required = true)
    protected CentralSwarmObject centralObject;
    @XmlElement(name = "TrafficDefinition", required = true)
    protected TrafficDefinition trafficDefinition;
    @XmlAttribute(name = "innerRadius", required = true)
    protected String innerRadius;
    @XmlAttribute(name = "numberOfVehicles", required = true)
    protected String numberOfVehicles;
    @XmlAttribute(name = "offset", required = true)
    protected String offset;
    @XmlAttribute(name = "semiMajorAxis", required = true)
    protected String semiMajorAxis;
    @XmlAttribute(name = "semiMinorAxis", required = true)
    protected String semiMinorAxis;
    @XmlAttribute(name = "velocity")
    protected String velocity;

    /**
     * ��ȡcentralObject���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link CentralSwarmObject }
     *     
     */
    public CentralSwarmObject getCentralObject() {
        return centralObject;
    }

    /**
     * ����centralObject���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link CentralSwarmObject }
     *     
     */
    public void setCentralObject(CentralSwarmObject value) {
        this.centralObject = value;
    }

    /**
     * ��ȡtrafficDefinition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TrafficDefinition }
     *     
     */
    public TrafficDefinition getTrafficDefinition() {
        return trafficDefinition;
    }

    /**
     * ����trafficDefinition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TrafficDefinition }
     *     
     */
    public void setTrafficDefinition(TrafficDefinition value) {
        this.trafficDefinition = value;
    }

    /**
     * ��ȡinnerRadius���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInnerRadius() {
        return innerRadius;
    }

    /**
     * ����innerRadius���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInnerRadius(String value) {
        this.innerRadius = value;
    }

    /**
     * ��ȡnumberOfVehicles���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfVehicles() {
        return numberOfVehicles;
    }

    /**
     * ����numberOfVehicles���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfVehicles(String value) {
        this.numberOfVehicles = value;
    }

    /**
     * ��ȡoffset���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOffset() {
        return offset;
    }

    /**
     * ����offset���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOffset(String value) {
        this.offset = value;
    }

    /**
     * ��ȡsemiMajorAxis���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSemiMajorAxis() {
        return semiMajorAxis;
    }

    /**
     * ����semiMajorAxis���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSemiMajorAxis(String value) {
        this.semiMajorAxis = value;
    }

    /**
     * ��ȡsemiMinorAxis���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSemiMinorAxis() {
        return semiMinorAxis;
    }

    /**
     * ����semiMinorAxis���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSemiMinorAxis(String value) {
        this.semiMinorAxis = value;
    }

    /**
     * ��ȡvelocity���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVelocity() {
        return velocity;
    }

    /**
     * ����velocity���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVelocity(String value) {
        this.velocity = value;
    }

}
