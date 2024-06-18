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
 * <p>PrivateAction enity
 * 
 * 
 * 
 * <pre>
 * complexType name="PrivateAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="LongitudinalAction" type="{}LongitudinalAction" minOccurs="0"/>
 *         element name="LateralAction" type="{}LateralAction" minOccurs="0"/>
 *         element name="VisibilityAction" type="{}VisibilityAction" minOccurs="0"/>
 *         element name="SynchronizeAction" type="{}SynchronizeAction" minOccurs="0"/>
 *         element name="ActivateControllerAction" type="{}ActivateControllerAction" minOccurs="0"/>
 *         element name="ControllerAction" type="{}ControllerAction" minOccurs="0"/>
 *         element name="TeleportAction" type="{}TeleportAction" minOccurs="0"/>
 *         element name="RoutingAction" type="{}RoutingAction" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PrivateAction", propOrder = {
    "longitudinalAction",
    "lateralAction",
    "visibilityAction",
    "synchronizeAction",
    "activateControllerAction",
    "controllerAction",
    "teleportAction",
    "routingAction"
})
public class PrivateAction {

    @XmlElement(name = "LongitudinalAction")
    protected LongitudinalAction longitudinalAction;
    @XmlElement(name = "LateralAction")
    protected LateralAction lateralAction;
    @XmlElement(name = "VisibilityAction")
    protected VisibilityAction visibilityAction;
    @XmlElement(name = "SynchronizeAction")
    protected SynchronizeAction synchronizeAction;
    @XmlElement(name = "ActivateControllerAction")
    protected ActivateControllerAction activateControllerAction;
    @XmlElement(name = "ControllerAction")
    protected ControllerAction controllerAction;
    @XmlElement(name = "TeleportAction")
    protected TeleportAction teleportAction;
    @XmlElement(name = "RoutingAction")
    protected RoutingAction routingAction;

    /**
     * ��ȡlongitudinalAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link LongitudinalAction }
     *     
     */
    public LongitudinalAction getLongitudinalAction() {
        return longitudinalAction;
    }

    /**
     * ����longitudinalAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link LongitudinalAction }
     *     
     */
    public void setLongitudinalAction(LongitudinalAction value) {
        this.longitudinalAction = value;
    }

    /**
     * ��ȡlateralAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link LateralAction }
     *     
     */
    public LateralAction getLateralAction() {
        return lateralAction;
    }

    /**
     * ����lateralAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link LateralAction }
     *     
     */
    public void setLateralAction(LateralAction value) {
        this.lateralAction = value;
    }

    /**
     * ��ȡvisibilityAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link VisibilityAction }
     *     
     */
    public VisibilityAction getVisibilityAction() {
        return visibilityAction;
    }

    /**
     * ����visibilityAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link VisibilityAction }
     *     
     */
    public void setVisibilityAction(VisibilityAction value) {
        this.visibilityAction = value;
    }

    /**
     * ��ȡsynchronizeAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SynchronizeAction }
     *     
     */
    public SynchronizeAction getSynchronizeAction() {
        return synchronizeAction;
    }

    /**
     * ����synchronizeAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SynchronizeAction }
     *     
     */
    public void setSynchronizeAction(SynchronizeAction value) {
        this.synchronizeAction = value;
    }

    /**
     * ��ȡactivateControllerAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ActivateControllerAction }
     *     
     */
    public ActivateControllerAction getActivateControllerAction() {
        return activateControllerAction;
    }

    /**
     * ����activateControllerAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ActivateControllerAction }
     *     
     */
    public void setActivateControllerAction(ActivateControllerAction value) {
        this.activateControllerAction = value;
    }

    /**
     * ��ȡcontrollerAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ControllerAction }
     *     
     */
    public ControllerAction getControllerAction() {
        return controllerAction;
    }

    /**
     * ����controllerAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ControllerAction }
     *     
     */
    public void setControllerAction(ControllerAction value) {
        this.controllerAction = value;
    }

    /**
     * ��ȡteleportAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TeleportAction }
     *     
     */
    public TeleportAction getTeleportAction() {
        return teleportAction;
    }

    /**
     * ����teleportAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TeleportAction }
     *     
     */
    public void setTeleportAction(TeleportAction value) {
        this.teleportAction = value;
    }

    /**
     * ��ȡroutingAction���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RoutingAction }
     *     
     */
    public RoutingAction getRoutingAction() {
        return routingAction;
    }

    /**
     * ����routingAction���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RoutingAction }
     *     
     */
    public void setRoutingAction(RoutingAction value) {
        this.routingAction = value;
    }

}
