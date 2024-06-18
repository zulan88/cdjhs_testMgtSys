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
 * <p>InRoutePosition enity
 * 
 * 
 * 
 * <pre>
 * complexType name="InRoutePosition">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="FromCurrentEntity" type="{}PositionOfCurrentEntity" minOccurs="0"/>
 *         element name="FromRoadCoordinates" type="{}PositionInRoadCoordinates" minOccurs="0"/>
 *         element name="FromLaneCoordinates" type="{}PositionInLaneCoordinates" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InRoutePosition", propOrder = {
    "fromCurrentEntity",
    "fromRoadCoordinates",
    "fromLaneCoordinates"
})
public class InRoutePosition {

    @XmlElement(name = "FromCurrentEntity")
    protected PositionOfCurrentEntity fromCurrentEntity;
    @XmlElement(name = "FromRoadCoordinates")
    protected PositionInRoadCoordinates fromRoadCoordinates;
    @XmlElement(name = "FromLaneCoordinates")
    protected PositionInLaneCoordinates fromLaneCoordinates;

    /**
     * ��ȡfromCurrentEntity���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link PositionOfCurrentEntity }
     *     
     */
    public PositionOfCurrentEntity getFromCurrentEntity() {
        return fromCurrentEntity;
    }

    /**
     * ����fromCurrentEntity���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link PositionOfCurrentEntity }
     *     
     */
    public void setFromCurrentEntity(PositionOfCurrentEntity value) {
        this.fromCurrentEntity = value;
    }

    /**
     * ��ȡfromRoadCoordinates���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link PositionInRoadCoordinates }
     *     
     */
    public PositionInRoadCoordinates getFromRoadCoordinates() {
        return fromRoadCoordinates;
    }

    /**
     * ����fromRoadCoordinates���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link PositionInRoadCoordinates }
     *     
     */
    public void setFromRoadCoordinates(PositionInRoadCoordinates value) {
        this.fromRoadCoordinates = value;
    }

    /**
     * ��ȡfromLaneCoordinates���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link PositionInLaneCoordinates }
     *     
     */
    public PositionInLaneCoordinates getFromLaneCoordinates() {
        return fromLaneCoordinates;
    }

    /**
     * ����fromLaneCoordinates���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link PositionInLaneCoordinates }
     *     
     */
    public void setFromLaneCoordinates(PositionInLaneCoordinates value) {
        this.fromLaneCoordinates = value;
    }

}
