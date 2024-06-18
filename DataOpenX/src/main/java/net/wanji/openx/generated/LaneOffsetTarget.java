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
 * <p>LaneOffsetTarget enity
 * 
 *
 * 
 * <pre>
 * complexType name="LaneOffsetTarget">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="RelativeTargetLaneOffset" type="{}RelativeTargetLaneOffset" minOccurs="0"/>
 *         element name="AbsoluteTargetLaneOffset" type="{}AbsoluteTargetLaneOffset" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LaneOffsetTarget", propOrder = {
    "relativeTargetLaneOffset",
    "absoluteTargetLaneOffset"
})
public class LaneOffsetTarget {

    @XmlElement(name = "RelativeTargetLaneOffset")
    protected RelativeTargetLaneOffset relativeTargetLaneOffset;
    @XmlElement(name = "AbsoluteTargetLaneOffset")
    protected AbsoluteTargetLaneOffset absoluteTargetLaneOffset;

    /**
     * ��ȡrelativeTargetLaneOffset���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RelativeTargetLaneOffset }
     *     
     */
    public RelativeTargetLaneOffset getRelativeTargetLaneOffset() {
        return relativeTargetLaneOffset;
    }

    /**
     * ����relativeTargetLaneOffset���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RelativeTargetLaneOffset }
     *     
     */
    public void setRelativeTargetLaneOffset(RelativeTargetLaneOffset value) {
        this.relativeTargetLaneOffset = value;
    }

    /**
     * ��ȡabsoluteTargetLaneOffset���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link AbsoluteTargetLaneOffset }
     *     
     */
    public AbsoluteTargetLaneOffset getAbsoluteTargetLaneOffset() {
        return absoluteTargetLaneOffset;
    }

    /**
     * ����absoluteTargetLaneOffset���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link AbsoluteTargetLaneOffset }
     *     
     */
    public void setAbsoluteTargetLaneOffset(AbsoluteTargetLaneOffset value) {
        this.absoluteTargetLaneOffset = value;
    }

}
