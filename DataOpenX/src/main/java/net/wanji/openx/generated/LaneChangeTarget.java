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
 * <p>LaneChangeTarget enity
 * 
 *
 * 
 * <pre>
 * complexType name="LaneChangeTarget">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="RelativeTargetLane" type="{}RelativeTargetLane" minOccurs="0"/>
 *         element name="AbsoluteTargetLane" type="{}AbsoluteTargetLane" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LaneChangeTarget", propOrder = {
    "relativeTargetLane",
    "absoluteTargetLane"
})
public class LaneChangeTarget {

    @XmlElement(name = "RelativeTargetLane")
    protected RelativeTargetLane relativeTargetLane;
    @XmlElement(name = "AbsoluteTargetLane")
    protected AbsoluteTargetLane absoluteTargetLane;

    /**
     * ��ȡrelativeTargetLane���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RelativeTargetLane }
     *     
     */
    public RelativeTargetLane getRelativeTargetLane() {
        return relativeTargetLane;
    }

    /**
     * ����relativeTargetLane���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RelativeTargetLane }
     *     
     */
    public void setRelativeTargetLane(RelativeTargetLane value) {
        this.relativeTargetLane = value;
    }

    /**
     * ��ȡabsoluteTargetLane���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link AbsoluteTargetLane }
     *     
     */
    public AbsoluteTargetLane getAbsoluteTargetLane() {
        return absoluteTargetLane;
    }

    /**
     * ����absoluteTargetLane���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link AbsoluteTargetLane }
     *     
     */
    public void setAbsoluteTargetLane(AbsoluteTargetLane value) {
        this.absoluteTargetLane = value;
    }

}
