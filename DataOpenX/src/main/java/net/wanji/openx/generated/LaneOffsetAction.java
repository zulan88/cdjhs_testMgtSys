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
 * <p>LaneOffsetAction enity
 * 
 *
 * 
 * <pre>
 * complexType name="LaneOffsetAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="LaneOffsetActionDynamics" type="{}LaneOffsetActionDynamics"/>
 *         element name="LaneOffsetTarget" type="{}LaneOffsetTarget"/>
 *       /all>
 *       attribute name="continuous" use="required" type="{}Boolean" />
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LaneOffsetAction", propOrder = {

})
public class LaneOffsetAction {

    @XmlElement(name = "LaneOffsetActionDynamics", required = true)
    protected LaneOffsetActionDynamics laneOffsetActionDynamics;
    @XmlElement(name = "LaneOffsetTarget", required = true)
    protected LaneOffsetTarget laneOffsetTarget;
    @XmlAttribute(name = "continuous", required = true)
    protected String continuous;

    /**
     * ��ȡlaneOffsetActionDynamics���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link LaneOffsetActionDynamics }
     *     
     */
    public LaneOffsetActionDynamics getLaneOffsetActionDynamics() {
        return laneOffsetActionDynamics;
    }

    /**
     * ����laneOffsetActionDynamics���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link LaneOffsetActionDynamics }
     *     
     */
    public void setLaneOffsetActionDynamics(LaneOffsetActionDynamics value) {
        this.laneOffsetActionDynamics = value;
    }

    /**
     * ��ȡlaneOffsetTarget���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link LaneOffsetTarget }
     *     
     */
    public LaneOffsetTarget getLaneOffsetTarget() {
        return laneOffsetTarget;
    }

    /**
     * ����laneOffsetTarget���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link LaneOffsetTarget }
     *     
     */
    public void setLaneOffsetTarget(LaneOffsetTarget value) {
        this.laneOffsetTarget = value;
    }

    /**
     * ��ȡcontinuous���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContinuous() {
        return continuous;
    }

    /**
     * ����continuous���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContinuous(String value) {
        this.continuous = value;
    }

}
