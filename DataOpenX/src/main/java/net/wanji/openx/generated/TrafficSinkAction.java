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
 * <p>TrafficSinkAction enity
 * 
 *
 * 
 * <pre>
 * complexType name="TrafficSinkAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="Position" type="{}Position"/>
 *         element name="TrafficDefinition" type="{}TrafficDefinition" minOccurs="0"/>
 *       /all>
 *       attribute name="radius" use="required" type="{}Double" />
 *       attribute name="rate" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrafficSinkAction", propOrder = {

})
public class TrafficSinkAction {

    @XmlElement(name = "Position", required = true)
    protected Position position;
    @XmlElement(name = "TrafficDefinition")
    protected TrafficDefinition trafficDefinition;
    @XmlAttribute(name = "radius", required = true)
    protected String radius;
    @XmlAttribute(name = "rate")
    protected String rate;

    /**
     * ��ȡposition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Position }
     *     
     */
    public Position getPosition() {
        return position;
    }

    /**
     * ����position���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Position }
     *     
     */
    public void setPosition(Position value) {
        this.position = value;
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
     * ��ȡradius���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRadius() {
        return radius;
    }

    /**
     * ����radius���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRadius(String value) {
        this.radius = value;
    }

    /**
     * ��ȡrate���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRate() {
        return rate;
    }

    /**
     * ����rate���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRate(String value) {
        this.rate = value;
    }

}
