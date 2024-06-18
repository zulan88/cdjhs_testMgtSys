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
 * <p>LongitudinalAction enity
 * 
 * 
 * 
 * <pre>
 * complexType name="LongitudinalAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="SpeedAction" type="{}SpeedAction" minOccurs="0"/>
 *         element name="LongitudinalDistanceAction" type="{}LongitudinalDistanceAction" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LongitudinalAction", propOrder = {
    "speedAction",
    "longitudinalDistanceAction"
})
public class LongitudinalAction {

    @XmlElement(name = "SpeedAction")
    protected SpeedAction speedAction;
    @XmlElement(name = "LongitudinalDistanceAction")
    protected LongitudinalDistanceAction longitudinalDistanceAction;

    /**
     * ��ȡspeedAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SpeedAction }
     *     
     */
    public SpeedAction getSpeedAction() {
        return speedAction;
    }

    /**
     * ����speedAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SpeedAction }
     *     
     */
    public void setSpeedAction(SpeedAction value) {
        this.speedAction = value;
    }

    /**
     * ��ȡlongitudinalDistanceAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link LongitudinalDistanceAction }
     *     
     */
    public LongitudinalDistanceAction getLongitudinalDistanceAction() {
        return longitudinalDistanceAction;
    }

    /**
     * ����longitudinalDistanceAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link LongitudinalDistanceAction }
     *     
     */
    public void setLongitudinalDistanceAction(LongitudinalDistanceAction value) {
        this.longitudinalDistanceAction = value;
    }

}
