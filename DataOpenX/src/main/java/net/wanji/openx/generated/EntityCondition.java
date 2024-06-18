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
 * <p>EntityCondition enity
 * 
 *
 * 
 * <pre>
 * complexType name="EntityCondition">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="EndOfRoadCondition" type="{}EndOfRoadCondition" minOccurs="0"/>
 *         element name="CollisionCondition" type="{}CollisionCondition" minOccurs="0"/>
 *         element name="OffroadCondition" type="{}OffroadCondition" minOccurs="0"/>
 *         element name="TimeHeadwayCondition" type="{}TimeHeadwayCondition" minOccurs="0"/>
 *         element name="TimeToCollisionCondition" type="{}TimeToCollisionCondition" minOccurs="0"/>
 *         element name="AccelerationCondition" type="{}AccelerationCondition" minOccurs="0"/>
 *         element name="StandStillCondition" type="{}StandStillCondition" minOccurs="0"/>
 *         element name="SpeedCondition" type="{}SpeedCondition" minOccurs="0"/>
 *         element name="RelativeSpeedCondition" type="{}RelativeSpeedCondition" minOccurs="0"/>
 *         element name="TraveledDistanceCondition" type="{}TraveledDistanceCondition" minOccurs="0"/>
 *         element name="ReachPositionCondition" type="{}ReachPositionCondition" minOccurs="0"/>
 *         element name="DistanceCondition" type="{}DistanceCondition" minOccurs="0"/>
 *         element name="RelativeDistanceCondition" type="{}RelativeDistanceCondition" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EntityCondition", propOrder = {
    "endOfRoadCondition",
    "collisionCondition",
    "offroadCondition",
    "timeHeadwayCondition",
    "timeToCollisionCondition",
    "accelerationCondition",
    "standStillCondition",
    "speedCondition",
    "relativeSpeedCondition",
    "traveledDistanceCondition",
    "reachPositionCondition",
    "distanceCondition",
    "relativeDistanceCondition"
})
public class EntityCondition {

    @XmlElement(name = "EndOfRoadCondition")
    protected EndOfRoadCondition endOfRoadCondition;
    @XmlElement(name = "CollisionCondition")
    protected CollisionCondition collisionCondition;
    @XmlElement(name = "OffroadCondition")
    protected OffroadCondition offroadCondition;
    @XmlElement(name = "TimeHeadwayCondition")
    protected TimeHeadwayCondition timeHeadwayCondition;
    @XmlElement(name = "TimeToCollisionCondition")
    protected TimeToCollisionCondition timeToCollisionCondition;
    @XmlElement(name = "AccelerationCondition")
    protected AccelerationCondition accelerationCondition;
    @XmlElement(name = "StandStillCondition")
    protected StandStillCondition standStillCondition;
    @XmlElement(name = "SpeedCondition")
    protected SpeedCondition speedCondition;
    @XmlElement(name = "RelativeSpeedCondition")
    protected RelativeSpeedCondition relativeSpeedCondition;
    @XmlElement(name = "TraveledDistanceCondition")
    protected TraveledDistanceCondition traveledDistanceCondition;
    @XmlElement(name = "ReachPositionCondition")
    protected ReachPositionCondition reachPositionCondition;
    @XmlElement(name = "DistanceCondition")
    protected DistanceCondition distanceCondition;
    @XmlElement(name = "RelativeDistanceCondition")
    protected RelativeDistanceCondition relativeDistanceCondition;

    /**
     * ��ȡendOfRoadCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link EndOfRoadCondition }
     *     
     */
    public EndOfRoadCondition getEndOfRoadCondition() {
        return endOfRoadCondition;
    }

    /**
     * ����endOfRoadCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link EndOfRoadCondition }
     *     
     */
    public void setEndOfRoadCondition(EndOfRoadCondition value) {
        this.endOfRoadCondition = value;
    }

    /**
     * ��ȡcollisionCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link CollisionCondition }
     *     
     */
    public CollisionCondition getCollisionCondition() {
        return collisionCondition;
    }

    /**
     * ����collisionCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link CollisionCondition }
     *     
     */
    public void setCollisionCondition(CollisionCondition value) {
        this.collisionCondition = value;
    }

