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
 * <p>FinalSpeed enity
 * 
 * 
 * 
 * <pre>
 * complexType name="FinalSpeed">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="AbsoluteSpeed" type="{}AbsoluteSpeed" minOccurs="0"/>
 *         element name="RelativeSpeedToMaster" type="{}RelativeSpeedToMaster" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FinalSpeed", propOrder = {
    "absoluteSpeed",
    "relativeSpeedToMaster"
})
public class FinalSpeed {

    @XmlElement(name = "AbsoluteSpeed")
    protected AbsoluteSpeed absoluteSpeed;
    @XmlElement(name = "RelativeSpeedToMaster")
    protected RelativeSpeedToMaster relativeSpeedToMaster;

    /**
     * ��ȡabsoluteSpeed���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link AbsoluteSpeed }
     *     
     */
    public AbsoluteSpeed getAbsoluteSpeed() {
        return absoluteSpeed;
    }

    /**
     * ����absoluteSpeed���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link AbsoluteSpeed }
     *     
     */
    public void setAbsoluteSpeed(AbsoluteSpeed value) {
        this.absoluteSpeed = value;
    }

    /**
     * ��ȡrelativeSpeedToMaster���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RelativeSpeedToMaster }
     *     
     */
    public RelativeSpeedToMaster getRelativeSpeedToMaster() {
        return relativeSpeedToMaster;
    }

    /**
     * ����relativeSpeedToMaster���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RelativeSpeedToMaster }
     *     
     */
    public void setRelativeSpeedToMaster(RelativeSpeedToMaster value) {
        this.relativeSpeedToMaster = value;
    }

}
