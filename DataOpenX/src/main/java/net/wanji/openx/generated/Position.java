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
 * <p>Position enity
 * 
 * 
 * 
 * <pre>
 * complexType name="Position">
 *   complexContent>
 *     restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       choice>
 *         element name="WorldPosition" type="{}WorldPosition" minOccurs="0"/>
 *         element name="RelativeWorldPosition" type="{}RelativeWorldPosition" minOccurs="0"/>
 *         element name="RelativeObjectPosition" type="{}RelativeObjectPosition" minOccurs="0"/>
 *         element name="RoadPosition" type="{}RoadPosition" minOccurs="0"/>
 *         element name="RelativeRoadPosition" type="{}RelativeRoadPosition" minOccurs="0"/>
 *         element name="LanePosition" type="{}LanePosition" minOccurs="0"/>
 *         element name="RelativeLanePosition" type="{}RelativeLanePosition" minOccurs="0"/>
 *         element name="RoutePosition" type="{}RoutePosition" minOccurs="0"/>
 *       /choice>
 *     /restriction>
 *   /complexContent>
 * /complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Position", propOrder = {
    "worldPosition",
    "relativeWorldPosition",
    "relativeObjectPosition",
    "roadPosition",
    "relativeRoadPosition",
    "lanePosition",
    "relativeLanePosition",
    "routePosition"
})
public class Position {

    @XmlElement(name = "WorldPosition")
    protected WorldPosition worldPosition;
    @XmlElement(name = "RelativeWorldPosition")
    protected RelativeWorldPosition relativeWorldPosition;
    @XmlElement(name = "RelativeObjectPosition")
    protected RelativeObjectPosition relativeObjectPosition;
    @XmlElement(name = "RoadPosition")
    protected RoadPosition roadPosition;
    @XmlElement(name = "RelativeRoadPosition")
    protected RelativeRoadPosition relativeRoadPosition;
    @XmlElement(name = "LanePosition")
    protected LanePosition lanePosition;
    @XmlElement(name = "RelativeLanePosition")
    protected RelativeLanePosition relativeLanePosition;
    @XmlElement(name = "RoutePosition")
    protected RoutePosition routePosition;

    /**
     * ��ȡworldPosition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link WorldPosition }
     *     
     */
    public WorldPosition getWorldPosition() {
        return worldPosition;
    }

    /**
     * ����worldPosition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link WorldPosition }
     *     
     */
    public void setWorldPosition(WorldPosition value) {
        this.worldPosition = value;
    }

    /**
     * ��ȡrelativeWorldPosition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RelativeWorldPosition }
     *     
     */
    public RelativeWorldPosition getRelativeWorldPosition() {
        return relativeWorldPosition;
    }

    /**
     * ����relativeWorldPosition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RelativeWorldPosition }
     *     
     */
    public void setRelativeWorldPosition(RelativeWorldPosition value) {
        this.relativeWorldPosition = value;
    }

    /**
     * ��ȡrelativeObjectPosition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RelativeObjectPosition }
     *     
     */
    public RelativeObjectPosition getRelativeObjectPosition() {
        return relativeObjectPosition;
    }

    /**
     * ����relativeObjectPosition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RelativeObjectPosition }
     *     
     */
    public void setRelativeObjectPosition(RelativeObjectPosition value) {
        this.relativeObjectPosition = value;
    }

    /**
     * ��ȡroadPosition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RoadPosition }
     *     
     */
    public RoadPosition getRoadPosition() {
        return roadPosition;
    }

    /**
     * ����roadPosition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RoadPosition }
     *     
     */
    public void setRoadPosition(RoadPosition value) {
        this.roadPosition = value;
    }

    /**
     * ��ȡrelativeRoadPosition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RelativeRoadPosition }
     *     
     */
    public RelativeRoadPosition getRelativeRoadPosition() {
        return relativeRoadPosition;
    }

    /**
     * ����relativeRoadPosition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RelativeRoadPosition }
     *     
     */
    public void setRelativeRoadPosition(RelativeRoadPosition value) {
        this.relativeRoadPosition = value;
    }

    /**
     * ��ȡlanePosition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link LanePosition }
     *     
     */
    public LanePosition getLanePosition() {
        return lanePosition;
    }

    /**
     * ����lanePosition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link LanePosition }
     *     
     */
    public void setLanePosition(LanePosition value) {
        this.lanePosition = value;
    }

    /**
     * ��ȡrelativeLanePosition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RelativeLanePosition }
     *     
     */
    public RelativeLanePosition getRelativeLanePosition() {
        return relativeLanePosition;
    }

    /**
     * ����relativeLanePosition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RelativeLanePosition }
     *     
     */
    public void setRelativeLanePosition(RelativeLanePosition value) {
        this.relativeLanePosition = value;
    }

    /**
     * ��ȡroutePosition���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link RoutePosition }
     *     
     */
    public RoutePosition getRoutePosition() {
        return routePosition;
    }

    /**
     * ����routePosition���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link RoutePosition }
     *     
     */
    public void setRoutePosition(RoutePosition value) {
        this.routePosition = value;
    }

}
