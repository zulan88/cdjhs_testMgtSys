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
 * <p>SpeedAction enity
 * 
 * 
 * 
 * <pre>
 * complexType name="SpeedAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="SpeedActionDynamics" type="{}TransitionDynamics"/>
 *         element name="SpeedActionTarget" type="{}SpeedActionTarget"/>
 *       /all>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SpeedAction", propOrder = {

})
public class SpeedAction {

    @XmlElement(name = "SpeedActionDynamics", required = true)
    protected TransitionDynamics speedActionDynamics;
    @XmlElement(name = "SpeedActionTarget", required = true)
    protected SpeedActionTarget speedActionTarget;

    /**
     * ��ȡspeedActionDynamics���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TransitionDynamics }
     *     
     */
    public TransitionDynamics getSpeedActionDynamics() {
        return speedActionDynamics;
    }

    /**
     * ����speedActionDynamics���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TransitionDynamics }
     *     
     */
    public void setSpeedActionDynamics(TransitionDynamics value) {
        this.speedActionDynamics = value;
    }

    /**
     * ��ȡspeedActionTarget���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SpeedActionTarget }
     *     
     */
    public SpeedActionTarget getSpeedActionTarget() {
        return speedActionTarget;
    }

    /**
     * ����speedActionTarget���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SpeedActionTarget }
     *     
     */
    public void setSpeedActionTarget(SpeedActionTarget value) {
        this.speedActionTarget = value;
    }

}
