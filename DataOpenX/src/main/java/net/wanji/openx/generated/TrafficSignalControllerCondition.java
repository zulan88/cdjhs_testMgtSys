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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>TrafficSignalControllerCondition enity
 * 
 *
 * 
 * <pre>
 * complexType name="TrafficSignalControllerCondition">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="trafficSignalControllerRef" use="required" type="{}String" />
 *       attribute name="phase" use="required" type="{}String" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrafficSignalControllerCondition")
public class TrafficSignalControllerCondition {

    @XmlAttribute(name = "trafficSignalControllerRef", required = true)
    protected String trafficSignalControllerRef;
    @XmlAttribute(name = "phase", required = true)
    protected String phase;

    /**
     * ��ȡtrafficSignalControllerRef���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrafficSignalControllerRef() {
        return trafficSignalControllerRef;
    }

    /**
     * ����trafficSignalControllerRef���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrafficSignalControllerRef(String value) {
        this.trafficSignalControllerRef = value;
    }

    /**
     * ��ȡphase���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhase() {
        return phase;
    }

    /**
     * ����phase���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhase(String value) {
        this.phase = value;
    }

}
