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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>LaneChangeAction enity
 * 
 *
 * 
 * <pre>
 * complexType name="LaneChangeAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="LaneChangeActionDynamics" type="{}TransitionDynamics"/>
 *         element name="LaneChangeTarget" type="{}LaneChangeTarget"/>
 *       /all>
 *       attribute name="targetLaneOffset" type="{}Double" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LaneChangeAction", propOrder = {

})
public class LaneChangeAction {

    @XmlElement(name = "LaneChangeActionDynamics", required = true)
    protected TransitionDynamics laneChangeActionDynamics;
    @XmlElement(name = "LaneChangeTarget", required = true)
    protected LaneChangeTarget laneChangeTarget;
    @XmlAttribute(name = "targetLaneOffset")
    protected String targetLaneOffset;

    /**
     * ��ȡlaneChangeActionDynamics���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TransitionDynamics }
     *     
     */
    public TransitionDynamics getLaneChangeActionDynamics() {
        return laneChangeActionDynamics;
    }

    /**
     * ����laneChangeActionDynamics���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TransitionDynamics }
     *     
     */
    public void setLaneChangeActionDynamics(TransitionDynamics value) {
        this.laneChangeActionDynamics = value;
    }

    /**
     * ��ȡlaneChangeTarget���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link LaneChangeTarget }
     *     
     */
    public LaneChangeTarget getLaneChangeTarget() {
        return laneChangeTarget;
    }

    /**
     * ����laneChangeTarget���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link LaneChangeTarget }
     *     
     */
    public void setLaneChangeTarget(LaneChangeTarget value) {
        this.laneChangeTarget = value;
    }

    /**
     * ��ȡtargetLaneOffset���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetLaneOffset() {
        return targetLaneOffset;
    }

    /**
     * ����targetLaneOffset���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetLaneOffset(String value) {
        this.targetLaneOffset = value;
    }

}
