//
// ���ļ����� JavaTM Architecture for XML Binding (JAXB) ����ʵ�� v2.2.8-b130911.1802 ���ɵ�
// ����� <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// �����±���Դģʽʱ, �Դ��ļ��������޸Ķ�����ʧ��
// ����ʱ��: 2023.10.24 ʱ�� 10:56:57 AM CST 
//


package net.wanji.openx.generated;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>TrafficAction enity
 * 
 *
 * 
 * <pre>
 * complexType name="TrafficAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="TrafficSourceAction" type="{}TrafficSourceAction" minOccurs="0"/>
 *         element name="TrafficSinkAction" type="{}TrafficSinkAction" minOccurs="0"/>
 *         element name="TrafficSwarmAction" type="{}TrafficSwarmAction" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrafficAction", propOrder = {
    "trafficSourceAction",
    "trafficSinkAction",
    "trafficSwarmAction"
})
public class TrafficAction {

    @XmlElement(name = "TrafficSourceAction")
    protected TrafficSourceAction trafficSourceAction;
    @XmlElement(name = "TrafficSinkAction")
    protected TrafficSinkAction trafficSinkAction;
    @XmlElement(name = "TrafficSwarmAction")
    protected TrafficSwarmAction trafficSwarmAction;

    /**
     * ��ȡtrafficSourceAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TrafficSourceAction }
     *     
     */
    public TrafficSourceAction getTrafficSourceAction() {
        return trafficSourceAction;
    }

    /**
     * ����trafficSourceAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TrafficSourceAction }
     *     
     */
    public void setTrafficSourceAction(TrafficSourceAction value) {
        this.trafficSourceAction = value;
    }

    /**
     * ��ȡtrafficSinkAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TrafficSinkAction }
     *     
     */
    public TrafficSinkAction getTrafficSinkAction() {
        return trafficSinkAction;
    }

    /**
     * ����trafficSinkAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TrafficSinkAction }
     *     
     */
    public void setTrafficSinkAction(TrafficSinkAction value) {
        this.trafficSinkAction = value;
    }

    /**
     * ��ȡtrafficSwarmAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TrafficSwarmAction }
     *     
     */
    public TrafficSwarmAction getTrafficSwarmAction() {
        return trafficSwarmAction;
    }

    /**
     * ����trafficSwarmAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TrafficSwarmAction }
     *     
     */
    public void setTrafficSwarmAction(TrafficSwarmAction value) {
        this.trafficSwarmAction = value;
    }

}
