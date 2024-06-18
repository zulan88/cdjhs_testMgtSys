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
 * <p>SpeedActionTarget enity
 * 
 * 
 * 
 * <pre>
 * complexType name="SpeedActionTarget">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="RelativeTargetSpeed" type="{}RelativeTargetSpeed" minOccurs="0"/>
 *         element name="AbsoluteTargetSpeed" type="{}AbsoluteTargetSpeed" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpeedActionTarget", propOrder = {
    "relativeTargetSpeed",
    "absoluteTargetSpeed"
})
public class SpeedActionTarget {

    @XmlElement(name = "RelativeTargetSpeed")
    protected RelativeTargetSpeed relativeTargetSpeed;
    @XmlElement(name = "AbsoluteTargetSpeed")
    protected AbsoluteTargetSpeed absoluteTargetSpeed;

    /**
     * ��ȡrelativeTargetSpeed���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RelativeTargetSpeed }
     *     
     */
    public RelativeTargetSpeed getRelativeTargetSpeed() {
        return relativeTargetSpeed;
    }

    /**
     * ����relativeTargetSpeed���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RelativeTargetSpeed }
     *     
     */
    public void setRelativeTargetSpeed(RelativeTargetSpeed value) {
        this.relativeTargetSpeed = value;
    }

    /**
     * ��ȡabsoluteTargetSpeed���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link AbsoluteTargetSpeed }
     *     
     */
    public AbsoluteTargetSpeed getAbsoluteTargetSpeed() {
        return absoluteTargetSpeed;
    }

    /**
     * ����absoluteTargetSpeed���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link AbsoluteTargetSpeed }
     *     
     */
    public void setAbsoluteTargetSpeed(AbsoluteTargetSpeed value) {
        this.absoluteTargetSpeed = value;
    }

}
