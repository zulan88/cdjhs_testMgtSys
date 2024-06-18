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
 * <p>LateralAction enity
 * 
 * 
 * 
 * <pre>
 * complexType name="LateralAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="LaneChangeAction" type="{}LaneChangeAction" minOccurs="0"/>
 *         element name="LaneOffsetAction" type="{}LaneOffsetAction" minOccurs="0"/>
 *         element name="LateralDistanceAction" type="{}LateralDistanceAction" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LateralAction", propOrder = {
    "laneChangeAction",
    "laneOffsetAction",
    "lateralDistanceAction"
})
public class LateralAction {

    @XmlElement(name = "LaneChangeAction")
    protected LaneChangeAction laneChangeAction;
    @XmlElement(name = "LaneOffsetAction")
    protected LaneOffsetAction laneOffsetAction;
    @XmlElement(name = "LateralDistanceAction")
    protected LateralDistanceAction lateralDistanceAction;

    /**
     * ��ȡlaneChangeAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link LaneChangeAction }
     *     
     */
    public LaneChangeAction getLaneChangeAction() {
        return laneChangeAction;
    }

    /**
     * ����laneChangeAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link LaneChangeAction }
     *     
     */
    public void setLaneChangeAction(LaneChangeAction value) {
        this.laneChangeAction = value;
    }

    /**
     * ��ȡlaneOffsetAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link LaneOffsetAction }
     *     
     */
    public LaneOffsetAction getLaneOffsetAction() {
        return laneOffsetAction;
    }

    /**
     * ����laneOffsetAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link LaneOffsetAction }
     *     
     */
    public void setLaneOffsetAction(LaneOffsetAction value) {
        this.laneOffsetAction = value;
    }

    /**
     * ��ȡlateralDistanceAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link LateralDistanceAction }
     *     
     */
    public LateralDistanceAction getLateralDistanceAction() {
        return lateralDistanceAction;
    }

    /**
     * ����lateralDistanceAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link LateralDistanceAction }
     *     
     */
    public void setLateralDistanceAction(LateralDistanceAction value) {
        this.lateralDistanceAction = value;
    }

}
