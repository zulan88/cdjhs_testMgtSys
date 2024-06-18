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
 * <p>VisibilityAction enity
 * 
 * 
 * 
 * <pre>
 * complexType name="VisibilityAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       attribute name="graphics" use="required" type="{}Boolean" />
 *       attribute name="sensors" use="required" type="{}Boolean" />
 *       attribute name="traffic" use="required" type="{}Boolean" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VisibilityAction")
public class VisibilityAction {

    @XmlAttribute(name = "graphics", required = true)
    protected String graphics;
    @XmlAttribute(name = "sensors", required = true)
    protected String sensors;
    @XmlAttribute(name = "traffic", required = true)
    protected String traffic;

    /**
     * ��ȡgraphics���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGraphics() {
        return graphics;
    }

    /**
     * ����graphics���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGraphics(String value) {
        this.graphics = value;
    }

    /**
     * ��ȡsensors���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSensors() {
        return sensors;
    }

    /**
     * ����sensors���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSensors(String value) {
        this.sensors = value;
    }

    /**
     * ��ȡtraffic���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTraffic() {
        return traffic;
    }

    /**
     * ����traffic���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTraffic(String value) {
        this.traffic = value;
    }

}