    /**
     * ��ȡoffroadCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link OffroadCondition }
     *     
     */
    public OffroadCondition getOffroadCondition() {
        return offroadCondition;
    }

    /**
     * ����offroadCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link OffroadCondition }
     *     
     */
    public void setOffroadCondition(OffroadCondition value) {
        this.offroadCondition = value;
    }

    /**
     * ��ȡtimeHeadwayCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TimeHeadwayCondition }
     *     
     */
    public TimeHeadwayCondition getTimeHeadwayCondition() {
        return timeHeadwayCondition;
    }

    /**
     * ����timeHeadwayCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TimeHeadwayCondition }
     *     
     */
    public void setTimeHeadwayCondition(TimeHeadwayCondition value) {
        this.timeHeadwayCondition = value;
    }

    /**
     * ��ȡtimeToCollisionCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TimeToCollisionCondition }
     *     
     */
    public TimeToCollisionCondition getTimeToCollisionCondition() {
        return timeToCollisionCondition;
    }

    /**
     * ����timeToCollisionCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TimeToCollisionCondition }
     *     
     */
    public void setTimeToCollisionCondition(TimeToCollisionCondition value) {
        this.timeToCollisionCondition = value;
    }

    /**
     * ��ȡaccelerationCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link AccelerationCondition }
     *     
     */
    public AccelerationCondition getAccelerationCondition() {
        return accelerationCondition;
    }

    /**
     * ����accelerationCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link AccelerationCondition }
     *     
     */
    public void setAccelerationCondition(AccelerationCondition value) {
        this.accelerationCondition = value;
    }

    /**
     * ��ȡstandStillCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link StandStillCondition }
     *     
     */
    public StandStillCondition getStandStillCondition() {
        return standStillCondition;
    }

    /**
     * ����standStillCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link StandStillCondition }
     *     
     */
    public void setStandStillCondition(StandStillCondition value) {
        this.standStillCondition = value;
    }

    /**
     * ��ȡspeedCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link SpeedCondition }
     *     
     */
    public SpeedCondition getSpeedCondition() {
        return speedCondition;
    }

    /**
     * ����speedCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link SpeedCondition }
     *     
     */
    public void setSpeedCondition(SpeedCondition value) {
        this.speedCondition = value;
    }

    /**
     * ��ȡrelativeSpeedCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RelativeSpeedCondition }
     *     
     */
    public RelativeSpeedCondition getRelativeSpeedCondition() {
        return relativeSpeedCondition;
    }

    /**
     * ����relativeSpeedCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RelativeSpeedCondition }
     *     
     */
    public void setRelativeSpeedCondition(RelativeSpeedCondition value) {
        this.relativeSpeedCondition = value;
    }

    /**
     * ��ȡtraveledDistanceCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link TraveledDistanceCondition }
     *     
     */
    public TraveledDistanceCondition getTraveledDistanceCondition() {
        return traveledDistanceCondition;
    }

    /**
     * ����traveledDistanceCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link TraveledDistanceCondition }
     *     
     */
    public void setTraveledDistanceCondition(TraveledDistanceCondition value) {
        this.traveledDistanceCondition = value;
    }

    /**
     * ��ȡreachPositionCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ReachPositionCondition }
     *     
     */
    public ReachPositionCondition getReachPositionCondition() {
        return reachPositionCondition;
    }

    /**
     * ����reachPositionCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ReachPositionCondition }
     *     
     */
    public void setReachPositionCondition(ReachPositionCondition value) {
        this.reachPositionCondition = value;
    }

    /**
     * ��ȡdistanceCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link DistanceCondition }
     *     
     */
    public DistanceCondition getDistanceCondition() {
        return distanceCondition;
    }

    /**
     * ����distanceCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link DistanceCondition }
     *     
     */
    public void setDistanceCondition(DistanceCondition value) {
        this.distanceCondition = value;
    }

    /**
     * ��ȡrelativeDistanceCondition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RelativeDistanceCondition }
     *     
     */
    public RelativeDistanceCondition getRelativeDistanceCondition() {
        return relativeDistanceCondition;
    }

    /**
     * ����relativeDistanceCondition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RelativeDistanceCondition }
     *     
     */
    public void setRelativeDistanceCondition(RelativeDistanceCondition value) {
        this.relativeDistanceCondition = value;
    }

}
