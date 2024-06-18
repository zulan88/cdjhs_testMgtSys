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
 * <p>TimeReference enity
 * 
 * 
 * 
 * <pre>
 * complexType name="TimeReference">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="None" type="{}None" minOccurs="0"/>
 *         element name="Timing" type="{}Timing" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimeReference", propOrder = {
    "none",
    "timing"
})
public class TimeReference {

    @XmlElement(name = "None")
    protected None none;
    @XmlElement(name = "Timing")
    protected Timing timing;

    /**
     * ��ȡnone���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link None }
     *     
     */
    public None getNone() {
        return none;
    }

    /**
     * ����none���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link None }
     *     
     */
    public void setNone(None value) {
        this.none = value;
    }

    /**
     * ��ȡtiming���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Timing }
     *     
     */
    public Timing getTiming() {
        return timing;
    }

    /**
     * ����timing���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Timing }
     *     
     */
    public void setTiming(Timing value) {
        this.timing = value;
    }

}
