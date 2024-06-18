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
 * <p>OverrideControllerValueAction enity
 * 
 * 
 * 
 * <pre>
 * complexType name="OverrideControllerValueAction">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       all>
 *         element name="Throttle" type="{}OverrideThrottleAction"/>
 *         element name="Brake" type="{}OverrideBrakeAction"/>
 *         element name="Clutch" type="{}OverrideClutchAction"/>
 *         element name="ParkingBrake" type="{}OverrideParkingBrakeAction"/>
 *         element name="SteeringWheel" type="{}OverrideSteeringWheelAction"/>
 *         element name="Gear" type="{}OverrideGearAction"/>
 *       /all>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OverrideControllerValueAction", propOrder = {

})
public class OverrideControllerValueAction {

    @XmlElement(name = "Throttle", required = true)
    protected OverrideThrottleAction throttle;
    @XmlElement(name = "Brake", required = true)
    protected OverrideBrakeAction brake;
    @XmlElement(name = "Clutch", required = true)
    protected OverrideClutchAction clutch;
    @XmlElement(name = "ParkingBrake", required = true)
    protected OverrideParkingBrakeAction parkingBrake;
    @XmlElement(name = "SteeringWheel", required = true)
    protected OverrideSteeringWheelAction steeringWheel;
    @XmlElement(name = "Gear", required = true)
    protected OverrideGearAction gear;

    /**
     * ��ȡthrottle���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link OverrideThrottleAction }
     *     
     */
    public OverrideThrottleAction getThrottle() {
        return throttle;
    }

    /**
     * ����throttle���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link OverrideThrottleAction }
     *     
     */
    public void setThrottle(OverrideThrottleAction value) {
        this.throttle = value;
    }

    /**
     * ��ȡbrake���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link OverrideBrakeAction }
     *     
     */
    public OverrideBrakeAction getBrake() {
        return brake;
    }

    /**
     * ����brake���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link OverrideBrakeAction }
     *     
     */
    public void setBrake(OverrideBrakeAction value) {
        this.brake = value;
    }

    /**
     * ��ȡclutch���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link OverrideClutchAction }
     *     
     */
    public OverrideClutchAction getClutch() {
        return clutch;
    }

    /**
     * ����clutch���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link OverrideClutchAction }
     *     
     */
    public void setClutch(OverrideClutchAction value) {
        this.clutch = value;
    }

    /**
     * ��ȡparkingBrake���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link OverrideParkingBrakeAction }
     *     
     */
    public OverrideParkingBrakeAction getParkingBrake() {
        return parkingBrake;
    }

    /**
     * ����parkingBrake���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link OverrideParkingBrakeAction }
     *     
     */
    public void setParkingBrake(OverrideParkingBrakeAction value) {
        this.parkingBrake = value;
    }

    /**
     * ��ȡsteeringWheel���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link OverrideSteeringWheelAction }
     *     
     */
    public OverrideSteeringWheelAction getSteeringWheel() {
        return steeringWheel;
    }

    /**
     * ����steeringWheel���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link OverrideSteeringWheelAction }
     *     
     */
    public void setSteeringWheel(OverrideSteeringWheelAction value) {
        this.steeringWheel = value;
    }

    /**
     * ��ȡgear���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link OverrideGearAction }
     *     
     */
    public OverrideGearAction getGear() {
        return gear;
    }

    /**
     * ����gear���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link OverrideGearAction }
     *     
     */
    public void setGear(OverrideGearAction value) {
        this.gear = value;
    }

}
