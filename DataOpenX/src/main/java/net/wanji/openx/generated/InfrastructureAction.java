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
 * <p>InfrastructureAction enity
 * 
 * 
 * 
 * <pre>
 * complexType name="InfrastructureAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="TrafficSignalAction" type="{}TrafficSignalAction"/>
 *       /all>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InfrastructureAction", propOrder = {

})
public class InfrastructureAction {

    @XmlElement(name = "TrafficSignalAction", required = true)
    protected TrafficSignalAction trafficSignalAction;

    /**
     * ��ȡtrafficSignalAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TrafficSignalAction }
     *     
     */
    public TrafficSignalAction getTrafficSignalAction() {
        return trafficSignalAction;
    }

    /**
     * ����trafficSignalAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TrafficSignalAction }
     *     
     */
    public void setTrafficSignalAction(TrafficSignalAction value) {
        this.trafficSignalAction = value;
    }

}
